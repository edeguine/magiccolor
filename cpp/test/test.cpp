#include <ctime>
#include <iomanip>
#include <iostream>
#include <png++/png.hpp>

#include "Kandinsky.h"

using namespace std;

void testKandinsky(myImage *mim) {
	string jpalette = "{\"palette\":[[255,0,0,255],[255,255,0,0], [255, 247, 297, 22]]}";
	//string jpalette = "{\"palette\":[[255,0,0,255]]}";
	vector<myColorRGB> palette;
	myPoint start(200, 200);
	myPoint end(400, 400);
	utilcore::importPalette(jpalette, &palette);

	Kandinsky::pipeline(mim, 0.5, &palette, start, end);
}

int main(int argc, char** argv) {
	cout << "starting " << endl;
	myColorRGB col;

	myImage *mim;
	if(argc > 1) {
		cout << "Loading image " << argv[1] << endl;
		png::image<png::rgba_pixel> imload(argv[1]);
		mim = new myCImage(imload.get_width(), imload.get_height());

		int r,g,b,a;
		for(int i = 0; i < mim->w; i++) {
			for(int j = 0; j < mim->h; j++) {
				r = imload[j][i].red;
				g = imload[j][i].green;
				b = imload[j][i].blue;
				a = imload[j][i].alpha;
				mim->setCol(myPoint(i, j), myColorRGB(r, g, b, a));
			}
		}
	} else {
		mim = new myCImage(800, 800);
	}

    testKandinsky(mim);

	string outputfilename = "test.png";
	cout << "Writing " << outputfilename << endl;

	myColorRGB mycol;
	png::image<png::rgba_pixel> out(mim->w, mim->h);
	for(int i = 0; i < out.get_width(); i++) {
		for(int j = 0; j < out.get_height(); j++) {
			mycol = mim->getCol(myPoint(i, j));
			out[j][i].red = mycol.red();
			out[j][i].green = mycol.green();
			out[j][i].blue = mycol.blue();
			out[j][i].alpha = mycol.alpha();
		}
	}
	out.write(outputfilename);
}
