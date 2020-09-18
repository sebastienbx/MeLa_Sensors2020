package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data;


import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class Constant extends Data {

    Logger logger = new Logger();
    String value;

    public Constant(DataType type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getValueAsInt() {
        if (this.getDataType() != DataType.Int) {
            logger.err("Try to get \"" + value + "\" as an Int whereas it is a \"" + this.getDataType() + "\"");
        }
        return Integer.parseInt(value);
    }

    public Double getValueAsDouble() {
        if (this.getDataType() != DataType.Float || this.getDataType() != DataType.Int) {
            logger.err("Try to get \"" + value + "\" as an Double whereas it is a \"" + this.getDataType() + "\"");
        }
        return Double.parseDouble(value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.getValue();
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

    @Override
    public String toString() {
        return value;
    }

    public Constant copy() {
        return new Constant(this.getDataType(), this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Constant)) {
            return false;
        }
        Constant other = (Constant) obj;
        if (other.toString().equals(this.toString())) {
            return true;
        } else {
            return false;
        }
    }

 */