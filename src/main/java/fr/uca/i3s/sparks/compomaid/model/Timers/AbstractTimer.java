package fr.uca.i3s.sparks.compomaid.model.Timers;

import fr.uca.i3s.sparks.compomaid.tools.Logger;

public abstract class AbstractTimer {

    private static Logger logger = new Logger();

    private double period_us;
    private long period;
    private String unit;

    AbstractTimer (long period, String unit) {
        this.period = period;
        this.unit = unit;

        if (unit.equals("usec")) {
            this.period_us = (double)this.period;
        } else if (unit.equals("msec")) {
            this.period_us = (double)this.period*1000.;
        } else if (unit.equals("sec")) {
            this.period_us = (double)this.period*1000.*1000.;
        } else if (unit.equals("min")) {
            this.period_us = (double)this.period*1000.*1000.*60.;
        } else if (unit.equals("hour")) {
            this.period_us = (double)this.period*1000.*1000.*60.*60.;
        } else if (unit.equals("mHz")) {
            this.period_us = 1000.*1000.*1000./(double)this.period;
        } else if (unit.equals("Hz")) {
            this.period_us = 1000.*1000./(double)this.period;
        } else if (unit.equals("kHz")) {
            this.period_us = 1000./(double)this.period;
        } else {
            logger.err("Cannot find any time unit equal to " + unit);
        }
    }

    public long getPeriod() {
        return period;
    }

    public String getUnit() {
        return unit;
    }

    public double getPeriod_us() {
        return period_us;
    }

    public boolean isFrequencyUnit() {
        boolean ret = false;
        if (unit.equals("mHz")) {
            ret = true;
        } else if (unit.equals("Hz")) {
            ret = true;
        } else if (unit.equals("kHz")) {
            ret = true;
        }
        return ret;
    }

    public double getPeriod_ms() {
        return this.period_us / 1000.;
    }

    public double getPeriod_s() {
        return this.period_us / 1000000.;
    }


    public long getPeriod_us_as_long() {
        long test_long = (long)period_us;
        if ((double)test_long != period_us) {
            logger.wrn("Casting the period (double) " + period_us + " to a (long) give " + test_long);
        }
        return (long)period_us;
    }

}
