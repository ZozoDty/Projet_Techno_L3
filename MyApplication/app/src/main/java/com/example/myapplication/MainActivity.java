package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

public class MainActivity extends AppCompatActivity {

    int picture_id = R.drawable.multicolor;

    Bitmap p, p_tmp;

    Tools tools = new Tools();

    Functions function = new Functions(tools);
    Convolution convolution_function = new Convolution(tools);
    Contrast contrast_function = new Contrast(tools, function);
    Test test_function = new Test(function, tools);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View scroll_parameter = findViewById(R.id.scroll_parameter);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);

        reset_p();
        set_p_tmp();

        // Button to test new functions
        final Button button_test = findViewById(R.id.test_button);
        button_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = convolution_function.filter_Moyenneur(p, 9);
                setImage(p);
            }
        });

        final Button button_reset_color = findViewById(R.id.reset_c_button);
        button_reset_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset_p();
                set_p_tmp();
                function.setLeft_selected_color(0);
                function.setRight_selected_color(360);
                setImage(p);
            }
        });

        final Button button_negative = findViewById(R.id.negative_button);
        button_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = function.negative(p);
                setImage(p);
            }
        });

        final Button button_histogram_egalisation = findViewById(R.id.egalisation_histogram_button);
        button_histogram_egalisation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = contrast_function.histogramEqualizationGRAY(p);
                setImage(p);
            }
        });

        final Button button_random = findViewById(R.id.random_button);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = function.colorize(p);
                setImage(p);
            }
        });

        final Button button_linear_transformation = findViewById(R.id.linear_transformation_button);
        button_linear_transformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = contrast_function.linear_transformation(p);
                setImage(p);
            }
        });

        final Button button_gray = findViewById(R.id.gray_button);
        button_gray.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //p = function.toGray(p);
                toGrayRS(p);
                setImage(p);
            }
        });

        /* --- Selected Colors --- */

        final Button button_selected_color = findViewById(R.id.selected_color_button);
        button_selected_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_selected_color = findViewById(R.id.selected_color_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_selected_color.setVisibility(View.VISIBLE);
            }
        });

        final Button button_selected_color_return = findViewById(R.id.selected_color_return_button);
        button_selected_color_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 10, 0);
                setImage(p_tmp);
            }
        });

        final Button button_selected_color_less_left = findViewById(R.id.selected_color_less_left_button);
        button_selected_color_less_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, -10, 0);
                setImage(p_tmp);
            }
        });

        final Button button_selected_color_more_right = findViewById(R.id.selected_color_more_right_button);
        button_selected_color_more_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 0, 10);
                setImage(p_tmp);

            }
        });

        final Button button_selected_color_less_right = findViewById(R.id.selected_color_less_right_button);
        button_selected_color_less_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 0, -10);
                setImage(p_tmp);
            }
        });

        /* --- Saturation --- */

        final Button button_saturation = findViewById(R.id.saturation_button);
        button_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_saturation = findViewById(R.id.saturation_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_saturation.setVisibility(View.VISIBLE);
            }
        });

        final Button button_saturation_return = findViewById(R.id.saturation_return_button);
        button_saturation_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            public void onClick(View v) {
                p_tmp = function.change_saturation(p_tmp, 0.1);
                setImage(p_tmp);
            }
        });

        final Button button_less_saturation = findViewById(R.id.saturation_less_button);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_saturation(p_tmp, -0.1);
                setImage(p_tmp);
            }
        });

        /* --- Brightness --- */

        final Button button_brightness = findViewById(R.id.brightness_button);
        button_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_brightness = findViewById(R.id.brightness_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                layout_brightness.setVisibility(View.VISIBLE);
            }
        });

        final Button button_brightness_return = findViewById(R.id.brightness_return_button);
        button_brightness_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            public void onClick(View v) {
                p_tmp = function.change_brightness(p_tmp, 0.1);
                setImage(p_tmp);
            }
        });

        final Button button_less_brightness = findViewById(R.id.brightness_less_button);
        button_less_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_brightness(p_tmp, -0.1);
                setImage(p_tmp);
            }
        });
    }

    protected void reset_p() {
        p = BitmapFactory.decodeResource(getResources(), picture_id);
        p = p.copy(p.getConfig(), true);
    }

    protected void set_p_tmp() {
        p_tmp = p;
        p_tmp = p_tmp.copy(p_tmp.getConfig(), true);
    }

    protected void setImage(Bitmap bmp) {
        ImageView pic_image = findViewById(R.id.picture);
        pic_image.setImageBitmap(bmp);
    }

    public void toGrayRS(Bitmap bmp){

        //1. Creer un contexte RenderScript
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        //2. Creer des Allocations pour passer les donnees
        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        //3. Creer le script
        ScriptC_Gray GrayScript = new ScriptC_Gray(rs);

        //4. Copier les donnee dans les Allocations.

        //5. Initialiser les variables globales potentielles

        //6. Lancer le noyau
        GrayScript.forEach_toGray(input, output);

        //7. Recuperre les donnees dans les Allocations(s)
        output.copyTo(bmp);

        //8. Detruire le context, les Allocations et le script
        input.destroy();
        output.destroy();
        GrayScript.destroy();
        rs.destroy();
    }
}