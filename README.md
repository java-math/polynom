# polynom
##Very simple Java library working with polynomial.
Thank effusively to [@phillveber](https://github.com/phillveber) for help with testing.

###Usage

For detailed description and examples see Main.java

####Constructors:

1) `Polynom()` - Creates "x^0" polynomial, which is exactly "1.0".

2) `Polynom(int[] coefficients)`, `Polynom(double[] coefficients)` - You can send an array of coefficients to constructor.

3) `Polynom(a, deg)` - Creates a*x^deg polynomial.

4) `Polynom(String s)` - Creates polynomial from String. F.e. s = "x^2+2x+3"

####Methods:

1) `double[] getCoeffs()` - Will return array of double[] with coefficients of polynomial.

2) `int degree()` - Will return the degree of polynomial.

3) `boolean isConst()` - Check if polynomial degree is 0

4) `boolean equals(Polynom p)` - Compare two polynomial

5) `boolean equals(double a)` - Compare polynomial to double number

6) `Polynom add(Polynom p)` - Addition

7) `Polynom subtract(Polynom p)` - Subtraction

8) `Polynom multiply(Polynom p)` - Multiplication

9) `Polynom[] mod(Polynom p)` - Modulo operation

10) `Polynom power(int a)` - Exponentiation

11) `Polynom differentiate()` - First derivative

12) `Polynom differentiate(int n)` - n'th derivative

13) `int sign(double x)` - Signum

14) `double valueOf(double x)` - value of polynomial at x

15) `double[] valueOf(double[] xs)` - array of values of polynomial at each x of array xs

16) `double[] solve()` - All real roots of polynomial

17) `double[] solve(double a)` - All real roots with manual set accuracy

18) `Polynom gcd(Polynom p)` - Greatest common divisor of two polynomial
