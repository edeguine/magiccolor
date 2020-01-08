#include "KlkMColor.h"
#include <cmath>
#include <iostream>

using namespace std;

double KlkMColor::calculateReflectance(double absorbtionRatio) {

		return 1.0 + absorbtionRatio - sqrt(absorbtionRatio * absorbtionRatio + (2.0 * absorbtionRatio));
}

double KlkMColor::calculateAbsorbance(double RGBChannelValue) {

		return (1.0 - RGBChannelValue) * (1.0 - RGBChannelValue) / (2.0 * RGBChannelValue);
}

KlkMColor::KlkMColor(int rr, int gg, int bb) {

 		r = rr == 0 ? 0.001 : rr / 255.0f;
 		g = gg == 0 ? 0.001 : gg / 255.0f;
 		b = bb == 0 ? 0.001 : bb / 255.0f;

		A_r = KlkMColor::calculateAbsorbance(r);
		A_g = KlkMColor::calculateAbsorbance(g);
		A_b = KlkMColor::calculateAbsorbance(b);
}

KlkMColor KlkMColor::KlkMColorfromAbsorbance(double Ar, double Ag, double Ab) {

		double tr = KlkMColor::calculateReflectance(Ar);	
		double tg = KlkMColor::calculateReflectance(Ag);	
		double tb = KlkMColor::calculateReflectance(Ab);	

		return KlkMColor(tr * 255, tg * 255, tb * 255);
};

KlkMColor KlkMColor::mix(KlkMColor cola, KlkMColor colb) {

	double concentration = 0.5f;
	double Amix_r = concentration * cola.A_r + (1.0f - concentration) * colb.A_r;
	double Amix_g = concentration * cola.A_g + (1.0f - concentration) * colb.A_g;
	double Amix_b = concentration * cola.A_b + (1.0f - concentration) * colb.A_b;

	return KlkMColor::KlkMColorfromAbsorbance(Amix_r, Amix_g, Amix_b);
}


KlkMColor KlkMColor::mix(vector<KlkMColor> *colors, vector<float> *concentration) {
	double Amix_r = 0;
	for(int i = 0; i < concentration->size(); i++) {
		Amix_r += concentration->at(i) * colors->at(i).A_r;
	}

	double Amix_g = 0;
	for(int i = 0; i < concentration->size(); i++) {
		Amix_g += concentration->at(i) * colors->at(i).A_g;
	}

	double Amix_b = 0;
	for(int i = 0; i < concentration->size(); i++) {
		Amix_b += concentration->at(i) * colors->at(i).A_b;
	}

	return KlkMColor::KlkMColorfromAbsorbance(Amix_r, Amix_g, Amix_b);
}

KlkMColor KlkMColor::mix(KlkMColor cola, KlkMColor colb, float concentration) {

	double Amix_r = concentration * cola.A_r + (1 - concentration) * colb.A_r;
	double Amix_g = concentration * cola.A_g + (1 - concentration) * colb.A_g;
	double Amix_b = concentration * cola.A_b + (1 - concentration) * colb.A_b;

	return KlkMColor::KlkMColorfromAbsorbance(Amix_r, Amix_g, Amix_b);
}
