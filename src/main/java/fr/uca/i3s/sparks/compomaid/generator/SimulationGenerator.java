package fr.uca.i3s.sparks.compomaid.generator;


import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.ConditionalBranch;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.*;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.model.Platform.FunctionPrototype;
import fr.uca.i3s.sparks.compomaid.model.Platform.Library;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;


public class SimulationGenerator extends Visitor {

    private Logger logger = new Logger(true);

    // Directory of the generated code
    private String destdir;

    // Code writer
    private CWriter c = new CWriter();


    public void generate(App app, String destdir) {
        this.destdir = destdir;

        // Rename MelaFile name since they become Global for C code generation
        for (AcqMode acqMode : app.getAcqModes()) {
            for (Variable variable : acqMode.getVariables()) {
                if (variable.getDataType().equals(DataType.File)) {
                    variable.setName(variable.getName() + "_" + acqMode.getName());
                }
            }
        }
        // Visit App
        this.visit(app);

        // Restore MelaFile name
        for (AcqMode acqMode : app.getAcqModes()) {
            for (Variable variable : acqMode.getVariables()) {
                if (variable.getDataType().equals(DataType.File)) {
                    variable.setName(variable.getName().replace("_" + acqMode.getName(), ""));
                }
            }
        }
    }

    @Override
    public void visit(App app) {

        int i = 0;

        // Shared variables
        c.clear();
        c.w("#include \"shared.h\"");
        c.w("");
        c.w("");

        c.w("// Task handler");
        for (AcqMode acqMode : app.getAcqModes()) {
            c.w("pthread_t " + acqMode.getName() + "_task_handle;");
        }

        c.w("// Files declaration");
        for (AcqMode acqMode : app.getAcqModes()) {
            for (Variable variable : acqMode.getVariables()) {
                if (variable.getDataType().equals(DataType.File)) {
                    String nm = variable.getName();
                    c.w("char " + nm + "_buff_data[SD_BUFF_SZ];");
                    c.w("FILE " + nm + "_fil;");
                    c.w("buffer_t " + nm + "_buff = {" + nm + "_buff_data, 0};");
                    c.w("buffered_file_t " + nm + " =  {\"" + nm + ".out\", &" + nm + "_fil, " + "&" + nm + "_buff};");
                    c.w("");
                }
            }
        }
        c.w("");

        c.writeToFile(destdir + "shared.c");



        // Generate header of shared variables
        c.clear();
        c.w("#ifndef SHARED_H_");
        c.w("#define SHARED_H_");
        c.w("");
        c.w("#include <pthread.h>");
        c.w("#include <stdbool.h>");
        c.w("#include <stdio.h>");
        c.w("");
        c.w("#include <Array.h>");
        c.w("#include <CircularBuffer.h>");
        c.w("#include <Math.h>");
        c.w("#include <arm_const_structs.h>");
        c.w("#include <FFT.h>");
        c.w("#include <Serial.h>");
        c.w("#include <Record.h>");
        c.w("#include <CDF24.h>");
        c.w("#include <StaLta.h>");
        c.w("#include <Trigger.h>");
        c.w("#include <Distribution.h>");
        c.w("#include <Filter.h>");
        c.w("");

        for (AcqMode acqMode : app.getAcqModes()) {
            c.w("extern pthread_t " + acqMode.getName() + "_task_handle;");
        }

        c.w("");
        for (AcqMode acqMode : app.getAcqModes()) {
            for (Variable variable : acqMode.getVariables()) {
                if (variable.getDataType().equals(DataType.File)) {
                    c.w("extern buffered_file_t " + variable.getName() + ";");
                }
            }
        }
        c.w("");


        c.w("#endif");
        c.writeToFile(destdir + "shared.h");



        // Makefile generation
        c.clear();
        c.w("SIMULATION_ROOT=simulation\n" +
                "CPPFLAGS+=-I$(SIMULATION_ROOT)\n" +
                "################################################################################\n" +
                "SIMULATION_SRC+=$(SIMULATION_ROOT)/shared.c\n");
        for (AcqMode acqMode : app.getAcqModes()) {
            c.w("SIMULATION_SRC+=$(SIMULATION_ROOT)/" + acqMode.getName() + "_task.c\n");
        }
        c.w("SIMULATION_OBJ=$(SIMULATION_SRC:.c=.o)\n" +
                "\n" +
                "libsimulation.a: $(SIMULATION_ROOT)/simulation.mk $(SIMULATION_OBJ) \n" +
                "\t@echo \"Library $@ built\"\n" +
                "\t@$(AR) rcs $@ $^\n" +
                "\t@echo\n" +
                "\n" +
                "################################################################################\n" +
                "MAIN_SRC=$(SIMULATION_ROOT)/main.c\n" +
                "MAIN_OBJ=$(MAIN_SRC:.c=.o)\n" +
                "\n" +
                "simulation.bin: $(MAIN_OBJ) \\\n" +
                "        libsimulation.a \\\n" +
                "        libmela.a \\\n" +
                "        libcmsis_sim.a\n" +
                "\t@echo 'Building target: $@ ($^)'\n" +
                "\t$(CC) -o \"simulation.bin\" $^ -Wl,--gc-sections -lm -pthread");
        c.writeToFile(destdir + "simulation.mk");


        // main.c file generation
        c.clear();
        c.w("#include <pthread.h>");
        c.w("#include <unistd.h>");
        //w("#include \"Coordinator_task.h\"\n");
        for (AcqMode acqMode : app.getAcqModes()) {
            c.w("#include \"" + acqMode.getName() + "_task.h\"");
        }
        c.w("#include \"shared.h\"");
        c.w("");
        c.w("");
        c.w("int main( void ) {");
        for (AcqMode acqMode : app.getAcqModes()) {
            c.w("pthread_create(&" + acqMode.getName() + "_task_handle, NULL, " + acqMode.getName() + "_task, NULL);");
        }
        c.w("while(1) {");
        c.w("sleep(1);");
        c.w("}");
        c.w("}");


        c.writeToFile(destdir + "main.c");


        for (AcqMode acqMode : app.getAcqModes()) {
            acqMode.accept(this);
        }

    }

    @Override
    public void visit(AcqMode acqMode) {

        c.clear();
        String nm = acqMode.getName();

        c.w("#include \"" + nm + "_task.h\"");
        c.w("#include \"shared.h\"\n");
        c.w("#include <dirent.h>\n");
        c.w("#include <fnmatch.h>\n");
        //w("#include \"" + acqMode.getPeriodicSensor().getName() + "_task.h\"");
        c.w("");
        c.w("");



        c.generateVariableDeclaration(acqMode);

        c.w("");

        c.w("struct dirent **namelist;");
        c.w("static int number_of_files;");
        c.w("static int n = 0;");
        c.w("");
        c.w("static char* input_dir_name = \"simInput\";");
        c.w("static char* output_dir_name = \"simOutput\";");
        c.w("static char input_file_name[512] = \"\";");
        c.w("static char output_file_name[512] = \"\";");
        c.w("static FILE *fp;");
        c.w("static size_t r;");
        c.w("");
        c.w("int sampleindex;");


        c.w("");
        c.w("");


        c.w("void getSampleIndex(int* ret) {");
        c.w("*ret = sampleindex;");
        c.w("}");


        c.w("");
        c.w("");


        c.w("void* " + nm + "_task(void * parametres){");
        //w("vTaskSuspend(NULL);");

        c.w("number_of_files = scandir(input_dir_name, &namelist, 0, alphasort);");

        c.w("while (n < number_of_files) {");


        c.w("strcpy(input_file_name, \"\");");
        c.w("strcat(input_file_name, input_dir_name);");
        c.w("strcat(input_file_name, \"/\");");
        c.w("strcat(input_file_name, namelist[n]->d_name);");

        c.w("strcpy(output_file_name, \"\");");
        c.w("strcat(output_file_name, output_dir_name);");
        c.w("strcat(output_file_name, \"/\");");
        c.w("strcat(output_file_name, namelist[n]->d_name);");
        c.w("strcat(output_file_name, \".out\");");
        c.w("n ++;");

        c.w("if (fnmatch( \"*.bin\", input_file_name, 0)) {");
        c.w("continue;");
        c.w("}");


        c.w("printf(\"__PCT__s\\n\", input_file_name);");
        c.w("fp = fopen(input_file_name, \"r\");");
        c.w("r = fread(" + acqMode.getInputVariable().getName() + ".data, 4, "
                + acqMode.getInputVariable().getVParam(0).getValue() + ", fp);");
        c.w("sampleindex = 0;");

        c.generateVariableInit(acqMode);

        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.File)) {
                c.w("clearFile(&" + variable.getName() + ");");
                c.w("openFile(&" + variable.getName() + ");");
            }
        }

        c.w("while (r == " + acqMode.getInputVariable().getVParam(0).getValue() + "){");

        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.ArrayFloat)) {
                c.w("unselectArrayF32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.ArrayInt)) {
                c.w("unselectArrayI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.ArrayComplexInt)) {
                c.w("unselectArrayCI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.ArrayComplexFloat)) {
                c.w("unselectArrayCF32(&" + variable.getName() + ");");
            }
        }

        acqMode.getMainSequence().accept(this);

        c.w("r = fread(" + acqMode.getInputVariable().getName() + ".data, 4, "
                + acqMode.getInputVariable().getVParam(0).getValue() + ", fp);");

        c.w("sampleindex += " + acqMode.getInputVariable().getVParam(0).getValue() + ";");

        c.w("}");
        c.w("fclose(fp);");

        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.File)) {
                c.w("flush_buffer_on_file(&" + variable.getName() + ");");
                c.w("closeFile(&" + variable.getName() + ");");
                c.w("rename(" + variable.getName() + ".fname, output_file_name);");
            }
        }


        c.w("}");
        c.w("printf(\"Terminate " + nm + "_task\\n\");");
        c.w("}");
        c.w("");
        c.w("");


        c.writeToFile(destdir + acqMode.getName() + "_task.c");



        c.clear();

        c.w("#ifndef " +  acqMode.getName().toUpperCase() + "_TASK_H_");
        c.w("#define " + acqMode.getName().toUpperCase() + "_TASK_H_");
        c.w("");
        c.w("void* " + acqMode.getName() + "_task(void * parametres);");
        /*
        c.w("void resume_" + acqMode.getName() + "_task(void);");
        if (acqMode instanceof ContinuousAcqMode) {
            c.w("void suspend_" + acqMode.getName() + "_task(void);");
        }
        */
        c.w("");
        c.w("#endif");

        c.writeToFile(destdir + acqMode.getName() + "_task.h");
    }

    @Override
    public void visit(Branch branch) {

    }

    @Override
    public void visit(Sequence sequence) {
        for (Instruction instruction: sequence.getInstructions()){
            instruction.accept(this);
        }
    }

    @Override
    public void visit(CallInstruction callInstruction) {
        callInstruction.getCall().accept(this);
        c.w(";");
    }

    @Override
    public void visit(ForLoopInstruction forLoopInstruction) {
        String idxName = forLoopInstruction.getIdx().getName();
        String valName = forLoopInstruction.getVal().getName();
        String forVarName = forLoopInstruction.getForVar().getName();
        c.w("for(" + idxName + " = 0; " + idxName + " < " + forVarName + ".len; " + idxName  + "++){");
        c.w(valName + " = " + forVarName + ".data[" + idxName + "];");
        forLoopInstruction.getSequence().accept(this);
        c.w("}");
    }

    @Override
    public void visit(AssignmentInstruction assignmentInstruction) {
        assignmentInstruction.getAssigned().accept(this);
        c.w(" = ");
        assignmentInstruction.getSource().accept(this);
        c.w(";");
    }


    @Override
    public void visit(ConditionalInstruction conditionalInstruction) {
        ConditionalBranch ifBranch = conditionalInstruction.getIfBranch();
        if (ifBranch != null) {
            c.w("if (");
            ifBranch.getGuard().accept(this);
            c.w(") {");
            ifBranch.getSequence().accept(this);
            c.w("}");
        }
    }

    @Override
    public void visit(Operation operation) {
        //logger.dbg("Visit operation");
        //logger.dbg("Left");
        operation.getLeft().accept(this);
        c.w(" " + operation.getComparator() + " ");
        //logger.dbg("Rigth");
        operation.getRight().accept(this);
    }

    @Override
    public void visit(Constant constant) {
        //logger.dbg("Const: " + constant.getValue());
        c.w(constant.getValue());
    }

    @Override
    public void visit(Variable variable) {
        //logger.dbg("Var: " + variable.getName());
        c.w(variable.getName());
    }

    @Override
    public void visit(Function function) {
        c.generateFunctionCall(function, this);
    }

    @Override
    public void visit(Library library) {

    }

    @Override
    public void visit(FunctionPrototype functionPrototype) {

    }
}