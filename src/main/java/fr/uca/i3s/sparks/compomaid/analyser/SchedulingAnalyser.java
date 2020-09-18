package fr.uca.i3s.sparks.compomaid.analyser;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.ConditionalInstruction;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Function;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Operation;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.Schedule;
import fr.uca.i3s.sparks.compomaid.model.Platform.*;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.text.DecimalFormat;
import java.util.HashMap;

import static java.lang.Math.pow;


public class SchedulingAnalyser extends Visitor {

    Logger logger = new Logger(true);

    @Override
    public void visit(App app) {

        // For each mission step
        for (MissionStep missionStep : app.getMissionSteps()) {

            HashMap<Schedule, Double> scheduleCpuUse = new HashMap<>();
            // For each schedule
            for (Schedule schedule : missionStep.getSchedules()) {
                AcqMode acqMode = schedule.getAcqMode();
                scheduleCpuUse.put(schedule, new Double(0));

                if (acqMode instanceof ShortAcqMode) {
                    double period_us = schedule.getPeriod_us();
                    for (Branch branch : acqMode.getMainSequence().getExecutionPaths()) {
                        Double cpuUse = branch.getWCET_us()/period_us;
                        if(cpuUse > scheduleCpuUse.get(schedule)) {
                            scheduleCpuUse.replace(schedule, cpuUse);
                        }
                    }
                } else if (acqMode instanceof ContinuousAcqMode) {
                    double period_s = schedule.getAcqMode().getSensorConfiguration().getPeriod_s();
                    period_s = period_s*schedule.getAcqMode().getInputVariable().getLength();
                    double period_us = 1000000.*period_s;
                    //logger.dbg("Execution period: " + period_us + "us");
                    for (Branch branch : acqMode.getMainSequence().getRealTimeExecutionPaths()) {
                        //logger.dbg("WCET: " + branch.getWCET_us() + "us");
                        Double cpuUse = branch.getWCET_us()/period_us;
                        if(cpuUse > scheduleCpuUse.get(schedule)) {
                            scheduleCpuUse.replace(schedule, cpuUse);
                        }
                    }
                }
            }

            // Liu and Layland bound
            double totalCPU = 0;
            double totalTasks = 0;
            for (Schedule schedule : missionStep.getSchedules()) {
                totalCPU += scheduleCpuUse.get(schedule);
                totalTasks += 1;
            }
            if (totalTasks >= 1) {
                logger.log("");
                logger.log("Maximum processor usage during " + missionStep.getName().toUpperCase() + ":");

                double bound = totalTasks*(pow(2.,(1./totalTasks)) - 1);

                if  (totalCPU < bound) {
                    logger.log( "  Valid: " + new DecimalFormat("#.##").format(totalCPU*100)
                            + " % < " + new DecimalFormat("#.##").format(bound*100) + " %");
                } else {
                    logger.log( "  Error: " + new DecimalFormat("#.##").format(totalCPU*100)
                            + " % > " + new DecimalFormat("#.##").format(bound*100) + " %");
                }
                if (totalTasks >= 2) {
                    for (Schedule schedule : missionStep.getSchedules()) {
                        logger.log("  " + schedule.toString() + " : " +
                                new DecimalFormat("#.##").format(scheduleCpuUse.get(schedule)*100) + " %");
                    }
                }
            }
        }
    }

    /*
        public double getWCET_us() {
        double wcet_s = 0;
        for (Instruction instruction : instructions) {
            wcet_s += instruction.getWCET_us();
        }
        return wcet_s;
    }
     */

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
