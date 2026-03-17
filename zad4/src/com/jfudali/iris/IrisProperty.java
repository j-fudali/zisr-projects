package com.jfudali.iris;

public class IrisProperty {
    private final Double avg;
    private final Double dev;

    public Double getAvg() {
        return avg;
    }

    public Double getDev() {
        return dev;
    }

    public IrisProperty(Double avg, Double dev) {
        this.avg = avg;
        this.dev = dev;
    }
}
