package fr.uca.i3s.sparks.compomaid.model.App.Mission;

import fr.uca.i3s.sparks.compomaid.tools.MyMath;

import java.util.ArrayList;

public class ScheduleSum {
    private int sum[];

    public ScheduleSum(ArrayList<Schedule> schedules) {
        sum = schedules.get(0).toSignal();
        for (int i = 0; i < schedules.size(); i++) {
            if (i == 0) {
                sum = schedules.get(i).toSignal();
            } else {
                sum = sumTwo(sum, schedules.get(i).toSignal());
            }
        }
    }

    private int[] sumTwo(int[] s1, int[] s2) {
        int size;
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
        }
        int localsum[] = new int[s1.length];
        for (int i = 0; i < s1.length; i++) {
            localsum[i] = s1[i] + s2[i];
        }
        return localsum;
    }

    public int[] getSum() {
        return sum;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sum.length; i++){
            str.append(sum[i]);
            if (i < sum.length-1) {
                str.append(", ");
            }
        }
        return str.toString();
    }
}
