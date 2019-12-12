package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {

    private Tools tools;

    public Convolution(Tools tools){
        this.tools = tools;
    }

    // TODO
    private Bitmap applyfilter(Bitmap bmp, double[] core){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];

        double line = Math.sqrt(core.length);
        int dif = (int)(line - 1) / 2;

        for (int y = dif; y < (bmp.getHeight() - dif); y++){
            for (int x = dif; x < (bmp.getWidth() - dif); x++){

                int id_color_to_set = y * bmp.getWidth() + x;
                double color = 0;

                for (int j = 0; j < line; j++){
                    for (int i = 0; i < line; i++){
                        int id_color = id_color_to_set + (j - dif) * bmp.getWidth() + (i - dif);
                        int id_core = (int) (j * line + i);

                        color += pixels[id_color] * core[id_core];
                    }
                }

                colors[id_color_to_set] = (int) color;
            }
        }

        System.out.println("---------- FINISH --------");
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public Bitmap filter_Moyenneur(Bitmap bmp, int size){
        double[] core = new double[size];
        for (int i = 0; i < core.length; i++){
            core[i] = 1.0/size;
        }
        return applyfilter(bmp, core);
    }

}
