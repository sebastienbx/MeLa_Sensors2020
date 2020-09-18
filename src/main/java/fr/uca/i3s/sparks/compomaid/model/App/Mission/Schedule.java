package fr.uca.i3s.sparks.compomaid.model.App.Mission;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.AcqMode;
import fr.uca.i3s.sparks.compomaid.tools.Logger;


public class Schedule {

    Logger logger = new Logger(true);

    private AcqMode acqMode;
    private boolean isContinuousExecution;
    private Integer period;
    private String periodUnit;

    Schedule(AcqMode acqMode) {
        this.acqMode = acqMode;
        this.isContinuousExecution = true;
        this.period = Integer.MAX_VALUE;
    }

    Schedule(AcqMode acqMode, int period, String unit) {
        this.acqMode = acqMode;
        this.isContinuousExecution = false;
        this.period = period;
        this.periodUnit = unit;
    }

    public void setAcqMode(AcqMode acqMode) {
        this.acqMode = acqMode;
    }

    public AcqMode getAcqMode() {
        return acqMode;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public Integer getPeriod_s() {
        Integer period_s = 0;
        if (periodUnit.equals("seconds")) {
            period_s = period;
        } else if (periodUnit.equals("minutes")) {
            period_s = period*60;
        } else if (periodUnit.equals("hours")) {
            period_s = period*60*60;
        } else if (periodUnit.equals("days")) {
            period_s = period*60*60*24;
        } else {
            logger.err("Unknown unit");
        }
        return period_s;
    }

    public double getPeriod_us() {
        return this.getPeriod_s()*1000000;
    }

    public boolean isContinuousExecution() {
        return this.isContinuousExecution;
    }

    public int[] toSignal() {
        int signal[];
        if (this.isContinuousExecution) {
            signal = new int[1];
            signal[0] = 1;
        } else {
            signal = new int[this.getPeriod_s()];
            int activationTime = (int)Math.ceil(acqMode.getSensorActivationTime_s());
            for (int i = 0; i < activationTime; i++) {
                signal[i] = 1;
            }
        }
        return signal;
    }

    public String toString() {
        String str;
        if (this.isContinuousExecution) {
            str = this.acqMode.getName() + " continuously";
        } else  {
            str = this.acqMode.getName() + " with period " + this.getPeriod_s() + "s";
        }
        return str;
    }
}


/*


    @Override
    public double getTimeConsumption() {
        // Return the period
        return this.period_min;
    }

    @Override
    public double getEnergyConsumption() {
        // A schedule doesn't have time limit if it is continuous so energy consumption cannot be calculated
        logger.err("Energy of schedules is not valid");
        return Double.MAX_VALUE;
    }

    @Override
    public double getPowerConsumption() {
        // Return power in mW
        logger.err("getPowerConsumption() of Schedule Not implemented");
        double power_mW = acqMode.getPowerConsumption();
        //power_mW = ((double)this.duration_min / (double)this.period_min) * power_mW;
        return power_mW;
    }


 */


/*
    public boolean equals(Schedule toCompare) {
        if (this.isContinuousExecution && toCompare.isContinuousExecution) {
            return true;
        } else if (this.period_min.equals(toCompare.period_min)) {
            return true;
        } else {
            return false;
        }
    }
 */