package fr.uca.i3s.sparks.compomaid.composer;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.MissionStep;
import fr.uca.i3s.sparks.compomaid.model.App.Mission.Schedule;
import fr.uca.i3s.sparks.compomaid.tools.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Composer {

    private static Logger logger = new Logger(false);


    public static void compose2Apps(App app1, App app2) {
        // Check mission configuration
        if (app1.getParkDepth_m() != app2.getParkDepth_m()) {
            logger.err("parkDepth of App1 and App2 must be the same");
        }
        if (app1.getPark().getDefaultDuration_min() != app2.getPark().getDefaultDuration_min()) {
            logger.err("parkTime of App1 and App2 must be the same");
        }


        // Change name of app1 that will contain the app2
        app1.setName(app1.getName() + "U" + app2.getName());

        // Copy acquisition modes
        for (AcqMode acqMode : app2.getAcqModes()) {
            app1.addAcqMode(acqMode);
        }

        // Compose each mission step
        ArrayList<MissionStep> app1MissionSteps = new ArrayList<>();
        ArrayList<MissionStep> app2MissionSteps = new ArrayList<>();
        app1MissionSteps.addAll(0,app1.getMissionSteps());
        app2MissionSteps.addAll(0,app2.getMissionSteps());

        // Verify that the number of mission mission steps are equals (useless since this number is fixed)
        if (app1MissionSteps.size() != app2MissionSteps.size()) {
            logger.err("App1 and App2 have a different number of mission steps");
        }

        // For each mission step
        MissionStep app1MissionStep;
        MissionStep app2MissionStep;
        for(int i = 0; i < app1MissionSteps.size(); i++) {
            app1MissionStep = app1MissionSteps.get(i);
            app2MissionStep = app2MissionSteps.get(i);

            // Verify that the sensor configuration of schedules are compatible
            for (Schedule schedule1 : app1MissionStep.getSchedules()) {
                for (Schedule schedule2 : app2MissionStep.getSchedules()) {
                    // If sensors are the same
                    if (schedule1.getAcqMode().getSensorConfiguration().getSensor() == schedule2.getAcqMode().getSensorConfiguration().getSensor()) {
                        // Compare configuration of sensors
                        if (schedule1.getAcqMode().getSensorConfiguration().getFrequency_hz() != schedule2.getAcqMode().getSensorConfiguration().getFrequency_hz()) {
                            logger.err("The sensor " + schedule1.getAcqMode().getSensorConfiguration().getSensor().getName()
                                    + " is is used by two schedules at the same time with a different configuration");
                        }
                    }
                }
            }
        }


        // Copy schedules
        for (Schedule schedule : app2.getDescent().getSchedules()) {
            app1.getDescent().addSchedule(schedule);
        }
        for (Schedule schedule : app2.getPark().getSchedules()) {
            app1.getPark().addSchedule(schedule);
        }
        for (Schedule schedule : app2.getAscent().getSchedules()) {
            app1.getAscent().addSchedule(schedule);
        }
    }

}
