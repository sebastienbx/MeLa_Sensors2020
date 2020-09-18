package fr.uca.i3s.sparks.compomaid.model.Platform;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Constant;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;
import fr.uca.i3s.sparks.compomaid.model.Types.*;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.ArrayList;

public class Library implements Visitable {
    private Logger logger = new Logger(true);
    private ArrayList<FunctionPrototype> functionPrototypes;
    private ArrayList<PeriodicSensor> periodicSensors;
    private static Library instance;

    private Library() {
        functionPrototypes = new ArrayList<>();
        periodicSensors = new ArrayList<>();

        // Add sensors
        periodicSensors.add(new PeriodicSensor("HydrophoneBF", 10., 200., 60.));

        periodicSensors.add(new PeriodicSensor("HydrophoneHF", 100., 10000, 80.));

        // Add function prototypes
        FunctionPrototype fProto;


        fProto = new FunctionPrototype("toFloat", "toFloatI32");
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.67);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toInt", "toIntF32");
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.53);
        functionPrototypes.add(fProto);


        /*
           Array functions
        */

        fProto = new FunctionPrototype("put", "putArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.17);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("get", "getArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.23);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("copy", "copyArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.24*p.get(4).getValueAsInt());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toComplex", "toComplexArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.4*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toFloat", "toFloatArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.56*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("select", "selectArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);





        fProto = new FunctionPrototype("put", "putArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.setWCET_us((p) -> 0.17);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("get", "getArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.23);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("copy", "copyArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.24*p.get(4).getValueAsInt());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toComplex", "toComplexArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toInt", "toIntArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("select", "selectArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);





        fProto = new FunctionPrototype("put", "putArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ComplexInt, DataDir.Input);
        fProto.setWCET_us((p) -> 0.55);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("get", "getArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ComplexInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("copy", "copyArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.48*p.get(4).getValueAsInt());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("select", "selectArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);






        fProto = new FunctionPrototype("put", "putArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ComplexFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.55);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("get", "getArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ComplexFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("copy", "copyArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.48*p.get(4).getValueAsInt());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("select", "selectArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);




        /*
           Buffer functions
        */

        fProto = new FunctionPrototype("push", "pushCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.setWCET_us((p) -> 0.13);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("push", "pushCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.setWCET_us((p) -> 0.14);
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("push", "pushArrayCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.setWCET_us((p) -> 0.83*p.get(1).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("push", "pushArrayCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.83*p.get(1).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("get", "getFromBegCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.43);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("get", "getFromEndCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.43);
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("toArray", "toArrayCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37*p.get(1).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("toArray", "toArrayCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37*p.get(1).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("isFull", "isFullCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearCircularBufferI32");
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("isFull", "isFullCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("clear", "clearCircularBufferF32");
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);



        /*
           FFT functions
        */

        fProto = new FunctionPrototype("fft", "fftI32");
        fProto.addParameterType(DataType.FFTInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.setWCET_us((p) -> {
            int len = p.get(0).getVParam(0).getValueAsInt();
            switch (len) {
                case 32:
                    return 200; // TODO
                case 64:
                    return 300; // TODO
                case 128:
                    return 500;
                case 256:
                    return 900;
                case 512:
                    return 2500;
                case 1024:
                    return 4500;
                case 2048:
                    return 11500;
                case 4096:
                    return 21400;
                default:
                    logger.err("FFT default");
                    return 1000000.;
            }
        });
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("fft", "fftF32");
        fProto.addParameterType(DataType.FFTFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.setWCET_us((p) -> {
            int len = p.get(0).getVParam(0).getValueAsInt();
            switch (len) {
                case 32:
                    return 450; // TODO
                case 64:
                    return 900; // TODO
                case 128:
                    return 1800;
                case 256:
                    return 3900;
                case 512:
                    return 8600;
                case 1024:
                    return 20100;
                case 2048:
                    return 43500;
                case 4096:
                    return 93700;
                default:
                    logger.err("FFT default");
                    return 1000000.;
            }
        });
        functionPrototypes.add(fProto);


        /*
           cdf24 functions
        */
        fProto = new FunctionPrototype("cdf24", "cdf24I32");
        fProto.addParameterType(DataType.CDF24Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.setWCET_us((p) -> {
            int i;
            int K = p.get(0).getVParam(0).getValueAsInt();
            int length = p.get(0).getVParam(1).getValueAsInt();
            int t0 = (int)(2.246*length);
            int time = 0;
            for(i=0; i<K; i++) {
                time += t0/Math.pow(2,i);
            }
            return time;
        });
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("cdf24ScalesPower", "cdf24ScalesPowerI32");
        fProto.addParameterType(DataType.CDF24Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("cdf24", "cdf24F32");
        fProto.addParameterType(DataType.CDF24Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.setWCET_us((p) -> {
            int i;
            int K = p.get(0).getVParam(0).getValueAsInt();
            int length = p.get(0).getVParam(1).getValueAsInt();
            int t0 = (int)(2.539*length);
            int time = 0;
            for(i=0; i<K; i++) {
                time += t0/Math.pow(2,i);
            }
            return time;
        });
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("cdf24ScalesPower", "cdf24ScalesPowerF32");
        fProto.addParameterType(DataType.CDF24Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);


        /*
           Filter functions
        */

        fProto = new FunctionPrototype("iir", "iirI32");
        fProto.addParameterType(DataType.IIRInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 15 + (p.get(0).getVParam(0).getValueAsInt() +
                                       p.get(0).getVParam(1).getValueAsInt()) * p.get(1).getLength() * 0.7);
        // EXTRAM: 15 + (den+num) * 0.7 us/bytes
        functionPrototypes.add(fProto);

        /* BiquadDF1
        fProto = new FunctionPrototype("iirBiquad", "iirBiquadI32");
        fProto.addParameterType(DataType.IIRInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 1.1*p.get(0).getVParam(0).getValueAsInt()*p.get(1).getLength());
        functionPrototypes.add(fProto);
        */

        fProto = new FunctionPrototype("fir", "firI32");
        fProto.addParameterType(DataType.FIRInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> (0.75 + 0.1*p.get(0).getVParam(1).getValueAsInt())*p.get(1).getLength()); // TODO reimplement FIR without State array dependent of input array
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("iir", "iirF32");
        fProto.addParameterType(DataType.IIRFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 12 + (p.get(0).getVParam(0).getValueAsInt() +
                                       p.get(0).getVParam(1).getValueAsInt()) * p.get(1).getLength() * 2.75);
        functionPrototypes.add(fProto);

        /* BiquadDF1
        fProto = new FunctionPrototype("iirBiquad", "iirBiquadF32");
        fProto.addParameterType(DataType.IIRFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 7.0*p.get(0).getVParam(0).getValueAsInt()*p.get(1).getLength());
        functionPrototypes.add(fProto);
        */

        fProto = new FunctionPrototype("fir", "firF32");
        fProto.addParameterType(DataType.FIRFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 1.65*p.get(0).getVParam(1).getValueAsInt()*p.get(1).getLength()); // TODO reimplement FIR without State array dependent of input array
        functionPrototypes.add(fProto);



        /*
           STALTA functions
        */

        fProto = new FunctionPrototype("stalta", "staltaI32");
        fProto.addParameterType(DataType.StaLtaInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.35); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("stalta", "staltaArrayI32");
        fProto.addParameterType(DataType.StaLtaInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.35*p.get(1).getLength());
        functionPrototypes.add(fProto);


        fProto = new FunctionPrototype("stalta", "staltaF32");
        fProto.addParameterType(DataType.StaLtaFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.15); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("stalta", "staltaArrayF32");
        fProto.addParameterType(DataType.StaLtaFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.15*p.get(1).getLength());
        functionPrototypes.add(fProto);



        /*
           Trigger function
        */

        fProto = new FunctionPrototype("trigger", "triggerI32");
        fProto.addParameterType(DataType.TriggerInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Bool, DataDir.Output);
        fProto.setWCET_us((p) -> 0.65); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("trigger", "triggerArrayI32");
        fProto.addParameterType(DataType.TriggerInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Bool, DataDir.Output);
        fProto.setWCET_us((p) -> 0.65*p.get(1).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("trigger", "triggerF32");
        fProto.addParameterType(DataType.TriggerFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Bool, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("trigger", "triggerArrayF32");
        fProto.addParameterType(DataType.TriggerFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Bool, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6*p.get(1).getLength());
        functionPrototypes.add(fProto);



        /*
           Probability function
        */

        fProto = new FunctionPrototype("cumulativeDistribution", "cumulativeDistributionFunctionI32");
        fProto.addParameterType(DataType.DistributionInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.39*p.get(0).getVParam(0).getValueAsInt());
        functionPrototypes.add(fProto);


        fProto = new FunctionPrototype("cumulativeDistribution", "cumulativeDistributionFunctionF32");
        fProto.addParameterType(DataType.DistributionFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 2.4*p.get(0).getVParam(0).getValueAsInt());
        functionPrototypes.add(fProto);



        /*
           Math functions
        */

        fProto = new FunctionPrototype("max", "maxArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.18*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("max", "maxArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("mean", "meanArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.15*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("mean", "meanArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.4*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("sum", "sumArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.22*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("sum", "sumArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.85*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("min", "minArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.18*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("min", "minArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 1.7*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("energy", "energyArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.24*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("energy", "energyArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 1.2*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("rms", "rmsArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.15*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("rms", "rmsArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 1.2*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("stdDev", "stdDevArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.18*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("stdDev", "stdDevArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("var", "varArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.18*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("var", "varArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("abs", "absI32");
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.16);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("abs", "absArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.25*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("abs", "absF32");
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.15);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("abs", "absArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.25*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("add", "addArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("add", "addArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.63*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("sub", "subArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.37*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("sub", "subArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.63*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("dotProduct", "dotProductArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 0.43*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("dotProduct", "dotProductArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 1.3*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("mult", "multArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.4*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("mult", "multArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.67*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("mult", "multWithIntArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.27*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("mult", "multWithFloatArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.63*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("div", "divArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.44*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("div", "divArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 2.1*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("div", "divWithIntArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.30*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("div", "divWithFloatArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 1.9*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("negate", "negateArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.25*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("negate", "negateArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.25*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("offset", "offsetArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.25*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("offset", "offsetArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.5*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("conv", "convArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.28*p.get(0).getLength()*p.get(1).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("conv", "convArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 1.3*p.get(0).getLength()*p.get(1).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("corr", "corrArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.28*p.get(0).getLength()*p.get(1).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("corr", "corrArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 1.3*p.get(0).getLength()*p.get(1).getLength());
        functionPrototypes.add(fProto);




        fProto = new FunctionPrototype("sqrt", "sqrtArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 3.6*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("sqrt", "sqrtArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 6.2*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("cos", "cosArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.74*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("cos", "cosArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 7.6*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("sin", "sinArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.74*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("sin", "sinArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 8.0*p.get(0).getLength());
        functionPrototypes.add(fProto);


        /*

        fProto = new FunctionPrototype("log10", "log10I32");
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 54.);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("log10", "log10ArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 52.*p.get(0).getLength()*p.get(1).getLength());
        functionPrototypes.add(fProto);

        */

        fProto = new FunctionPrototype("log10", "log10F32");
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 54.);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("log10", "log10ArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 51.*p.get(0).getLength());
        functionPrototypes.add(fProto);



        /*

        fProto = new FunctionPrototype("pow", "powI32");
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> );
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("pow", "powArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> );
        functionPrototypes.add(fProto);

         */

        fProto = new FunctionPrototype("pow", "powF32");
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Output);
        fProto.setWCET_us((p) -> 120.);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("pow", "powArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 120.*p.get(0).getLength());
        functionPrototypes.add(fProto);



        /*
        fProto = new FunctionPrototype("scale", "scaleArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 9.71*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("scale", "scaleArrayF32");
        fProto.addParameterType(DataType.ArrayFloat);
        fProto.addParameterType(DataType.Float);
        fProto.addParameterType(DataType.ArrayFloat);
        fProto.setWCET_us((p) -> 0.5*p.get(0).getLength());
        functionPrototypes.add(fProto);
        */


        /*
        fProto = new FunctionPrototype("conjugate", "conjugateArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Output);
        fProto.setWCET_us((p) -> 1.0); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("conjugate", "conjugateArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 1.0*p.get(0).getLength()); // TODO
        functionPrototypes.add(fProto);
        */



        fProto = new FunctionPrototype("magnitude", "magnitudeArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 7.2*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("magnitude", "magnitudeArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 14.2*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("multcomplex", "multcomplexArrayCI32");
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexInt, DataDir.Output);
        fProto.setWCET_us((p) -> 1.6*p.get(0).getLength());
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("multcomplex", "multcomplexArrayCF32");
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayComplexFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 8.1*p.get(0).getLength());
        functionPrototypes.add(fProto);



        fProto = new FunctionPrototype("diff", "diffArrayI32");
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("diff", "diffArrayF32");
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);



        /*
           Utils functions
        */

        fProto = new FunctionPrototype("ascentRequest", "ascentRequest");
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("getTimestamp", "getTimestamp");
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("getSampleIndex", "getSampleIndex");
        fProto.addParameterType(DataType.Int, DataDir.Output);
        fProto.setWCET_us((p) -> 0.); // TODO
        functionPrototypes.add(fProto);



        /*
           Record functions
        */

        fProto = new FunctionPrototype("record", "recordI32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.Int, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordArrayI32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordCircularBufferI32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.BufferInt, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordF32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.Float, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordArrayF32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordCircularBufferF32");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.BufferFloat, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("record", "recordString");
        fProto.setWCET_us((p) -> 500000.);
        fProto.addParameterType(DataType.File, DataDir.Input);
        fProto.addParameterType(DataType.String, DataDir.Input);
        functionPrototypes.add(fProto);



        /*
           Serial functions
        */

        fProto = new FunctionPrototype("serialPrint", "serialPrintI32");
        fProto.setWCET_us((p) -> 0.); // TODO. How to measure this asynchronous function?
        fProto.addParameterType(DataType.Int, DataDir.Input);
        functionPrototypes.add(fProto);

        //fProto = new FunctionPrototype("serialPrintln", "serialPrintlnI32");
        //fProto.setWCET_us((p) -> 0.); // TODO
        //fProto.addParameterType(DataType.Int, DataDir.Input);
        //functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("serialPrint", "serialPrintI32Array");
        fProto.setWCET_us((p) -> 0.); // TODO
        fProto.addParameterType(DataType.ArrayInt, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("serialPrint", "serialPrintF32");
        fProto.setWCET_us((p) -> 0.); // TODO
        fProto.addParameterType(DataType.Float, DataDir.Input);
        functionPrototypes.add(fProto);

        //fProto = new FunctionPrototype("serialPrintln", "serialPrintlnF32");
        //fProto.setWCET_us((p) -> 0.); // TODO
        //fProto.addParameterType(DataType.Float, DataDir.Input);
        //functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("serialPrint", "serialPrintF32Array");
        fProto.setWCET_us((p) -> 0.); // TODO
        fProto.addParameterType(DataType.ArrayFloat, DataDir.Input);
        functionPrototypes.add(fProto);

        fProto = new FunctionPrototype("serialPrint", "printf");
        fProto.setWCET_us((p) -> 0.); // TODO
        fProto.addParameterType(DataType.String, DataDir.Input);
        functionPrototypes.add(fProto);

        //fProto = new FunctionPrototype("serialPrintln", "printf");
        //fProto.setWCET_us((p) -> 0.); // TODO
        //fProto.addParameterType(DataType.String, DataDir.Input);
        //functionPrototypes.add(fProto);

    }

    /*
    public void addFunction(FunctionPrototype functionPrototype) {
        functionPrototypes.add(functionPrototype);
    }

    public FunctionPrototype getFunction(String name) throws FunctionNotFoundException {
        FunctionPrototype find = null;
        for(FunctionPrototype a : functionPrototypes) {
            if(a.getName().equals(name)){
                find = a;
            }
        }
        if (find == null){
            throw new FunctionNotFoundException("function \"" + name + "\" not found");
        }
        return find;
    }

    public ArrayList<FunctionPrototype> getFunctionPrototypes() {
        return functionPrototypes;
    }
    */

    public FunctionPrototype getFunctionPrototype(String name, ArrayList<Data> parameters) {
        FunctionPrototype functionPrototype = null;

        for(FunctionPrototype fp : functionPrototypes) {
            boolean find = true;
            if(fp.getName().equals(name)) {
                int protoParamSize = fp.getParameterType().size();
                int callParamSize = parameters.size();
                if (protoParamSize != callParamSize) {
                    find = false;
                } else {
                    for(int i =0; i < protoParamSize; i++){
                        DataType protoDataType = fp.getParameterType().get(i);
                        DataType paramDataType = parameters.get(i).getDataType();
                        if(protoDataType.equals(paramDataType)) {
                            // keep find = true
                        } else {
                            find = false;
                        }
                    }
                }
            } else {
                find = false;
            }

            if (find) {
                functionPrototype = fp;
            }
        }

        if (functionPrototype == null){
            logger.err("\"" + name + "\" not found");
            for(Data param : parameters) {
                logger.err(param.getDataType().toString());
            }

        }

        return functionPrototype;
    }

    public void addPeriodicSensor(PeriodicSensor periodicSensor) {
        periodicSensors.add(periodicSensor);
    }

    public PeriodicSensor getPeriodicSensor(String name) {
        PeriodicSensor find = null;
        for(PeriodicSensor ps : periodicSensors) {
            if(ps.getName().equals(name)){
                find = ps;
            }
        }
        if (find == null){
            logger.err("\"" + name + "\" not found");
        }
        return find;
    }

    public ArrayList<PeriodicSensor> getPeriodicSensors() {
        return periodicSensors;
    }

    public static Library getInstance(){
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
