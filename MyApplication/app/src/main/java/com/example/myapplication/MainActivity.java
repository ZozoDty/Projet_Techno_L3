package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    Bitmap p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p = BitmapFactory.decodeResource(getResources(), R.drawable.multicolor);
        p = p.copy(p.getConfig(), true);

        final Button button_reset_color = findViewById(R.id.reset_c_button_id);
        button_reset_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = BitmapFactory.decodeResource(getResources(), R.drawable.multicolor);
                p = p.copy(p.getConfig(), true);

                ImageView imageView= findViewById(R.id.picture);
                imageView.setImageResource(R.drawable.multicolor);
            }
        });

        final Button button_test_HSV = findViewById(R.id.test_HSV_button_id);
        button_test_HSV.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                p = testRGBToHSV(p);
                setImage(p);
            }
        });

        final Button button_gray = findViewById(R.id.gray_button_id);
        button_gray.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                p = toGray(p);
                setImage(p);
            }
        });

        final Button button_random = findViewById(R.id.random_button_id);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = colorize(p);
                setImage(p);
            }
        });

        final Button button_one_color = findViewById(R.id.one_color_button_id);
        button_one_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = one_color(p);
                setImage(p);
            }
        });

        final Button button_more_saturation = findViewById(R.id.saturation_more_button_id);
        button_more_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = change_saturation(p,0.1);
                setImage(p);
            }
        });

        final Button button_less_saturation = findViewById(R.id.saturation_less_button_id);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = change_saturation(p,-0.1);
                setImage(p);
            }
        });
    }

    protected void setImage (Bitmap p){
        ImageView pic_image = findViewById(R.id.picture);
        pic_image.setImageBitmap(p);
    }

    // Tool functions

    protected float[] RGBToHSV(int red, int green, int blue){
        float[] hsv = new float[3];

        float r = red / 255.F;
        float g = green / 255.F;
        float b = blue / 255.F;

        float Cmax = Math.max(r, Math.max(g, b));
        float Cmin = Math.min(r, Math.min(g, b));
        float delta = Cmax - Cmin;

        // Hue calculation
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

        // Saturation calculation
        if (Cmax == 0){
            hsv[1] = 0;
        }
        else{
            hsv[1] = delta / Cmax;
        }

        // Value calculation
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

    protected int minGray(int[] pixels){
        int min = 255;
        for (int i = 0; i < pixels.length; i++){
            int color = colorToGray(pixels[i]);
            if (color < min){
                min = color;
            }
        }
        return min;
    }

    protected int maxGray(int[] pixels){
        int max = 0;
        for (int i = 0; i < pixels.length; i++){
            int color = colorToGray(pixels[i]);
            if (color > max){
                max = color;
            }
        }
        return max;
    }

    // Button functions

    protected Bitmap testRGBToHSV(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            float[] hsv = RGBToHSV(red, green, blue);
            colors[i] = HSVToRGB(hsv);
        }

        bmp.setPixels(colors,0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    protected Bitmap toGray(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];

        for(int i = 0; i < pixels.length; i++) {
            colors[i] = colorToGray(pixels[i]);
        }

        bmp.setPixels(colors,0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    protected Bitmap colorize(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth()*bmp.getHeight()];

        int red, green, blue;
        int new_color = (int) (Math.random() * 255);

        for(int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = RGBToHSV(red, green, blue);
            hsv[0] = new_color;

            colors[i] = HSVToRGB(hsv);
        }

        bmp.setPixels(colors,0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    protected Bitmap one_color(Bitmap bmp) {
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = RGBToHSV(red, green, blue);
            if (!isInside(hsv[0],100,140)){ //min : 0 ; max : 360
                colors[i] = colorToGray(pixels[i]);
            }
            else{
                colors[i] = HSVToRGB(hsv);
            }

        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    protected Bitmap change_saturation(Bitmap bmp, double saturation_change){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = RGBToHSV(red, green, blue);
            hsv[2] += saturation_change;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
            colors[i] = HSVToRGB(hsv);
        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    // TODO
    protected Bitmap moreContrast(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int min = minGray(pixels);
        int max = maxGray(pixels);
        int[] LUT = new int[256];

        for (int ng = 0; ng < 256; ng++){
            LUT[ng] = (255 * (ng - min) * (max - min));
        }

        for (int i = 0; i < colors.length; i++){
            colors[i] = LUT[colorToGray(pixels[i])];
        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

}