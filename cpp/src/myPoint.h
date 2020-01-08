#ifndef MYPOINT_H
#define MYPOINT_H

#define PI 3.14159265359 

#include <math.h>
#include <ostream>
#include <vector>

using namespace std;

class myPoint {

  public:

	  float x, y, z;

		myPoint() {x = 0; y = 0;};
		myPoint(float _x, float _y): x(_x), y(_y) {};

		myPoint scale(float t);


	static float crossProduct(myPoint a, myPoint b);
	static float cotangent(myPoint a, myPoint b, myPoint c);
	static void baryPaper(myPoint p, vector<myPoint> *edges, vector<float> *bary);
	static bool lexiSort(myPoint a, myPoint b) ;
	static float relativeCoords(myPoint middle, myPoint a, myPoint b);
	static void closestPointCoordinates(myPoint p, vector<myPoint> *refs, vector<float> *coordinates);
	static void baryPolygonCoordinates(myPoint p, vector<myPoint> *polygon, vector<float> *coordinates);

		static float triangleArea(myPoint a, myPoint b, myPoint c);

		void getRotatedRad(float angle, myPoint *p);

		void rotateRad(float angle);  

		float dot(myPoint p);

    void makeInteger() {
      x = int(x);
      y = int(y);
    }

		static void baryCentricCoordinates(myPoint p, myPoint a, myPoint b, myPoint c, vector<float> *coords);

		static float colorDist(myPoint a, myPoint b, float frequency);

		static float colorDistTan(myPoint a, myPoint b, float frequency);

		float distsq(myPoint other) {
			return (other.x - x) * (other.x - x) + (other.y - y) * (other.y - y);
		}

		float dist(myPoint other) {
			return sqrtf((other.x - x) * (other.x - x) + (other.y - y) * (other.y - y));
		}
    
			float angleRad();
    	float angleRad(myPoint vec);

			static float angle(myPoint a, myPoint b, myPoint c);
			static float distance(myPoint a, myPoint b, myPoint c); // distance to a line
			float L1();
			float L2();
    

			static myPoint middle(vector<myPoint> *points) {
			// The centroid of a polygon is
			// Cx = 1/6A * SUM((xi + xi+1) * (xi * yi+1 - xi+1 * yi))
			// Cy = 1/6A * SUM((yi + yi+1) * (xi * yi+1 - xi+1 * yi))
			// A = .0.5 * SUM(xi * yi+1  - xi+1 * yi)

			myPoint m;
			int n = points->size();
			float A = 0;
			float Cx = 0;
			float Cy = 0;
			int xi, xii, yi, yii;

			for(int i = 0; i <= (n - 1); i++) {
				xi = points->at(i).x;
				xii = points->at((i + 1) % n).x;

				yi = points->at(i).y;
				yii = points->at((i + 1) % n).y;

				Cx += (xi + xii) * (xi * yii - xii * yi);
				Cy += (yi + yii) * (xi * yii - xii * yi);
				A += (xi * yii  - xii * yi);
			}

			A *= 0.5f;
			Cx *= 1.0f / (6.0f * A);
			Cy *= 1.0f / (6.0f * A);

			return myPoint((int) Cx, (int) Cy);
		}
	
		myPoint operator+(const myPoint& vec) const;
		myPoint operator-(const myPoint& vec) const;
		myPoint operator*(const float f) const;

		static bool isEqual(myPoint a, myPoint b) {
			return (a.x == b.x) && (a.y == b.y);
		}

		static float sameSide(myPoint a, myPoint b, myPoint ref, myPoint cur);
  
		friend ostream& operator<<(ostream& os, const myPoint& vec);
};

#endif
