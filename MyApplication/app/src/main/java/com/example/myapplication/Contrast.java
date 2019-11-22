package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Contrast {

    private Tools tools;
    private Functions functions;

    public Contrast (Tools tools, Functions functions){
        this.tools = tools;
        this.functions = functions;
    }

    // TODO contraste with HSV !
    protected Bitmap linear_transformation(Bitmap bmp){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        int[] max_min_array = tools.max_min_colors(pixels);
        int[] LUTred = tools.createLUTred(max_min_array);
        int[] LUTgreen = tools.createLUTgreen(max_min_array);
        int[] LUTblue = tools.createLUTblue(max_min_array);

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            red = LUTred[red];
            green = LUTgreen[green];
            blue = LUTblue[blue];

            colors[i] = Color.rgb(red, green, blue);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    // TODO doesn't work
    protected Bitmap histogramEqualizationGRAY (Bitmap bmp){ // Gray version
        Bitmap p_modif = functions.toGray(bmp);
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        // Initialisation histogramC
        int[] histogram = tools.createHistogramGRAY(pixels);
        int[] histogramC = new int [256];
        histogramC[0] = histogram[0];

        for (int i = 1; i < 256; i++){
            histogramC[i] = histogramC[i-1] + histogram[i];
        }

        // Initialisation LUT
        int[] LUT = new int [256];

        for (int i = 0; i < 256; i++){
            int color = Color.red(tools.colorToGray(pixels[i]));
            LUT[i] = (255 * histogramC[color]) / (p_modif.getWidth() * p_modif.getHeight());
        }

        // Set colors
        for (int i = 0; i < pixels.length; i++){
            int color = Color.red(tools.colorToGray(pixels[i]));
            colors[i] = Color.rgb(LUT[color], LUT[color], LUT[color]);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }
}
