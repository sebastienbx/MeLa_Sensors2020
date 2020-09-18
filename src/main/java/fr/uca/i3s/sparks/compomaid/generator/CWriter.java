package fr.uca.i3s.sparks.compomaid.generator;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Function;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Variable;
import fr.uca.i3s.sparks.compomaid.model.Types.DataDir;
import fr.uca.i3s.sparks.compomaid.model.Types.DataType;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static fr.uca.i3s.sparks.compomaid.tools.FileWriter.writeInFile;

public class CWriter {

    private int indent = 0;
    private boolean newline = true;
    private StringBuffer code = new StringBuffer();

    public void clear() {
        code = new StringBuffer();
    }


    public void generateVariableDeclaration(AcqMode acqMode) {

        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.Bool)) {
                w("static bool " + variable.getName() + ";");
            }

            if (variable.getDataType().equals(DataType.Int)) {
                w("static int32_t " + variable.getName() + " = 0;");
            }

            if (variable.getDataType().equals(DataType.Float)) {
                w("static float32_t " + variable.getName() + ";");
            }

            if (variable.getDataType().equals(DataType.ArrayInt)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                if (variable.getVParams().size() > 1) {
                    w("static int32_t " + vn + "_data[] = {");
                    for (Constant cst : variable.getVParams()) {
                        w(cst.getValue());
                    }
                    w("};");
                } else {
                    w("static int32_t " + vn + "_data[" + vl + "]" + ";");
                }
                w("static array_i32_t " + vn + " = {" + vn + "_data, " + vl + ", " + vn + "_data, " + vl + "}" + ";");
            }

            if (variable.getDataType().equals(DataType.ArrayFloat)) {
                String vn = variable.getName();
                int paramSize = variable.getVParams().size();
                String vl = variable.getVParam(0).getValue();
                if (paramSize > 1) {
                    w("static float32_t " + vn + "_data[] = { ");
                    for (int i = 1; i < paramSize; i++) {
                        w(variable.getVParam(i).getValue());
                        if (i < paramSize-1) {
                            w(", ");
                        }
                    }
                    w(" };");

                } else {
                    w("static float32_t " + vn + "_data[" + vl + "]" + ";");
                }
                w("static array_f32_t " + vn + " = {" + vn + "_data, " + vl  + ", " + vn + "_data, " + vl + "}" + ";");
            }

            if (variable.getDataType().equals(DataType.ArrayComplexInt)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("static complex_i32_t " + vn + "_data[" + vl + "]" + ";");
                w("static array_ci32_t " + vn + " = {" + vn + "_data, " + vl  + ", " + vn + "_data, " + vl + "}" + ";");
            }

            if (variable.getDataType().equals(DataType.ArrayComplexFloat)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("static complex_f32_t " + vn + "_data[" + vl + "]" + ";");
                w("static array_cf32_t " + vn + " = {" + vn + "_data, " + vl + ", " + vn + "_data, " + vl + "}" + ";");
            }

            if (variable.getDataType().equals(DataType.BufferInt)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("static int32_t " + vn + "_data[" + vl + "]" + ";");
                w("static circular_buffer_i32_t " + vn + " = {" + vn + "_data, " + vl + ", 0, 0, true}" + ";");
            }

            if (variable.getDataType().equals(DataType.BufferFloat)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("static float32_t " + vn + "_data[" + vl + "]" + ";");
                w("static circular_buffer_f32_t " + vn + " = {" + vn + "_data, " + vl + ", 0, 0, true}" + ";");
            }

            if (variable.getDataType().equals(DataType.FFTInt)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("#define " + vn + " arm_cfft_sR_q31_len" + vl);
            }

            if (variable.getDataType().equals(DataType.FFTFloat)) {
                String vn = variable.getName();
                String vl = variable.getVParam(0).getValue();
                w("#define " + vn + " arm_cfft_sR_f32_len" + vl);
            }

            if (variable.getDataType().equals(DataType.FIRInt)) {
                String vn = variable.getName();
                String blockSize = variable.getVParam(0).getValue();
                String coeffsNbStr = variable.getVParam(1).getValue();
                Integer coeffsNb = Integer.parseInt(coeffsNbStr);

                w("static int32_t pState_" + vn + "["+ coeffsNbStr + " + " + blockSize + " - 1];");
                w("static int32_t pCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2; i < coeffsNb + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < coeffsNb) {
                        w(", ");
                    }
                }
                w(" };");

                w("static arm_fir_instance_i32 S_" + vn + " = {" + blockSize + ", pState_" + vn + ", pCoeffs_" + vn + "};");
                w("static fir_i32_t " + vn + " = {&S_" + vn + ", " + blockSize + "};");
            }

            if (variable.getDataType().equals(DataType.FIRFloat)) {
                String vn = variable.getName();
                String blockSize = variable.getVParam(0).getValue();
                String coeffsNbStr = variable.getVParam(1).getValue();
                Integer coeffsNb = Integer.parseInt(coeffsNbStr);

                w("static float32_t pState_" + vn + "["+ coeffsNbStr + " + " + blockSize + " - 1];");
                w("static float32_t pCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2; i < coeffsNb + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < coeffsNb) {
                        w(", ");
                    }
                }
                w(" };");

                w("static arm_fir_instance_f32 S_" + vn + " = {" + blockSize + ", pState_" + vn + ", pCoeffs_" + vn + "};");
                w("static fir_f32_t " + vn + " = {&S_" + vn + ", " + blockSize + "};");
            }

            /* TODO:
            if (variable.getDataType().equals(DataType.IIRInt)) {
                String vn = variable.getName();
                String stageNbStr = variable.getVParam(0).getValue();
                Integer stageNb = Integer.parseInt(stageNbStr);

                w("static int32_t pState_" + vn + "[" + 4*stageNb + "];");
                w("static int32_t pCoeffs_" + vn + "[]" + " = { ");
                for (int i = 1; i < 5*stageNb + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < 5*stageNb) {
                        w(", ");
                    }
                }
                w(" };");

                w("static iir_i32_t " + vn + "  = {" + stageNbStr + ", pState_" + vn + ", pCoeffs_" + vn + "};");
            }
            */


            /*

static float32_t aCoeffs_filter27[] = { 4.24129782787665e-06, 0, -1.69651913115066e-05, 0, 2.54477869672599e-05, 0, -1.69651913115066e-05, 0, 4.24129782787665e-06 };
static float32_t bCoeffs_filter27[] = { 1, 1.53641976121290, 4.74448639323704, 4.68066619996194, 7.33807297682948, 4.53706927641676, 4.45797408583918, 1.39919668596330, 0.882759792954546 };
static float32_t aState_filter27_data[9];
static circular_buffer_f32_t aState_filter27 = {aState_filter27_data,9,0,0, true};
static float32_t bState_filter27_data[9];
static circular_buffer_f32_t bState_filter27 = {bState_filter27_data,9,0,0, true};
static iir_f32_t filter27  = {aCoeffs_filter27, bCoeffs_filter27, &aState_filter27, &bState_filter27, 9, 9};

             */



            if (variable.getDataType().equals(DataType.IIRFloat)) {
                String vn = variable.getName();
                String numCoeffsNbStr = variable.getVParam(0).getValue();
                String denCoeffsNbStr = variable.getVParam(1).getValue();
                Integer numCoeffsNb = Integer.parseInt(numCoeffsNbStr);
                Integer denCoeffsNb = Integer.parseInt(denCoeffsNbStr);

                w("static float32_t numCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2; i < numCoeffsNb + 2; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < numCoeffsNb + 2 - 1) {
                        w(", ");
                    }
                }
                w(" };");

                w("static float32_t denCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2 + numCoeffsNb; i < denCoeffsNb + 2 + numCoeffsNb; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < denCoeffsNb + 2 + numCoeffsNb - 1) {
                        w(", ");
                    }
                }
                w(" };");

                w("static float32_t numState_" + vn + "_data[" + numCoeffsNbStr + "];");
                w("static circular_buffer_f32_t numState_" + vn + " = {numState_" + vn + "_data," + numCoeffsNbStr + ",0,0, true};");
                w("static float32_t denState_" + vn + "_data[" + denCoeffsNbStr + "];");
                w("static circular_buffer_f32_t denState_" + vn + " = {denState_" + vn + "_data," + denCoeffsNbStr + ",0,0, true};");
                w("static iir_f32_t " + vn + " = {numCoeffs_" + vn + ", denCoeffs_" + vn
                        + ", &numState_" + vn + ", &denState_" + vn + ","
                        + numCoeffsNbStr + ", " + denCoeffsNbStr + "};");

            }

            if (variable.getDataType().equals(DataType.IIRInt)) {
                String vn = variable.getName();
                String numCoeffsNbStr = variable.getVParam(0).getValue();
                String denCoeffsNbStr = variable.getVParam(1).getValue();
                Integer numCoeffsNb = Integer.parseInt(numCoeffsNbStr);
                Integer denCoeffsNb = Integer.parseInt(denCoeffsNbStr);

                w("static int32_t numCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2; i < numCoeffsNb + 2; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < numCoeffsNb + 2 - 1) {
                        w(", ");
                    }
                }
                w(" };");

                w("static int32_t denCoeffs_" + vn + "[]" + " = { ");
                for (int i = 2 + numCoeffsNb; i < denCoeffsNb + 2 + numCoeffsNb; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < denCoeffsNb + 2 + numCoeffsNb - 1) {
                        w(", ");
                    }
                }
                w(" };");

                w("static int32_t numState_" + vn + "_data[" + numCoeffsNbStr + "];");
                w("static circular_buffer_i32_t numState_" + vn + " = {numState_" + vn + "_data," + numCoeffsNbStr + ",0,0, true};");
                w("static int32_t denState_" + vn + "_data[" + denCoeffsNbStr + "];");
                w("static circular_buffer_i32_t denState_" + vn + " = {denState_" + vn + "_data," + denCoeffsNbStr + ",0,0, true};");
                w("static iir_i32_t " + vn + " = {numCoeffs_" + vn + ", denCoeffs_" + vn
                        + ", &numState_" + vn + ", &denState_" + vn + ","
                        + numCoeffsNbStr + ", " + denCoeffsNbStr + "};");

            }

            /* Biquad version of IIR

            if (variable.getDataType().equals(DataType.IIRInt)) {
                String vn = variable.getName();
                String stageNbStr = variable.getVParam(1).getValue();
                Integer stageNb = Integer.parseInt(stageNbStr);

                w("static int32_t pState_" + vn + "[" + 4*stageNb + "];");
                w("static int32_t pCoeffs_" + vn + "[]" + " = { ");
                for (int i = 1; i < 5*stageNb + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < 5*stageNb) {
                        w(", ");
                    }
                }
                w(" };");

                w("static iir_i32_t " + vn + "  = {" + stageNbStr + ", pState_" + vn + ", pCoeffs_" + vn + "};");
            }

            if (variable.getDataType().equals(DataType.IIRFloat)) {
                String vn = variable.getName();
                String stageNbStr = variable.getVParam(1).getValue();
                Integer stageNb = Integer.parseInt(stageNbStr);

                w("static float32_t pState_" + vn + "[" + 4*stageNb + "];");
                w("static float32_t pCoeffs_" + vn + "[]" + " = { ");
                for (int i = 1; i < 5*stageNb + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < 5*stageNb) {
                        w(", ");
                    }
                }
                w(" };");

                w("static iir_f32_t " + vn + "  = {" + stageNbStr + ", pState_" + vn + ", pCoeffs_" + vn + "};");
            }

             */

            if (variable.getDataType().equals(DataType.CDF24Int)) {
                String vn = variable.getName();
                String scalesNb = variable.getVParam(0).getValue();
                String cdfLength = variable.getVParam(1).getValue();
                String adLength = String.valueOf(Integer.parseInt(cdfLength)/2);
                w("static int32_t " + vn + "_a[" + adLength + "];");
                w("static int32_t " + vn + "_d[" + adLength + "];");
                w("static cdf24_i32_t " + vn + " = {" + scalesNb + ", 1, 1, " + cdfLength + ", " + vn + "_a, " + vn + "_d};");
            }

            if (variable.getDataType().equals(DataType.CDF24Float)) {
                String vn = variable.getName();
                String scalesNb = variable.getVParam(0).getValue();
                String cdfLength = variable.getVParam(1).getValue();
                String adLength = String.valueOf(Integer.parseInt(cdfLength)/2);
                w("static float32_t " + vn + "_a [" + adLength + "];");
                w("static float32_t " + vn + "_d [" + adLength + "];");
                w("static cdf24_f32_t " + vn + " = {" + scalesNb + ", 1, 1, " + cdfLength + ", " + vn + "_a, " + vn + "_d};");
            }

            if (variable.getDataType().equals(DataType.StaLtaInt)) {
                String vn = variable.getName();
                String sta_len = variable.getVParam(0).getValue();
                String lta_len = variable.getVParam(1).getValue();
                int lta_len_int = variable.getVParam(1).getValueAsInt();
                String lta_offset = variable.getVParam(2).getValue();
                int lta_offset_int = variable.getVParam(2).getValueAsInt();
                String scale_factor = variable.getVParam(3).getValue();
                String cbuff_len = String.valueOf(lta_len_int+lta_offset_int);
                w("static int32_t " + vn + "_cbuff_data[" + cbuff_len + "]" + ";");
                w("static circular_buffer_i32_t " + vn + "_cbuff = {" + vn + "_cbuff_data, " + cbuff_len + ", 0, 0, true}" + ";");
                w("static stalta_i32_t " + vn + " = {&" + vn + "_cbuff, " + sta_len + ", " + lta_len + ", " +
                        lta_offset + ", " + scale_factor + ", 0, 0};");
            }

            if (variable.getDataType().equals(DataType.StaLtaFloat)) {
                String vn = variable.getName();
                String sta_len = variable.getVParam(0).getValue();
                String lta_len = variable.getVParam(1).getValue();
                int lta_len_int = variable.getVParam(1).getValueAsInt();
                String lta_offset = variable.getVParam(2).getValue();
                int lta_offset_int = variable.getVParam(2).getValueAsInt();
                String cbuff_len = String.valueOf(lta_len_int+lta_offset_int);
                w("static float32_t " + vn + "_cbuff_data[" + cbuff_len + "]" + ";");
                w("static circular_buffer_f32_t " + vn + "_cbuff = {" + vn + "_cbuff_data, " + cbuff_len + ", 0, 0, true}" + ";");
                w("static stalta_f32_t " + vn + " = {&" + vn + "_cbuff, " + sta_len + ", " + lta_len + ", " +
                        lta_offset + ", 0, 0};");
            }

            if (variable.getDataType().equals(DataType.TriggerInt)) {
                String vn = variable.getName();
                String trigType = variable.getVParam(0).getValue();
                String threshold = variable.getVParam(1).getValue();
                String delay = variable.getVParam(2).getValue();
                String minInterval = variable.getVParam(3).getValue();
                w("static trigger_i32_t " + vn + " = {" + trigType + ", " + threshold + ", " + delay + ", " + minInterval + ", 0, false, false, false};");
            }

            if (variable.getDataType().equals(DataType.TriggerFloat)) {
                String vn = variable.getName();
                String trigType = variable.getVParam(0).getValue();
                String threshold = variable.getVParam(1).getValue();
                String delay = variable.getVParam(2).getValue();
                String minInterval = variable.getVParam(3).getValue();
                w("static trigger_f32_t " + vn + " = {" + trigType + ", " + threshold + ", " + delay + ", " + minInterval + ", 0, false, false, false};");
            }

            if (variable.getDataType().equals(DataType.DistributionInt)) {
                String vn = variable.getName();
                Integer distribLen = variable.getVParam(0).getValueAsInt();
                String distribLenStr = variable.getVParam(0).getValue();

                w("static int32_t " + vn + "_data[]" + " = {");
                for (int i = 1; i < distribLen+1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < distribLen) {
                        w(", ");
                    }
                }
                w("};");

                w("static int32_t " + vn + "_proba[]" + " = {");
                for (int i = distribLen + 1; i < distribLen*2 + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < distribLen*2) {
                        w(", ");
                    }
                }
                w("};");
                w("static distribution_i32_t " + vn + " = {" + vn + "_data, " + vn + "_proba, " + distribLenStr + "};");
            }

            if (variable.getDataType().equals(DataType.DistributionFloat)) {
                String vn = variable.getName();
                Integer distribLen = variable.getVParam(0).getValueAsInt();
                String distribLenStr = variable.getVParam(0).getValue();

                w("static float32_t " + vn + "_data[]" + " = { ");
                for (int i = 1; i < distribLen+1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < distribLen) {
                        w(", ");
                    }
                }
                w(" };");

                w("static float32_t " + vn + "_proba[]" + " = { ");
                for (int i = distribLen + 1; i < distribLen*2 + 1; i++) {
                    w(variable.getVParam(i).getValue());
                    if (i < distribLen*2) {
                        w(", ");
                    }
                }
                w(" };");
                w("static distribution_f32_t " + vn + " = {" + vn + "_data, " + vn + "_proba, " + distribLenStr + "};");
            }
        }
    }


    public void generateVariableInit(AcqMode acqMode) {
        for (Variable variable : acqMode.getVariables()) {
            if (variable.getDataType().equals(DataType.StaLtaInt)) {
                w("initStaltaI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.StaLtaFloat)) {
                w("initStaltaF32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.IIRInt)) {
                w("initIirI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.IIRFloat)) {
                w("initIirF32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.FIRInt)) {
                w("initFirI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.FIRFloat)) {
                w("initFirF32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.BufferInt)) {
                w("initCircularBufferI32(&" + variable.getName() + ");");
            }
            if (variable.getDataType().equals(DataType.BufferFloat)) {
                w("initCircularBufferF32(&" + variable.getName() + ");");
            }
        }
    }


    public void generateFunctionCall(Function function, Visitor visitor) {
        // Add function
        String function_name_str = function.getFunctionPrototype().getCname();

        w(function_name_str);

        // Add input of function
        w("(");
        int i = 0;
        while (i < function.getParameters().size()) {
            DataType dataType = function.getFunctionPrototype().getParameterType().get(i);
            DataDir dataDir = function.getFunctionPrototype().getParameterDir().get(i);
            if (dataDir.equals(DataDir.Input) && (dataType.equals(DataType.Int) || dataType.equals(DataType.Float) || dataType.equals(DataType.String))) {
                // Pass parameter as a number
            } else {
                // Pass parameter as a pointer
                w("&");
            }
            function.getParameters().get(i).accept(visitor);
            i += 1;
            if (i < function.getParameters().size()) {
                w(", ");
            }
        }
        w(")");
    }


    // Write code lines into a buffer with automatic indent
    public void w(String s, Object... args) {
        s = String.format(s, args);

        // If string is empty, this a new line
        if (s.isEmpty()) {
            s = "\n";
        }

        // If block close, decrease the indent now
        if (s.endsWith("}") && indent > 0) indent --;

        // Add indents
        if (this.newline) {
            // Add indent
            for (int i = 0; i < indent; i++) {
                s = "    " + s;
            }
        }

        // If block open, increase the indent for the next line
        if (s.endsWith("{") && indent < 10) indent++;

        // Add new line if line end with ";" or "{"
        if (s.endsWith(";") || s.endsWith("{") || s.endsWith("}") || s.contains("//") || s.contains("#") || s.isEmpty()){
            s += "\n";
        }
        if (s.startsWith("}")){
            s += "\n";
        }
        this.newline = s.endsWith("\n");

        // Add code line
        this.code.append(String.format("%s",s));
    }

    public void writeToFile(String filename) {
        writeInFile(filename, code.toString());
    }

}
