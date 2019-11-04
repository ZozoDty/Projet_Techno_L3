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

    Bitmap p, p_tmp, p_modif;
    int left_selected_color = 0;
    int right_selected_color = 360;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View scroll_parameter = findViewById(R.id.scroll_parameter);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE);

        reset_p();
        set_p_tmp();

        final Button button_reset_color = findViewById(R.id.reset_c_button);
        button_reset_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                reset_p();
                set_p_tmp();
                left_selected_color = 0;
                right_selected_color = 360;
                setImage(p);
            }
        });

        final Button button_negative = findViewById(R.id.negative_button);
        button_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = negative(p);
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

        final Button button_linear_transformation = findViewById(R.id.linear_transformation_button);
        button_linear_transformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p = linear_transformation(p);
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
                p_tmp = change_saturation(p_tmp,0.1);
                setImage(p_tmp);
            }
        });

        final Button button_less_saturation = findViewById(R.id.saturation_less_button);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = change_saturation(p_tmp,-0.1);
                setImage(p_tmp);
            }
        });

        // Brightness

        final Button button_brightness = findViewById(R.id.brightness_button);
        button_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_brightness = findViewById(R.id.brightness_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_brightness.setVisibility(View.VISIBLE);
            }
        });

        final Button button_brightness_return = findViewById(R.id.brightness_return_button);
        button_brightness_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                View layout_brightness = findViewById(R.id.brightness_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_brightness.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
            }
        });

        final Button button_more_brightness = findViewById(R.id.brightness_more_button);
        button_more_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = change_brightness(p_tmp,0.1);
                setImage(p_tmp);
            }
        });

        final Button button_less_brightness = findViewById(R.id.brightness_less_button);
        button_less_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                p_tmp = change_brightness(p_tmp,-0.1);
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
            LUTred[ng] = (255 * (ng - min_red) / (max_red - min_red));
        }
        return LUTred;
    }

    protected int[] createLUTgreen (int[] max_min_array){
        int max_green = max_min_array[2];
        int min_green = max_min_array[3];
        int[] LUTgreen = new int[256];

        for (int ng = 0; ng < 256; ng++){
            LUTgreen[ng] = (255 * (ng - min_green) / (max_green - min_green));
        }
        return LUTgreen;
    }

    protected int[] createLUTblue (int[] max_min_array){
        int max_blue = max_min_array[4];
        int min_blue = max_min_array[5];
        int[] LUTblue = new int[256];

        for (int ng = 0; ng < 256; ng++){
            LUTblue[ng] = 255 * (ng - min_blue) / (max_blue - min_blue);
        }
        return LUTblue;
    }

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

    /*protected int[][] createHistogram (int[] pixels){
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
    }*/

    /*
    protected int[] createHistogram (int[] pixels){     // Gray version

        int[] histogram = new int[256]; // histogram = {histogramRed[256], histogramGreen[256], histogramBlue[256]}

        for (int i = 0; i < pixels.length; i++){
            int gray = Color.red(colorToGray(pixels[i]));

            histogram[gray]++;
        }

        return histogram;
    }*/

    // Button functions

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
        if (left_selected_color < 0)
            left_selected_color = 0;
        if (left_selected_color > 360)
            left_selected_color = 360;
        right_selected_color = right_selected_color + change_right;
        if (right_selected_color < 0)
            right_selected_color = 0;
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
            hsv[1] += saturation_change;
            if (hsv[1] < 0){
                hsv[1] = 0;
            }
            if (hsv[1] > 1){
                hsv[1] = 1;
            }
            colors[i] = HSVToRGB(hsv);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap change_brightness (Bitmap bmp, double brightness_change){
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
            hsv[2] += brightness_change;
            if (hsv[2] < 0){
                hsv[2] = 0;
            }
            if (hsv[2] > 1){
                hsv[2] = 1;
            }
            colors[i] = HSVToRGB(hsv);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap negative(Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        for(int i = 0; i < pixels.length; i++) {
            red = Color.red(pixels[i]);
            green = Color.green(pixels[i]);
            blue = Color.blue(pixels[i]);

            red = 255 - red;
            green = 255 - green;
            blue = 255 - blue;

            colors[i] = Color.rgb(red, green, blue);
        }

        p_modif.setPixels(colors,0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }

    protected Bitmap linear_transformation(Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        int[] max_min_array = max_min_colors(pixels);
        int[] LUTred = createLUTred(max_min_array);
        int[] LUTgreen = createLUTgreen(max_min_array);
        int[] LUTblue = createLUTblue(max_min_array);

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

    /*protected Bitmap histogramEgalisation (Bitmap bmp){
        p_modif = bmp;
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];
        int red, green, blue;

        int[][] histogram = createHistogram(pixels);
        int[] histogramCred = new int [256];
        int[] histogramCgreen = new int [256];
        int[] histogramCblue = new int [256];

        for (int i = 0; i < 256; i++){
            for (int j = 0; j <= i; j++){
                histogramCred[i] += histogram[0][j];
                histogramCgreen[i] += histogram[1][j];
                histogramCblue[i] += histogram[2][j];
            }
        }

        int[] LUTred = new int [256];
        int[] LUTgreen = new int [256];
        int[] LUTblue = new int [256];

        for (int i = 0; i < 256; i++){
            LUTred[i] = (255 / (bmp.getWidth() * bmp.getHeight())) * histogramCred[i];
            LUTgreen[i] = (255 / (bmp.getWidth() * bmp.getHeight())) * histogramCgreen[i];
            LUTblue[i] = (255 / (bmp.getWidth() * bmp.getHeight())) * histogramCblue[i];
        }

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
    }*/

    /*
    protected Bitmap histogramEgalisation (Bitmap bmp){ // Gray version
        p_modif = toGray(bmp);
        p_modif = p_modif.copy(p_modif.getConfig(), true);

        int[] pixels = new int[p_modif.getWidth()*p_modif.getHeight()];
        p_modif.getPixels(pixels, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        int[] colors = new int[p_modif.getWidth()*p_modif.getHeight()];

        int[] histogram = createHistogram(pixels);
        int[] histogramC = new int [256];

        for (int i = 0; i < 256; i++){
            for (int j = 0; j <= i; j++){
                histogramC[i] += histogram[j];
            }
        }

        int[] LUT = new int [256];

        for (int i = 0; i < 256; i++){
            LUT[i] = (255 / (bmp.getWidth() * bmp.getHeight())) * histogramC[i];
        }

        for (int i = 0; i < pixels.length; i++){
            int color = Color.red(colorToGray(pixels[i]));
            colors[i] = Color.rgb(LUT[color], LUT[color], LUT[color]);
        }

        p_modif.setPixels(colors, 0, p_modif.getWidth(), 0, 0, p_modif.getWidth(), p_modif.getHeight());
        return p_modif;
    }*/
}