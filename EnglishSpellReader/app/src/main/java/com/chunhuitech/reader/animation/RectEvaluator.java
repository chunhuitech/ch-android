package com.chunhuitech.reader.animation;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

public class RectEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Rect startRect = (Rect)startValue;
        Rect endRect = (Rect)endValue;
        return new Rect(startRect.left + (int)(fraction*(endRect.left - startRect.left)),
                startRect.top + (int)(fraction*(endRect.top - startRect.top)),
                startRect.right + (int)(fraction*(endRect.right - startRect.right)),
                startRect.bottom + (int)(fraction*(endRect.bottom - startRect.bottom)));
    }
}
