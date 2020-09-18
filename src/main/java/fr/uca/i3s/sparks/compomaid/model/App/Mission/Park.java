package fr.uca.i3s.sparks.compomaid.model.App.Mission;


import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Condition.Probability;
import fr.uca.i3s.sparks.compomaid.tools.Logger;

import java.util.ArrayList;

public class Park extends MissionStep {
    Logger logger = new Logger(true);

    public Park(int duration_min, int depth_m) {
        super("park", duration_min, depth_m);
    }

    public int getDefaultDuration_min() {
        return super.getDuration_min();
    }

    @Override
    public int getDuration_min() {
        ArrayList<Probability> probabilities = new ArrayList<>();
        for (AcqMode acqMode : this.getAcqModes()){
            probabilities.addAll(acqMode.getAscentProbabilities());
        }

        double totalAscentProbaPerMin = 0;
        for (Probability probability : probabilities){
            totalAscentProbaPerMin += probability.getProbaPerMin();
        }

        if (totalAscentProbaPerMin > 0) {
            // Poisson distribution:
            // https://towardsdatascience.com/the-poisson-distribution-and-poisson-process-explained-4e2cb17d459

            // Compute probability to see 0 event
            double lambda = totalAscentProbaPerMin * super.getDuration_min();
            double probabilityToSee0Event = Math.exp( -lambda ) * 1 / 1; // with 1/1 = Math.pow(_lambda, 0) / factorial(0)

            // Compute probability to see more than 0 event, in other words, probability to do an ascent request during the park time
            double probabilityToAscent = 1 - probabilityToSee0Event;

            // Mean par duration is equal tu the probability to detect an event multiplied by the mean arrival time of events (1/totalAscentProbaPerMin)
            // For 1 event per hour and a park time of 60 min: (1 - exp(-1/60*60))/(1/60) = 37 min
            // For 1 event per day and a park time of 60 min = 59 min
            // For 1 event per minute and a park time of 60 min = 1 min
            int meanParkDurationWithAscentRequest = (int)Math.round(probabilityToAscent/totalAscentProbaPerMin);
            return meanParkDurationWithAscentRequest;
        } else {
            return super.getDuration_min();
        }
    }
}


/*

    @Override
    public double getTimeConsumption() {
        // Get default time for the park
        return 0;
    }

    @Override
    public double getEnergyConsumption() {
        // return energy in Wh
        double energyWh = 0;
        for (Schedule schedule : this.getSchedulesWithoutSleep()) {
            energyWh += schedule.getPowerConsumption() / 1000. * this.getTimeConsumption() / 60.;
        }
        return energyWh;
    }

    @Override
    public double getPowerConsumption() {
        // return energy in W
        return this.getEnergyConsumption() / (this.getTimeConsumption() / 60.);
    }

 */