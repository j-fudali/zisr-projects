package com.jfudali.fuzzyset;

public class TriangleFS implements FuzzySet{
    private final Double a;
    private final Double b;
    private final Double c;

    public TriangleFS(Double a, Double b, Double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Double getMembership(Double x) {
        if (x <= a || x >= c) {
            return 0.0;
        } else if (x.equals(b)) {
            return 1.0;
        } else if (x > a && x < b) {
            return (x - a) / (b - a);
        } else {
            return (c - x) / (c - b);
        }
    }
}
