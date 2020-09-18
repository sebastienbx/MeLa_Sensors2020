package fr.uca.i3s.sparks.compomaid.tools;


public class MyMath {

    public static long greatestCommonDivisor(long n1, long n2) {
        int gcd = 0;
        for(int i = 1; i <= n1 && i <= n2; ++i) {
            // Checks if i is factor of both integers
            if(n1 % i == 0 && n2 % i == 0)
                gcd = i;
        }
        return gcd;
    }

    public static long leastCommonMultiple(long n1, long n2) {
        return (n1 * n2) / greatestCommonDivisor(n1, n2);
    }

    public static void main(String[] args) {
        System.out.println("greatestCommonDivisor");
        System.out.println(greatestCommonDivisor(8, 2));
        System.out.println(greatestCommonDivisor(5, 3));
        System.out.println(greatestCommonDivisor(60, 168));

        System.out.println("leastCommonMultiple");
        System.out.println(leastCommonMultiple(8, 2));
        System.out.println(leastCommonMultiple(5, 3));
        System.out.println(leastCommonMultiple(60, 168));
    }
}