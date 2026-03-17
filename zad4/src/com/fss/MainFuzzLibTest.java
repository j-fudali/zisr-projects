package com.fss;

import fuzzlib.FuzzySet;
import fuzzlib.creators.OperationCreator;
import fuzzlib.norms.Norm;
import fuzzlib.norms.SNorm;
import fuzzlib.norms.TNorm;

public class MainFuzzLibTest {
    public static void main(String[] args) {
//        FuzzySet set = new FuzzySet();
//        FuzzySet set2 = new FuzzySet();
//        FuzzySet set4 = new FuzzySet();
//
//        set.addPoint(-2.0, 0.0);
//        set.addPoint(0.0, 1.0);
//        set.addPoint(2.0, 0.0);
//
//        set2.newTriangle(0.0, 1.0);
//
//        set.fuzzyfy(-1);
//
//        System.out.println(set);
//        System.out.println(set2);
//
//        Norm op = OperationCreator.newTNorm(TNorm.TN_MINIMUM);
//        FuzzySet.processSetsWithNorm(set4, set, set2, op);

//        set4.PackFlatSections();

//        System.out.println(set4);

        FuzzySet set = new FuzzySet();
        FuzzySet set2 = new FuzzySet();
        FuzzySet set3 = new FuzzySet();

        set.newTriangle(0.0, 1.0);
        set2.newGaussian(0.0, 2.0);

        set.fuzzyfy(-5.0);

        System.out.println(set);
        System.out.println(set2);

        Norm op = OperationCreator.newSNorm(SNorm.SN_MAXIMUM);
        FuzzySet.processSetsWithNorm(set3, set, set2, op);

        System.out.println(set3);
        System.out.println(set3.getMaximumMembership());
    }
}
