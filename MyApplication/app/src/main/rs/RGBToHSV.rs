#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

float4 RS_KERNEL rgbToHsv(uchar4 in){

    float4 pixel = rsUnpackColor8888(in);

    float Cmax = max(pixel.r, max(pixel.g, pixel.b));
    float Cmin = min(pixel.r, min(pixel.g, pixel.b));
    float delta = Cmax - Cmin;

    float hsv_0 = 0.0;
    float hsv_1;
    float hsv_2;


    if (delta == 0){
        hsv_0 = 0;
    }
    else{
        if (Cmax == pixel.r){
            hsv_0 = fmod((pixel.g - pixel.b)/delta, 6.0);
        }
        else{
            if (Cmax == pixel.g){
                hsv_0 = (pixel.b - pixel.r)/delta + 2.0;
            }
            else{
                if (Cmax == pixel.b){
                    hsv_0 = (pixel.r - pixel.g)/delta + 4.0;
                }
            }
        }
    }

    hsv_0 = round(hsv_0 * 60);

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

    return (float4){0.0, 0.0, 0.0, in.a};
}