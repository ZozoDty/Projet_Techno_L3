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

    // TODO contraste with HSV and linear extension less
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

    protected Bitmap histogramEqualizationGRAY (Bitmap bmp){ // Gray version
        Bitmap p_modif = bmp;
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
        int[] LUT_red = new int [pixels.length];
        int[] LUT_green = new int [pixels.length];
        int[] LUT_blue = new int [pixels.length];

        for (int i = 0; i < pixels.length; i++){
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            LUT_red[i] = 255 * histogramC[red] / pixels.length;
            LUT_green[i] = 255 * histogramC[green] / pixels.length;
            LUT_blue[i] = 255 * histogramC[blue] / pixels.length;
        }

        for (int i = 0; i < pixels.length; i++){
            colors[i] = Color.rgb(LUT_red[i], LUT_green[i], LUT_blue[i]);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap histogramEqualizationRGB(Bitmap bmp){ // Gray version
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        // Initialisation histogramC
        int[][] histogram = tools.createHistogramRGB(pixels);
        int[] histogramC_red = new int [256];
        int[] histogramC_green = new int [256];
        int[] histogramC_blue = new int [256];
        histogramC_red[0] = histogram[0][0];
        histogramC_green[0] = histogram[1][0];
        histogramC_blue[0] = histogram[2][0];

        for (int i = 1; i < 256; i++){
            histogramC_red[i] = histogramC_red[i-1] + histogram[0][i];
            histogramC_green[i] = histogramC_green[i-1] + histogram[1][i];
            histogramC_blue[i] = histogramC_blue[i-1] + histogram[2][i];
        }

        // Initialisation LUT
        int[] LUT_red = new int [pixels.length];
        int[] LUT_green = new int [pixels.length];
        int[] LUT_blue = new int [pixels.length];

        for (int i = 0; i < pixels.length; i++){
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            LUT_red[i] = 255 * histogramC_red[red] / pixels.length;
            LUT_green[i] = 255 * histogramC_green[green] / pixels.length;
            LUT_blue[i] = 255 * histogramC_blue[blue] / pixels.length;
        }

        for (int i = 0; i < pixels.length; i++){
            colors[i] = Color.rgb(LUT_red[i], LUT_green[i], LUT_blue[i]);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }
}
