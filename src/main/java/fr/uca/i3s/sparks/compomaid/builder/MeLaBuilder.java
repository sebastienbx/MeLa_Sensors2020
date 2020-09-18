package fr.uca.i3s.sparks.compomaid.builder;


import fr.uca.i3s.sparks.MeLaListener;
import fr.uca.i3s.sparks.MeLaParser;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.Probability;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.ConditionalInstruction;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqModeNotFoundException;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.*;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.model.Platform.*;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Stack;


public class MeLaBuilder implements MeLaListener {

    private Logger logger = new Logger();

    private App app;
    private AcqMode current_acq_mode; // ct_mode = current_mode
    private Data current_data;
    private Sequence current_sequence;
    private static int _unamed_sequence_id = 0;
    private Stack<Sequence> saved_sequences = new Stack<>();


    /*
        Retrieve app
    */
    public App retrieve(){
        return this.app;
    }


    /*
        App
    */
    @Override
    public void enterApp(MeLaParser.AppContext ctx) {
        // Create App
        /*
        System.out.println("test");
        ctx.mission().enterRule(this);
        System.out.println("test");
        ctx.mission().enterRule(this);
        System.out.println("test");
        */

        int time_minutes = Integer.parseInt(ctx.mission().parkTime().NUM().getText());
        int depth_m = Integer.parseInt(ctx.mission().missionDepth().NUM().getText());
        app = new App(time_minutes, depth_m);

        // Create acquisition mode
        if (ctx.acqMode().size() > 0) {
            for (MeLaParser.AcqModeContext acqModeContext : ctx.acqMode()) {
                if(acqModeContext.continuousAcqMode() != null) {
                    app.addAcqMode(new ContinuousAcqMode(acqModeContext.continuousAcqMode().acqModeName().getText()));
                } else if(acqModeContext.singleAcqMode() != null) {
                    app.addAcqMode(new ShortAcqMode(acqModeContext.singleAcqMode().acqModeName().getText()));
                }
            }
        } else {
            logger.err("No acquisition mode defined");
        }

        // Scheduling of each mission step
        if (ctx.coordinator().descentAcqModes() != null) {
            parseMissionStepScheduling(ctx.coordinator().descentAcqModes().scheduling(), app.getDescent());
        }
        if (ctx.coordinator().parkAcqModes() != null) {
            parseMissionStepScheduling(ctx.coordinator().parkAcqModes().scheduling(), app.getPark());
        }
        if (ctx.coordinator().ascentAcqModes() != null) {
            parseMissionStepScheduling(ctx.coordinator().ascentAcqModes().scheduling(), app.getAscent());
        }

        /*
        // Ascent request permission
        if (ctx.ascentRequestPermission() != null) {
            if (ctx.ascentRequestPermission().navigationPermissionType().getText().equals("allowed")) {
                app.setAscentRequestPermission(NavigationPermission.ALLOWED);
            } else if (ctx.ascentRequestPermission().navigationPermissionType().getText().equals("forbidden")) {
                app.setAscentRequestPermission(NavigationPermission.FORBIDDEN);
            } else {
                logger.err("unknown permission: " + ctx.ascentRequestPermission().navigationPermissionType().getText());
            }
        }
        // Landing request permission
        if (ctx.landingRequestPermission() != null) {
            if (ctx.landingRequestPermission().navigationPermissionType().getText().equals("allowed")) {
                app.setLandingRequestPermission(NavigationPermission.ALLOWED);
            } else if (ctx.landingRequestPermission().navigationPermissionType().getText().equals("forbidden")) {
                app.setLandingRequestPermission(NavigationPermission.FORBIDDEN);
            } else {
                logger.err("unknown permission: " + ctx.landingRequestPermission().navigationPermissionType().getText());
            }
        }
        */
    }


    private void parseMissionStepScheduling(MeLaParser.SchedulingContext schedulingContext, MissionStep missionStep) {
        for (MeLaParser.ContinuousScheduleContext continuousScheduleContext : schedulingContext.continuousSchedule()) {
            String acqModeName = continuousScheduleContext.acqModeName().getText();
            AcqMode acqMode;
            try {
                acqMode = app.getAcqMode(acqModeName);
                missionStep.addSchedule(acqMode);
            } catch (AcqModeNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (MeLaParser.SequenceScheduleContext sequenceScheduleContext : schedulingContext.sequenceSchedule()) {
            String acqModeName = sequenceScheduleContext.acqModeName().getText();
            int period = Integer.parseInt(sequenceScheduleContext.NUM().getText());
            String unit = sequenceScheduleContext.sequenceScheduleTimeUnit().getText();
            AcqMode acqMode;
            try {
                acqMode = app.getAcqMode(acqModeName);
                missionStep.addSchedule(acqMode, period, unit);
            } catch (AcqModeNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void exitApp(MeLaParser.AppContext ctx) {

    }

    @Override
    public void enterMission(MeLaParser.MissionContext ctx) {

    }

    @Override
    public void exitMission(MeLaParser.MissionContext ctx) {

    }

    @Override
    public void enterParkTime(MeLaParser.ParkTimeContext ctx) {

    }

    @Override
    public void exitParkTime(MeLaParser.ParkTimeContext ctx) {

    }

    @Override
    public void enterMissionDepth(MeLaParser.MissionDepthContext ctx) {

    }

    @Override
    public void exitMissionDepth(MeLaParser.MissionDepthContext ctx) {

    }

    @Override
    public void enterCoordinator(MeLaParser.CoordinatorContext ctx) {

    }

    @Override
    public void exitCoordinator(MeLaParser.CoordinatorContext ctx) {

    }

    @Override
    public void enterDescentAcqModes(MeLaParser.DescentAcqModesContext ctx) {

    }

    @Override
    public void exitDescentAcqModes(MeLaParser.DescentAcqModesContext ctx) {

    }

    @Override
    public void enterParkAcqModes(MeLaParser.ParkAcqModesContext ctx) {

    }

    @Override
    public void exitParkAcqModes(MeLaParser.ParkAcqModesContext ctx) {

    }

    @Override
    public void enterAscentAcqModes(MeLaParser.AscentAcqModesContext ctx) {

    }

    @Override
    public void exitAscentAcqModes(MeLaParser.AscentAcqModesContext ctx) {

    }

    @Override
    public void enterScheduling(MeLaParser.SchedulingContext ctx) {

    }

    @Override
    public void exitScheduling(MeLaParser.SchedulingContext ctx) {

    }

    @Override
    public void enterSequenceSchedule(MeLaParser.SequenceScheduleContext ctx) {

    }

    @Override
    public void exitSequenceSchedule(MeLaParser.SequenceScheduleContext ctx) {

    }

    @Override
    public void enterSequenceScheduleTimeUnit(MeLaParser.SequenceScheduleTimeUnitContext ctx) {

    }

    @Override
    public void exitSequenceScheduleTimeUnit(MeLaParser.SequenceScheduleTimeUnitContext ctx) {

    }

    @Override
    public void enterContinuousSchedule(MeLaParser.ContinuousScheduleContext ctx) {

    }

    @Override
    public void exitContinuousSchedule(MeLaParser.ContinuousScheduleContext ctx) {

    }




    /*
        Variable
    */

    @Override
    public void enterVariable(MeLaParser.VariableContext ctx) {
        DataType varType = null;
        String vt = ctx.varType().getText();
        switch (vt) {
            case "Bool":
                varType = DataType.Bool;
                break;
            case "Int":
                varType = DataType.Int;
                break;
            case "Float":
                varType = DataType.Float;
                break;
            case "ComplexInt":
                varType = DataType.ComplexInt;
                break;
            case "ComplexFloat":
                varType = DataType.ComplexFloat;
                break;
            case "ArrayInt":
                varType = DataType.ArrayInt;
                break;
            case "ArrayFloat":
                varType = DataType.ArrayFloat;
                break;
            case "ArrayComplexInt":
                varType = DataType.ArrayComplexInt;
                break;
            case "ArrayComplexFloat":
                varType = DataType.ArrayComplexFloat;
                break;
            case "BufferInt":
                varType = DataType.BufferInt;
                break;
            case "BufferFloat":
                varType = DataType.BufferFloat;
                break;
            case "FFTInt":
                varType = DataType.FFTInt;
                break;
            case "FFTFloat":
                varType = DataType.FFTFloat;
                break;
            case "IIRInt":
                varType = DataType.IIRInt;
                break;
            case "IIRFloat":
                varType = DataType.IIRFloat;
                break;
            case "FIRInt":
                varType = DataType.FIRInt;
                break;
            case "FIRFloat":
                varType = DataType.FIRFloat;
                break;
            case "StaLtaInt":
                varType = DataType.StaLtaInt;
                break;
            case "StaLtaFloat":
                varType = DataType.StaLtaFloat;
                break;
            case "TriggerInt":
                varType = DataType.TriggerInt;
                break;
            case "TriggerFloat":
                varType = DataType.TriggerFloat;
                break;
            case "CDF24Int":
                varType = DataType.CDF24Int;
                break;
            case "CDF24Float":
                varType = DataType.CDF24Float;
                break;
            case "DistributionInt":
                varType = DataType.DistributionInt;
                break;
            case "DistributionFloat":
                varType = DataType.DistributionFloat;
                break;
            case "File":
                varType = DataType.File;
                break;

                default:
                    logger.err("Unknown data type \"" + vt + "\"");
        }

        String varName = ctx.varName().getText();

        ArrayList<Constant> vParam = new ArrayList<>();

        for (MeLaParser.NumParamContext vParamCtx : ctx.numParam ()) {
            Constant vp;
            if(vParamCtx.NUM() != null) {
                vp = new Constant(DataType.Int, vParamCtx.getText());
            } else if (vParamCtx.DEC() != null) {
                vp = new Constant(DataType.Float, vParamCtx.getText());
            } else if (vParamCtx.EXPONENT() != null) {
                vp = new Constant(DataType.Float, vParamCtx.getText());
            } else {
                vp = new Constant(DataType.String, vParamCtx.getText());
            }
            vParam.add(vp);
        }

        Variable var = new Variable(varType, varName, vParam);
        current_acq_mode.addVariable(var);
    }

    @Override
    public void exitVariable(MeLaParser.VariableContext ctx) {

    }

    @Override
    public void enterVarType(MeLaParser.VarTypeContext ctx) {

    }

    @Override
    public void exitVarType(MeLaParser.VarTypeContext ctx) {

    }

    @Override
    public void enterNumParam(MeLaParser.NumParamContext ctx) {

    }

    @Override
    public void exitNumParam(MeLaParser.NumParamContext ctx) {

    }

    /*
    @Override
    public void enterRecordfile(MeLaParser.RecordfileContext ctx) {
        Variable var = new Variable(DataType.RecordFile, ctx.varName().getText(), 1);
        current_acq_mode.addVariable(var);
    }

    @Override
    public void exitRecordfile(MeLaParser.RecordfileContext ctx) {

    }
    */

    @Override
    public void enterMainsequence(MeLaParser.MainsequenceContext ctx) {

    }

    @Override
    public void exitMainsequence(MeLaParser.MainsequenceContext ctx) {

    }

    @Override
    public void enterSubsequence(MeLaParser.SubsequenceContext ctx) {

    }

    @Override
    public void exitSubsequence(MeLaParser.SubsequenceContext ctx) {

    }





    /*
        Acquisition mode
    */

    @Override
    public void enterAcqMode(MeLaParser.AcqModeContext ctx) {
        /* Get the acquisition mode */
        String acqModeName;
        if (ctx.singleAcqMode() != null) {
            acqModeName = ctx.singleAcqMode().acqModeName().getText();
        } else {
            acqModeName = ctx.continuousAcqMode().acqModeName().getText();
        }

        try {
            current_acq_mode = app.getAcqMode(acqModeName);
        } catch (AcqModeNotFoundException e) {
            e.printStackTrace();
        }

        if (ctx.singleAcqMode() != null) {
            acqModeName = ctx.singleAcqMode().acqModeName().getText();
        } else {
            acqModeName = ctx.continuousAcqMode().acqModeName().getText();
        }

        if (ctx.mainsequence().processingSequence() != null) {
            String sequenceName = ctx.mainsequence().processingSequence().sequenceName().getText();
            Sequence sequence = new ProcessingSequence(sequenceName, current_acq_mode);
            current_acq_mode.addSequence(sequence);
        } else {
            String sequenceName = ctx.mainsequence().realTimeSequence().sequenceName().getText();
            Sequence sequence = new RealTimeSequence(sequenceName, current_acq_mode);
            current_acq_mode.addSequence(sequence);
        }
        for (MeLaParser.SubsequenceContext subsequenceContext : ctx.subsequence()) {
            if (subsequenceContext.processingSequence() != null) {
                String sequenceName = subsequenceContext.processingSequence().sequenceName().getText();
                Sequence sequence = new ProcessingSequence(sequenceName, current_acq_mode);
                current_acq_mode.addSequence(sequence);
            } else {
                String sequenceName = subsequenceContext.realTimeSequence().sequenceName().getText();
                Sequence sequence = new RealTimeSequence(sequenceName, current_acq_mode);
                current_acq_mode.addSequence(sequence);
            }
        }
    }

    @Override
    public void exitAcqMode(MeLaParser.AcqModeContext ctx) {

    }

    @Override
    public void enterContinuousAcqMode(MeLaParser.ContinuousAcqModeContext ctx) {

    }

    @Override
    public void exitContinuousAcqMode(MeLaParser.ContinuousAcqModeContext ctx) {

    }

    @Override
    public void enterSingleAcqMode(MeLaParser.SingleAcqModeContext ctx) {

    }

    @Override
    public void exitSingleAcqMode(MeLaParser.SingleAcqModeContext ctx) {

    }

    @Override
    public void enterAcqModeName(MeLaParser.AcqModeNameContext ctx) {

    }

    @Override
    public void exitAcqModeName(MeLaParser.AcqModeNameContext ctx) {

    }

    @Override
    public void enterSensorName(MeLaParser.SensorNameContext ctx) {

    }

    @Override
    public void exitSensorName(MeLaParser.SensorNameContext ctx) {

    }

    @Override
    public void enterSensorFrequency(MeLaParser.SensorFrequencyContext ctx) {

    }

    @Override
    public void exitSensorFrequency(MeLaParser.SensorFrequencyContext ctx) {

    }

    @Override
    public void enterInput(MeLaParser.InputContext ctx) {
        /* Get input sensor */
        String sensorName = ctx.sensorName().getText();
        PeriodicSensor periodicSensor = Library.getInstance().getPeriodicSensor(sensorName);

        double sensorfrequency = Double.parseDouble(ctx.sensorFrequency().getText());

        SensorConfiguration sensorConfiguration = new SensorConfiguration(periodicSensor, sensorfrequency);
        current_acq_mode.setSensorConfiguration(sensorConfiguration);

        /* Define input variable */
        ArrayList<Constant> parameters = new ArrayList<>();
        Constant p1 = new Constant(DataType.Int, ctx.inputVarLen().getText());
        parameters.add(p1); // length of the input variable

        String inputVarName = ctx.inputVar().getText();
        Variable inputVar = new Variable(DataType.ArrayInt, inputVarName, parameters);
        current_acq_mode.setInputVariable(inputVar);
    }

    @Override
    public void exitInput(MeLaParser.InputContext ctx) {

    }

    @Override
    public void enterInputVar(MeLaParser.InputVarContext ctx) {

    }

    @Override
    public void exitInputVar(MeLaParser.InputVarContext ctx) {

    }

    @Override
    public void enterInputVarLen(MeLaParser.InputVarLenContext ctx) {

    }

    @Override
    public void exitInputVarLen(MeLaParser.InputVarLenContext ctx) {

    }

    @Override
    public void enterVariables(MeLaParser.VariablesContext ctx) {

    }

    @Override
    public void exitVariables(MeLaParser.VariablesContext ctx) {

    }




    /*
        Sequence of instructions
    */

    @Override
    public void enterRealTimeSequence(MeLaParser.RealTimeSequenceContext ctx) {
        try {
            current_sequence = current_acq_mode.getSequence(ctx.sequenceName().getText());
        } catch (SequenceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitRealTimeSequence(MeLaParser.RealTimeSequenceContext ctx) {

    }

    @Override
    public void enterProcessingSequence(MeLaParser.ProcessingSequenceContext ctx) {
        try {
            current_sequence = current_acq_mode.getSequence(ctx.sequenceName().getText());
        } catch (SequenceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitProcessingSequence(MeLaParser.ProcessingSequenceContext ctx) {

    }

    @Override
    public void enterSequenceName(MeLaParser.SequenceNameContext ctx) {

    }

    @Override
    public void exitSequenceName(MeLaParser.SequenceNameContext ctx) {

    }

    @Override
    public void enterInstruction(MeLaParser.InstructionContext ctx) {

    }

    @Override
    public void exitInstruction(MeLaParser.InstructionContext ctx) {

    }

    @Override
    public void enterAssignmentInstruction(MeLaParser.AssignmentInstructionContext ctx) {
        // Assigned variable
        Variable assigned = null;
        String var_name = ctx.varUse().varName().getText();
        assigned = current_acq_mode.getVariable(var_name);

        ctx.data().enterRule(this);

        current_sequence.addInstruction(new AssignmentInstruction(assigned, current_data));
    }

    @Override
    public void exitAssignmentInstruction(MeLaParser.AssignmentInstructionContext ctx) {

    }

    @Override
    public void enterCallInstruction(MeLaParser.CallInstructionContext ctx) {
        ctx.data().enterRule(this);
        current_sequence.addInstruction(new CallInstruction(current_data));
    }

    @Override
    public void exitCallInstruction(MeLaParser.CallInstructionContext ctx) {

    }

    @Override
    public void enterCondition(MeLaParser.ConditionContext ctx) {

        // Search or create target sequence
        Sequence targetSequence = null;
        if (ctx.ifbranch().call() != null) {
            String trueSequenceName = ctx.ifbranch().call().sequenceName().getText();
            try {
                targetSequence = current_acq_mode.getSequence(trueSequenceName);
            } catch (SequenceNotFoundException e) {
                e.printStackTrace();
            }
        } else if (ctx.ifbranch().instruction().size() > 0) {
            targetSequence = new UntypedSequence("_Condition" + _unamed_sequence_id + "_", current_acq_mode);
            _unamed_sequence_id ++;
            current_acq_mode.addSequence(targetSequence);
        }

        // Get the guard data
        ctx.ifbranch().data().enterRule(this);
        Data guard = current_data;

        // Get probability
        int nb = Integer.parseInt(ctx.ifbranch().probability().nbPerTimeUnit().NUM().getText());
        String unit = ctx.ifbranch().probability().nbPerTimeUnit().perTimeUnit().getText();
        unit = unit.substring(4);
        Probability probability = new Probability(nb, unit);

        // Create the conditional branch (only one if actually)
        ConditionalBranch conditionalBranch = new ConditionalBranch(guard, targetSequence, probability);

        // Create the conditional instruction
        ConditionalInstruction conditionnalInstruction = new ConditionalInstruction();
        conditionnalInstruction.addBranch(conditionalBranch);

        current_sequence.addInstruction(conditionnalInstruction);

        saved_sequences.push(current_sequence);
        current_sequence = targetSequence;
    }

    @Override
    public void exitCondition(MeLaParser.ConditionContext ctx) {
        current_sequence = saved_sequences.pop();
    }

    @Override
    public void enterIfbranch(MeLaParser.IfbranchContext ctx) {
        /*
        // Get condition of the branch
        Instruction conditionalInstruction = null;
        Operation guard = null;

        ctx.data().enterRule(this);

        conditionalInstruction = new ConditionalInstruction();
        current_data


        // It is a conditional variable
        if (ctx.conditionalExpression().conditionalVariable() != null) {
            String varName = ctx.conditionalExpression().conditionalVariable().varName().getText();
            try {
                conditionalVariable = ct_acq_mode.getVariable(varName);
            } catch (VariableNotFoundException e) {
                e.printStackTrace();
            }
        }

        // It is a conditional expression
        else if (ctx.conditionalExpression().conditionalInstruction() != null) {
            FunctionPrototype conditionalFunctionPrototype = null;
            // Get function name
            String functionName = ctx.conditionalExpression().conditionalInstruction().functionName().getText();
            try {
                conditionalFunctionPrototype = Library.getInstance().getFunction(functionName);
            } catch (FunctionNotFoundException e) {
                e.printStackTrace();
            }
            // Get parameters
            ArrayList<Parameter> parameters = new ArrayList<>();
            for (MeLaParser.IinputContext ivar : ctx.conditionalExpression().conditionalInstruction().iinput()) {
                try {
                    parameters.add(ct_acq_mode.getVariable(ivar.getText()));
                } catch (VariableNotFoundException e) {
                    e.printStackTrace();
                }
            }
            conditionalOldInstruction = new oldInstruction(conditionalFunctionPrototype, parameters, null);
        }

        // It is a conditional test
        else if (ctx.conditionalExpression().conditionalTest() != null) {
            Parameter left = null;
            Parameter rigth = null;
            if (ctx.conditionalExpression().conditionalTest().testVarL().varName() != null) {
                String varName = ctx.conditionalExpression().conditionalTest().testVarL().getText();
                try {
                    left = ct_acq_mode.getVariable(varName);
                } catch (VariableNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (ctx.conditionalExpression().conditionalTest().testVarL().NUM() != null) {
                String cste = ctx.conditionalExpression().conditionalTest().testVarL().NUM().getText();
                left = new Constant(new ConstInt(), cste);
            } else {
                logger.err("No correspondance for testVarL");
            }
            if (ctx.conditionalExpression().conditionalTest().testVarR().varName() != null) {
                String varName = ctx.conditionalExpression().conditionalTest().testVarL().getText();
                try {
                    rigth = ct_acq_mode.getVariable(varName);
                } catch (VariableNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (ctx.conditionalExpression().conditionalTest().testVarR().NUM() != null) {
                String cste = ctx.conditionalExpression().conditionalTest().testVarR().NUM().getText();
                rigth = new Constant(new ConstInt(), cste);
            } else {
                logger.err("No correspondance for testVarR");
            }
            String testerString = ctx.conditionalExpression().conditionalTest().tester().getText();
            conditionalTest = new Test(left, testerString, rigth);

        } else {
            logger.err("The branch doesn't contain any conditional expression");
        }




        // Check validity of the condition
        if (conditionalVariable != null) {
            if (!(conditionalVariable.getDataType() instanceof Bool)) {
                logger.err("Conditional variable \"" + conditionalVariable.getName() + "\" is not a boolean");
            }
        } else if (conditionalOldInstruction != null) {
            if (!(conditionalOldInstruction.getFunctionPrototype().getReturnType() instanceof Bool)) {
                logger.err("Conditional function \"" + conditionalOldInstruction.getFunctionPrototype().getName() + "\" doesn't return a boolean");
            }
        }

        // Create the conditional expression
        if (conditionalVariable != null) {
            ct_conditionalExpression = new ConditionalExpression(conditionalVariable);
        } else if (conditionalOldInstruction != null) {
            ct_conditionalExpression = new ConditionalExpression(conditionalOldInstruction);
        } else if (conditionalTest != null) {
            ct_conditionalExpression = new ConditionalExpression(conditionalTest);
        }

        // Get probability per count to enter in the conditional branch
        ct_probability = 0;
        MeLaParser.ProbabilityContext pbctx = ctx.probability();
        if (pbctx.dec() != null) {
            ct_probability = Double.parseDouble(pbctx.dec().getText());
        } else if (pbctx.ratio() != null) {
            ct_probability = Double.parseDouble(pbctx.ratio().NUM(0).getText());
            ct_probability /= Double.parseDouble(pbctx.ratio().NUM(1).getText());

        } else if (pbctx.percent() != null) {
            if (pbctx.percent().DEC() != null) {
                ct_probability = Double.parseDouble(pbctx.percent().DEC().getText());
                ct_probability = ct_probability / 100;
            } else if (pbctx.percent().NUM() != null) {
                ct_probability = Integer.parseInt(pbctx.percent().NUM().getText());
                ct_probability = ct_probability / 100;
            }
        } else if (pbctx.nbPerTimeUnit() != null) {
            double nb = Integer.parseInt(pbctx.nbPerTimeUnit().NUM().getText());
            String unit = pbctx.nbPerTimeUnit().timeUnit().getText();
            if (unit.equals("sec")) {
                ct_probability = nb * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("min")) {
                ct_probability = nb / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("hour")) {
                ct_probability = nb / 60. / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("day")) {
                ct_probability = nb / 24. / 60. / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("week")) {
                ct_probability = nb / 7. / 24. / 60. / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("month")) {
                ct_probability = nb / 30. / 24. / 60. / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            } else if (unit.equals("year")) {
                ct_probability = nb / 365. / 24. / 60. / 60. * ct_acq_mode.getAbstractTimer().getPeriod_s();
            }  else {
                logger.err("Unit is not compatible");
            }
        }
        ct_remaining_proba -= ct_probability;

        // Find targeted sequence

        saved_sequence = ct_sequence;
        if (ctx.ggoto() != null) {
            String trueSequenceName = ctx.ggoto().sequenceName().getText();
            try {
                ct_sequence = ct_acq_mode.getSequence(trueSequenceName);
            } catch (SequenceNotFoundException e) {
                ct_sequence = new Sequence(trueSequenceName, ct_acq_mode, true);
            }
        } else if (ctx.instruction().size() > 0) {
            ct_sequence = new Sequence("_Condition" + ct_sequence_id + "_", ct_acq_mode, false);
            ct_sequence_id ++;
            //ct_acq_mode.addSequence(ct_sequence);
        }
        */
    }

    @Override
    public void exitIfbranch(MeLaParser.IfbranchContext ctx) {
        /*
        // Create the branch
        Sequence sequenceToAdd = ct_sequence;
        ct_sequence = saved_sequence;
        //sequenceToAdd.addParent(ct_sequence);
        ConditionalBranch cdBranch = new ConditionalBranch(ct_conditionalExpression, sequenceToAdd, ct_probability);
        ct_sequence.getLastCondition().addBranch(cdBranch);
        */
    }

    @Override
    public void enterConditionalInstruction(MeLaParser.ConditionalInstructionContext ctx) {

    }

    @Override
    public void exitConditionalInstruction(MeLaParser.ConditionalInstructionContext ctx) {

    }

    @Override
    public void enterForLoopInstruction(MeLaParser.ForLoopInstructionContext ctx) {
        String var_name = ctx.varName(0).getText();
        Variable index = current_acq_mode.getVariable(var_name);

        var_name = ctx.varName(1).getText();
        Variable value = current_acq_mode.getVariable(var_name);

        var_name = ctx.varName(2).getText();
        Variable forVar = current_acq_mode.getVariable(var_name);


        Sequence targetSequence = new UntypedSequence("_Condition" + _unamed_sequence_id + "_", current_acq_mode);
        _unamed_sequence_id ++;
        current_acq_mode.addSequence(targetSequence);

        ForLoopInstruction forLoopInstruction = new ForLoopInstruction(index, value, forVar, targetSequence);
        current_sequence.addInstruction(forLoopInstruction);

        saved_sequences.push(current_sequence);
        current_sequence = targetSequence;
    }

    @Override
    public void exitForLoopInstruction(MeLaParser.ForLoopInstructionContext ctx) {
        current_sequence = saved_sequences.pop();
    }

    @Override
    public void enterProbability(MeLaParser.ProbabilityContext ctx) {

    }

    @Override
    public void exitProbability(MeLaParser.ProbabilityContext ctx) {

    }

    @Override
    public void enterNbPerTimeUnit(MeLaParser.NbPerTimeUnitContext ctx) {

    }

    @Override
    public void exitNbPerTimeUnit(MeLaParser.NbPerTimeUnitContext ctx) {

    }

    @Override
    public void enterPerTimeUnit(MeLaParser.PerTimeUnitContext ctx) {

    }

    @Override
    public void exitPerTimeUnit(MeLaParser.PerTimeUnitContext ctx) {

    }

    @Override
    public void enterCall(MeLaParser.CallContext ctx) {

    }

    @Override
    public void exitCall(MeLaParser.CallContext ctx) {

    }





    /*
     Sequence of instructions
    */

    @Override
    public void enterData(MeLaParser.DataContext ctx) {
        if (ctx.functionCall() != null) {
            ctx.functionCall().enterRule(this);
        } else if (ctx.operation() != null) {
            ctx.operation().enterRule(this);
        } else if (ctx.varUse() != null) {
            ctx.varUse().enterRule(this);
        } else if (ctx.constant() != null) {
            ctx.constant().enterRule(this);
        }
    }

    @Override
    public void exitData(MeLaParser.DataContext ctx) {

    }

    @Override
    public void enterVarUse(MeLaParser.VarUseContext ctx) {
        current_data = current_acq_mode.getVariable(ctx.varName().getText());
    }

    @Override
    public void exitVarUse(MeLaParser.VarUseContext ctx) {

    }

    @Override
    public void enterConstant(MeLaParser.ConstantContext ctx) {
        if (ctx.STRING() != null) {
            current_data = new Constant(DataType.String, ctx.STRING().getText());
        } else if (ctx.NUM() != null) {
            current_data = new Constant(DataType.Int, ctx.NUM().getText());
        } else if (ctx.DEC() != null) {
            current_data = new Constant(DataType.Float, ctx.DEC().getText());
        } else {
            current_data = null;
        }
    }

    @Override
    public void exitConstant(MeLaParser.ConstantContext ctx) {

    }

    @Override
    public void enterFunctionCall(MeLaParser.FunctionCallContext ctx) {
        // Get function name
        String functionName = ctx.functionName().getText();

        // Get function parameters
        ArrayList<Data> parameters = new ArrayList<>();
        for (MeLaParser.DataContext data : ctx.data()) {
            data.enterRule(this);
            parameters.add(current_data);

            /*
            if (parameter.varName() != null) {
                String name = parameter.varName().getText();
                try {
                    parameters.add(current_acq_mode.getVariable(name));
                } catch (VariableNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (parameter.constant() != null) {
                if (parameter.constant().NUM() != null) {
                    String name = parameter.constant().NUM().getText();
                    parameters.add(new Constant(new ConstInt(), name));
                }
                if (parameter.constant().DEC() != null) {
                    String name = parameter.constant().NUM().getText();
                    parameters.add(new Constant(new ConstFloat(), name));
                }
                if (parameter.constant().STRING() != null) {
                    String name = parameter.constant().STRING().getText();
                    parameters.add(new Constant(new ConstStr(), name));
                }
            }
            */
        }

        // Get function object
        FunctionPrototype functionPrototype = null;
        functionPrototype = Library.getInstance().getFunctionPrototype(functionName, parameters);

        // Create function call
        current_data = new Function(functionPrototype, parameters);
    }

    @Override
    public void exitFunctionCall(MeLaParser.FunctionCallContext ctx) {

    }

    @Override
    public void enterFunctionName(MeLaParser.FunctionNameContext ctx) {

    }

    @Override
    public void exitFunctionName(MeLaParser.FunctionNameContext ctx) {

    }

    @Override
    public void enterOperation(MeLaParser.OperationContext ctx) {
        String operator = ctx.operator().get(0).getText();

        // Get left operand
        if (ctx.leftOperand().constant() != null) {
            ctx.leftOperand().constant().enterRule(this);
        } else if (ctx.leftOperand().varUse() != null) {
            ctx.leftOperand().varUse().enterRule(this);
        }
        Data leftOperand = current_data;

        // Get right operand
        if (ctx.rightOperand().get(0).constant() != null) {
            ctx.rightOperand().get(0).constant().enterRule(this);
        } else if (ctx.rightOperand().get(0).varUse() != null) {
            ctx.rightOperand().get(0).varUse().enterRule(this);
        }
        Data rightOperand = current_data;

        Operation build_operation = new Operation(leftOperand, operator, rightOperand);


        for(int i = 1; i < ctx.operator().size(); i++) {
            operator = ctx.operator().get(i).getText();
            if (ctx.rightOperand().get(i).constant() != null) {
                ctx.rightOperand().get(i).constant().enterRule(this);
            } else if (ctx.rightOperand().get(i).varUse() != null) {
                ctx.rightOperand().get(i).varUse().enterRule(this);
            }
            rightOperand = current_data;
            leftOperand = build_operation;
            build_operation = new Operation(leftOperand, operator, rightOperand);
        }

        current_data = build_operation;
    }

    @Override
    public void exitOperation(MeLaParser.OperationContext ctx) {

    }

    @Override
    public void enterOperator(MeLaParser.OperatorContext ctx) {

    }

    @Override
    public void exitOperator(MeLaParser.OperatorContext ctx) {

    }

    @Override
    public void enterLeftOperand(MeLaParser.LeftOperandContext ctx) {

    }

    @Override
    public void exitLeftOperand(MeLaParser.LeftOperandContext ctx) {

    }

    @Override
    public void enterRightOperand(MeLaParser.RightOperandContext ctx) {

    }

    @Override
    public void exitRightOperand(MeLaParser.RightOperandContext ctx) {

    }




    /*
        Comment
    */

    @Override
    public void enterComment(MeLaParser.CommentContext ctx) {

    }

    @Override
    public void exitComment(MeLaParser.CommentContext ctx) {

    }

    @Override
    public void enterVarName(MeLaParser.VarNameContext ctx) {

    }

    @Override
    public void exitVarName(MeLaParser.VarNameContext ctx) {

    }




    /*
        Default
    */

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

}

