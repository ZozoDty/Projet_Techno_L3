#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

uchar4 RS_KERNEL hsvToRgb(float4 in){

    float h = in.s0;
    float s = in.s1;
    float v = in.s2;

    float c = v * s;
    float x = c * (1 - fabs( fmod((h/60), 2) - 1));

    float r = 0;
    float g = 0;
    float b = 0;

    if (h >= 0){
        if (h < 60){
            r = c;
            g = x;
            b = 0;
        }
        else{
            if (h < 120){
                r = x;
                g = c;
                b = 0;
            }
            else{
                if( h < 180){
                    r = 0;
                    g = c;
                    b = x;
                }
                else{
                    if (h < 240){
                        r = 0;
                        g = c;
                        b = x;
                    }
                    else{
                        if (h < 300){
                            r = x;
                            g = 0;
                            b = c;
                        }
                        else{
                            if (h < 360){
                                r = c;
                                g = 0;
                                b = x;
                            }
                        }
                    }
                }
            }
        }
    }

    float m = v - c;

    uchar red = (r + m) * 255;
    uchar green = (g + m) * 255;
    uchar blue = (b + m) * 255;

    return rsPackColorTo8888(red, green, blue);
}