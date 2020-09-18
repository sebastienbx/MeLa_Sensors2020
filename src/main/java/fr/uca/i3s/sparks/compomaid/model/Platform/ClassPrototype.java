package fr.uca.i3s.sparks.compomaid.model.Platform;
import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.tools.Logger;


import java.util.ArrayList;

public class ClassPrototype {
    private Logger logger = new Logger(true);
    private String name;
    private DataType dataType;
    private ArrayList<FunctionPrototype> functionPrototypes;

    public ClassPrototype(String name, DataType dataType)  {
        this.name = name;
        this.dataType = dataType;
        this.functionPrototypes = new ArrayList<>();
    }

    public void addFunctionPrototypes(FunctionPrototype functionPrototype) {
        this.functionPrototypes.add(functionPrototype);
    }

    public FunctionPrototype getFunctionPrototype(String name) {
        FunctionPrototype find = null;
        for(FunctionPrototype cp : functionPrototypes) {
            if(cp.getName().equals(name)){
                find = cp;
            }
        }
        if (find == null){
            logger.err("\"" + name + "\" not found");
        }
        return find;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
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