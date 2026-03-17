package com.jfudali.classifier;

import com.jfudali.dto.ClassificationInput;
import com.jfudali.dto.ClassificationOutput;

public interface Classifier {
    ClassificationOutput classify(ClassificationInput classificationInput);
}
