package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition;


import fr.uca.i3s.sparks.compomaid.tools.Logger;

public class Probability {

    private Logger logger = new Logger(true);

    private int value;
    private String unit;

    public Probability(int value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getProbaPerMin() {
        double probaPerMin = 0;
        if (unit.equals("sec")) {
            probaPerMin = value * 60.;
        } else if (unit.equals("min")) {
            probaPerMin = value;
        } else if (unit.equals("hour")) {
            probaPerMin = value / 60.;
        } else if (unit.equals("day")) {
            probaPerMin = value / 24. / 60.;
        } else if (unit.equals("week")) {
            probaPerMin = value / 7. / 24. / 60.;
        } else if (unit.equals("month")) {
            probaPerMin = value / 30. / 24. / 60.;
        } else if (unit.equals("year")) {
            probaPerMin = value / 365. / 24. / 60.;
        }  else {
            logger.err("Unit is not compatible");
        }
        return probaPerMin;
    }

    public double getProbaPerHour() {
        double probaPerHour = 0;
        if (unit.equals("sec")) {
            probaPerHour = value * 60. * 60.;
        } else if (unit.equals("min")) {
            probaPerHour = value * 60.;
        } else if (unit.equals("hour")) {
            probaPerHour = value;
        } else if (unit.equals("day")) {
            probaPerHour = value / 24.;
        } else if (unit.equals("week")) {
            probaPerHour = value / 7. / 24.;
        } else if (unit.equals("month")) {
            probaPerHour = value / 30. / 24.;
        } else if (unit.equals("year")) {
            probaPerHour = value / 365. / 24.;
        }  else {
            logger.err("Unit is not compatible");
        }
        return probaPerHour;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return Integer.toString(value) + " per " + unit;
    }
}

/*

    public ConditionalBranch copy(AcqMode newAcqMode) {
        ConditionalExpression newCondtionalExpression = this.conditionalExpression.copy(newAcqMode.getVariables());
        Sequence conditionalSequence = null;
        for (Sequence sq : newAcqMode.getSequences()) {
            if (sq.equals(this.sequence)) {
                conditionalSequence = sq;
            }
        }
        if (conditionalSequence == null) {
            conditionalSequence = sequence.copy(newAcqMode);
        }

        double proba = this.probability;
        return new ConditionalBranch(newCondtionalExpression, conditionalSequence, proba);
    }

    public void replaceVariable(Variable toReplace, Variable newVar) {
        conditionalExpression.replaceVariable(toReplace, newVar);
        sequence.replaceVariable(toReplace, newVar);
    }



 */