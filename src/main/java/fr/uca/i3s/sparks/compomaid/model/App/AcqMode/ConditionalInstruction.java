package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;


import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;

public class ConditionalInstruction extends Instruction implements Visitable {

    private ConditionalBranch ifBranch;
    //private ArrayList<ConditionalBranch> elseifBranches;
    //private ConditionalBranch elseBranch;

    public ConditionalInstruction() {
        super();
        ifBranch = null;
        //elseifBranches = new ArrayList<>();
        //elseBranch = null;
    }

    public ConditionalInstruction(ConditionalBranch branch) {
        super();
        ifBranch = null;
        //elseifBranches = new ArrayList<>();
        //elseBranch = null;
        this.addBranch(branch);
    }

    public void addBranch(ConditionalBranch branch) {
        if (ifBranch == null) {
            ifBranch = branch;
        }
        /* else if (branch.getGuard() != null) {
            elseifBranches.add(branch);
        } else {
            elseBranch = branch;
        }
        */
    }

    public ConditionalBranch getIfBranch() {
        return ifBranch;
    }

    /*
    public ArrayList<ConditionalBranch> getElseifBranches() {
        return elseifBranches;
    }

    public ConditionalBranch getElseBranch() {
        return elseBranch;
    }
    */

    public ArrayList<ConditionalBranch> getConditionalBranches() {
        ArrayList<ConditionalBranch> branches = new ArrayList<>();
        if (ifBranch != null) {
            branches.add(ifBranch);
        }
        /*
        branches.addAll(elseifBranches);
        if (elseBranch != null) {
            branches.add(elseBranch);
        }
        */
        return branches;
    }

    public ArrayList<Sequence> getSequences() {
        ArrayList<Sequence> sequences = new ArrayList<>();
        if (ifBranch!= null) {
            sequences.add(ifBranch.getSequence());
        }
        /*
        for(ConditionalBranch branch: elseifBranches) {
            sequences.add(branch.getSequence());
        }
        if (elseBranch != null) {
            sequences.add(elseBranch.getSequence());
        }
        */
        return sequences;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String str = "";
        str += "if " + ifBranch.toString();
        return str;
    }
}


/*
    @Override
    public double getTimeConsumption() {
        return 0;
    }

    @Override
    public double getEnergyConsumption() {
        return 0;
    }

    @Override
    public double getPowerConsumption() {
        return 0;
    }
 */

/*

    public void replaceVariable(Variable toReplace, Variable newVar) {
        for (ConditionalBranch cb : this.getConditionalBranches()) {
            cb.replaceVariable(toReplace, newVar);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public ConditionalInstruction copy(AcqMode newAcqMode) {
        ConditionalInstruction newConditionalInstruction = new ConditionalInstruction();

        ConditionalBranch newIfBranch = this.getIfBranch().copy(newAcqMode);
        ArrayList<ConditionalBranch> newEsleIfBranchs = new ArrayList<>();
        for (ConditionalBranch elseIfBranch : this.getElseifBranches()) {
            newEsleIfBranchs.add(elseIfBranch.copy(newAcqMode));
        }
        ConditionalBranch newElseBranch = null;
        if (this.getElseBranch() != null) {
            newElseBranch = this.getElseBranch().copy(newAcqMode);
        }

        newConditionalInstruction.addBranch(newIfBranch);
        for (ConditionalBranch elseIfBranch : newEsleIfBranchs) {
            newConditionalInstruction.addBranch(elseIfBranch);
        }
        if (newElseBranch != null) {
            newConditionalInstruction.addBranch(newElseBranch);
        }

        return newConditionalInstruction;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConditionalInstruction)) {
            return false;
        }
        ConditionalInstruction other = (ConditionalInstruction)obj;
        boolean equals = true;
        if (!other.getIfBranch().equals(this.getIfBranch())) {
            equals = false;
        }
        if (other.getElseifBranches().size() != this.getElseifBranches().size()) {
            equals = false;
        } else {
            for (int i = 0; i < other.getElseifBranches().size(); i++) {
                if (!other.getElseifBranches().get(i).equals(this.getElseifBranches().get(i))) {
                    equals = false;
                }
            }
        }
        if (other.getElseBranch() != null && this.getElseBranch() == null) {
            equals = false;
        } else if (other.getElseBranch() == null && this.getElseBranch() != null) {
            equals = false;
        } else if (other.getElseBranch() != null && this.getElseBranch() != null) {
            if (!other.getElseBranch().equals(this.getElseBranch())) {
                equals = false;
            }
        }
        return equals;
    }

 */