package fr.uca.i3s.sparks.compomaid.model.App.AcqMode;

import fr.uca.i3s.sparks.compomaid.model.Platform.PeriodicSensor;

public class SensorConfiguration {

    private PeriodicSensor periodicSensor;
    private double frequency_hz;

    public SensorConfiguration(PeriodicSensor periodicSensor, double frequency_hz) {
        this.periodicSensor = periodicSensor;
        this.frequency_hz = frequency_hz;
    }

    public PeriodicSensor getSensor() {
        return periodicSensor;
    }

    public double getFrequency_hz() {
        return frequency_hz;
    }

    public double getPeriod_s() {
        return 1./frequency_hz;
    }

}
