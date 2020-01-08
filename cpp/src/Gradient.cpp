#include "Gradient.h"

void Gradient::linearGradient(myImage *mim, vector<myPoint> centers, vector<myColorRGB> palette) {

	myPoint p;
	myColorRGB col;
	float concentration;
	int nc = palette.size();
	int np = centers.size();
	int first, second;
	float t = 0;
	myPoint start, end;
	start = centers[0];
	end = centers[centers.size() - 1];
	float d = end.dist(start);

	for(int i = 0; i < mim->w; i++) {
		for(int j = 0; j < mim->h; j++) {
			p.x = i;
			p.y = j;

			t = (p - start).dot(end - start) / (d * d);

			first = ((int) (t * np)) % nc;
			second = (first + 1) % nc;
			concentration =	1.0f - (t * np - ((int) (t * np)));

			col = myColorRGB::ciel_mix(palette.at(first), palette.at(second), concentration);
			mim->setCol(p, col);
		}
	}
}

myColorRGB Gradient::Hue(float t, float cycle) {
    hsv hcol;
    hcol.h = fmodf(fabs(t), cycle) * 360.0f / cycle;
    hcol.s = 1.0f;
    hcol.v = 200.0f;
    
    return myColorRGB::hsv2col(hcol);
}


void Gradient::rainbow(myImage *mim, myPoint minXY, myPoint maxXY, myPoint direction) {

	float period = direction.L2();

	float t;
	myPoint current;
	myColorRGB col;
	hsv h;

	for(int i = 0; i < mim->w; i++) {
		for(int j = 0; j < mim->h; j++) {
			current = myPoint(i, j);	
			t = (current - minXY).dot(direction) / period;
			
			col = Gradient::Hue(t, period);

			mim->setCol(current, col);
		}
	}
}

void Gradient::sampleGradient(myImage *mim, vector<myColorRGB> *palette) {

	myPoint p;
	myColorRGB col;
	float concentration;
	int n = palette->size();
	int first, second;

	for(int i = 0; i < mim->w; i++) {
		for(int j = 0; j < mim->h; j++) {
			p.x = i;
			p.y = j;
			first = (i / (mim->w / n)) % n;
			second = (first + 1) % n;
			concentration =	1.0 - ((i * 1.0f / (mim->w / n)) - first);

			col = myColorRGB::ciel_mix(palette->at(first), palette->at(second), concentration);
			mim->setCol(p, col);
		}
	}
}
