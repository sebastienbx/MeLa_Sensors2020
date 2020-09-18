package fr.uca.i3s.sparks.compomaid.model.Timers;

import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;


public class SensorTimer extends AbstractTimer {
    private PeriodicSensor periodicSensor;

    public SensorTimer(PeriodicSensor periodicSensor, long period, String unit) {
        super(period, unit);
        this.periodicSensor = periodicSensor;
    }

    public PeriodicSensor getPeriodicSensor() {
        return periodicSensor;
    }

    public SensorTimer copy () {
        SensorTimer newSensorTimer = new SensorTimer(this.periodicSensor, this.getPeriod(), this.getUnit());
        return newSensorTimer;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SensorTimer)) {
            return false;
        }
        SensorTimer other = (SensorTimer) obj;
        boolean equals = true;
        if (this.getPeriod_us() != other.getPeriod_us()) {
            equals = false;
        }
        if (this.periodicSensor != other.getPeriodicSensor()) {
            equals = false;
        }
        return equals;
    }
}
