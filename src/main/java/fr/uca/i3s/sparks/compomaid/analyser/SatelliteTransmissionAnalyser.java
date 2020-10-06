package fr.uca.i3s.sparks.compomaid.analyser;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.Probability;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Function;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Operation;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.Schedule;
import fr.uca.i3s.sparks.compomaid.model.Platform.FunctionPrototype;
import fr.uca.i3s.sparks.compomaid.model.Platform.Library;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.text.DecimalFormat;


public class SatelliteTransmissionAnalyser extends Visitor {

    Logger logger = new Logger(false);

    @Override
    public void visit(App app) {
        double transmissionPerCycle = 0.;
        double cycleDuration_h = 0.;
        for (MissionStep missionStep : app.getMissionSteps()) {
            logger.dbg("Mission step ");
            double missionStepDuration_h = missionStep.getDuration_min()/60.;
            cycleDuration_h += missionStepDuration_h;
            double transmissionPerMissionStep = 0.;
            for (Schedule schedule : missionStep.getSchedules()) {
                AcqMode acqMode = schedule.getAcqMode();
                double scheduleTransmissionPerHour = 0;
                logger.dbg("Acq step ");
                for (Sequence sequence : acqMode.getSequences()) {
                    int transmissionDataSize = sequence.getTransmissionDataSize();
                    double transmissionPerHour = 0;
                    if (transmissionDataSize > 0) {
                        logger.dbg("name: " + sequence.getName());
                        logger.dbg("size: " + transmissionDataSize);

                        double period_h = 0;
                        if (sequence.equals(acqMode.getMainSequence())) {
                            if (acqMode instanceof ShortAcqMode) {
                                period_h = (double)schedule.getPeriod_s()/60/60;
                            }
                            if (acqMode instanceof ContinuousAcqMode) {
                                double period_s = acqMode.getSensorConfiguration().getPeriod_s()*acqMode.getInputVariable().getLength();
                                period_h = period_s/(60.*60.);
                            }
                        } else {
                            period_h = 0;
                            for (Probability probability : acqMode.getSequenceProbability(sequence)) {
                                period_h += probability.getProbaPerHour();
                                //logger.log("proba: " + probability.getProbaPerHour());
                            }
                            period_h = 1 / period_h;
                        }
                        transmissionPerHour = transmissionDataSize/period_h;
                    }
                    //logger.dbg("transmissionPerHour: " + transmissionPerHour);
                    scheduleTransmissionPerHour += transmissionPerHour;
                }
                transmissionPerMissionStep += scheduleTransmissionPerHour * missionStepDuration_h;
                logger.dbg("scheduleTransmissionPerHour: " + scheduleTransmissionPerHour);
                logger.dbg("Mission step duration: " + missionStepDuration_h);
                logger.dbg("transmissionPerMissionStep: " + transmissionPerMissionStep);
            }
            transmissionPerCycle += transmissionPerMissionStep;
            //logger.log("Transmission per mission step: " + transmissionPerMissionStep);
        }

        logger.log("");
        logger.log("Transmission per cycle: " +
                new DecimalFormat("#.#").format(transmissionPerCycle/1024) + " kB");

        double bytes_per_hour = transmissionPerCycle/cycleDuration_h;
        double kilo_bytes_per_hour = bytes_per_hour/1024.;
        /*
        if (kilo_bytes_per_hour < 1) {
            logger.log("Transmission per hour: " + new DecimalFormat("#.##").format(bytes_per_hour) + " B");
        } else {
            logger.log("Transmission per hour: " + new DecimalFormat("#.##").format(kilo_bytes_per_hour) + " kB");
        }
        */
        double bytes_per_month = bytes_per_hour*24*30;
        double kilo_bytes_per_month = bytes_per_month/1024.;

        if (kilo_bytes_per_month < 1) {
            logger.log("Transmission per month: " + new DecimalFormat("#.##").format(bytes_per_month) + " B");
        } else {
            logger.log("Transmission per month: " + new DecimalFormat("#.##").format(kilo_bytes_per_month) + " kB");
        }
    }


    /* Same function thann th visit one but without log in order to be used by the energy analyser
    *  TODO: separate analysis computation from results printing
    * */
    public double getTransmissionPerCycle(App app) {
        double transmissionPerCycle = 0.;
        double cycleDuration_h = 0.;
        for (MissionStep missionStep : app.getMissionSteps()) {
            double missionStepDuration_h = missionStep.getDuration_min()/60.;
            cycleDuration_h += missionStepDuration_h;
            double transmissionPerMissionStep = 0.;
            for (Schedule schedule : missionStep.getSchedules()) {
                AcqMode acqMode = schedule.getAcqMode();
                double scheduleTransmissionPerHour = 0;
                for (Sequence sequence : acqMode.getSequences()) {
                    int transmissionDataSize = sequence.getTransmissionDataSize();
                    double transmissionPerHour = 0;
                    if (transmissionDataSize > 0) {
                        double period_h = 0;
                        if (sequence.equals(acqMode.getMainSequence())) {
                            if (acqMode instanceof ShortAcqMode) {
                                period_h = (double)schedule.getPeriod_s()/60/60;;
                            }
                            if (acqMode instanceof ContinuousAcqMode) {
                                double period_s = acqMode.getSensorConfiguration().getPeriod_s()*acqMode.getInputVariable().getLength();
                                period_h = period_s/(60.*60.);
                            }
                        } else {
                            period_h = 0;
                            for (Probability probability : acqMode.getSequenceProbability(sequence)) {
                                period_h += probability.getProbaPerHour();
                            }
                            period_h = 1 / period_h;
                        }
                        transmissionPerHour = transmissionDataSize/period_h;
                    }
                    scheduleTransmissionPerHour += transmissionPerHour;
                }
                transmissionPerMissionStep += scheduleTransmissionPerHour * missionStepDuration_h;
            }
            transmissionPerCycle += transmissionPerMissionStep;
        }
        return transmissionPerCycle;
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
