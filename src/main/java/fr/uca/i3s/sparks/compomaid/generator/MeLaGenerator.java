package fr.uca.i3s.sparks.compomaid.generator;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.ConditionalInstruction;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.*;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.Schedule;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.Types.*;
import fr.uca.i3s.sparks.compomaid.model.Platform.*;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


import static fr.uca.i3s.sparks.compomaid.tools.FileWriter.writeInFile;


public class MeLaGenerator extends Visitor {

    Logger logger = new Logger(false);

    // Contain the generated code
    private String sline = "";
    private StringBuffer code;

    // Directory of the generated code
    private String destdir;

    // Used for auto indent
    private int indent = 0;
    private boolean newline = true;


    public void generate(App app, String destdir) {
        this.destdir = destdir;
        this.visit(app);
    }

    @Override
    public void visit(App app) {

        code = new StringBuffer();

        w("/********************************************************\n");
        w("* Mission configuration\n");
        w("********************************************************/\n");
        w("");
        w("Mission:");
        w("   ParkTime: " + app.getPark().getDuration_min() + " minutes\n");
        w("   ParkDepth: " + app.getParkDepth_m() + " meters\n");
        w("");
        w("");
        w("/********************************************************\n");
        w("* Coordination of acquisition modes\n");
        w("********************************************************/\n");
        w("");
        for (MissionStep missionStep : app.getMissionSteps()) {
            if (missionStep.getSchedules().size() == 0) {
                continue;
            }
            w(missionStep.getName().substring(0, 1).toUpperCase() + missionStep.getName().substring(1) + "AcqMode:");
            for (Schedule schedule : missionStep.getSchedules()) {
                if (schedule.isContinuousExecution()) {
                    w("   " + schedule.getAcqMode().getName() + ";");
                } else {
                    w("   " + schedule.getAcqMode().getName() + " every " + schedule.getPeriod() + " " + schedule.getPeriodUnit() + ";");
                }
            }
        }
        w("");
        w("");
        for (AcqMode acqMode : app.getAcqModes()) {
            w("/********************************************************\n");
            w("* Acquisition mode: " + acqMode.getName() + "\n");
            w("********************************************************/\n");
            w("");
            if (acqMode instanceof ContinuousAcqMode) {
                w("ContinuousAcqMode ");
            } else {
                w("ShortAcqMode ");
            }
            w(acqMode.getName() + ":");
            w("");
            acqMode.accept(this);
            w("endmode;");
            w("");
            w("");
        }

        writeInFile(destdir + app.getName() + ".mela", code.toString());
    }

    @Override
    public void visit(AcqMode acqMode) {

        w("Input:");
        w("   sensor: " + acqMode.getSensorConfiguration().getSensor().getName() + ";");
        w("   data: " + acqMode.getInputVariable().getName() + "[" + acqMode.getInputVariable().getLength() + "]" + ";");
        w("");

        w("Variables:");
        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.Bool)) {
                w("   bool " + variable.getName() + ";");
            }
            if (variable.getDataType().equals(DataType.Int)) {
                w("   int " + variable.getName() + ";");
            }
            if (variable.getDataType().equals(DataType.ArrayInt)) {
                w("   int[" + variable.getLength() + "] " + variable.getName() + ";");
            }
            if (variable.getDataType().equals(DataType.File)) {
                w("   transmitFile " + variable.getName() + ";");
            }
        }
        w("");

        for (Sequence sq : acqMode.getSequences()) {
            if (!(sq instanceof UntypedSequence)) {
                sq.accept(this);
            }
        }
    }

    @Override
    public void visit(Branch branch) {

    }

    @Override
    public void visit(Sequence sequence) {
        if (sequence instanceof RealTimeSequence) {
            w("RealTimeSequence " + sequence.getName() + ":");
        } else if (sequence instanceof ProcessingSequence) {
            w("ProcessingSequence " + sequence.getName() + ":");
        }

        for (Instruction instruction: sequence.getInstructions()){
            instruction.accept(this);
        }

        if (!(sequence instanceof UntypedSequence)) {
            w("endseq;");
            w("");
        }
    }


    /*
    @Override
    public void visit(oldInstruction oldInstruction) {
        StringBuilder instruction_str = new StringBuilder();

        // Add output of instruction
        Variable ret = oldInstruction.getRet();
        if (ret != null) {
            instruction_str.append(ret.getName());
            instruction_str.append(" = ");
        }

        // Add instruction
        String instruction_name_str = oldInstruction.getFunctionPrototype().getName();
        instruction_str.append(instruction_name_str);

        // Add input of instruction
        ArrayList<Parameter> parameters = oldInstruction.getParameters();
        instruction_str.append("(");
        if (parameters.size() > 0) {
            int i = 0;
            while (i < parameters.size()-1) {
                instruction_str.append(parameters.get(i).toString());
                instruction_str.append(", ");
                i++;
            }
            instruction_str.append(parameters.get(i).toString());
        }
        instruction_str.append(");");

        w(instruction_str.toString());
    }
    */

    /*
    @Override
    public void visit(ConditionalInstruction conditionalInstruction) {
        //DecimalFormat df = new DecimalFormat("#.######");


        ConditionalBranch ifBranch = conditionalInstruction.getIfBranch();
        if (ifBranch != null) {
            wa("if ");
            ifBranch.getConditionalExpression().accept(this);
            w(":");
            w("@Probability = " + ifBranch.getProbability());
            if (ifBranch.getSequence().isDeclared()) {
                wa("goto ");
                w(ifBranch.getSequence().getName());
                w(";");
            } else {
                for (oldInstruction oldInstruction : ifBranch.getSequence().getInstructions()) {
                    oldInstruction.accept(this);
                }
            }
        }
        */

        /*
        for (ConditionalBranch cdBranch : conditionalInstruction.getElseifBranches()) {
            wa("else if ");
            cdBranch.getConditionalExpression().accept(this);
            w(":");
            w("@Probability = " + cdBranch.getProbability());
            if (cdBranch.getSequence().isDeclared()) {
                wa("goto ");
                w(cdBranch.getSequence().getName());
                w(";");
            } else {
                for (oldInstruction oldInstruction : cdBranch.getSequence().getInstructions()) {
                    oldInstruction.accept(this);
                }
            }
        }

        ConditionalBranch elseBranch = conditionalInstruction.getElseBranch();
        if (elseBranch != null) {
            wa("else");
            w(":");
            w("@Probability = " + elseBranch.getProbability());
            if (elseBranch.getSequence().isDeclared()) {
                wa("goto ");
                w(elseBranch.getSequence().getName());
                w(";");
            } else {
                for (oldInstruction oldInstruction : elseBranch.getSequence().getInstructions()) {
                    oldInstruction.accept(this);
                }
            }
        }

        w("endif;");
    }
    */

    /*
    @Override
    public void visit(ConditionalExpression conditionalExpression) {
        // Add instruction
        StringBuffer instruction_str = new StringBuffer();

        if (conditionalExpression.isVariable()) {
            instruction_str.append(conditionalExpression.getVariable().getName());
        } else if (conditionalExpression.isTest()) {
            // Left part
            Parameter left = conditionalExpression.getTest().getLeft();
            instruction_str.append(left.toString());
            // Test part
            instruction_str.append(" ");
            instruction_str.append(conditionalExpression.getTest().getComparator());
            instruction_str.append(" ");
            // Right part
            Parameter right = conditionalExpression.getTest().getRight();
            instruction_str.append(right.toString());
        } else if (conditionalExpression.getOldInstruction()) {
            oldInstruction oldInstruction = conditionalExpression.getInstruction();
            instruction_str.append(oldInstruction.getFunctionPrototype().getName());
            ArrayList<Parameter> parameters = oldInstruction.getParameters();
            instruction_str.append("(");
            if (parameters.size() > 0) {
                int i = 0;
                while (i < parameters.size()) {
                    instruction_str.append(parameters.get(i).toString());
                    if (i < parameters.size()-1) {
                        instruction_str.append(", ");
                    }
                    i++;
                }
            }
            instruction_str.append(")");
        }
        w(instruction_str.toString());
    }
    */

    @Override
    public void visit(CallInstruction callInstruction) {
        callInstruction.getCall().accept(this);
        w(";");
    }

    @Override
    public void visit(ForLoopInstruction forLoopInstruction) {
        w("for :");
        forLoopInstruction.getSequence().accept(this);
        w("endfor;");
    }

    @Override
    public void visit(AssignmentInstruction assignmentInstruction) {
        assignmentInstruction.getAssigned().accept(this);
        w(" = ");
        assignmentInstruction.getSource().accept(this);
        w(";");
    }


    @Override
    public void visit(ConditionalInstruction conditionalInstruction) {
        ConditionalBranch ifBranch = conditionalInstruction.getIfBranch();
        if (ifBranch != null) {
            w("if ");
            ifBranch.getGuard().accept(this);
            w(":");
            w("@Probability = " + ifBranch.getProbability().getValue() + " per " + ifBranch.getProbability().getUnit() + "\n");
            if (ifBranch.getSequence() instanceof UntypedSequence) {
                ifBranch.getSequence().accept(this);
            } else {
                w("call " + ifBranch.getSequence().getName() + ";");
            }
            w("endif;");
        }
        /*
        for (ConditionalBranch cdBranch : conditionalInstruction.getElseifBranches()) {
            w("else if (");
            cdBranch.getConditionalExpression().accept(this);

            w(") {");
            cdBranch.getSequence().accept(this);
            w("}");
        }
        ConditionalBranch elseBranch = conditionalInstruction.getElseBranch();
        if (elseBranch != null) {
            w("else");
            w("{");
            elseBranch.getSequence().accept(this);
            w("}");
        }
        */
    }

    @Override
    public void visit(Operation operation) {
        operation.getLeft().accept(this);
        w(" " + operation.getComparator() + " ");
        operation.getRight().accept(this);
    }

    @Override
    public void visit(Constant constant) {
        w(constant.getValue());
    }

    @Override
    public void visit(Variable variable) {
        w(variable.getName());
    }

    @Override
    public void visit(Function function) {
        // Add function
        String function_name_str = function.getFunctionPrototype().getName();

        w(function_name_str);

        // Add input of function
        w("(");
        int i = 0;
        for (Data parameter : function.getParameters()) {
            parameter.accept(this);
            i += 1;
            if (i < function.getParameters().size()) {
                w(", ");
            }
        }
        w(")");
    }

    @Override
    public void visit(Library library) {
    }

    @Override
    public void visit(FunctionPrototype functionPrototype) {

    }


    // Write code lines into a buffer with automatic indent
    private void w(String s, Object... args) {
        sline += String.format(s, args);

        // Check if a new line must be created
        boolean newline = false;
        if (sline.endsWith(";") || sline.endsWith(":") || sline.endsWith("\n") || sline.isEmpty() ){
            newline = true;
        }

        if (newline) {
            // Set the indent for current line
            logger.dbg(sline);
            if (indent > 0) {
                if (sline.startsWith("endseq;")) indent -= 1;
                else if (sline.startsWith("endif;")) indent -= 1;
            }

            // Add indent and new line
            for (int i = 0; i < indent; i++) {
                sline = "   " + sline;
            }
            if (!sline.endsWith("\n")) sline += "\n";

            // Set the indent for next line
            if (indent < 10) {
                if (sline.startsWith("RealTimeSequence")) indent += 1;
                else if (sline.startsWith("ProcessingSequence")) indent += 1;
                else if (sline.trim().startsWith("if")) indent += 1;
            }

            // Write code line
            this.code.append(String.format("%s",sline));

            // Clear line
            sline = "";
        }
    }
}

/*
            else if (sline.startsWith("RealTimeSequence")) indent = 0;
                    else if (sline.startsWith("ProcessingSequence")) indent = 0;
                    */