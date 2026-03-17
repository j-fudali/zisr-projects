package com.jfudali;

import com.jfudali.classifier.Classifier;
import com.jfudali.classifier.SimpleIrisGaussClassifier;
import com.jfudali.dto.ClassificationInput;

import java.util.List;

public class Main {

    static void main() {
        showClassification();
    }

    private static void showClassification() {
        final Classifier classifier = new SimpleIrisGaussClassifier();
        final List<ClassificationInput> classificationInputs = List.of(
                new ClassificationInput(5.4,3.4, 1.7,0.2),
                new ClassificationInput(6.7, 3.0,5.0, 1.7),
                new ClassificationInput(6.8, 3.0, 5.5, 2.1),
                new ClassificationInput(6.6, 2.9, 4.6, 1.3),
                new ClassificationInput(4.6, 3.1, 1.5, 0.2)
        );
        classificationInputs.stream()
                .map(classificationInput ->
                        classifier.classify(classificationInput).name())
                .forEach(System.out::println);
    }
}
