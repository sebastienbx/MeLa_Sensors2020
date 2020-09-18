package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class AssignmentInstruction extends Instruction implements Visitable {

    private Logger logger = new Logger(true);

    private Data source;
    private Variable assigned;

    public AssignmentInstruction(Variable assigned, Data source)  {
        super();
        this.assigned = assigned;
        this.source = source;
    }

    public Variable getAssigned() {
        return assigned;
    }

    public Data getSource() {
        return source;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String str = "";
        str += assigned.toString() + " = " + source.toString();
        return str;
    }

    @Override
    public double getWCET_us() {
        return source.getWCET_us();
    }
}

/*
    @Override
    public double getTimeConsumption() {
        return source.getTimeConsumption();
    }

    @Override
    public double getEnergyConsumption() {
        // Energy consumed in mWh.us
        return source.getEnergyConsumption();
    }

    @Override
    public double getPowerConsumption() {
        return source.getEnergyConsumption();
    }

 */