package me.samboycoding.gowguildtool.utils;

import java.math.BigDecimal;

/**
 * Helper for math.
 * @author Sam
 */
public class MathHelper
{

    public static double round(double unrounded, int precision, int roundingMode)
    {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
}
