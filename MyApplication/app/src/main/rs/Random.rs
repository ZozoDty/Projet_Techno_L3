#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

float rand;

static float4 rgbToHsv(uchar4 in){

    float4 pixel = rsUnpackColor8888(in);

    float Cmax = fmax(pixel.r, fmax(pixel.g, pixel.b));
    float Cmin = fmin(pixel.r, fmin(pixel.g, pixel.b));
    float delta = Cmax - Cmin;

    float hsv_0 = 0.0;
    float hsv_1;
    float hsv_2;


    if (delta == 0){
        hsv_0 = 0;
    }
    else if (Cmax == pixel.r){
            hsv_0 = 60 * fmod((pixel.g - pixel.b)/delta, 6.0);
    }else if (Cmax == pixel.g){
            hsv_0 = 60 * ((pixel.b - pixel.r)/delta + 2.0);
    }else if (Cmax == pixel.b){
            hsv_0 = 60 * ((pixel.r - pixel.g)/delta + 4.0);
    }

    if (hsv_0 < 0){
        hsv_0 += 360;
    }

    if (Cmax == 0.0){
        hsv_1 = 0.0;
    }
    else{
        hsv_1 = delta / Cmax;
    }

    hsv_2 = Cmax;

    return (float4){0.0, 0.0, 0.0, 0.0};
}


static uchar4 hsvToRgb(float4 in){

    float h = in.s0;
    float s = in.s1;
    float v = in.s2;

    float c = v * s;
    float x = c * (1 - fabs( fmod((h/60.0f), 2) - 1));

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
        }else if( h < 180){
            r = 0;
            g = c;
            b = x;
        }else if (h < 240){
            r = 0;
            g = c;
            b = x;
        }else if (h < 300){
            r = x;
            g = 0;
            b = c;
        }else if (h < 360){
            r = c;
            g = 0;
            b = x;
        }
    }

    float m = v - c;

    uchar red = (r + m) * 255;
    uchar green = (g + m) * 255;
    uchar blue = (b + m) * 255;

    return rsPackColorTo8888(red, green, blue, in.a);
}

uchar4 RS_KERNEL random(uchar4 in){

    float4 pixelHSV = rgbToHsv(in);
    pixelHSV.x = rand;
    uchar4 pixelRGB = hsvToRgb(pixelHSV);

    return pixelRGB;
}