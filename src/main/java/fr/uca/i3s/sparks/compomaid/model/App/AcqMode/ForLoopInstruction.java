package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class ForLoopInstruction extends Instruction implements Visitable {

    private Logger logger = new Logger(false);

    private Variable idx;
    private Variable val;
    private Variable forVar;
    private Sequence sequence;

    public ForLoopInstruction(Variable idx, Variable val, Variable forVar, Sequence sequence)  {
        super();
        this.idx = idx;
        this.val = val;
        this.forVar = forVar;
        this.sequence = sequence;
    }

    public Variable getIdx() {
        return idx;
    }

    public Variable getVal() {
        return val;
    }

    public Variable getForVar() {
        return forVar;
    }

    public Sequence getSequence() {
        return sequence;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "TODO: toString in ForLoopInstruction";
    }

    @Override
    public double getWCET_us() {
        return sequence.getWCET_us()*forVar.getLength();
    }

    @Override
    public boolean containAscentRequest() {
        if (sequence.containAscentRequest()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getTransmissionDataSize() {
        logger.dbg("for loop transmission data size");
        logger.dbg(sequence.getTransmissionDataSize());
        return sequence.getTransmissionDataSize();
    }
}

