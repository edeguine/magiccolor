#ifndef GRADIENT_H
#define GRADIENT_H

#include "myColorRGB.h"
#include "myImage.h"
#include "myPoint.h"

class Gradient {
	public:
	static void rainbow(myImage *mim, myPoint minXY, myPoint maxXY, myPoint direction);
	static void	linearGradient(myImage *mim, vector<myPoint> centers, vector<myColorRGB> palette);

    static void sampleGradient(myImage *mim, vector<myColorRGB> *palette);
    static myColorRGB Hue(float t, float cycle);
};

#endif
