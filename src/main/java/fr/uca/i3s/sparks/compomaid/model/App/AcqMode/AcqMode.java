package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.Probability;
import fr.uca.i3s.sparks.compomaid.model.App.VariableNotFoundException;
import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


import java.util.*;

public abstract class AcqMode implements Visitable {
    private Logger logger = new Logger(true);

    private String name;
    private ArrayList<Sequence> sequences;
    private SensorConfiguration sensorConf;
    private ArrayList<Variable> variables;
    private Variable inputVariable;
    private int priority = -1;

    public AcqMode(String name) {
        this.name = name;
        this.sensorConf = null;
        variables = new ArrayList<>();
        sequences = new ArrayList<>();
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void setInputVariable(Variable variable) {
        variables.add(variable);
        inputVariable = variable;
    }

    public Variable getInputVariable() {
        return inputVariable;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public Variable getVariable(String name) {
        Variable find = null;
        for(Variable v : variables) {
            if(v.getName().equals(name)){
                find = v;
            }
        }
        if (find == null){
            logger.err("variable \"" + name + "\" have not been declared");
        }
        return find;
    }

    public Sequence getMainSequence() {
        return sequences.get(0);
    }

    public void addSequence(Sequence sequence) {
        this.sequences.add(sequence);
    }

    public ArrayList<Sequence> getSequences() {
        return sequences;
    }

    public Sequence getSequence (String sequenceName) throws SequenceNotFoundException {
        Sequence match = null;
        for (Sequence sq : this.getSequences()) {
            if (sq.getName().equals(sequenceName)){
                match = sq;
            }
        }
        if (match == null) {
            throw new SequenceNotFoundException("sequence \"" + sequenceName + "\" not found");
        }
        return match;
    }

    public Sequence getSequence (Instruction instruction) {
        Sequence match = null;
        for (Sequence sq : this.getSequences()) {
            if (sq.contains(instruction)){
                match = sq;
            }
        }
        if (match == null) {
            logger.err("Instruction \"" + instruction + "\" doesn't exist in AcqMode \"" + name + "\"");
        }
        return match;
    }

    public ArrayList<Probability> getSequenceProbability (Sequence sequence) {
        ArrayList<Probability> probabilities = new ArrayList<>();
        for (Sequence sq : this.getSequences()) {
            for (ConditionalInstruction condition : sq.getConditions()) {
                for (ConditionalBranch conditionalBranch : condition.getConditionalBranches()) {
                    if (conditionalBranch.getSequence().equals(sequence)) {
                        probabilities.add(conditionalBranch.getProbability());
                    }
                }
            }
        }
        if (probabilities.size() == 0) {
            logger.log(sequence.getInstructions().get(0).toString());
            logger.err("No probability found");
            probabilities.add(new Probability(0, "min"));
        }
        return probabilities;
    }

    public boolean contains (Instruction oldInstruction) {
        boolean contains = false;
        for (Sequence sq : this.getSequences()) {
            if (sq.contains(oldInstruction)){
                contains = true;
            }
        }
        return contains;
    }

    public String getName() {
        return name;
    }

    public void setSensorConfiguration(SensorConfiguration sensorConf) {
        this.sensorConf = sensorConf;
    }

    public SensorConfiguration getSensorConfiguration() {
        return sensorConf;
    }

    public double getSensorActivationTime_s() {
        return inputVariable.getLength() * sensorConf.getPeriod_s();
    }

    public ArrayList<Probability> getAscentProbabilities() {
        ArrayList<Probability> probabilities = new ArrayList<>();
        for (Sequence sequence : sequences) {
            if (sequence.containAscentRequest()) {
                probabilities.addAll(this.getSequenceProbability(sequence));
            }
        }
        return probabilities;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}


    /*
    public ArrayList<Branch> getExecutionPaths() {
        ArrayList<Branch> branchs = mainSequence.getExecutionPaths();

        // Print branchs

        //logger.dbg("Execution branchs of " + this.getName() + ":");
        //for (Branch branch : branchs) {
        //    logger.dbg("* " + branch);
        //}

        return branchs;
    }

    public ArrayList<Branch> getStructuralBranchs() {
        ArrayList<Branch> branchs = mainSequence.getStructuralBranchs();

        // Print branchs
        //logger.dbg("Structural branchs of " + this.getName() + ":");
        //for (Branch branch : branchs) {
        //    logger.dbg("* " + branch);
        //}

        return branchs;
    }
    */

    /*
    public HashMap<Sequence, Double> getSequenceProba() {
        ArrayList<Branch> branchs = mainSequence.getStructuralBranchs();
        HashMap<Sequence, Double> sequenceProba = new HashMap<>();
        for (Sequence sq : this.getSequences()) {
            if (!sequenceProba.containsKey(sq)) {
                sequenceProba.put(sq, 0.);
            }
        }
        for (Sequence key : sequenceProba.keySet()) {
            for (Branch branch : branchs) {
                ArrayList<Sequence> brSeq = branch.getSequences();
                if (brSeq.get(brSeq.size()-1) == key) {
                    double newProba = sequenceProba.get(key) + branch.getProbability();
                    sequenceProba.put(key , newProba);
                }
            }
        }

        return sequenceProba;
    }
    */

/*
    @Override
    public double getTimeConsumption() {
        //this.getPeriodicSensor().getPeriod_us()
        return 0;
    }

    @Override
    public double getEnergyConsumption() {
        // Energy consumed in mW.us
        double totalEnergy = 0;

        //HashMap<Sequence, Double> sequenceProba = this.getSequenceProba();
        //for (Sequence key : sequenceProba.keySet()) {
        //    double seqEnergy = sequenceProba.get(key) * key.getEnergyConsumption();
        //    //logger.dbg("Energy consumed by " + key.getName() + " = " + seqEnergy);
        //    totalEnergy += seqEnergy;
        //}

        //logger.dbg("Total energy consumed by " + this.getName() + " = " + totalEnergy);
        return totalEnergy;
    }

    @Override
    public double getPowerConsumption() {
        // Power consumed in mW
        //logger.dbg("Acqmode: " + this.getName() + " => energy " + this.getEnergyConsumption() + " time " + this.getTimeConsumption() +
        //        " power " + this.getEnergyConsumption() / this.getTimeConsumption());
        return this.getEnergyConsumption() / this.getTimeConsumption();
    }
*/



    /*
    public boolean containAscentRequest() {
        if (this.getAscentRequests().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean containLandingRequest() {
        if (this.getLandingRequests().size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    */

    /*
    public int getAscentRequestTime_min() {
        // Return minus 1 if the acquisition mode doesn't contain any ascent request
        if (!this.containAscentRequest()) {
            return -1;
        }
        // Find the mean time until to reach the ascent request instruction
        int ascentRequestTime_min = -2;
        for (Branch branch : this.getBranchs()) {
            if (branch.containAscentRequest()) {
                // Probability to reach the ascent == branch probability
                double probability_per_cycle = branch.getProbability();
                // Get the period of execution of the acquisition mode (and not the execution time of instructions
                // in the branch given by branch.getTimeConsumption())
                double executionTime_per_cycle = this.getAbstractTimer().getPeriod_ms();
                // Find the mean time before reaching the ascent instruction
                double branchTimeConsumption_ms = executionTime_per_cycle / probability_per_cycle;
                int branchTimeConsumption_min = (int)(branchTimeConsumption_ms / 1000. / 60.);
                if (branchTimeConsumption_min < ascentRequestTime_min || ascentRequestTime_min == -2) {
                    ascentRequestTime_min = branchTimeConsumption_min;
                }
                // TODO: do wee need to take into account the limited time of the Park?
            }
        }
        return ascentRequestTime_min;
    }
    */


/*


    public void replaceVariable(Variable toReplace, Variable newVar) {
        //logger.dbg("AcqMode "  + this.getName() + " rename " + toReplace.getName() + " with " + newVar.getName());
        this.variables.remove(toReplace);
        this.mainSequence.replaceVariable(toReplace, newVar);
    }

    public void removeVariable(Variable toRemove) {
        this.variables.remove(toRemove);
    }

    public boolean existVariable(String varName) {
        boolean exists = false;
        for (Variable v : this.getVariables()) {
            if (varName.equals(v.getName())) {
                exists = true;
            }
        }
        return exists;
    }

    private void preCompose(AcqMode toInclude) {
        // Add variables
        for (Variable variable : toInclude.getVariables()) {
            String varName = variable.getName();
            // Check if the variable name exists
            boolean duplicateName = this.existVariable(varName);
            // Rename the variable
            if (duplicateName) {
                int i = 0;
                while (duplicateName) {
                    varName = variable.getName() + "_" + i;
                    duplicateName = this.existVariable(varName);
                    i++;
                }
                // Set the new name
                variable.setName(varName);
            }
            // Add the new variable
            this.addVariable(variable);
        }

        // Check that all sequences have a different name
        ArrayList<String> sqNames = new ArrayList<>();
        for (Sequence sq : this.getSequences()) {
            sqNames.add(sq.getName());
        }
        for (Sequence sq : toInclude.getSequences()) {
            sqNames.add(sq.getName());
        }
        for (int i = 0; i < sqNames.size(); i++) {
            for (int j = i+1; j < sqNames.size(); j++) {
                if (i != j && sqNames.get(i).equals(sqNames.get(j))) {
                    logger.err("There is two sequences with the same name");
                }
            }
        }
    }

    public AcqMode copy() {
        AcqMode newAcqMode = new AcqMode(this.getName());

        // Copy variables
        for (Variable variable : this.variables) {
            newAcqMode.addVariable(variable.copy());
        }

        // Copy periodicSensor
        newAcqMode.setPeriodicSensor(this.getPeriodicSensor());

        // Add sequences
        Sequence copiedMainSequence = this.getMainSequence().copy(newAcqMode);
        newAcqMode.setMainSequence(copiedMainSequence);

        return newAcqMode;
    }
 */