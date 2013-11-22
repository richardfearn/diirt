/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene;
import java.util.ArrayList;
import java.awt.Color;

/**
 *
 * @author carcassi
 */
public class ValueColorSchemes {

    public static ValueColorScheme grayScale(final Range range) {
        return singleRangeGradient(range, Color.BLACK, Color.WHITE, Color.RED);
    }
    
    public static ValueColorScheme jetScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(0,0,138));
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(new Color(138,0,0));
        colors.add(Color.BLACK);
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));
    }
    
    public static ValueColorScheme hotScale(final Range range) {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(30,0,0));
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.WHITE);
        colors.add(Color.BLUE);
        
        return RangeGradient(range, colors, percentageRange(colors.size() - 2));
    }
    
    public static ValueColorScheme singleRangeGradient(final Range range, final Color minValueColor, final Color maxValueColor, final Color nanColor) {
        return new ValueColorScheme() {

            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                
                double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue(), range.getMaximum().doubleValue());
                normalValue = Math.min(normalValue, 1.0);
                normalValue = Math.max(normalValue, 0.0);
                int alpha = 255;
                int red = (int) (minValueColor.getRed() + (maxValueColor.getRed() - minValueColor.getRed()) * normalValue);
                int green = (int) (minValueColor.getGreen() + (maxValueColor.getGreen() - minValueColor.getGreen()) * normalValue);
                int blue = (int) (minValueColor.getBlue() + (maxValueColor.getBlue() - minValueColor.getBlue()) * normalValue);
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
    
    public static ValueColorScheme quintipleRangeGradient(final Range range, final Color firstValueColor, final Color secondValueColor,final Color thirdValueColor,final Color fourthValueColor,final Color fifthValueColor,final Color sixthValueColor, final Color nanColor) {
        return new ValueColorScheme() {

            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                double firstNum = range.getMinimum().doubleValue();
                double secondNum = (range.getMaximum().doubleValue()-range.getMinimum().doubleValue())/5;
                double thirdNum = secondNum*2;
                double fourthNum = secondNum*3;
                double fifthNum = secondNum*4;
                double sixthNum = range.getMaximum().doubleValue();
                
                int alpha = 0, red = 0, green = 0, blue = 0;
                if(value<secondNum){
                    double normalValue = NumberUtil.normalize(value, firstNum, secondNum);
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (firstValueColor.getRed() + (secondValueColor.getRed() - firstValueColor.getRed()) * normalValue);
                    green = (int) (firstValueColor.getGreen() + (secondValueColor.getGreen() - firstValueColor.getGreen()) * normalValue);
                    blue = (int) (firstValueColor.getBlue() + (secondValueColor.getBlue() - firstValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=secondNum && value < thirdNum){
                    double normalValue = NumberUtil.normalize(value, secondNum,thirdNum);
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (secondValueColor.getRed() + (thirdValueColor.getRed() - secondValueColor.getRed()) * normalValue);
                    green = (int) (secondValueColor.getGreen() + (thirdValueColor.getGreen() - secondValueColor.getGreen()) * normalValue);
                    blue = (int) (secondValueColor.getBlue() + (thirdValueColor.getBlue() - secondValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=thirdNum && value < fourthNum){
                    double normalValue = NumberUtil.normalize(value, thirdNum,fourthNum);
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (thirdValueColor.getRed() + (fourthValueColor.getRed() - thirdValueColor.getRed()) * normalValue);
                    green = (int) (thirdValueColor.getGreen() + (fourthValueColor.getGreen() - thirdValueColor.getGreen()) * normalValue);
                    blue = (int) (thirdValueColor.getBlue() + (fourthValueColor.getBlue() - thirdValueColor.getBlue()) * normalValue);  
                }
                
                if(value>=fourthNum && value < fifthNum){
                    double normalValue = NumberUtil.normalize(value, fourthNum,fifthNum);
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (fourthValueColor.getRed() + (fifthValueColor.getRed() - fourthValueColor.getRed()) * normalValue);
                    green = (int) (fourthValueColor.getGreen() + (fifthValueColor.getGreen() - fourthValueColor.getGreen()) * normalValue);
                    blue = (int) (fourthValueColor.getBlue() + (fifthValueColor.getBlue() - fourthValueColor.getBlue()) * normalValue);  
                }
                if(value>=fifthNum && value <= sixthNum){
                    double normalValue = NumberUtil.normalize(value, fifthNum,sixthNum);
                    normalValue = Math.min(normalValue, 1.0);
                    normalValue = Math.max(normalValue, 0.0);
                    alpha = 255;
                    red = (int) (fifthValueColor.getRed() + (sixthValueColor.getRed() - fifthValueColor.getRed()) * normalValue);
                    green = (int) (fifthValueColor.getGreen() + (sixthValueColor.getGreen() - fifthValueColor.getGreen()) * normalValue);
                    blue = (int) (fifthValueColor.getBlue() + (sixthValueColor.getBlue() - fifthValueColor.getBlue()) * normalValue);  
                }
                
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }

    public static ValueColorScheme RangeGradient(final Range range, final ArrayList<Color> colors, final ArrayList<Double> percentages){
        return new ValueColorScheme() {
            
            Color nanColor = colors.get(colors.size()-1); 
            @Override
            public int colorFor(double value) {
                if (Double.isNaN(value)) {
                    return nanColor.getRGB();
                }
                double fullRange = range.getMaximum().doubleValue() - range.getMinimum().doubleValue();
                int alpha = 0, red = 0, green = 0, blue = 0;
                for(int i = 0; i < percentages.size()-1;i++){
                    if(range.getMinimum().doubleValue()+percentages.get(i)*fullRange <= value && value <= range.getMinimum().doubleValue()+percentages.get(i+1)*fullRange){
                        double normalValue = NumberUtil.normalize(value, range.getMinimum().doubleValue()+percentages.get(i)*fullRange, range.getMinimum().doubleValue()+percentages.get(i+1)*fullRange);
                        normalValue = Math.min(normalValue, 1.0);
                        normalValue = Math.max(normalValue, 0.0);
                        alpha = 255;
                        red = (int) (colors.get(i).getRed() + (colors.get(i+1).getRed() - colors.get(i).getRed()) * normalValue);
                        green = (int) (colors.get(i).getGreen() + (colors.get(i+1).getGreen() - colors.get(i).getGreen()) * normalValue);
                        blue = (int) (colors.get(i).getBlue() + (colors.get(i+1).getBlue() - colors.get(i).getBlue()) * normalValue);
                    }
                }
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        };
    }
    
    private static ArrayList<Double> percentageRange(int size){
        ArrayList<Double> percentages = new ArrayList<>();
        
        percentages.add(0.0);
        
        for (int i = 1; i <= size; i++){
            percentages.add((double) i / size);
        }
        
        return percentages;
    }
}
