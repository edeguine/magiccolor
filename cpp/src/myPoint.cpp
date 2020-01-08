#include "myPoint.h" 

myPoint myPoint::operator+(const myPoint& vec ) const {
	return myPoint( x + vec.x, y + vec.y);
}

 myPoint myPoint::operator-(const myPoint& vec ) const {
	return myPoint( x - vec.x, y - vec.y);
}

 myPoint myPoint::operator*(const float f) const {
	return myPoint(x * f, y * f);
}

ostream& operator<<(ostream& os, const myPoint& vec) {
	os << vec.x << ", " << vec.y;
		return os;
};

float myPoint::dot(myPoint p) {
	return (p.x * x + p.y * y);
}

float myPoint::L1() {
    return ((fabs(x) > fabs(y)) ? fabs(x) : fabs(y));
}

float myPoint::L2() {
    return sqrtf(x * x + y * y);
}

myPoint myPoint::scale(float t) {
	return myPoint(t * x, t * y);
}
