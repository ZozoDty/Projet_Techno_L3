package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptC;

public class MainActivity extends AppCompatActivity {

    int multicolor_id = R.drawable.multicolor;
    int cat_id = R.drawable.cat;
    int lenna_id = R.drawable.lenna;

    int picture_id = lenna_id;


    Bitmap p, p_tmp;

    Tools tools = new Tools();

    Spinner spinner_picture;
    View scroll_parameter, scroll_parameter_rs;
    View convolution_layout, convolution_average_layout, convolution_prewitt_layout, convolution_sobel_layout;

    Functions function = new Functions(tools);
    Convolution convolution_function = new Convolution(tools, function);
    Contrast contrast_function = new Contrast(tools, function);
    Test test_function = new Test(function, tools);

    boolean isInConvolution = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_picture = findViewById(R.id.spinner_change_picture);
        scroll_parameter = findViewById(R.id.scroll_parameter);
        scroll_parameter_rs = findViewById(R.id.scroll_parameter_RS);
        convolution_layout = findViewById(R.id.convolution_layout);
        convolution_average_layout = findViewById(R.id.convolution_average_layout);
        convolution_prewitt_layout = findViewById(R.id.convolution_prewitt_layout);
        convolution_sobel_layout = findViewById(R.id.convolution_sobel_layout);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                                        View.SYSTEM_UI_FLAG_IMMERSIVE);

        set_p(picture_id);
        set_p_tmp();
        setImage(p);

        /* ---------- Top Buttons ---------- */

        // Change picture
        setPictureButton();

        // Reset color
        setResetColorButton();

        /* ---------- Functions ---------- */

        // Button to test new functions
        setTestButton();

        //Convolution
        setConvolutionButton();

        // Histogram egalisation
        setHistogramEgalisationButton();

        // Random color
        setRandomColorButton();

        // Linear transformation
        setLinearTransformationButton();

        // Negative
        setNegativeButtton();

        // Gray
        setGrayButton();

        // Selected colors
        setSelectedColorButtons();

        // Saturation
        setSaturationButtons();

        // Brightness
        setBrightnessButtons();

        /* ---------- RS functions ---------- */

        // Gray RS
        setGrayRSButton();

        // Negative RS
        setNegativeRSButton();

        // Random RS
        setRandomButtonRSButton();
    }

    protected void set_p(int picture_id) {
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

    /* Set Buttons */

    private void setPictureButton(){
        final Button button_set_picture = findViewById(R.id.change_picture_button);
        button_set_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch ((String) spinner_picture.getSelectedItem()){
                    case("Multicolor"):
                        picture_id = multicolor_id;
                        set_p(picture_id);
                        setImage(p);
                        break;
                    case("Cat"):
                        picture_id = cat_id;
                        set_p(picture_id);
                        setImage(p);
                        break;
                    case("Girl"):
                        picture_id = lenna_id;
                        set_p(picture_id);
                        setImage(p);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void setResetColorButton(){
        final Button button_reset_color = findViewById(R.id.reset_c_button);
        button_reset_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_p(picture_id);
                set_p_tmp();
                function.setLeft_selected_color(0);
                function.setRight_selected_color(360);
                if(isInConvolution){
                    p = function.toGray(p);
                }
                setImage(p);
            }
        });
    }

    private void setTestButton(){
        final Button button_test = findViewById(R.id.test_button);
        button_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = function.toGray(p);
                p = convolution_function.filter_Prewitt_horizontal(p);
                setImage(p);
            }
        });
    }

    private void setConvolutionButton(){
        final Button button_convolution = findViewById(R.id.convolution_button);
        button_convolution.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isInConvolution = true;
                p = function.toGray(p);
                setImage(p);

                scroll_parameter.setVisibility(View.GONE);
                scroll_parameter_rs.setVisibility(View.GONE);
                convolution_layout.setVisibility(View.VISIBLE);
            }
        });

        final Button button_convolution_return = findViewById(R.id.convolution_return_button);
        button_convolution_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isInConvolution = false;
                convolution_layout.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
                scroll_parameter_rs.setVisibility(View.VISIBLE);
            }
        });

        final Button button_gaussien = findViewById(R.id.gaussien_button);
        button_gaussien.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = convolution_function.filter_Gaussien(p);
                setImage(p);
            }
        });

        setConvolutionAverageButton();
        setConvolutionPrewittButton();
        setConvolutionSobelButton();
    }

    private void setConvolutionAverageButton(){
        final Button button_average = findViewById(R.id.average_button);
        button_average.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                convolution_layout.setVisibility(View.GONE);
                convolution_average_layout.setVisibility(View.VISIBLE);
            }
        });

        final Button button_average_return = findViewById(R.id.convolution_average_return_button);
        button_average_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                convolution_average_layout.setVisibility(View.GONE);
                convolution_layout.setVisibility(View.VISIBLE);

            }
        });

        final Button button_average_3 = findViewById(R.id.average_3_button);
        button_average_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Moyenneur(p, 9);
                setImage(p_tmp);
            }
        });

        final Button button_average_7 = findViewById(R.id.average_7_button);
        button_average_7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Moyenneur(p, 49);
                setImage(p_tmp);
            }
        });

        final Button button_average_15 = findViewById(R.id.average_15_button);
        button_average_15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Moyenneur(p, 225);
                setImage(p_tmp);
            }
        });
    }

    private void setConvolutionPrewittButton(){
        final Button button_prewitt = findViewById(R.id.prewitt_button);
        button_prewitt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                convolution_layout.setVisibility(View.GONE);
                convolution_prewitt_layout.setVisibility(View.VISIBLE);
            }
        });

        final Button button_prewitt_return = findViewById(R.id.convolution_prewitt_return_button);
        button_prewitt_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                convolution_prewitt_layout.setVisibility(View.GONE);
                convolution_layout.setVisibility(View.VISIBLE);

            }
        });

        final Button button_prewitt_horizontal = findViewById(R.id.prewitt_horizontal_button);
        button_prewitt_horizontal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Prewitt_horizontal(p_tmp);
                setImage(p_tmp);
            }
        });

        final Button button_prewitt_vertical = findViewById(R.id.prewitt_vertical_button);
        button_prewitt_vertical.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Prewitt_vertical(p_tmp);
                setImage(p_tmp);
            }
        });

        final Button button_prewitt_all = findViewById(R.id.prewitt_all_button);
        button_prewitt_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Prewitt(p_tmp);
                setImage(p_tmp);
            }
        });
    }

    private void setConvolutionSobelButton(){
        final Button button_sobel = findViewById(R.id.sobel_button);
        button_sobel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                convolution_layout.setVisibility(View.GONE);
                convolution_sobel_layout.setVisibility(View.VISIBLE);
            }
        });

        final Button button_sobel_return = findViewById(R.id.convolution_sobel_return_button);
        button_sobel_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                convolution_sobel_layout.setVisibility(View.GONE);
                convolution_layout.setVisibility(View.VISIBLE);

            }
        });

        final Button button_sobel_horizontal = findViewById(R.id.sobel_horizontal_button);
        button_sobel_horizontal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Sobel_horizontal(p_tmp);
                setImage(p_tmp);
            }
        });

        final Button button_sobel_vertical = findViewById(R.id.sobel_vertical_button);
        button_sobel_vertical.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Sobel_vertical(p_tmp);
                setImage(p_tmp);
            }
        });

        final Button button_sobel_all = findViewById(R.id.sobel_all_button);
        button_sobel_all.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = convolution_function.filter_Sobel(p_tmp);
                setImage(p_tmp);
            }
        });
    }



    private void setHistogramEgalisationButton(){
        final Button button_histogram_egalisation = findViewById(R.id.egalisation_histogram_button);
        button_histogram_egalisation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = contrast_function.histogramEqualizationRGB(p);
                setImage(p);
            }
        });
    }

    private void setRandomColorButton(){
        final Button button_random = findViewById(R.id.random_button);
        button_random.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = function.colorize(p);
                setImage(p);
            }
        });
    }

    private void setLinearTransformationButton(){
        final Button button_linear_transformation = findViewById(R.id.linear_transformation_button);
        button_linear_transformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = contrast_function.linear_transformation(p);
                setImage(p);
            }
        });
    }

    private void setNegativeButtton(){
        final Button button_negative = findViewById(R.id.negative_button);
        button_negative.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p = function.negative(p);
                setImage(p);
            }
        });
    }

    private void setGrayButton(){
        final Button button_gray = findViewById(R.id.gray_button);
        button_gray.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                p = function.toGray(p);
                setImage(p);
            }
        });
    }

    private void setSelectedColorButtons(){
        // Functions
        final Button button_selected_color = findViewById(R.id.selected_color_button);
        button_selected_color.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_selected_color = findViewById(R.id.selected_color_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                scroll_parameter_rs.setVisibility(View.GONE);
                layout_selected_color.setVisibility(View.VISIBLE);
            }
        });

        // Return
        final Button button_selected_color_return = findViewById(R.id.selected_color_return_button);
        button_selected_color_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_selected_color = findViewById(R.id.selected_color_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_selected_color.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
                scroll_parameter_rs.setVisibility(View.VISIBLE);
            }
        });

        // More left
        final Button button_selected_color_more_left = findViewById(R.id.selected_color_more_left_button);
        button_selected_color_more_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 10, 0);
                setImage(p_tmp);
            }
        });

        // Less left
        final Button button_selected_color_less_left = findViewById(R.id.selected_color_less_left_button);
        button_selected_color_less_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, -10, 0);
                setImage(p_tmp);
            }
        });

        // More right
        final Button button_selected_color_more_right = findViewById(R.id.selected_color_more_right_button);
        button_selected_color_more_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 0, 10);
                setImage(p_tmp);

            }
        });

        // Less right
        final Button button_selected_color_less_right = findViewById(R.id.selected_color_less_right_button);
        button_selected_color_less_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.selected_color(p, 0, -10);
                setImage(p_tmp);
            }
        });
    }

    private void setSaturationButtons(){
        // Function
        final Button button_saturation = findViewById(R.id.saturation_button);
        button_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_saturation = findViewById(R.id.saturation_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                scroll_parameter_rs.setVisibility(View.GONE);
                layout_saturation.setVisibility(View.VISIBLE);
            }
        });

        // Return
        final Button button_saturation_return = findViewById(R.id.saturation_return_button);
        button_saturation_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_saturation = findViewById(R.id.saturation_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_saturation.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
                scroll_parameter_rs.setVisibility(View.VISIBLE);
            }
        });

        // More
        final Button button_more_saturation = findViewById(R.id.saturation_more_button);
        button_more_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_saturation(p_tmp, 0.1);
                setImage(p_tmp);
            }
        });

        // Less
        final Button button_less_saturation = findViewById(R.id.saturation_less_button);
        button_less_saturation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_saturation(p_tmp, -0.1);
                setImage(p_tmp);
            }
        });
    }

    private void setBrightnessButtons(){
        // Function
        final Button button_brightness = findViewById(R.id.brightness_button);
        button_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_brightness = findViewById(R.id.brightness_layout);

                p_tmp = p;
                p_tmp = p_tmp.copy(p_tmp.getConfig(), true);

                scroll_parameter.setVisibility(View.GONE);
                scroll_parameter_rs.setVisibility(View.GONE);
                layout_brightness.setVisibility(View.VISIBLE);
            }
        });

        // Return
        final Button button_brightness_return = findViewById(R.id.brightness_return_button);
        button_brightness_return.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View layout_brightness = findViewById(R.id.brightness_layout);

                p = p_tmp;
                p = p.copy(p.getConfig(), true);
                setImage(p);

                layout_brightness.setVisibility(View.GONE);
                scroll_parameter.setVisibility(View.VISIBLE);
                scroll_parameter_rs.setVisibility(View.VISIBLE);
            }
        });

        // More
        final Button button_more_brightness = findViewById(R.id.brightness_more_button);
        button_more_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_brightness(p_tmp, 0.1);
                setImage(p_tmp);
            }
        });

        // Less
        final Button button_less_brightness = findViewById(R.id.brightness_less_button);
        button_less_brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                p_tmp = function.change_brightness(p_tmp, -0.1);
                setImage(p_tmp);
            }
        });
    }


    /* Create button RS */

    private void setGrayRSButton(){
        final Button button_gray_rs = findViewById(R.id.gray_button_rs);
        button_gray_rs.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                toGrayRS(p);
                setImage(p);
            }
        });
    }

    private void setNegativeRSButton(){
        final Button button_negative_rs = findViewById(R.id.negative_button_rs);
        button_negative_rs.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                negativeRS(p);
                setImage(p);
            }
        });
    }

    public void setRandomButtonRSButton(){
        final Button button_random_rs = findViewById(R.id.random_button_rs);
        button_random_rs.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                randomRS(p);
                setImage(p);
            }
        });
    }

    /* Create functions RS */

    public void toGrayRS(Bitmap bmp){
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Gray GrayScript = new ScriptC_Gray(rs);

        GrayScript.forEach_toGray(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        GrayScript.destroy();
        rs.destroy();
    }

    public void negativeRS(Bitmap bmp){
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Negative NegativeScript = new ScriptC_Negative(rs);

        NegativeScript.forEach_negative(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        NegativeScript.destroy();
        rs.destroy();
    }

    public void randomRS(Bitmap bmp){
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_Random RandomScript = new ScriptC_Random(rs);

        RandomScript.forEach_random(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        RandomScript.destroy();
        rs.destroy();
    }

    public void RGBToHSVRS(Bitmap bmp){
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_RGBToHSV RGBToHSVScript = new ScriptC_RGBToHSV(rs);

        RGBToHSVScript.forEach_rgbToHsv(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        RGBToHSVScript.destroy();
        rs.destroy();
    }

    public void HSVToRGBRS(Bitmap bmp){
        androidx.renderscript.RenderScript rs = RenderScript.create(this);

        androidx.renderscript.Allocation input = androidx.renderscript.Allocation.createFromBitmap(rs, bmp);
        androidx.renderscript.Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_HSVToRGB HSVToRGBScript = new ScriptC_HSVToRGB(rs);

        HSVToRGBScript.forEach_hsvToRgb(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        HSVToRGBScript.destroy();
        rs.destroy();
    }
}