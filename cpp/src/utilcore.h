#ifndef UTILCORE_H
#define UTILCORE_H
//
//  util_core.h
//  coloreyes
//
//  Created by Etienne Deguine on 2/16/16.
//  Copyright © 2016 Etienne Deguine. All rights reserved.
//

#define LOZENGE 0
#define SIN 1
#define ARROW 2
#define TRIANGLE 3
#define TRIANGLE_WEIGHTED 4
#define AUTO 5
#define PLAIN 6
#define AMBER 7
#define LAVA 8
#define GRADIANCE 9

// Coloring styles

#define HUE 0

// Orientations

#define HORIZONTAL 0
#define VERTICAL 1
#define FREE 2

#include <random>

#include "json.h"
#include "myColorRGB.h"
#include "myPoint.h"
#include "myImage.h"

#define PI 3.14159265359 

using namespace std;
using namespace nlohmann;

class utilcore {
  public:

    // JSON functions
    template<typename T>
    static T loadParam(string jstring, string paramName, T defaultValue);
    static void importPalette(string jpalette, vector<myColorRGB> *colpalette);
	static string exportPalette(vector<myColorRGB> *palette);

    // pixels functions
	static void importARGB8888(int *pixels, int w, int h, myImage *mim);
	static void exportARGB8888(myImage *mim, int *pixels, int w, int h);

		static mt19937 getMTgenerator(int seed);
		static double randomDouble(double seed, double min, double max);
		static int randomInt(int min, int max);
		static int randomInt(int min, int max, int seed);
		static int randomInt(int min, int max, mt19937 gen);
		static float randomFloat(float min, float max);
		static float randomFloat(float min, float max, mt19937 generator);
		static float randomFloat(float min, float max, int seed);
		static myPoint randomVec(myPoint topleft, myPoint bottomright);
		static myPoint randomVec(myImage *image);
};

template<typename T>
T utilcore::loadParam(string jstring, string paramName, T defaultValue) {

            T res = defaultValue;
            auto params = json::parse(jstring);
            auto it = params.find(paramName);
            if(it != params.end()) {
                res = params[paramName];
            }

            return res;
        }

#endif
