#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

uchar4 RS_KERNEL negative(uchar4 in){

    const uchar red = 255 - in.r;
    const uchar green = 255 - in.g;
    const uchar blue = 255 - in.b;

    return (uchar4){red, green, blue, in.a};
}