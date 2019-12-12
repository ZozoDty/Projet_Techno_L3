#pragma version(1)
#pragma rs java_package_name(com.example.myapplication)

uchar4 RS_KERNEL random(uchar4 in){

    const uchar rand = rsRand(256);
    return (uchar4){rand, rand, rand, in.a};
}