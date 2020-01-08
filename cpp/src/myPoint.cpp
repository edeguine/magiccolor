#include "myPoint.h" 
#include "myColorRGB.h"

myPoint myPoint::scale(float t) {
	return myPoint(t * x, t * y);
}

float myPoint::crossProduct(myPoint a, myPoint b) {
		return a.x * b.y - a.y * b.x;
}

float myPoint::cotangent(myPoint a, myPoint b, myPoint c) {
	myPoint ba, bc;
	ba = a - b;
	bc = c - b;
	float n, d;

	n = bc.dot(ba);
	d = myPoint::crossProduct(bc, ba);

	if(d != 0) {
		return n / d;
	} else {
		return 0;
	}
}

void myPoint::baryPaper(myPoint p, vector<myPoint> *edges, vector<float> *bary) {
	
	float tot = 0;
	int prev, next;
	int n = edges->size();
	int j = 0;

	float cprev, cnext, dist;
	float w;

	for(auto e: *edges) {
		prev = (j + n - 1) % n;
		next = (j + 1) % n;
		
		cprev = myPoint::cotangent(p, e, edges->at(prev));
		cnext = myPoint::cotangent(p, e, edges->at(next));
		dist = (p - e).L2() * (p - e).L2();
		if(dist != 0) {
			w = cprev * cnext / dist;
		} else {
			w = 0;
		}
		tot += w;

		bary->push_back(w);
		j++;
	}

	tot = tot == 0? 1: tot;

	for(int i = 0; i < bary->size(); i++) {
		(*bary)[i] = bary->at(i) / tot;
	}
}

float myPoint::relativeCoords(myPoint middle, myPoint a, myPoint b) {
	float res;

	res = 1.0f - a.dist(middle) / (a.dist(middle));
	res = 1.0f - a.dist(middle) / 1100.0f; 
	res = 1.0f - middle.dist(a) / (middle.dist(a) + middle.dist(b));
	res = 1.0f - (middle - a).dot(b-a) / (b.dist(a) * b.dist(a));
	res = res < 0.0f? 0.0f: res;
	res = res > 1.0f? 1.0f: res;
	
	return res;
}

bool myPoint::lexiSort(myPoint a, myPoint b) {
 // return b > a is true or not
 // x coord first
	if(b.x > a.x) {
		return true;
	}

	if(b.x == a.x) {
		return b.y >= a.y;
	}

	if(b.x < a.x) {
		return false;
	}

	return false;
}

void myPoint::closestPointCoordinates(myPoint p, vector<myPoint> *refs, vector<float> *coordinates) {

	int npoints = refs->size();

	// Calc distances
	vector<tuple<int, float>> distances;
	for(int i = 0; i < npoints; i++) {
		distances.push_back(make_tuple(i, p.dist(refs->at(i))));
	}

	sort(distances.begin(), distances.end(),
    [](const tuple<int, float> & a, const tuple<int, float> & b) -> bool
			{
    	return get<1>(a) < get<1>(b);});


	myPoint a, b, c;
	int idxa, idxb, idxc;
	float ca, cb;

	idxa = get<0>(distances[0]);
	idxb = get<0>(distances[1]);

	a = refs->at(idxa);
	b = refs->at(idxb);

	bool aisleft = myPoint::lexiSort(a, b);

	if(aisleft == false) {
		// We have to swap the points so that the color order is stable
		idxc = idxb;
		idxb = idxa;
		idxa = idxc;

		c = b;
		b = a;
		a = c;
	}

	ca = myPoint::relativeCoords(p, a, b);
	cb = 1.0f - ca;

	for(int i = 0; i < npoints; i++) {
		if(i == idxa) {
			coordinates->push_back(ca);
		}

		if(i == idxb) {
			coordinates->push_back(cb);
		}

		if(i != idxa && i != idxb) {
			coordinates->push_back(0);
		}
	}

}

void myPoint::baryPolygonCoordinates(myPoint p, vector<myPoint> *polygon, vector<float> *coordinates) {

	// The polygon has to be in trigo or anti trigo order, not random

	if(polygon->size() > 2) {

		int first, second;
		float area = 0;

		for(int i = 0; i < polygon->size(); i++) {

			area = 0;
			for(int j = 0; j < polygon->size(); j++) {

				first = j;
				second = (j + 1) % polygon->size();

				if(first != i && second != i) {

					area += myPoint::triangleArea(p, polygon->at(first), polygon->at(second));
				}
			}
			coordinates->push_back(area);
		}
	}


	float concentration;
	if(polygon->size() == 2) {
		concentration = 1.0f - (p.dist(polygon->at(0)) / (polygon->at(0).dist(polygon->at(1))));
		concentration = concentration < 0? 0: concentration;
		coordinates->push_back(concentration);
		coordinates->push_back(1.0f - concentration);
	}
}

float myPoint::triangleArea(myPoint a, myPoint b, myPoint c) {

	float area = fabs(a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2.0f;
	return area;
}

void myPoint::baryCentricCoordinates(myPoint p, myPoint a, myPoint b, myPoint c, vector<float> *coords) {

    myPoint v0 = b - a;
    myPoint v1 = c - a;
	myPoint v2 = p - a;

    float d00 = v0.dot(v0);
    float d01 = v0.dot(v1);
    float d11 = v1.dot(v1);
    float d20 = v2.dot(v0);
    float d21 = v2.dot(v1);
    float denom = d00 * d11 - d01 * d01;
    float v = (d11 * d20 - d01 * d21) / denom;
    float w = (d00 * d21 - d01 * d20) / denom;
    float u = 1.0f - v - w;

		coords->push_back(u);
		coords->push_back(v);
		coords->push_back(w);
}

float myPoint::dot(myPoint p) {
	return (p.x * x + p.y * y);
}

void myPoint::getRotatedRad(float angle, myPoint *p) {
			float a = angleRad();
      float r = L2();
      // return myPoint(x*cos(a) - y*sin(a),
      //                     x*sin(a) + y*cos(a) );
			p->x = r * cos(a + angle);
			p->y = r * sin(a + angle);
}

void myPoint::rotateRad(float angle) {
       float a = angleRad();
       float r = L2();
       // return myPoint(x*cos(a) - y*sin(a),
       //                     x*sin(a) + y*cos(a) );
       x = r * cos(a + angle);
       y = r * sin(a + angle);
}


myPoint myPoint::operator+(const myPoint& vec ) const {
	return myPoint( x + vec.x, y + vec.y);
}

 myPoint myPoint::operator-(const myPoint& vec ) const {
	return myPoint( x - vec.x, y - vec.y);
}

 myPoint myPoint::operator*(const float f) const {
	return myPoint(x * f, y * f);
}

float myPoint::angleRad() {
    return myPoint::angleRad(myPoint(1, 0));
}

float myPoint::angleRad(myPoint vec) {
		return -atan2(x * vec.y - y * vec.x, x * vec.x + y * vec.y);
}


 ostream& operator<<(ostream& os, const myPoint& vec) {
	os << vec.x << ", " << vec.y;
		return os;
};

float myPoint::L1() {
    return ((fabs(x) > fabs(y)) ? fabs(x) : fabs(y));
}

float myPoint::L2() {
    return sqrtf(x * x + y * y);
}

float myPoint::colorDist(myPoint a, myPoint b, float frequency) {

	float dist = a.dist(b);
	float res = cosf(dist * 2.0f * PI * frequency / 20.0f);

	return res;
}

float myPoint::colorDistTan(myPoint a, myPoint b, float frequency) {

	float dist = a.dist(b);
	float res = tanf(dist * 2.0f * PI * frequency / 20.0f);

	return res;
}

float myPoint::angle(myPoint a, myPoint b, myPoint c) {
/*
    The return value with acos is 0, pi
    a is the center
 
        Old code:
        myPoint ab(b.x - a.x, b.y - a.y);
    	myPoint cb(c.x - b.x, c.y - b.y);
    	float dot = (ab.x * cb.x + ab.y * cb.y);
        float cross = (ab.x * cb.y - ab.y * cb.x);
	    float alpha = atan2(cross, dot);
    	return alpha;
*/

	float dx21 = b.x - a.x;
	float dx31 = c.x - a.x;
	float dy21 = b.y - a.y;
	float dy31 = c.y - a.y;
	float m12 = sqrt(dx21 * dx21 + dy21 * dy21);
	float m13 = sqrt(dx31 * dx31 + dy31 * dy31);
	float theta = acos((dx21 * dx31 + dy21 * dy31) / (m12 * m13));
	return theta;
}

float myPoint::distance(myPoint a, myPoint b, myPoint c) {
  // distance of point C from line AB
	float num = fabs((a.y - b.y) * c.x - (a.x - b.x) * c.y + a.x * b.y - a.y * b.x);
	float den = sqrtf((b.y - a.y) * (b.y - a.y) + (b.x - a.x) * (b.x - a.x));

	return num / den;
}
			
float myPoint::sameSide(myPoint a, myPoint b, myPoint ref, myPoint cur) {
			float da, db, dc;

			da = b.y - a.y;
			db = -(b.x - a.x);
			dc = (b.x * a.y - a.x * b.y);

			float s0 = da * ref.x + db * ref.y + dc;
			float s1 = da * cur.x + db * cur.y + dc; 
			return (s0 * s1 <= 0.0f ? -1.0f : 1.0f);
}
