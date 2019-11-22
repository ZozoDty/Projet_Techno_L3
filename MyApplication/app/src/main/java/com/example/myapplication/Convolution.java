package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Convolution {

    private Tools tools;

    public Convolution(Tools tools){
        this.tools = tools;
    }

    // TODO Change in blue
    private Bitmap applyfilter(Bitmap bmp, double[] core){
        Bitmap p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] picturePixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(picturePixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        double line = Math.sqrt(core.length);
        int dif = (int)(line - 1) / 2; // 1 = core 3*3

        for (int y = dif; y < p_modif.getHeight() - dif; y++){
            for (int x = dif; x < p_modif.getWidth() - dif; x++){

                int id = y * p_modif.getWidth() + x;
                int color = 0;

                for (int j = 0; j < line; j++){
                    for (int i = 0; i < line; i++){
                        int id_color = id + (j - dif) * bmp.getWidth() + (i - dif);
                        int id_core = (int) (j * line + i);
                        color += Color.red(picturePixels[id_color]) * core[id_core];
                    }
                }
                colors[id] = color;
            }
        }
        System.out.println("---------- FINISH --------");
        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    public Bitmap filtreMoyenneur(Bitmap bmp){
        double[] core = new double[9];
        for (int i = 0; i < core.length; i++){
            core[i] = 1.0/9;
        }
        return applyfilter(bmp, core);
    }

}
