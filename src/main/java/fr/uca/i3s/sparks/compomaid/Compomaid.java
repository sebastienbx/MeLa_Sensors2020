package fr.uca.i3s.sparks.compomaid;


import fr.uca.i3s.sparks.compomaid.analyser.EnergyAnalyser;
import fr.uca.i3s.sparks.compomaid.analyser.SchedulingAnalyser;
import fr.uca.i3s.sparks.compomaid.analyser.SatelliteTransmissionAnalyser;
import fr.uca.i3s.sparks.compomaid.builder.Parser;
import fr.uca.i3s.sparks.compomaid.composer.Composer;
//import fr.uca.i3s.sparks.compomaid.generator.CortexGenerator;
import fr.uca.i3s.sparks.compomaid.generator.MeLaGenerator;
import fr.uca.i3s.sparks.compomaid.generator.SimulationGenerator;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import org.codehaus.plexus.util.FileUtils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Compomaid {
    private static Logger logger = new Logger(true);


    private static void printHeader (String header) {
        logger.log();
        logger.log();
        logger.log("********************************************");
        logger.log("* " + header);
        logger.log("********************************************");
    }

    private static void printSimpleHeader (String header) {
        logger.log();
        logger.log();
        logger.log("* " + header);
    }

    public static void main( String[] args ) throws IOException {
        String destdir;

        // Clean genrated directory
        Files.createDirectories(Paths.get("generated/mela"));
        FileUtils.cleanDirectory("generated/mela");
        Files.createDirectories(Paths.get("generated/simulation"));
        FileUtils.cleanDirectory("generated/simulation");
        /*
        Files.createDirectories(Paths.get("generated/cortex"));
        FileUtils.cleanDirectory("generated/cortex");
        */


        // Library
        /*
        String lib_file = "MeLaLib/cortexLibrary.mela";
        StringBuilder lib_string = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get(lib_file))) {
            lib_string.append(line);
            lib_string.append("\n");
        }

        printHeader("Parse library");
        Parser.parseMeLa_lib(lib_string.toString());
        Library.getInstance();
        */

        // Applications names
        //Mermaid, BlueWhales, TutoApp
        String app1_name = "Mermaid";
        String app2_name = "BlueWhales";

        // Read applications code
        String app1_file = "MeLaApps/" + app1_name + ".mela";
        String app1_string = "";
        if (app1_name.length() > 0) {
            StringBuilder app1_stringBuilder = new StringBuilder();
            for (String line : Files.readAllLines(Paths.get(app1_file))) {
                app1_stringBuilder.append(line);
                app1_stringBuilder.append("\n");
            }
            app1_string = app1_stringBuilder.toString();
        }

        String app2_file = "MeLaApps/" + app2_name + ".mela"; //"MeLaApps/app2.mela";
        String app2_string = "";
        if (app2_name.length() > 0) {
            StringBuilder app2_stringBuilder = new StringBuilder();
            for (String line : Files.readAllLines(Paths.get(app2_file))) {
                app2_stringBuilder.append(line);
                app2_stringBuilder.append("\n");
            }
            app2_string = app2_stringBuilder.toString();
        }

        // Application 1
        if (app1_string.length() > 0){
            printHeader("Application " + app1_name);

            printSimpleHeader("Parse application " + app1_name);
            App app1;
            app1 = Parser.parseMeLa(app1_string.toString());
            app1.setName(app1_name);



            /*
            System.out.println("descent: ");
            for (Schedule schedule : app1.getDescent().getSchedules()) {
                System.out.println(schedule.toString());
            }
            System.out.println("park: ");
            for (Schedule schedule : app1.getPark().getSchedules()) {
                System.out.println(schedule.toString());
            }
            System.out.println("ascent: ");
            for (Schedule schedule : app1.getAscent().getSchedules()) {
                System.out.println(schedule.toString());
            }

            for (AcqMode acqmode : app1.getAcqModes()) {
                System.out.println("acqmode: " + acqmode.getName());
                System.out.println("main sequence: " + acqmode.getMainSequence().getName());
                for (Sequence sequence : acqmode.getSequences()) {
                    System.out.println("sequence: " + sequence.getName());
                    for (Instruction instruction : sequence.getInstructions()) {
                        System.out.println("instruction: " + instruction.toString());
                    }
                }
            }
            */

            app1.computeAcqModePriorities();

            printSimpleHeader("Verification of execution time:");
            SchedulingAnalyser schedulingAnalyser = new SchedulingAnalyser();
            schedulingAnalyser.visit(app1);

            printSimpleHeader("Estimation of energy consumption:");
            EnergyAnalyser energyAnalyser = new EnergyAnalyser();
            energyAnalyser.visit(app1);

            printSimpleHeader("Amount of data transmitted by satellite:");
            SatelliteTransmissionAnalyser satelliteTransmissionAnalyser = new SatelliteTransmissionAnalyser();
            satelliteTransmissionAnalyser.visit(app1);

            /*
            printSimpleHeader("Analysis of mission configuration:");
            MissionAnalyser missionAnalyser = new MissionAnalyser();
            missionAnalyser.visit(app1);
            */

            printSimpleHeader("Generate MeLa code");
            destdir = "generated/mela/" + app1.getName() + "/";
            MeLaGenerator meLaGenerator = new MeLaGenerator();
            meLaGenerator.generate(app1, destdir);

            /*
            printHeader("Generate Arduino code");
            arduino_str = Generator.generateArduino(app1);
            filename = "generated/arduino/" + app1.getName() + "/" + app1.getName() + ".ino";
            writeInFile(filename, arduino_str);
            */

            /*
            printSimpleHeader("Generate Mermaid float code");
            destdir = "generated/cortex/" + app1.getName() + "/";
            CortexGenerator cortexGenerator = new CortexGenerator();
            cortexGenerator.generate(app1, destdir);
            */

            /*
            printSimpleHeader("Generate mission parameters");
            destdir = "generated/config/" + app1.getName() + "/";
            MissionGenerator missionGenerator = new MissionGenerator();
            missionGenerator.generate(app1, destdir);
            */

            printSimpleHeader("Generate simulation code");
            destdir = "generated/simulation/" + app1.getName() + "/";
            SimulationGenerator simulationGenerator = new SimulationGenerator();
            simulationGenerator.generate(app1, destdir);
        }

        // Application 2
        if (app2_string.length() > 0) {
            printHeader("Application " + app2_name);
            printSimpleHeader("Parse application " + app2_name);
            App app2;
            app2 = Parser.parseMeLa(app2_string.toString());
            app2.setName(app2_name);

            app2.computeAcqModePriorities();

            printSimpleHeader("Verification of execution time:");
            SchedulingAnalyser schedulingAnalyser = new SchedulingAnalyser();
            schedulingAnalyser.visit(app2);

            printSimpleHeader("Estimation of energy consumption:");
            EnergyAnalyser energyAnalyser = new EnergyAnalyser();
            energyAnalyser.visit(app2);

            printSimpleHeader("Estimation of amount of data transmitted by satellite:");
            SatelliteTransmissionAnalyser satelliteTransmissionAnalyser = new SatelliteTransmissionAnalyser();
            satelliteTransmissionAnalyser.visit(app2);

            /*
            printSimpleHeader("Analysis of mission configuration:");
            MissionAnalyser missionAnalyser = new MissionAnalyser();
            missionAnalyser.visit(app2);
            */

            printSimpleHeader("Generate MeLa code");
            destdir = "generated/mela/" + app2.getName() + "/";
            MeLaGenerator meLaGenerator = new MeLaGenerator();
            meLaGenerator.generate(app2, destdir);

            /*
            printHeader("Generate Arduino code");
            arduino_str = Generator.generateArduino(app2);
            filename = "generated/arduino/" + app2.getName() + "/" + app2.getName() + ".ino";
            writeInFile(filename, arduino_str);
            */

            /*
            printSimpleHeader("Generate Mermaid float code");
            destdir = "generated/cortex/" + app2.getName() + "/";
            CortexGenerator cortexGenerator = new CortexGenerator();
            cortexGenerator.generate(app2, destdir);
            */

            /*
            printSimpleHeader("Generate mission parameters of application 2");
            destdir = "generated/config/" + app2.getName() + "/";
            MissionGenerator missionGenerator = new MissionGenerator();
            missionGenerator.generate(app2, destdir);
            */

            printSimpleHeader("Generate simulation code");
            destdir = "generated/simulation/" + app2.getName() + "/";
            SimulationGenerator simulationGenerator = new SimulationGenerator();
            simulationGenerator.generate(app2, destdir);
        }

        // Compose application 1 and 2
        if (app1_string.length() > 0 && app2_string.length() > 0) {
            printHeader("Compose applications 1 and 2");
            printSimpleHeader("Parse application 1 and 2");
            App app1ToCompose;
            app1ToCompose = Parser.parseMeLa(app1_string.toString());
            app1ToCompose.setName("app1");
            App app2ToCompose;
            app2ToCompose = Parser.parseMeLa(app2_string.toString());
            app2ToCompose.setName("app2");

            printSimpleHeader("Compose application 1 and 2");
            Composer.compose2Apps(app1ToCompose, app2ToCompose);
            App app1U2 = app1ToCompose;

            app1U2.computeAcqModePriorities();

            printSimpleHeader("Verification of execution time:");
            SchedulingAnalyser schedulingAnalyser = new SchedulingAnalyser();
            schedulingAnalyser.visit(app1U2);

            printSimpleHeader("Estimation of energy consumption:");
            EnergyAnalyser energyAnalyser = new EnergyAnalyser();
            energyAnalyser.visit(app1U2);

            printSimpleHeader("Estimation of amount of data transmitted by satellite:");
            SatelliteTransmissionAnalyser satelliteTransmissionAnalyser = new SatelliteTransmissionAnalyser();
            satelliteTransmissionAnalyser.visit(app1U2);

            /*
            printSimpleHeader("Analysis of mission configuration:");
            MissionAnalyser missionAnalyser = new MissionAnalyser();
            missionAnalyser.visit(app1U2);
            */

            printSimpleHeader("Generate MeLa code of application 1U2");
            destdir = "generated/mela/" + app1U2.getName() + "/";
            MeLaGenerator meLaGenerator = new MeLaGenerator();
            meLaGenerator.generate(app1U2, destdir);

            /*
            printHeader("Generate Arduino code");
            arduino_str = Generator.generateArduino(app1U2);
            filename = "generated/" + app1U2.getName() + "/" + app1U2.getName() + ".ino";
            writeInFile(filename, arduino_str);
            */

            /*
            printSimpleHeader("Generate Mermaid float code");
            destdir = "generated/cortex/" + app1U2.getName() + "/";
            CortexGenerator cortexGenerator = new CortexGenerator();
            cortexGenerator.generate(app1U2, destdir);
            */

            /*
            printSimpleHeader("Generate mission parameters of application 1U2");
            destdir = "generated/config/" + app1U2.getName() + "/";
            MissionGenerator missionGenerator = new MissionGenerator();
            missionGenerator.generate(app1U2, destdir);
            */

            printSimpleHeader("Generate simulation code of application 1U2");
            destdir = "generated/simulation/" + app1U2.getName() + "/";
            SimulationGenerator simulationGenerator = new SimulationGenerator();
            simulationGenerator.generate(app1U2, destdir);

        }
    }
}


