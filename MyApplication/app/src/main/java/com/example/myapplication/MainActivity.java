package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    Bitmap p, p_tmp, p_modif;
    int left_selected_color = 0;
    int right_selected_color = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View scroll_parameter = findViewById(R.id.scroll_parameter);

        reset_p();
        set_p_tmp();

        final Button button_reset_color = findViewById(R.id.reset_c_button);
        button_reset_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                reset_p();
                setImage(p);
            }
        });

        final Button button_gray = findViewById(R.id.gray_button);
        button_gray.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                p = toGray(p);
                setImage(p);
            }
        });

        final Button button_random = findViewById(R.id.random_button);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = colorize(p);
                setImage(p);
            }
        });

        // Selected Colors

        final Button button_selected_color = findViewById(R.id.selected_color_button);
        button_selected_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_selected_color = findViewById(R.id.selected_color_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_selected_color.setVisibility(View.VISIBLE);
            }
        });

        final Button button_selected_color_return = findViewById(R.id.selected_color_return_button);
        button_selected_color_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_selected_color = findViewById(R.id.selected_color_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_selected_color.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
            }
        });

        final Button button_selected_color_more_left = findViewById(R.id.selected_color_more_left_button);
        button_selected_color_more_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = selected_color(p, 10, 0);
                setImage(p_tmp);
            }
        });

        final Button button_selected_color_less_left = findViewById(R.id.selected_color_less_left_button);
        button_selected_color_less_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = selected_color(p, -10, 0);
                setImage(p_tmp);
            }
        });

        final Button button_selected_color_more_right = findViewById(R.id.selected_color_more_right_button);
        button_selected_color_more_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = selected_color(p, 0, 10);
                setImage(p_tmp);

            }
        });

        final Button button_selected_color_less_right = findViewById(R.id.selected_color_less_right_button);
        button_selected_color_less_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = selected_color(p, 0, -10);
                setImage(p_tmp);
            }
        });

        // Saturation

        final Button button_saturation = findViewById(R.id.saturation_button);
        button_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_saturation = findViewById(R.id.saturation_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_saturation.setVisibility(View.VISIBLE);
            }
        });

        final Button button_saturation_return = findViewById(R.id.saturation_return_button);
        button_saturation_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_saturation = findViewById(R.id.saturation_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_saturation.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
            }
        });

        final Button button_more_saturation = findViewById(R.id.saturation_more_button);
        button_more_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = change_saturation(p,0.1);
                setImage(p);
            }
        });

        final Button button_less_saturation = findViewById(R.id.saturation_less_button);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = change_saturation(p,-0.1);
                setImage(p_tmp);
            }
        });
    }

    protected void reset_p(){
        p = BitmapFactory.decodeResource(getResources(), R.drawable.multicolor);
        p = p.copy(p.getConfig(), true);
    }

    protected void set_p_tmp(){
        p_tmp = p;
        p_tmp = p_tmp.copy(p_tmp.getConfig(), true);
    }

    protected void setImage (Bitmap bmp){
        ImageView pic_image = findViewById(R.id.picture);
        pic_image.setImageBitmap(bmp);
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

    // Button functions

    protected Bitmap testRGBToHSV(Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        int[] pixels = new int[p_modif.getWidth() * p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth() * p_modif.getHeight()];

        int red, green, blue;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            float[] hsv = RGBToHSV(red, green, blue);
            colors[i] = HSVToRGB(hsv);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap toGray(Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        for(int i = 0; i < pixels.length; i++) {
            colors[i] = colorToGray(pixels[i]);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap colorize(Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);
        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

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

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    // TODO Manage colors around 0/360
    protected Bitmap selected_color(Bitmap bmp, int change_left, int change_right) {
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth() * p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth() * p_modif.getHeight()];

        int red, green, blue;
        left_selected_color = left_selected_color + change_left;
        if (left_selected_color < -360)
            left_selected_color = -360;
        if (left_selected_color > 360)
            left_selected_color = 360;
        right_selected_color = right_selected_color + change_right;
        if (right_selected_color < -360)
            right_selected_color = -360;
        if (right_selected_color > 360)
            right_selected_color = 360;

        for (int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);
            float[] hsv = RGBToHSV(red, green, blue);
            if (!isInside(hsv[0], left_selected_color, right_selected_color)){ //min : 0 ; max : 360
                colors[i] = colorToGray(pixels[i]);
            }
            else{
                colors[i] = HSVToRGB(hsv);
            }

        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
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
}