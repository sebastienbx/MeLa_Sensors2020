package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class CallInstruction extends Instruction implements Visitable {

    private Logger logger = new Logger(true);

    private Data call;

    public CallInstruction(Data call)  {
        super();
        this.call = call;
    }

    public Data getCall() {
        return call;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return call.toString();
    }

    @Override
    public double getWCET_us() {
        return call.getWCET_us();
    }

    @Override
    public boolean containAscentRequest() {
        if (call.isAscentRequest()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getTransmissionDataSize() {
        return call.getTransmissionDataSize();
    }
}

/*
    @Override
    public double getTimeConsumption() {
        return call.getTimeConsumption();
    }

    @Override
    public double getEnergyConsumption() {
        // Energy consumed in mWh.us
        return call.getEnergyConsumption();
    }

    @Override
    public double getPowerConsumption() {
        return call.getEnergyConsumption();
    }

 */