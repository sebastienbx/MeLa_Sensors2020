package fr.uca.i3s.sparks.compomaid.model.App.Mission;


public class Surface extends MissionStep {

    public Surface(int duration_min, int depth_m) {
        super("surface", duration_min, depth_m);
    }
}


/*

    @Override
    public double getTimeConsumption() {
        // Return default time
        return this.getParameterTime_min();
    }

    @Override
    public double getEnergyConsumption() {
        // return energy in Wh
        // Energy consumed for Iridium transmission
        return 1.;
    }

    @Override
    public double getPowerConsumption() {
        // return energy in mW
        return this.getEnergyConsumption() / (this.getTimeConsumption() / 60);
    }

 */