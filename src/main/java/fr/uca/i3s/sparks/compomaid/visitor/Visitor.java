package fr.uca.i3s.sparks.compomaid.visitor;


import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.ConditionalInstruction;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Function;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Operation;
import fr.uca.i3s.sparks.compomaid.model.Platform.*;

public abstract class Visitor {

    public abstract void visit(App app);

    public abstract void visit(AcqMode acqMode);

    public abstract void visit(Branch branch);

    public abstract void visit(Sequence stateMachine);

    public abstract void visit(ConditionalInstruction conditionalInstruction);

    public abstract void visit(Library library);

    public abstract void visit(FunctionPrototype functionPrototype);

    public abstract void visit(Operation operation);

    public abstract void visit(Constant constant);

    public abstract void visit(Variable variable);

    public abstract void visit(Function function);

    public abstract void visit(AssignmentInstruction assignmentInstruction);

    public abstract void visit(CallInstruction callInstruction);

    public abstract void visit(ForLoopInstruction forLoopInstruction);
}

