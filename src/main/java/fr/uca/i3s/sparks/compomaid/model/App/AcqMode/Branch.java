package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;


public class Branch implements Visitable {

    Logger logger = new Logger(true);

    private AcqMode acqMode;
    private ArrayList<Sequence> sequences;
    //private double probability = 1.;

    public Branch(AcqMode acqMode) {
        this.acqMode = acqMode;
        sequences = new ArrayList<>();
    }

    // public void addSequence (Sequence sequence, double probability) {
    public void addSequence (Sequence sequence) {
        sequences.add(sequence);
        //this.probability *= probability;
    }

    //public void appendBranch (Branch branch, double probability) {
    public void appendBranch (Branch branch) {
        sequences.addAll(branch.getSequences());
        //this.probability *= probability;
        //this.probability *= branch.getProbability();
    }

    public ArrayList<Sequence> getSequences() {
        return sequences;
    }

    public boolean isRealTime() {
        boolean isRealTime = true;
        for (Sequence sequence : sequences) {
            if (sequence instanceof ProcessingSequence) {
                isRealTime = false;
            }
        }
        return isRealTime;
    }

    public double getWCET_us() {
        double wcet_s = 0;
        for (Sequence sequence : sequences) {
            wcet_s += sequence.getWCET_us();
        }
        return wcet_s;
    }

    /*
    public double getProbability() {
        return this.probability;
    }
     */

    /*
    public boolean containAscentRequest() {
        boolean containAscentRequest = false;
        for (Sequence sq : sequences) {
            if (sq.containAscentRequest()) {
                containAscentRequest = true;
            }
        }
        return containAscentRequest;
    }
    */

    public AcqMode getAcqMode() {
        return acqMode;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < sequences.size()-1; i++) {
            str += sequences.get(i).getName() + " -> ";
        }
        str += sequences.get(sequences.size()-1).getName();
        //str += ", " + this.getProbability();
        return str;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /*
    @Override
    public int compareTo(Branch branch) {
        int ret;
        if (this.getSequences().size() < branch.getSequences().size()) {
            ret = -1;
        } else if (this.getSequences().size() > branch.getSequences().size()) {
            ret = +1;
        } else {
            ret = 0;
        }
        return ret;
    }
    */
}


/*
    @Override
    public double getTimeConsumption() {
        // Return processing time used by instruction in the branch
        int timeConsumption = 0;
        for (Sequence sq : sequences) {
            timeConsumption += sq.getTimeConsumption();
        }
        return timeConsumption;
    }

    @Override
    public double getEnergyConsumption() {
        // Return energy consumed by instruction in the branch
        double energyConsumption = 0;
        for (Sequence sq : sequences) {
            energyConsumption += sq.getEnergyConsumption();
        }
        return energyConsumption;
    }

    @Override
    public double getPowerConsumption() {
        return 0;
    }
 */