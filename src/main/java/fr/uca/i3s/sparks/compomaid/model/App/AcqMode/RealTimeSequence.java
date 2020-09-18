package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;


public class RealTimeSequence extends Sequence {

    public RealTimeSequence(String name, AcqMode acqMode) {
        super(name, acqMode);
    }


    /*
    public boolean isDeclared() {
        return true;
    }
    */
}
