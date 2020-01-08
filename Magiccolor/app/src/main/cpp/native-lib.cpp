#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_algofashion_magiccolor_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

#include "../../../../../cpp/src/Kandinsky.h"

extern "C"
int Color(int a, int r, int g, int b) {
    return (a << 24) | (r << 16) | (g << 8) | b;
}

int a(int color) {
    return (color & 0xFF000000) >> 24;
}

int r(int color) {
    return (color & 0x00FF0000) >> 16;
}

int g(int color) {
    return (color & 0x0000FF00) >> 8;
}

int b(int color) {
    return (color & 0x000000FF);
}

void import_pixels(jint* jpixels, int *fpixels, int w, int h) {
            int col;
            int channels = 4;

            for(int i = 0; i < w; i++) {
                for(int j = 0; j < h; j++) {
                    col = jpixels[j * w + i];
                    fpixels[j * w * channels + i * channels] = a(col);
                    fpixels[j * w * channels + i * channels + 1] = r(col);
                    fpixels[j * w * channels + i * channels + 2] = g(col);
                    fpixels[j * w * channels + i * channels + 3] = b(col);
                }
            }
}

void export_pixels(int *fpixels, jint* cpixels, int w, int h) {
            int ca, cr, cg, cb;
            int channels = 4;

            for(int i = 0; i < w; i++) {
                for(int j = 0; j < h; j++) {

                    ca = fpixels[j * w * channels + i * channels];
                    cr = fpixels[j * w * channels + i * channels + 1];
                    cg = fpixels[j * w * channels + i * channels + 2];
                    cb = fpixels[j * w * channels + i * channels + 3];

                    cpixels[j * w + i] = Color(ca, cr, cg, cb);
                }
            }
}

extern "C"
JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_FilterBWActivity_separateBW (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jfloat threshold,
        jstring jparam) {

            int channels = 4;
            int *fpixels;
            jint *jpixels = env->GetIntArrayElements(pixels, NULL);
            fpixels = new int[w * h * channels];
            jint cpixels[w * h];

            const char *jcparam = env->GetStringUTFChars(jparam, NULL);
            std::string jsparam = string(jcparam);

            import_pixels(jpixels, fpixels, w, h);
            Kandinsky::androidSeparateBW(fpixels, w, h, threshold, jsparam);
            export_pixels(fpixels, cpixels, w, h);

            env->SetIntArrayRegion(pixels, 0, w * h, cpixels);
}

extern "C"
JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_PaletteLibrary_separateBW (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jfloat threshold,
        jstring jparam) {

    int channels = 4;
    int *fpixels;
    jint *jpixels = env->GetIntArrayElements(pixels, NULL);
    fpixels = new int[w * h * channels];
    jint cpixels[w * h];

    const char *jcparam = env->GetStringUTFChars(jparam, NULL);
    std::string jsparam = string(jcparam);

    import_pixels(jpixels, fpixels, w, h);
    Kandinsky::androidSeparateBW(fpixels, w, h, threshold, jsparam);
    export_pixels(fpixels, cpixels, w, h);

    env->SetIntArrayRegion(pixels, 0, w * h, cpixels);
}


extern "C"
JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_PaletteLibrary_renderGradient (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jstring jparam) {

    int channels = 4;
    int *fpixels;
    jint *jpixels = env->GetIntArrayElements(pixels, NULL);
    fpixels = new int[w * h * channels];
    jint cpixels[w * h];

    const char *jcparam = env->GetStringUTFChars(jparam, NULL);
    std::string jsparam = string(jcparam);

    import_pixels(jpixels, fpixels, w, h);
    Kandinsky::androidPalette(fpixels, w, h, jsparam);
    export_pixels(fpixels, cpixels, w, h);

    env->SetIntArrayRegion(pixels, 0, w * h, cpixels);
}