package com.example.myapplication;

import android.graphics.Color;

public class Tools {

    public Tools(){}

    protected float[] RGBToHSV(int red, int green, int blue){
        float[] hsv = new float[3];

        float r = red / 255.F;
        float g = green / 255.F;
        float b = blue / 255.F;

        float Cmax = Math.max(r, Math.max(g, b));
        float Cmin = Math.min(r, Math.min(g, b));
        float delta = Cmax - Cmin;

        /* --- Hue calculation --- */
        if (delta == 0){
            hsv[0] = 0;
        }
        else if (Cmax == r){
            hsv[0] = ((g - b)/delta) % 6;
        }
        else if (Cmax == g){
            hsv[0] = (b - r)/delta + 2;
        }
        else if (Cmax == b){
            hsv[0] = (r - g)/delta + 4;
        }
        hsv[0] = Math.round(hsv[0] * 60);

        if (hsv[0] < 0){
            hsv[0] += 360;
        }

        /* --- Saturation calculation --- */
        if (Cmax == 0){
            hsv[1] = 0;
        }
        else{
            hsv[1] = delta / Cmax;
        }

        /* --- Value calculation --- */
        hsv[2] = Cmax;

        return hsv;
    }

    protected int HSVToRGB(float[] hsv){
        float h = hsv[0];
        float s = hsv[1];
        float v = hsv[2];

        float c = v * s;
        float x = c * (1 - Math.abs((h/60) % 2 - 1));

        float r = 0;
        float g = 0;
        float b = 0;

        if (h >= 0){
            if (h < 60){
                r = c;
                g = x;
                b = 0;
            }
            else if (h < 120){
                r = x;
                g = c;
                b = 0;
            }
            else if (h < 180){
                r = 0;
                g = c;
                b = x;
            }
            else if (h < 240){
                r = 0;
                g = x;
                b = c;
            }
            else if (h < 300){
                r = x;
                g = 0;
                b = c;
            }
            else if (h < 360){
                r = c;
                g = 0;
                b = x;
            }
        }

        float m = v - c;

        int red, green, blue;

        red = (int) ((r + m) * 255);
        green = (int) ((g + m) * 255);
        blue = (int) ((b + m) * 255);

        return Color.rgb(red, green, blue);
    }

    protected int colorToGray(int pixel){
        double red = Color.red(pixel) * 0.3;
        double green = Color.green(pixel) * 0.59;
        double blue = Color.blue(pixel) * 0.11;
        int tmp = (int) (red + green + blue);

        return Color.rgb(tmp, tmp, tmp);
    }

    protected boolean isInside(float test, int start, int end){
        return (test >= start && test <= end);
    }

    protected int[] max_min_colors (int[] pixels){
        int max_red = 0, min_red = 255,
                max_green = 0, min_green = 255,
                max_blue = 0, min_blue = 255,
                red, green, blue;

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            if (red > max_red)
                max_red = red;
            if (red < min_red)
                min_red = red;

            if (green > max_green)
                max_green = green;
            if (green < min_green)
                min_green = green;

            if (blue > max_blue)
                max_blue = blue;
            if (blue < min_blue)
                min_blue = blue;
        }
        return new int[] {max_red, min_red, max_green, min_green, max_blue, min_blue};
    }

    protected int[] createLUTred (int[] max_min_array){
        int max_red = max_min_array[0];
        int min_red = max_min_array[1];
        int[] LUTred = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (max_red != min_red || max_red - min_red == 0) {
                LUTred[ng] = (255 * (ng - min_red) / (max_red - min_red));
            }
            else {
                LUTred[ng] = max_red;
            }
        }
        return LUTred;
    }

    protected int[] createLUTgreen (int[] max_min_array){
        int max_green = max_min_array[2];
        int min_green = max_min_array[3];
        int[] LUTgreen = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (max_green != min_green || max_green - min_green == 0) {
                LUTgreen[ng] = (255 * (ng - min_green) / (max_green - min_green));
            }
            else {
                LUTgreen[ng] = max_green;
            }
        }
        return LUTgreen;
    }

    protected int[] createLUTblue (int[] max_min_array){
        int max_blue = max_min_array[4];
        int min_blue = max_min_array[5];
        int[] LUTblue = new int[256];

        for (int ng = 0; ng < 256; ng++){
            if (max_blue != min_blue || max_blue - min_blue == 0) {
                LUTblue[ng] = (255 * (ng - min_blue) / (max_blue - min_blue));
            }
            else {
                LUTblue[ng] = max_blue;
            }
        }
        return LUTblue;
    }

    // TODO Check if correct
    protected int[][] createHistogramRGB (int[] pixels){ // RGB version
        int red, green, blue;

        int[][] histogram = new int[3][256]; // histogram = {histogramRed[256], histogramGreen[256], histogramBlue[256]}

        for (int i = 0; i < pixels.length; i++){
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            histogram[0][red]++;
            histogram[1][green]++;
            histogram[2][blue]++;
        }

        return histogram;
    }

    // TODO Check if correct
    protected int[] createHistogramGRAY (int[] pixels){ // Gray version

        int[] histogram = new int[256]; // histogram = {histogramRed[256], histogramGreen[256], histogramBlue[256]}
        int gray;

        for (int i = 0; i < pixels.length; i++){
            gray = Color.red(colorToGray(pixels[i]));

            histogram[gray]++;
        }

        return histogram;
    }

}
