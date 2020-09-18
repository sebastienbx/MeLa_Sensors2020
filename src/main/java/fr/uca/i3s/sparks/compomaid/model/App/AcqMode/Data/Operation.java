package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data;

import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class Operation extends Data implements Visitable {

    private Logger logger = new Logger();

    private Data left;
    private Data right;
    private String comparator;

    public Operation(Data left, String comparator, Data right)  {
        super(DataType.Bool);
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    public Data getLeft() {
        return left;
    }

    public Data getRight() {
        return right;
    }

    public String getComparator() {
        return comparator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return left.toString() + " " + comparator + " " + right.toString();
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
    public Operation copy(ArrayList<Variable> refVars) {
        return new Operation(left.copy(refVars), comparator, right.copy(refVars));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Operation)) {
            return false;
        }
        Operation other = (Operation)obj;
        boolean equals = true;
        if (!other.getLeft().equals(this.getLeft())) {
            equals = false;
        }
        if (!other.getComparator().equals(this.getComparator())) {
            equals = false;
        }
        if (!other.getRight().equals(this.getRight())) {
            equals = false;
        }
        return equals;
    }
 */