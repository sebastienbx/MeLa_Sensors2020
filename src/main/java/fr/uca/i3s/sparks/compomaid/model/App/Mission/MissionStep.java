package fr.uca.i3s.sparks.compomaid.model.App.Mission;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;
import fr.uca.i3s.sparks.compomaid.tools.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public abstract class MissionStep {

    private String name;
    private int duration_min;
    private int depth_m;
    private ArrayList<Schedule> schedules;

    MissionStep (String name, int duration_min, int depth_m) {
        this.name = name;
        this.duration_min = duration_min;
        this.depth_m = depth_m;
        schedules = new ArrayList<>();
    }


    public void addSchedule(AcqMode acqMode) {
        schedules.add(new Schedule(acqMode));
    }

    public void addSchedule(AcqMode acqMode, int period, String unit) {
        schedules.add(new Schedule(acqMode, period, unit));
    }

    /*
    public void addSchedule() {
        schedules.add(new Schedule());
    }

    public void addSchedule(int duration_min, int period_min, int offset_min) {
        schedules.add(new Schedule(period_min));
    }
    */

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }


    public ArrayList<Schedule> getSchedulesWithoutSleep() {
        ArrayList<Schedule> schedulesWithoutSleep = new ArrayList<>();
        return schedulesWithoutSleep;
    }


    public void removeSchedule (Schedule schedule) {
        this.schedules.remove(schedule);
    }

    public LinkedHashSet<AcqMode> getAcqModes() {
        LinkedHashSet<AcqMode> acqmodes = new LinkedHashSet<>();
        for (Schedule schedule : schedules) {
            acqmodes.add(schedule.getAcqMode());
        }
        return acqmodes;
    }

    public int getDepth_m() {
        return depth_m;
    }

    /*
    public boolean containAscentRequest() {
        boolean containAscentRequest = false;
        for (AcqMode acqMode : this.getAcqModes()) {
            containAscentRequest |= acqMode.containAscentRequest();
        }
        return containAscentRequest;
    }

    public boolean containLandingRequest() {
        boolean containLandingRequest = false;
        for (AcqMode acqMode : this.getAcqModes()) {
            containLandingRequest |= acqMode.containLandingRequest();
        }
        return containLandingRequest;
    }
    */

    public String getName() {
        return name;
    }

    public int getDuration_min() {
        return duration_min;
    }

    public int getActuatorEnergy_mWh() {
        return 0;
    }

    public HashMap<PeriodicSensor, Double> getSensorUsage () {
        // Get sensor usage
        HashMap<PeriodicSensor, Double> sensorUsePercent = new HashMap<>();
        HashMap<PeriodicSensor, HashSet<Schedule>> scheduleSensorUse = new HashMap<>();
        for (Schedule schedule : schedules) {
            AcqMode acqMode = schedule.getAcqMode();
            PeriodicSensor sensor = acqMode.getSensorConfiguration().getSensor();
            if (!scheduleSensorUse.containsKey(sensor)) {
                scheduleSensorUse.put(sensor, new HashSet<>());
            }
            scheduleSensorUse.get(sensor).add(schedule);
        }

        /*
        for (PeriodicSensor sensor : scheduleSensorUse.keySet()) {
            System.out.println("Sensor: " + sensor.getName());
            for (Schedule schedule : scheduleSensorUse.get(sensor)) {
                System.out.println("Schedule: " + schedule.toString());
            }
        }
        */

        for (PeriodicSensor sensor : scheduleSensorUse.keySet()) {
            ArrayList schedules = new ArrayList<>(scheduleSensorUse.get(sensor));
            ScheduleSum scheduleSum = new ScheduleSum(schedules);
            // System.out.println("Schedule sum: " + scheduleSum.toString());
            double active_count = 0;
            double sleep_count = 0;
            for (int i = 0; i < scheduleSum.getSum().length; i++) {
                if (scheduleSum.getSum()[i] >= 1) {
                    active_count += 1;
                } else {
                    sleep_count += 1;
                }
            }

            Double percentUse = active_count/(active_count+sleep_count);
            //System.out.println("percent use " + percentUse);
            /*
            if (sleep_count == 0) {
                percentUse = new Double(1);
            } else {
                percentUse = new Double(active_count/(active_count+sleep_count));
            }
            */
            sensorUsePercent.put(sensor, percentUse);
        }
        return sensorUsePercent;
    }


}
