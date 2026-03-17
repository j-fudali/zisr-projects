package com.jfudali.fuzzyset;

public class GaussFS implements FuzzySet {
    private final Double m;
    private final Double sigma;

    public GaussFS(final Double m, final Double sigma) {
        this.m = m;
        this.sigma = sigma;
    }

    @Override
    public Double getMembership(Double x) {
        return Math.exp(-Math.pow(x - m, 2) / (2.0 * Math.pow(sigma, 2)));
    }
}
