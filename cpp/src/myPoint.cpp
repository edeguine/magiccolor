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
    // Compute the dot product with p.

	return (p.x * x + p.y * y);
}

float myPoint::L1() {
    // Compute the L1 norm.

    return ((fabs(x) > fabs(y)) ? fabs(x) : fabs(y));
}

float myPoint::L2() {
    // Compute the L2 norm.

    return sqrtf(x * x + y * y);
}

myPoint myPoint::scale(float t) {
    // Compute a linear scaling.

	return myPoint(t * x, t * y);
}
