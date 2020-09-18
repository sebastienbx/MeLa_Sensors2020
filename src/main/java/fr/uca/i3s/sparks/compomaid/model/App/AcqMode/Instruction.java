package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.visitor.Visitable;


public abstract class Instruction implements Visitable {

    public double getWCET_us() {
        return 0;
    }

    public boolean containAscentRequest() {
        return false;
    }

    public int getTransmissionDataSize() {
        return 0;
    }

}


    /*
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Instruction)) {
            return false;
        }
        Instruction other = (Instruction)obj;
        boolean equals = false;
        if (this instanceof AssignmentInstruction && other instanceof AssignmentInstruction) {
            if (other.equals(this)) {
                equals = true;
            }
        } else if (this instanceof CallInstruction && other instanceof CallInstruction) {
            if (other.equals(this)) {
                equals = true;
            }
        } else if (this instanceof ConditionalInstruction && other instanceof ConditionalInstruction) {
            if (other.equals(this)) {
                equals = true;
            }
        }
        return equals;
    }
    */