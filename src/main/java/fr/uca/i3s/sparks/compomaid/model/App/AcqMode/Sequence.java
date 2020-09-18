package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.Probability;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;


public abstract class Sequence implements Visitable {

    private Logger logger = new Logger(false);

    private String name;
    private AcqMode acqMode;
    private ArrayList<Instruction> instructions;
    private static int counter = 1;
    private int id;

    public Sequence(String name, AcqMode acqMode) {
        logger.dbg("New sequence " + name);
        this.name = name;
        this.acqMode = acqMode;
        this.instructions = new ArrayList<>();
        //this.parents = new ArrayList<>();
        this.id = counter;
        this.counter++;
    }

    public void addInstruction (Instruction instruction) {
        instructions.add(instruction);
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    /*
    public ArrayList<AscentRequestInstruction> getAscentRequestInstructions() {
        ArrayList<AscentRequestInstruction> instructions = new ArrayList<>();
        for (Instruction instruction : this.getInstructions()) {
            if (instruction instanceof AscentRequestInstruction) {
                instructions.add((AscentRequestInstruction) instruction);
            }
        }
        return instructions;
    }
    */

    public boolean contains(Instruction instruction) {
        // we don't use this.getInstructions().contains(instruction) because it use equals
        // and we want to compare the object itself
        boolean catched = false;
        for (Instruction ist : this.getInstructions()) {
            if (instruction == ist) {
                catched = true;
            }
        }
        return catched;
    }

    /*
    public boolean containAscentRequest() {
        return this.getAscentRequestInstructions().size() > 0;
    }
    */


    public ArrayList<ConditionalInstruction> getConditions() {
        ArrayList<ConditionalInstruction> conditions = new ArrayList<>();
        for (Instruction instruction : instructions) {
            if (instruction instanceof ConditionalInstruction) {
                conditions.add((ConditionalInstruction)instruction);
            }
        }
        return conditions;
    }

    public ConditionalInstruction getLastCondition() {
        return this.getConditions().get(this.getConditions().size()-1);
    }

    /*
    public ArrayList<ConditionalExpression> getParentConditionalExpressions() {
        ArrayList<ConditionalExpression> conditions = new ArrayList<>();
        for (Sequence sq : acqMode.getSequences()) {
            for (ConditionalInstruction cd : sq.getConditions()) {
                for (ConditionalBranch cdb : cd.getConditionalBranches()) {
                    if (cdb.getSequence() == this) {
                        conditions.add(cdb.getGuard());
                    }
                }
            }
        }

        return conditions;
    }
    */


    public ConditionalBranch getConditionalBranch(Sequence targetSequence) {
        ConditionalBranch catched = null;
        for (ConditionalInstruction cond : this.getConditions()) {
            for (ConditionalBranch cdBranch : cond.getConditionalBranches()) {
                if (cdBranch.getSequence() == targetSequence) {
                    catched = cdBranch;
                }
            }
        }
        return catched;
    }


    public ArrayList<Sequence> getChildSequences() {
        ArrayList<Sequence> child = new ArrayList<>();
        for (ConditionalInstruction cond : this.getConditions()) {
            for(Sequence condsq : cond.getSequences()) {
                child.add(condsq);
                child.addAll(condsq.getChildSequences());
            }
        }
        return child;
    }

    public ArrayList<Branch> getStructuralBranchs() {
        ArrayList<Branch> branchs = new ArrayList<>();

        // Add this branch
        Branch mainbr = new Branch(this.getAcqMode());
        mainbr.addSequence(this);
        branchs.add(mainbr);

        // Add conditional branchs
        for (ConditionalInstruction cd : this.getConditions()) {
            ArrayList<Branch> cdBranchs = new ArrayList<>();
            // Add a branch for each branch of each conditions
            for (ConditionalBranch cbr : cd.getConditionalBranches()) {
                // Add a branch for each sub-branchs of the sequence
                for (Branch subBrToAppend : cbr.getSequence().getStructuralBranchs()) {
                    Branch subbr = new Branch(this.getAcqMode());
                    subbr.appendBranch(subBrToAppend);
                    cdBranchs.add(subbr);
                }
            }
            for (Branch br : cdBranchs) {
                Branch b = new Branch(this.getAcqMode());
                b.appendBranch(mainbr);
                b.appendBranch(br);
                branchs.add(b);
            }
        }
        return branchs;
    }

    public ArrayList<Branch> getExecutionPaths() {
        ArrayList<Branch> branchs = new ArrayList<>();

        // Add this branch
        Branch mainbr = new Branch(this.getAcqMode());
        mainbr.addSequence(this);
        branchs.add(mainbr);

        // Add conditional branchs
        for (ConditionalInstruction cd : this.getConditions()) {
            ArrayList<Branch> cdBranchs = new ArrayList<>();

            // Add a branch for each branch of each conditions
            for (ConditionalBranch cbr : cd.getConditionalBranches()) {
                // Add a branch for each sub-branchs of the sequence
                for (Branch subBrToAppend : cbr.getSequence().getExecutionPaths()) {
                    Branch subbr = new Branch(this.getAcqMode());
                    subbr.appendBranch(subBrToAppend);
                    /*
                    for (Sequence seqToAppend : subBrToAppend.getSequences()) {
                        subbr.addSequence(seqToAppend, cbr.getProbability());
                    }*/
                    cdBranchs.add(subbr);
                }
            }

            for (Branch br1 : new ArrayList<>(branchs)) {
                for (Branch br2 : cdBranchs) {
                    Branch b = new Branch(this.getAcqMode());
                    b.appendBranch(br1);
                    b.appendBranch(br2);
                    branchs.add(b);
                }
            }
        }
        return branchs;
    }

    public ArrayList<Branch> getRealTimeExecutionPaths() {
        ArrayList<Branch> branchs = getExecutionPaths();
        ArrayList<Branch> realTimeBranchs = new ArrayList<>();
        for (Branch branch : branchs) {
            if (branch.isRealTime()) {
                realTimeBranchs.add(branch);
            }
        }
        return realTimeBranchs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        logger.dbg("Change sequence name " + this.name + " to " + name);
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setAcqMode(AcqMode acqMode) {
        this.acqMode = acqMode;
    }

    public AcqMode getAcqMode() {
        return acqMode;
    }

    public double getWCET_us() {
        double wcet_s = 0;
        for (Instruction instruction : instructions) {
            wcet_s += instruction.getWCET_us();
        }
        return wcet_s;
    }

    public int getTransmissionDataSize() {
        int transmission_data_size = 0;
        for (Instruction instruction : instructions) {
            transmission_data_size += instruction.getTransmissionDataSize();
        }
        return transmission_data_size;
    }

    public boolean containAscentRequest() {
        boolean containAscentRequest = false;
        for (Instruction instruction : this.instructions) {
            if (instruction.containAscentRequest()) {
                containAscentRequest = true;
            }
        }
        return containAscentRequest;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}


/*
    @Override
    public double getTimeConsumption() {
        // Time in us
        int time = 0;
        for (Instruction ist : this.getInstructions()) {
            time += ist.getTimeConsumption();
        }
        return time;
    }

    @Override
    public double getEnergyConsumption() {
        // Energy consumed in mW.us
        double energy = 0;
        for (Instruction ist : this.getInstructions()) {
            energy += ist.getEnergyConsumption();
        }
        return energy;
    }

    @Override
    public double getPowerConsumption() {
        // Energy consumed in mW
        // Power of a sequence is the mean of power consumed by the instructions
        return this.getEnergyConsumption() / this.getTimeConsumption();
    }
 */



/*
    public ArrayList<CallInstruction> getCallInstructions() {
        ArrayList<CallInstruction> callInstructions = new ArrayList<>();
        for (Instruction ist : instructions) {
            if (ist instanceof CallInstruction) {
                callInstructions.add((CallInstruction) ist);
            }
        }
        return callInstructions;
    }

 */


    /*
    public void replaceSequenceElement(SequenceElement toreplace, SequenceElement toadd) {
        this.sequenceElements.set(sequenceElements.indexOf(toreplace), toadd);
    }
    */

    /*
    public void addParent (Sequence parent) {
        this.parents.add(parent);
    }

    public ArrayList<Sequence> getParents() {
        return this.parents;
    }

    public ArrayList<Integer> getParentsId() {
        ArrayList<Integer> parentsId = new ArrayList<>();
        for (Sequence parent : parents) {
            parentsId.add(parent.getId());
        }
        return parentsId;
    }

        public void removeInstruction(Instruction toRemove) {
        // TODO remove child instructions
        this.instructions.remove(toRemove);
    }
    */


/*

    public Sequence copy(AcqMode newAcqMode) {
        // Copy sequence
        Sequence newSequence = new Sequence(this.getName(), newAcqMode, this.isDeclared());

        for (Instruction instruction : this.getInstructions()) {
            // Copy instruction
            if (instruction instanceof CallInstruction) {
                newSequence.addInstruction(((CallInstruction)instruction).copy(newAcqMode.getVariables()));
            } else if (instruction instanceof AssignmentInstruction) {
                newSequence.addInstruction(((AssignmentInstruction)instruction).copy(newAcqMode.getVariables()));
            } else if (instruction instanceof ConditionalInstruction) {
                newSequence.addInstruction(((ConditionalInstruction)instruction).copy(newAcqMode));
            }
        }
        return newSequence;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sequence)) {
            return false;
        }
        Sequence other = (Sequence)obj;
        boolean equals = true;

        if (this.getInstructions().size() != other.getInstructions().size()) {
            equals = false;
        } else {
            for (int i = 0; i < this.getInstructions().size(); i++) {
                if (!this.getInstructions().get(i).equals(other.getInstructions().get(i))) {
                    equals = false;
                }
            }
        }

        return equals;
    }

    */