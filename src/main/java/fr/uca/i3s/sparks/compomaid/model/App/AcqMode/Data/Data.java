package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data;

import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;

import java.util.ArrayList;


public abstract class Data implements Visitable {

    Logger logger = new Logger();

    private DataType dataType;

    public Data(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public double getWCET_us() {
        return 0;
    }

    public boolean isAscentRequest() {
        return false;
    }

    public int getTransmissionDataSize() {
        return 0;
    }

    public Integer getLength() {
        logger.err("getLength method is not defined for object of type " + this.getClass());
        return null;
    }

    public ArrayList<Constant> getVParams() {
        logger.err("getVParams method is not defined for object of type " + this.getClass());
        return null;
    }

    public Constant getVParam(Integer index) {
        logger.err("getVParam method is not defined for object of type " + this.getClass());
        return null;
    }

    public Integer getValueAsInt() {
        logger.err("getValueAsInt method is not defined for object of type " + this.getClass());
        return null;
    }

    public Double getValueAsDouble() {
        logger.err("getValueAsDouble method is not defined for object of type " + this.getClass());
        return null;
    }

}


 /*
    public Data copy(ArrayList<Data> refData) {
        Data copyData = null;
        if (this instanceof Constant) {
            copyData = ((Constant)this).copy();
        } else if (this instanceof Variable) {
            Variable copyVariable = null;
            for (Variable var : refVars) {
                if (var.equals(this)) {
                    copyVariable = var;
                }
            }
            copyData = copyVariable;
        } else if (this instanceof Function) {
            Function copyFunction = null;
            for (Function var : refVars) {
                if (var.equals(this)) {
                    copyFunction = var;
                }
            }
            copyData = copyFunction;
        }
        return copyData;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Constant) && !(obj instanceof Variable)) {
            return false;
        }

        boolean equals = true;
        if (obj instanceof Constant) {
            Constant other = (Constant) obj;
            if (!other.equals(((Constant)this))) {
                equals = false;
            }
        } else {
            Variable other = (Variable) obj;
            if (!other.equals(((Variable)this))) {
                equals = false;
            }
        }
        return equals;
    }

    */