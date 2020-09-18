package fr.uca.i3s.sparks.compomaid.model.Platform;


public class PeriodicSensor {

    private String name;
    private double frequencyMin_hz;
    private double frequencyMax_hz;
    private double power;

    public PeriodicSensor(String name, double frequencyMin_hz, double frequencyMax_hz, double power) {
        this.name = name;
        this.frequencyMin_hz = frequencyMin_hz;
        this.frequencyMax_hz = frequencyMax_hz;
        this.power = power;
    }

    public double getFrequencyMin_hz() {
        return frequencyMin_hz;
    }

    public double getFrequencyMax_hz() {
        return frequencyMax_hz;
    }

    //public double getPeriod_s() {
    //    return 1./frequency;
    //}

    public double getPower() {
        return this.power;
    }

    public String getName() {
        return name;
    }
}
