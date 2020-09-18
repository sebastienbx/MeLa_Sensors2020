package fr.uca.i3s.sparks.compomaid.model.App.Mission;


public class Descent extends MissionStep {

    public Descent(int duration_min, int depth_m) {
        super("descent", duration_min, depth_m);
    }

    @Override
    public int getActuatorEnergy_mWh() {
        return 200;
    }
}


/*
    @Override
    public double getTimeConsumption() {
        return this.getParameterTime_min();
    }

    @Override
    public double getEnergyConsumption() {
        // return energy in mWh
        // Energy consumed by actuators
        double actuatorEnergy = (double)(this.getDepth_m()) / 10000.;

        // Energy consumed by schedules
        double schedulesEnergy = 0;
        for (Schedule schedule : this.getSchedulesWithoutSleep()) {
            schedulesEnergy += schedule.getPowerConsumption() / 1000. * this.getTimeConsumption() / 60.;
        }

        return actuatorEnergy + schedulesEnergy;
    }

    @Override
    public double getPowerConsumption() {
        // return energy in mW
        return this.getEnergyConsumption() / (this.getTimeConsumption() / 60.);
    }
 */