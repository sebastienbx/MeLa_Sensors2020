package fr.uca.i3s.sparks.compomaid.model.App;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.*;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.*;

import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;
import fr.uca.i3s.sparks.compomaid.tools.Logger;
import fr.uca.i3s.sparks.compomaid.visitor.Visitable;
import fr.uca.i3s.sparks.compomaid.visitor.Visitor;

import java.util.*;


public class App implements Visitable {

    Logger logger = new Logger(true);

    private String name;

    private Descent descent;
    private Park park;
    private Ascent ascent;
    private Surface surface;
    private LinkedHashSet<MissionStep> missionSteps;
    private LinkedHashSet<AcqMode> acqModes;


    private int depth_m;

    public App(int park_time_min, int depth_m) {
        // Depth
        this.depth_m = depth_m;

        // Time for descent at 3cm/s at parametrized depth
        int descent_time_min = (depth_m * 100) / 3 / 60; // Speed of 3cm/s
        int ascent_time_min = (depth_m * 100) / 8 / 60; // Speed of 8cm/s

        // Initialize each mission steps individually
        descent = new Descent(descent_time_min, this.depth_m);
        park = new Park(park_time_min, this.depth_m);
        ascent = new Ascent(ascent_time_min, this.depth_m);
        surface = new Surface(60, 0);

        // Add each mission steps into an array
        missionSteps = new LinkedHashSet<>();
        missionSteps.add(descent);
        missionSteps.add(park);
        missionSteps.add(ascent);
        missionSteps.add(surface);

        // Initialize acqMode array
        acqModes = new LinkedHashSet<>();
    }

    public LinkedHashSet<MissionStep> getMissionSteps() {
        return missionSteps;
    }

    public Descent getDescent() {
        return descent;
    }

    public Park getPark() {
        return park;
    }

    public Ascent getAscent() {
        return ascent;
    }

    public Surface getSurface() {
        return surface;
    }

    public void addAcqMode(AcqMode acqMode) {
        acqModes.add(acqMode);
    }

    public HashSet<AcqMode> getAcqModes () {
        return acqModes;
    }

    public void removeAcqMode(AcqMode toRemove) {
        acqModes.remove(toRemove);
    }

    public AcqMode getAcqMode(Instruction instruction) {
        AcqMode match = null;
        for (AcqMode acqMode : acqModes) {
            if (acqMode.contains(instruction)) {
                match = acqMode;
            }
        }
        if (match == null) {
            logger.err("Instruction \"" + instruction + "\" doesn't exist in any acquisition mode");
        }
        return match;
    }

    public Sequence getSequence(Instruction instruction) {
        Sequence match = null;
        for (AcqMode acqMode : acqModes) {
            if (acqMode.contains(instruction)) {
                match = acqMode.getSequence(instruction);
            }
        }
        if (match == null) {
            logger.err("Instruction \"" + instruction + "\" doesn't exist in any acquisition mode");
        }
        return match;
    }

    /*
    public ArrayList<ConditionalExpression> getConditionalExpression(Instruction instruction) {
        ArrayList<ConditionalExpression> match = null;
        for (AcqMode acqMode : acqModes) {
            if (acqMode.contains(instruction)) {
                match = acqMode.getConditionalExpression(instruction);
            }
        }
        if (match == null) {
            logger.err("Instruction \"" + instruction + "\" doesn't exist in any acquisition mode");
        }
        return match;
    }
    */

    /*
    public ArrayList<AscentRequestInstruction> getAscentRequestInstructions() {
        ArrayList<AscentRequestInstruction> ret = new ArrayList<>();
        for (AcqMode acqMode : acqModes) {
            ret.addAll(acqMode.getAscentRequestInstructions());
        }
        return ret;
    }
    */

    /*
    public HashSet<AcqMode> getActiveAcqModes () {
        HashSet<AcqMode> acqModes = new HashSet<>();
        for (MissionStep missionStep : this.getMissionSteps()) {
            for (Schedule schedule : missionStep.getSchedulesWithoutSleep()) {
                acqModes.add(schedule.getAcqMode());
            }
        }
        return acqModes;
    }
    */

    public AcqMode getAcqMode (String acqModeName) throws AcqModeNotFoundException {
        AcqMode match = null;
        for (AcqMode acqMode : acqModes) {
            if (acqMode.getName().equals(acqModeName)){
                match = acqMode;
            }
        }
        if (match == null) {
            throw new AcqModeNotFoundException("acquisition mode \"" + acqModeName + "\" not found");
        }

        return match;
    }

    public HashMap<PeriodicSensor, HashSet<AcqMode>> getUsedSensors() {
        HashMap<PeriodicSensor, HashSet<AcqMode>> map = new HashMap<>();
        for (AcqMode acqMode : acqModes) {
            if (!map.containsKey(acqMode.getSensorConfiguration().getSensor())){
                map.put(acqMode.getSensorConfiguration().getSensor(), new HashSet<>());
            }
            map.get(acqMode.getSensorConfiguration().getSensor()).add(acqMode);
        }
        return map;
    }

    public HashMap<AcqMode, HashSet<Schedule>> getAcqModeSchedule() {
        HashMap<AcqMode, HashSet<Schedule>> acqModeSchedule = new HashMap<>();
        for (MissionStep missionStep : missionSteps) {
            for (Schedule schedule : missionStep.getSchedules()) {
                if (!acqModeSchedule.containsKey(schedule.getAcqMode())) {
                    acqModeSchedule.put(schedule.getAcqMode(), new HashSet<>());
                }
                acqModeSchedule.get(schedule.getAcqMode()).add(schedule);
            }
        }
        return acqModeSchedule;
    }

    public  HashMap<AcqMode, Double> getAcqModeShortestPeriod() {
        HashMap<AcqMode, HashSet<Schedule>> acqModeSchedule = getAcqModeSchedule();
        HashMap<AcqMode, Double> acqModeShortestPeriod = new HashMap<>();
        for (AcqMode acqMode : acqModeSchedule.keySet()) {
            if (acqMode instanceof ContinuousAcqMode) {
                acqModeShortestPeriod.put(acqMode, acqMode.getSensorConfiguration().getPeriod_s());
            } else {
                Double shortestPeriod = Double.MAX_VALUE;
                for (Schedule schedule : acqModeSchedule.get(acqMode)) {
                    if (schedule.getPeriod_s() < shortestPeriod) {
                        shortestPeriod = new Double(schedule.getPeriod_s());
                    }
                }
                acqModeShortestPeriod.put(acqMode, shortestPeriod);
            }
        }
        return acqModeShortestPeriod;
    }

    public void computeAcqModePriorities(){
        HashMap<AcqMode, Double> acqModeShortestPeriod = getAcqModeShortestPeriod();
        ArrayList<Double> periods = new ArrayList<>(acqModeShortestPeriod.values());
        Collections.sort(periods, Collections.reverseOrder());
        // Fixed priorities: Coordinator = 1, Processing sequence = 2, sensor tasks = 19
        int priority = 3;
        for (Double period : periods) {
            if (priority > 18) {
                logger.err("Max priority reached");
            }
            for (AcqMode acqMode : acqModeShortestPeriod.keySet()) {
                if (acqModeShortestPeriod.get(acqMode).equals(period)) {
                    acqMode.setPriority(priority);
                }
            }
            priority += 1;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParkDepth_m() {
        return depth_m;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}


/*
    @Override
    public double getTimeConsumption() {
        // TODO: manage units and print appropriate unit in function of their values
        double descentTime_min = this.getDescent().getTimeConsumption();
        double parkTime_min = this.getPark().getTimeConsumption();
        double ascentTime_min = this.getAscent().getTimeConsumption();
        return descentTime_min + parkTime_min + ascentTime_min;
    }

    @Override
    public double getEnergyConsumption() {
        // return energy in W.h
        double descentEnergy = this.getDescent().getEnergyConsumption();
        double parkEnergy = this.getPark().getEnergyConsumption();
        double ascentEnergy = this.getAscent().getEnergyConsumption();
        double surfaceEnergy = this.getSurface().getEnergyConsumption();
        return descentEnergy + parkEnergy + ascentEnergy + surfaceEnergy;
    }

    @Override
    public double getPowerConsumption() {
        return this.getEnergyConsumption() / (this.getTimeConsumption() / 60.);
    }
*/