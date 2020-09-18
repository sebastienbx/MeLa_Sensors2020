package fr.uca.i3s.sparks.compomaid.tools;


public class CrossCorrelation {

    public static int[] crossCorrelation(int[] f, int[] g) {

        int len = Math.max(f.length, g.length);
        int[] padded_f = new int[len];
        if (f.length < len) {
            int i = 0;
            while (i < f.length) {
                padded_f[i] = f[i];
                i++;
            }
            while (i < len) {
                padded_f[i] = 0;
                i++;
            }
            f = padded_f;
        }

        int[] padded_g = new int[len];
        if (g.length < 2*len) {
            int i = 0;
            while (i < g.length) {
                padded_g[i] = g[i];
                i++;
            }
            while (i < len) {
                padded_g[i] = 0;
                i++;
            }
            g = padded_g;
        }

        int[] doubled_g = new int[2*len];
        for (int i = 0; i < g.length; i++) {
            doubled_g[i] = g[i];
            doubled_g[g.length+i] = g[i];
        }
        g = doubled_g;

        int[] fcg = new int [len];
        for (int n = 0; n < len; n++) {
            for (int m = 0; m < len; m++) {
                fcg[n] += f[m] * g[n + m];
            }
        }

        return fcg;
    }

    public static void main(String[] args) {
        int[] f = {0, 0, 1, 1, 1, 1, 1, 0, 0, 0};
        int[] g = {1, 1, 1, 1, 1, 0, 0, 0, 0, 0};

        int[] out = crossCorrelation(f, g);

        for (int i = 0; i < out.length; i++) {
            System.out.println(out[i]);
        }
    }
}