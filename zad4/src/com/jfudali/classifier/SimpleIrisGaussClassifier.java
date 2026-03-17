package com.jfudali.classifier;

import com.jfudali.dto.ClassificationInput;
import com.jfudali.dto.ClassificationOutput;
import com.jfudali.iris.Iris;
import com.jfudali.iris.IrisProperty;
import fuzzlib.FuzzySet;
import fuzzlib.creators.OperationCreator;
import fuzzlib.norms.TNorm;

import java.util.Comparator;
import java.util.List;

public class SimpleIrisGaussClassifier implements Classifier {
    public static final String IRIS_SETOSA = "irisSetosa";
    public static final String IRIS_VERSICOLOR = "irisVersicolor";
    public static final String IRIS_VIRIGINICA = "irisVirginica";

    private final TNorm aggregationNorm = OperationCreator.newTNorm(TNorm.TN_PRODUCT);
    private final List<Iris> irisList = List.of(
            new Iris(IRIS_SETOSA,
                    new IrisProperty(5.0, 0.4),
                    new IrisProperty(3.4, 0.4),
                    new IrisProperty(1.5, 0.2),
                    new IrisProperty(0.2, 0.1)),
            new Iris(IRIS_VERSICOLOR,
                    new IrisProperty(5.9, 0.5),
                    new IrisProperty(2.8, 0.3),
                    new IrisProperty(4.3, 0.5),
                    new IrisProperty(1.3, 0.2)
            ),
            new Iris(IRIS_VIRIGINICA,
                    new IrisProperty(6.6, 0.6),
                    new IrisProperty(3.0, 0.3),
                    new IrisProperty(5.6, 0.6),
                    new IrisProperty(2.0, 0.3)
            )
    );
    @Override
    public ClassificationOutput classify(ClassificationInput classificationInput) {
        return irisList.stream()
                .map(iris -> getMembershipForClass(iris, classificationInput))
                .max(Comparator.comparing(ClassificationOutput::value))
                .orElseThrow(() -> new IllegalStateException("Brak zdefiniowanych irysów."));
    }

    private ClassificationOutput getMembershipForClass(final Iris iris, final ClassificationInput classificationInput) {
        double value = 1.0;
        value = aggregationNorm.calc(value, calculateMembershipForProperty(iris.getPetalLengthAvg(), classificationInput.pl()));
        value = aggregationNorm.calc(value, calculateMembershipForProperty(iris.getPetalWidthAvg(), classificationInput.pw()));
        value = aggregationNorm.calc(value, calculateMembershipForProperty(iris.getSepalLengthAvg(), classificationInput.sl()));
        value = aggregationNorm.calc(value, calculateMembershipForProperty(iris.getSepalWidthAvg(), classificationInput.sw()));
        return new ClassificationOutput(iris.getName(), value);
    }

    private Double calculateMembershipForProperty(final IrisProperty irisProperty,
                                                  final Double comparedValue) {
        final FuzzySet fs = new FuzzySet();
        fs.newGaussian(irisProperty.getAvg(), 2 * irisProperty.getDev());
        return fs.getMembership(comparedValue);
    }
}
