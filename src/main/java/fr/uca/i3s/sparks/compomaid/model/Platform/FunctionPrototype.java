package fr.uca.i3s.sparks.compomaid.model.Platform;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.model.Types.DataDir;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;

public class FunctionPrototype implements Visitable {
    private Logger logger = new Logger(false);
    private String name;
    private String cname;
    private ArrayList<DataType> parameterType;
    private ArrayList<DataDir> parameterDir;
    private DataType returnType;
    private WcetCompute wcet_us;

    public FunctionPrototype(String name, String cname)  {
        this.name = name;
        this.cname = cname;
        this.parameterType = new ArrayList<>();
        this.parameterDir = new ArrayList<>();
        this.returnType = DataType.Void;
    }

    public FunctionPrototype(String name, String cname, DataType returnType)  {
        this.name = name;
        this.cname = cname;
        this.parameterType = new ArrayList<>();
        this.parameterDir = new ArrayList<>();
        this.returnType = returnType;
    }

    public void addParameterType(DataType paramType, DataDir paramDir) {
        this.parameterType.add(paramType);
        this.parameterDir.add(paramDir);
    }

    public void setReturnType(DataType returnType) {
        this.returnType = returnType;
    }

    public ArrayList<DataType> getParameterType() {
        return parameterType;
    }

    public ArrayList<DataDir> getParameterDir() {
        return parameterDir;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public String getCname() {
        return cname;
    }

    public void setWCET_us(WcetCompute c) {
        wcet_us = c;
    }

    public double getWCET_us(ArrayList<Data> dataList) {
        return wcet_us.compute(dataList);
    }

    public int getTransmissionDataSize(ArrayList<Data> variableList) {
        int transmission_data_size = 0;
        if (this.cname.equals("recordArrayF32")) {
            Variable toRec = (Variable) variableList.get(1);
            transmission_data_size = 4 * toRec.getLength();
        }
        if (this.cname.equals("recordArrayI32")) {
            Variable toRec = (Variable) variableList.get(1);
            transmission_data_size = 4 * toRec.getLength();
        }
        if (this.cname.equals("recordCircularBufferF32")) {
            Variable toRec = (Variable) variableList.get(1);
            transmission_data_size = 4 * toRec.getLength();
        }
        if (this.cname.equals("recordCircularBufferI32")) {
            Variable toRec = (Variable) variableList.get(1);
            transmission_data_size = 4 * toRec.getLength();
        }
        if (this.cname.equals("recordI32")) {
            logger.dbg("ok");
            transmission_data_size = 4;
        }
        if (this.cname.equals("recordF32")) {
            transmission_data_size = 4;
        }
        return transmission_data_size;
    }

    public boolean isAscentRequest() {
        if (this.cname.equals("ascentRequest")) {
            return true;
        } else {
            return false;
        }
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
        return processingTime_us;
    }

    @Override
    public double getEnergyConsumption() {
        // Energy in mWh.us
        return power * processingTime_us;
    }

    @Override
    public double getPowerConsumption() {
        // Energy in mW
        return power;
    }

 */