package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data;


import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;

public class Variable extends Data {

    private Logger logger = new Logger();

    private String name;
    private ArrayList<Constant> vparam;

    public Variable(DataType type, String name, ArrayList<Constant> vparam) {
        super(type);
        this.name = name;
        this.vparam = vparam;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String str = name;
        str += "(";
        for (Constant param : vparam) {
            str += param.getValue();
        }
        str += ")";
        return str;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Integer getLength() {
        if (this.getDataType() != DataType.ArrayInt &&
                this.getDataType() != DataType.ArrayFloat &&
                this.getDataType() != DataType.ArrayComplexInt &&
                this.getDataType() != DataType.ArrayComplexFloat &&
                this.getDataType() != DataType.BufferInt &&
                this.getDataType() != DataType.BufferFloat) {
            logger.err("Cannot get length of a variable of type \"" + this.getDataType() + "\"");
        }
        return vparam.get(0).getValueAsInt();
    }

    @Override
    public ArrayList<Constant> getVParams() {
        return vparam;
    }

    @Override
    public Constant getVParam(Integer index) {
        return vparam.get(index);
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
    public Variable copy() {
        Variable variable = new Variable(this.getDataType(), this.getName());
        return variable;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }
        Variable other = (Variable) obj;
        if (other.getName().equals(this.getName()) && other.getDataType().equals(this.getDataType())) {
            return true;
        } else {
            return false;
        }
    }
  */