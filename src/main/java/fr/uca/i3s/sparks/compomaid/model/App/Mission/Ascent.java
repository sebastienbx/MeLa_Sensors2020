package fr.uca.i3s.sparks.compomaid.model.App.Mission;


public class Ascent extends MissionStep {

    public Ascent(int duration_min, int depth_m) {
        super("ascent", duration_min, depth_m);
    }

    @Override
    public int getActuatorEnergy_mWh() {
        double energy = 2000. + 0.001 * this.getDepth_m() * this.getDepth_m();
        return (int) energy;
    }
}

/*
    @Override
    public double getTimeConsumption() {
        return this.getParameterTime_min();
    }

    @Override
    public double getEnergyConsumption() {
        // return energy in Wh
        // Energy consumed by actuators
        double actuatorEnergy = (double)(this.getDepth_m()) / 1000.;

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
