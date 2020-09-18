package fr.uca.i3s.sparks.compomaid.model.Timers;

public class SoftTimer extends AbstractTimer{

    public SoftTimer (long period, String unit) {
        super(period, unit);
    }

    public SoftTimer copy () {
        return new SoftTimer(this.getPeriod(), this.getUnit());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SoftTimer)) {
            return false;
        }
        SoftTimer other = (SoftTimer) obj;
        boolean equals = true;
        if (this.getPeriod_us() != other.getPeriod_us()) {
            equals = false;
        }
        return equals;
    }
}
