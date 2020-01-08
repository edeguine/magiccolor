//
//  myCImage.cpp
//  coloreyes
//
//  Created by Etienne Deguine on 2/16/16.
//  Copyright © 2016 Etienne Deguine. All rights reserved.
//

#include "myCImage.h"

void myCImage::loadImage(string filename, myImage* dest) {
	/*
	png::image<png::rgba_pixel> image(filename);

	myCImage *ci;
	ci = (myCImage *) dest;
	ci->w = image.get_width();
	ci->h = image.get_height();
	ci->src = (unsigned char *) malloc(sizeof(unsigned char) * ci->w * ci->h * 4);
	ci->clear();

	myColorRGB col;
	for(int i = 0; i < dest->w; i++) {
		for(int j = 0; j < dest->h; j++) {
			col =myColorRGB(image[j][i].red, image[j][i].green, image[j][i].blue, image[j][i].alpha);
			dest->setCol(myPoint(i, j), col); 
		}
	}
	*/
}

myColorRGB myCImage::getCol(myPoint point, bool wrap) {
		// myCImage is ARGB
	if(wrap) {
		point.x = ((int) point.x) % w;
		point.y = ((int) point.y) % h;
	}

      
   if(inImage(point)) {
			int offset = 4 * (w * (int) point.y + (int) point.x);
			unsigned char a = src[offset];
			unsigned char r = src[offset + 1];
			unsigned char g = src[offset + 2];
			unsigned char b = src[offset + 3];

			return myColorRGB(r, g, b, a);
	} else {
          return myblack;
  }
}

void myCImage::setCol(myPoint point, myColorRGB col, bool wrap) {
	if(wrap) {
		point.x = ((int) point.x) % w;
		point.y = ((int) point.y) % h;
	}

	if(inImage(point)) {
		int offset = 4 * (((int) point.y) * w + ((int) point.x));
		// myCImage is ARGB
		src[offset] = col.a;
		src[offset + 1] = col.r;
		src[offset + 2] = col.g;
		src[offset + 3] = col.b;
	}
}

