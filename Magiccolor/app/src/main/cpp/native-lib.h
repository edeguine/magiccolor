//
// Created by Etienne DEGUINE on 2019-07-26.
//

#ifndef MAGICCOLOR_NATIVE_LIB_H
#define MAGICCOLOR_NATIVE_LIB_H

#include <jni.h>

JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_FilterBWActivity_redPaint (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h);

JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_FilterBWActivity_separateBW (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jfloat threshold,
        jstring jparam);

JNIEXPORT void
JNICALL
Java_com_algofashion_magiccolor_PaletteLibrary_separateBW (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jfloat threshold,
        jstring jparam);

JNIEXPORT void
        JNICALL
Java_com_algofashion_magiccolor_PaletteLibrary_renderGradient (
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint w,
        jint h,
        jstring jparam);

#endif //MAGICCOLOR_NATIVE_LIB_H
