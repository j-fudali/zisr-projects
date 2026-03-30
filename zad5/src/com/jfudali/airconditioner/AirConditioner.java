package com.jfudali.airconditioner;

import fuzzlib.FuzzySet;
import fuzzlib.creators.OperationCreator;
import fuzzlib.norms.SNorm;
import fuzzlib.norms.TNorm;

public class AirConditioner {
    private static final int LOW = 0;
    private static final int MEDIUM = 1;
    private static final int HIGH = 2;
    private final double temperature;
    private final double humidity;

    public AirConditioner(final double temperature, final double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public void runConditioner() {
        FuzzySet lowTemperature = gaussian(18.0, 4.0);
        FuzzySet mediumTemperature = gaussian(24.0, 4.0);
        FuzzySet highTemperature = gaussian(30.0, 4.0);

        FuzzySet lowHumidity = gaussian(35.0, 15.0);
        FuzzySet mediumHumidity = gaussian(55.0, 15.0);
        FuzzySet highHumidity = gaussian(75.0, 15.0);

        FuzzySet lowCooling = gaussian(0.0, 18.0);
        FuzzySet mediumCooling = gaussian(50.0, 18.0);
        FuzzySet highCooling = gaussian(100.0, 18.0);

        FuzzySet lowFan = gaussian(0.0, 18.0);
        FuzzySet mediumFan = gaussian(50.0, 18.0);
        FuzzySet highFan = gaussian(100.0, 18.0);

        FuzzySet[] temperatureSets = {lowTemperature, mediumTemperature, highTemperature};
        FuzzySet[] humiditySets = {lowHumidity, mediumHumidity, highHumidity};

        double[] temperatureMemberships = membershipsFor(temperatureSets, temperature);
        double[] humidityMemberships = membershipsFor(humiditySets, humidity);

        FuzzySet[][] coolingRules = {
                {lowCooling, mediumCooling, mediumCooling},
                {mediumCooling, mediumCooling, highCooling},
                {highCooling, highCooling, highCooling}
        };

        FuzzySet[][] fanRules = {
                {lowFan, lowFan, mediumFan},
                {mediumFan, mediumFan, highFan},
                {highFan, highFan, highFan}
        };

        FuzzySet coolingResult = evaluateRules(
                temperatureMemberships, humidityMemberships, coolingRules);
        FuzzySet fanResult = evaluateRules(
                temperatureMemberships, humidityMemberships, fanRules);

        double coolingLevel = coolingResult.DeFuzzyfy();
        double fanLevel = fanResult.DeFuzzyfy();

        System.out.println("Temperatura wejściowa = " + temperature + " C");
        System.out.println("Wilgotność wejściowa = " + humidity + " %");
        System.out.println();

        printMemberships("Temperatura",
                temperatureMemberships[LOW],
                temperatureMemberships[MEDIUM],
                temperatureMemberships[HIGH]);
        printMemberships("Wilgotność",
                humidityMemberships[LOW],
                humidityMemberships[MEDIUM],
                humidityMemberships[HIGH]);

        System.out.println();
        System.out.println("Poziom chłodzenia = " + formatPercent(coolingLevel) + "% (" +
                dominantLabel(coolingLevel, lowCooling, mediumCooling, highCooling) + ")");
        System.out.println("Poziom nawiewu = " + formatPercent(fanLevel) + "% (" +
                dominantLabel(fanLevel, lowFan, mediumFan, highFan) + ")");
    }

    private FuzzySet gaussian(final double center,  final double width) {
        FuzzySet set = new FuzzySet();
        set.newGaussian(center, width);
        return set;
    }

    private double[] membershipsFor(final FuzzySet[] sets, final double value) {
        double[] memberships = new double[sets.length];

        for (int i = 0; i < sets.length; i++) {
            memberships[i] = sets[i].getMembership(value);
        }

        return memberships;
    }

    private FuzzySet evaluateRules(
            double[] temperatureMemberships,
            double[] humidityMemberships,
            FuzzySet[][] outputRules) {
        TNorm and = OperationCreator.newTNorm(TNorm.TN_PRODUCT);
        SNorm or = OperationCreator.newSNorm(SNorm.SN_MAXIMUM);
        FuzzySet[] ruleOutputs = new FuzzySet[temperatureMemberships.length * humidityMemberships.length];
        int index = 0;

        for (int temperatureLevel = 0; temperatureLevel < temperatureMemberships.length; temperatureLevel++) {
            for (int humidityLevel = 0; humidityLevel < humidityMemberships.length; humidityLevel++) {
                double activation = and.calc(
                        temperatureMemberships[temperatureLevel],
                        humidityMemberships[humidityLevel]);
                ruleOutputs[index++] = clippedOutput(outputRules[temperatureLevel][humidityLevel], activation, and);
            }
        }

        return aggregateOutput(or, ruleOutputs);
    }

    private FuzzySet clippedOutput(FuzzySet prototype, double level, TNorm norm) {
        FuzzySet weighted = new FuzzySet();
        weighted.assign(prototype);
        weighted.processSetAndMembershipWithNorm(level, norm);
        weighted.PackFlatSections();
        return weighted;
    }

    private FuzzySet aggregateOutput(SNorm norm, FuzzySet... ruleOutputs) {
        FuzzySet result = new FuzzySet();
        result.assign(ruleOutputs[0]);

        for (int i = 1; i < ruleOutputs.length; i++) {
            FuzzySet tmp = new FuzzySet();
            FuzzySet.processSetsWithNorm(tmp, result, ruleOutputs[i], norm);
            tmp.PackFlatSections();
            result.assign(tmp);
        }

        return result;
    }

    private void printMemberships(String label, double low, double medium, double high) {
        System.out.println(label + ":");
        System.out.println("  niska  = " + low);
        System.out.println("  średnia = " + medium);
        System.out.println("  wysoka = " + high);
    }

    private String dominantLabel(double value, FuzzySet low, FuzzySet medium, FuzzySet high) {
        double lowMembership = low.getMembership(value);
        double mediumMembership = medium.getMembership(value);
        double highMembership = high.getMembership(value);

        if (lowMembership >= mediumMembership && lowMembership >= highMembership) {
            return "niski";
        }
        if (mediumMembership >= lowMembership && mediumMembership >= highMembership) {
            return "średni";
        }
        return "wysoki";
    }

    private String formatPercent(double value) {
        return String.format("%.2f", value);
    }
}
