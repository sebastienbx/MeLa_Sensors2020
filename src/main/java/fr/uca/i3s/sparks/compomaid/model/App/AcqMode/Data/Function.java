package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data;

import fr.uca.i3s.sparks.compomaid.model.Platform.FunctionPrototype;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;

public class Function extends Data {

    private Logger logger = new Logger(true);

    private FunctionPrototype functionPrototype;
    private ArrayList<Data> parameters;

    public Function(FunctionPrototype functionPrototype, ArrayList<Data> parameters)  {
        super(functionPrototype.getReturnType());
        this.functionPrototype = functionPrototype;
        this.parameters = parameters;
    }

    public FunctionPrototype getFunctionPrototype() {
        return functionPrototype;
    }

    public ArrayList<Data> getParameters() {
        return parameters;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        String str = functionPrototype.getName() + " ( ";
        for (Data param : parameters) {
            str += param.toString();
            str += ", ";
        }
        str += ")";
        return str;
    }

    @Override
    public double getWCET_us() {
        return functionPrototype.getWCET_us(parameters);
    }

    @Override
    public int getTransmissionDataSize() {
        return functionPrototype.getTransmissionDataSize(parameters);
    }

    @Override
    public boolean isAscentRequest() {
        return functionPrototype.isAscentRequest();
    }
}



/*
    @Override
    public double getTimeConsumption() {
        // Time in us
        double time = functionPrototype.getTimeConsumption();

        // Dependency of execution time to function parameters
        for (Data parameter: parameters) {
            int length = parameter.getLength();
            if (length > 0) {
                // TODO: définir une dépendence adaptée pour chaque fonction
                time += 0.1 * length * functionPrototype.getTimeConsumption();
            }
        }
        return time;
    }

    @Override
    public double getEnergyConsumption() {
        // Energy consumed in mWh.us
        return functionPrototype.getEnergyConsumption();
    }

    @Override
    public double getPowerConsumption() {
        return functionPrototype.getPowerConsumption();
    }

 */





  /*

      @Override
    public String toString() {
        String str = "";
        str += functionPrototype.getName();
        str += "(";
        for (int i = 0; i < parameters.size(); i++) {
            str += parameters.get(i).toString();
            if (i < parameters.size()-1) {
                str += ", ";
            }
        }
        str += ")";

        return str;
    }


    public Function copy(ArrayList<Variable> refVars) {
        // Copy function
        FunctionPrototype newFunctionPrototype = this.getFunction();
        // Copy parameters
        ArrayList<Parameter> newParameters = new ArrayList<>();
        for (Parameter parameterToCopy : this.getParameters()) {
            newParameters.add(parameterToCopy.copy(refVars));
        }

        // Create copied instruction
        return new Function(newFunctionPrototype, newParameters);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Function)) {
            return false;
        }
        Function other = (Function)obj;
        boolean equals = true;
        if (!other.getFunction().equals(this.getFunction())) {
            // Here we check only the reference of the function in the library
            equals = false;
        }
        if (other.getParameters().size() != this.getParameters().size()) {
            equals = false;
        } else {
            for(int i = 0; i < other.getParameters().size(); i++) {
                if (!other.getParameters().get(i).equals(this.getParameters().get(i))) {
                    equals = false;
                }
            }
        }
        return equals;
    }

    public boolean isComposableDataSource(Object obj) {
        // Two function with the same name that doesn't take any parameter
        if (!(obj instanceof Function)) {
            return false;
        }
        Function other = (Function)obj;
        boolean composable = true;
        if (!other.getFunction().equals(this.getFunction())) {
            // Here we check only the reference of the function in the library
            composable = false;
        }
        if (other.getParameters().size() != this.getParameters().size()) {
            composable = false;
        } else if (other.getParameters().size() != 0) {
            composable = false;
        }
        return composable;
    }
    */