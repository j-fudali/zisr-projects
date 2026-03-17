package com.jfudali.fuzzyset;

public class TrapezoidFS implements FuzzySet{
    private final Double a;
    private final Double b;
    private final Double c;
    private final Double d;

    public TrapezoidFS(Double a, Double b, Double c, Double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public Double getMembership(Double x) {
        if (x <= a || x >= d) {
            return 0.0;
        } else if (x >= b && x <= c) {
            return 1.0;
        } else if (x > a && x < b) {
            return (x - a) / (b - a);
        } else {
            return (d - x) / (d - c);
        }
    }
}
