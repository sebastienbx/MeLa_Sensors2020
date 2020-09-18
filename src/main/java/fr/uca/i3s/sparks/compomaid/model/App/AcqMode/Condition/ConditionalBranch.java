package fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition;


import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Sequence;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.tools.Logger;

public class ConditionalBranch {

    private Logger logger = new Logger(true);

    private Data guard;
    private Sequence sequence;
    private Probability probability;

    public ConditionalBranch(Data guard, Sequence sequence, Probability probability) {
        this.guard = guard;
        this.sequence = sequence;
        this.probability = probability;
    }

    public Data getGuard() {
        return guard;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public Probability getProbability() {
        return probability;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConditionalBranch)) {
            return false;
        }
        ConditionalBranch other = (ConditionalBranch)obj;
        boolean equals = true;
        if (!other.getGuard().equals(this.getGuard())) {
            equals = false;
        }
        if (!other.getSequence().equals(this.getSequence())) {
            equals = false;
        }
        if (other.getProbability() != this.getProbability()) {
            equals = false;
        }
        return equals;
    }

    @Override
    public String toString() {
        String str = "";
        str += "guard: \"" + guard.toString() + "\" proba: \"" + probability.toString() + "\" target: \"" + sequence.getName() + "\"";
        return str;
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