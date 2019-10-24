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

        final Button button_gray = findViewById(R.id.gray_button_id);
        button_gray.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                p = toGray(p);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });

        final Button button_random = findViewById(R.id.random_button_id);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = colorize(p);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });

        final Button button_one_color = findViewById(R.id.one_color_button_id);
        button_one_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = one_color(p);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });

        final Button button_more_saturation = findViewById(R.id.saturation_more_button_id);
        button_more_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = change_saturation(p,0.1);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });

        final Button button_less_saturation = findViewById(R.id.saturation_less_button_id);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = change_saturation(p,-0.1);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });

        final Button button_more_contrast = findViewById(R.id.contrast_more_button_id);
        button_more_contrast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = moreContrast(p);

                ImageView pic_image = findViewById(R.id.picture);
                pic_image.setImageBitmap(p);
            }
        });
    }


    // Tool functions

    protected float[] RGBToHSV(int red, int green, int blue){
        float[] hsv = new float[3];

        float red2 = red/255.F;
        float green2 = green/255.F;
        float blue2 = blue/255.F;

        float Cmax = Math.max(red2, Math.max(green2, blue2));
        float Cmin = Math.min(red2, Math.min(green2, blue2));
        float delta = Cmax - Cmin;

        // Hue calculation
        if (delta == 0){
            hsv[0] = 0;
        }
        else if (Cmax == red2){
            hsv[0] = 60 * (((green2 - blue2)/delta)%6);
        }
        else if (Cmax == green2){
            hsv[0] = 60 * ((blue2 - red2)/delta + 2);
        }
        else if (Cmax == blue2){
            hsv[0] = 60 * ((red2 - green2)/delta + 4);
        }
        else{
            System.out.println("Hue not in case\n");
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

    protected void testRGBToHSV(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        int[] colors = new int[bmp.getWidth() * bmp.getHeight()];

        int red, green, blue;
        boolean test = true;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = RGBToHSV(red, green, blue);
            colors[i] = Color.HSVToColor(hsv);

            // TODO Test if new color = old color
        }
    }

    protected int colorToGray(int pixel){
        double red = Color.red(pixel) * 0.3;
        double green = Color.green(pixel) * 0.59;
        double blue = Color.blue(pixel) * 0.11;
        int tmp = (int) (red + green + blue);

        return Color.rgb(tmp, tmp,  tmp);
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

            colors[i] = Color.HSVToColor(hsv);
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
                colors[i] = Color.HSVToColor(hsv);
            }

        }

        bmp.setPixels(colors, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    // TODO change the color when used
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
            colors[i] = Color.HSVToColor(hsv);
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