package fr.uca.i3s.sparks.compomaid.analyser;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Function;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Operation;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.Platform.FunctionPrototype;
import fr.uca.i3s.sparks.compomaid.model.Platform.Library;
import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.text.DecimalFormat;
import java.util.HashMap;


public class EnergyAnalyser extends Visitor {

    Logger logger = new Logger(false);

    @Override
    public void visit(App app) {

        String appEnergyLog = "";

        double processorPower_mW = 103./10;
        double batteryCapacity = 4320000; //3150; //4000; //mWh
        double cycleEnergy_mWh = 0; //mWh
        double cycleDuration_h = 0;

        // For each mission step
        for (MissionStep missionStep : app.getMissionSteps()) {
            HashMap<PeriodicSensor, Double> sensorUsage = missionStep.getSensorUsage();

            double missionStepEnergy_mWh = 0;

            logger.dbg("first call: " + missionStep.getName());

            double missionStepDuration_h = missionStep.getDuration_min()/60.;

            logger.dbg("end of first call");



            double actuatorEnergy_mWh = missionStep.getActuatorEnergy_mWh();
            missionStepEnergy_mWh += actuatorEnergy_mWh;
            String actuatorEnergyLog = "  Actuator consumption: "
                    + new DecimalFormat("#.#").format(actuatorEnergy_mWh) + "mWh\n";


            double processorEnergy_mWh = processorPower_mW*missionStepDuration_h;
            missionStepEnergy_mWh += processorEnergy_mWh;
            String processorEnergyLog = "  Processor consumption: "
                    + new DecimalFormat("#.#").format(processorEnergy_mWh) + "mWh\n";


            String sensorEnergyLog = "";
            for (PeriodicSensor sensor : sensorUsage.keySet()) {
                double sensorEnergy_mWh = sensor.getPower() * sensorUsage.get(sensor) * missionStepDuration_h;
                missionStepEnergy_mWh += sensorEnergy_mWh;
                sensorEnergyLog += "  " + sensor.getName() + ": "
                        + new DecimalFormat("#.#").format(sensorEnergy_mWh) + "mWh\n";
            }

            logger.dbg("Satellite transmission compute");

            String transmissionEnergyLog = "";
            if (missionStep.getName().toUpperCase().equals("SURFACE")) {
                SatelliteTransmissionAnalyser satelliteTransmissionAnalyser = new SatelliteTransmissionAnalyser();
                double amountOfBytes = satelliteTransmissionAnalyser.getTransmissionPerCycle(app);
                logger.dbg(amountOfBytes);
                double transmissionRate = 10*1024; // 10 kBytes per minute
                double transmissionTime_min = amountOfBytes/transmissionRate;
                double transmissionPower_mwatts = 4300;
                double transmissionEnergy_mwattsh = transmissionPower_mwatts*transmissionTime_min/60.;
                missionStepEnergy_mWh += transmissionEnergy_mwattsh;
                transmissionEnergyLog = "  Transmission consumption: "
                        + new DecimalFormat("#.#").format(transmissionEnergy_mwattsh) + "mWh\n";

            }
            logger.dbg("End of satelite transmission compute");

            appEnergyLog += "Energy consumption during " + missionStep.getName().toUpperCase() + ": "
                    + new DecimalFormat("#.#").format(missionStepEnergy_mWh) + "mWh\n";
            appEnergyLog += processorEnergyLog;
            appEnergyLog += sensorEnergyLog;
            appEnergyLog += actuatorEnergyLog;
            appEnergyLog += transmissionEnergyLog;

            appEnergyLog += "\n";

            cycleEnergy_mWh += missionStepEnergy_mWh;
            cycleDuration_h += missionStepDuration_h;
        }

        double autonomyCycles = batteryCapacity / cycleEnergy_mWh;
        logger.dbg(autonomyCycles);
        logger.dbg(cycleDuration_h);
        logger.dbg(cycleEnergy_mWh);
        double autonomyHours = autonomyCycles * cycleDuration_h;
        double autonomyYears = autonomyHours / 365. / 24.;
        logger.log("");
        logger.log("Autonomy: " + new DecimalFormat("#.#").format(autonomyYears) + " years");
        logger.log(appEnergyLog);

        /*
        DecimalFormat df = new DecimalFormat("#0.00");
        logger.log("* Descent: " + df.format(app.getDescent().getEnergyConsumption()) + " Wh");
        logger.log("* Park: " + df.format(app.getPark().getEnergyConsumption()) + " Wh");
        logger.log("* Ascent: " + df.format(app.getAscent().getEnergyConsumption()) + " Wh");
        logger.log("* Surface: " + df.format(app.getSurface().getEnergyConsumption()) + " Wh");
        logger.log("* Total: " + df.format(app.getEnergyConsumption()) + " Wh");

        // Battery PC portable = 60Wh
        // Battery of float = 4000Wh
        double battery_size_Wh = 4000;
        double lifeTimeCycles = battery_size_Wh / app.getEnergyConsumption();
        df = new DecimalFormat("#0.00");
        double lifeTimeYears = lifeTimeCycles*app.getTimeConsumption()/60./24./365.;
        logger.log("> Float life time is " + df.format(lifeTimeCycles) + " cycles (" + df.format(lifeTimeYears) + " years)");
        */
    }

    @Override
    public void visit(AcqMode acqMode) {

    }

    @Override
    public void visit(Branch branch) {

    }

    @Override
    public void visit(Sequence sequence) {

    }

    @Override
    public void visit(Library library) {

    }

    @Override
    public void visit(FunctionPrototype functionPrototype) {

    }

    @Override
    public void visit(Operation operation) {

    }

    @Override
    public void visit(Constant constant) {

    }

    @Override
    public void visit(Variable variable) {

    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(AssignmentInstruction assignmentInstruction) {

    }

    @Override
    public void visit(CallInstruction callInstruction) {

    }

    @Override
    public void visit(ForLoopInstruction forLoopInstruction) {

    }

    @Override
    public void visit(ConditionalInstruction conditionalInstruction) {

    }

}
