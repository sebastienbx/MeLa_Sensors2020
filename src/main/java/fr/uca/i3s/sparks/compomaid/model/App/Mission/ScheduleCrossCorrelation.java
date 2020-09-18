package fr.uca.i3s.sparks.compomaid.model.App.Mission;

import fr.uca.i3s.sparks.compomaid.tools.CrossCorrelation;
import fr.uca.i3s.sparks.compomaid.tools.MyMath;


public class ScheduleCrossCorrelation {
    private Schedule schedule1;
    private Schedule schedule2;
    private int correlation[];
    private int period_min;
    private boolean continuous = false;

    private void computeCrossCorrelation(Schedule schedule1, Schedule schedule2) {
        int s1[];
        int s2[];
        if (schedule1.isContinuousExecution() && schedule2.isContinuousExecution()) {
            s1 = new int[1];
            s1[0] = 1;
            s2 = new int[1];
            s2[0] = 1;
            period_min = 1;
            continuous = true;
        } else if (schedule1.isContinuousExecution()) {
            s2 = schedule2.toSignal();
            s1 = new int[s2.length];
            for (int i = 0; i < s1.length; i++){
                s1[i] = 1;
            }
            period_min = s1.length;
        } else if (schedule2.isContinuousExecution()) {
            s1 = schedule1.toSignal();
            s2 = new int[s1.length];
            for (int i = 0; i < s2.length; i++){
                s2[i] = 1;
            }
            period_min = s2.length;
        } else {
            s1 = schedule1.toSignal();
            s2 = schedule2.toSignal();
            if (s1.length != s2.length) {
                int lcm = (int)MyMath.leastCommonMultiple((long)s1.length, (long)s2.length);
                int full_s1[] = new int[lcm];
                for (int i = 0; i < lcm/s1.length; i++) {
                    for (int j = 0; j < s1.length; j++) {
                        full_s1[i*s1.length + j] = s1[j];
                    }
                }
                int full_s2[] = new int[lcm];
                for (int i = 0; i < lcm/s2.length; i++) {
                    for (int j = 0; j < s2.length; j++) {
                        full_s2[i*s2.length + j] = s2[j];
                    }
                }
                s1 = full_s1;
                s2 = full_s2;
                period_min = lcm;
            } else {
                period_min = s1.length;
            }
        }
        correlation = CrossCorrelation.crossCorrelation(s1, s2);
    }

    public int[] getCorrelation() {
        return correlation;
    }

    public int getMaxCorrelation() {
        int max = 0;
        for (int i = 0; i < correlation.length; i++) {
            if (correlation[i] > max) {
                max = correlation[i];
            }
        }
        return max;
    }

    public double getCorrelationFactor() {
        return (double)this.getMaxCorrelation()/(double)period_min;
    }

    public int getTimeOffset() {
        int offset = 0;
        int maxCorrelation = this.getMaxCorrelation();
        for (int i = 0; i < correlation.length; i++) {
            if (correlation[i] == maxCorrelation) {
                offset = i;
                break;
            }
        }
        return offset;
    }

    public Schedule getSchedule1() {
        return schedule1;
    }

    public Schedule getSchedule2() {
        return schedule2;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < correlation.length; i++){
            str.append(correlation[i]);
            if (i < correlation.length-1) {
                str.append(", ");
            }
        }
        return str.toString();
    }
}
