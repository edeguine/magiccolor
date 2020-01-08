#ifndef KLKMCOLOR_H 
#define KLKMCOLOR_H
#include <vector>

using namespace std;
class KlkMColor {
	
	public:

	KlkMColor(int rr, int gg, int bb);
	static KlkMColor KlkMColorfromAbsorbance(double Ar, double Ag, double Ab);

	static double calculateReflectance(double absorbtionRatio);
	static double calculateAbsorbance(double RGBChannelValue); // Value is 0 - 1.0
	static KlkMColor mix(KlkMColor cola, KlkMColor colb);
	static KlkMColor mix(KlkMColor cola, KlkMColor colb, float concentration);
	static KlkMColor mix(vector<KlkMColor> *colors, vector<float> *concentration);

	double r, g, b;
	double A_r, A_g, A_b;
};

#endif
