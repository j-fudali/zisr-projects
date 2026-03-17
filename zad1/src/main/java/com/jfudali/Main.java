package com.jfudali;

import com.jfudali.fuzzyset.FuzzySet;
import com.jfudali.fuzzyset.GaussFS;
import com.jfudali.fuzzyset.TrapezoidFS;
import com.jfudali.fuzzyset.TriangleFS;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main(String[] args) {
        testFuzzySets();
    }
    private static void testFuzzySets() {
        List<Double> points = new ArrayList<>();
        points.add(1.2d);
        points.add(2.4d);
        points.add(3.3d);
        FuzzySet gaussFS = new GaussFS(0.0, 2.0);
        FuzzySet trapezoidFS = new TrapezoidFS(1.0, 2.0, 3.0, 4.0);
        FuzzySet triangleFS = new TriangleFS(0.0, 2.0, 4.0);

        points.forEach(point -> {
            System.out.println("Gauss: " + gaussFS.getMembership(point));
            System.out.println("Trapezoid: " + trapezoidFS.getMembership(point));
            System.out.println("Triangle: " + triangleFS.getMembership(point));

        });
    }
}
