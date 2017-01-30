/**
 Copyright (c) 2017 Leontev Daniil
 prohormitrich53@gmail.com

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be included
 in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.polynom.polynom;

import com.polynom.solver.Solver;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daniel on 20.01.17.
 */

public class Polynom {
    private double[] coeffs;
    private String sPolynom;

    public Polynom() {
        setCoeffs(new double[]{1.0});
        recheck();
    }

    public Polynom(double[] coeffs) {
        if (coeffs.length == 0) {
            setCoeffs(new double[]{0.0});
        } else {
            setCoeffs(coeffs);
        }

        recheck();
    }

    public Polynom(int[] coeffs) {
        if (coeffs.length == 0) {
            setCoeffs(new double[]{0.0});
        } else {
            double[] c = new double[coeffs.length];

            for (int i = 0; i < c.length; i++) {
                c[i] = (double) coeffs[i];
            }

            setCoeffs(c);
        }

        recheck();
    }

    public Polynom(double c, int n) {
        double[] cs = new double[n+1];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = 0.0;
        }
        cs[n] = c;

        setCoeffs(cs);
    }

    public Polynom(int c, int n) {
        this((double) c, n);
    }

    public Polynom(String s) {
        String original = s;
        s = s.replaceAll("[*·\\s]", "");
        if (s.substring(0, 1).matches("\\d|x")) {
            s = "+" + s;
        }

        Pattern pattern = Pattern.compile("([+-]\\d*\\.?\\d*(x(\\^\\d*)?)?)+");
        Matcher matcher = pattern.matcher(s);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Incorrect syntax. Cannot evaluate " + original);
        } else {
            Pattern monomPattern = Pattern.compile("[+-]\\d*\\.?\\d*(x(\\^\\d*)?)?");
            Matcher monomMatcher = monomPattern.matcher(s);
            ArrayList<String> monoms = new ArrayList<String>();

            while (monomMatcher.find()) {
                String monom = s.substring(monomMatcher.start(), monomMatcher.end());
                monom = monom.replaceAll("\\+", "");

                if (!monom.equals("")) {
                    if (!monom.contains("x")) {
                        monom = monom + "x^0";
                    } else
                    if (monom.contains("x") && !monom.contains("^")) {
                        monom = monom + "^1";
                    }

                    monoms.add(monom);
                }
            }

            Map degCoeff = new TreeMap();

            for (int i = 0; i < monoms.size(); i++) {
                String m = monoms.get(i);
                int k = m.indexOf("x^");
                String stringCoef = m.substring(0, k);
                if (stringCoef.matches("(|-)")) {
                    stringCoef = stringCoef + "1.0";
                }
                String stringDeg = m.substring(k + 2);
                double coef = Double.valueOf(stringCoef);
                int deg = Integer.valueOf(stringDeg);

                if (!degCoeff.containsKey(deg)) {
                    degCoeff.put(deg, coef);
                } else {
                    throw new IllegalArgumentException("Incorrect syntax. Each degree must be used only once.");
                }

            }

            for (int i = 0; i < degCoeff.size(); i++) {
                if (degCoeff.get(i) == null) {
                    degCoeff.put(i, 0.0);
                }
            }

            double[] c = new double[degCoeff.size()];
            for (int i = 0; i < degCoeff.size(); i++) {
                c[i] = (double) degCoeff.get(i);
            }

            if (c.length == 0) {
                setCoeffs(new double[]{0.0});
            } else {
                setCoeffs(c);
            }

            recheck();

        }

    }

    public double[] getCoeffs(){
        return coeffs;
    }

    public int degree() {
        return this.getCoeffs().length - 1;
    }

    private void setCoeffs(double[] coeffs){
        this.coeffs = coeffs;
    }

    private void recheck() {
        double[] c = this.getCoeffs();
        int m = c.length - 1;

        if (m > 0) {
            if (c[m] == 0) {
                double[] t = new double[m];
                for (int i = 0; i < t.length; i++) {
                    t[i] = c[i];
                }
                this.setCoeffs(t);
                this.recheck();
            }
        }
    }

    public boolean equals(Polynom polynom) {
        double[] c1 = this.getCoeffs();
        double[] c2 = polynom.getCoeffs();
        boolean isTrue = true;

        if (c1.length == c2.length) {
            for (int i = 0; i < c1.length; i++) {
                if (c1[i] != c2[i]) {
                    isTrue = false;
                    break;
                }
            }
        } else
            return false;
        if (isTrue) {
            return true;
        } else
            return false;
    }

    public boolean equals(double x) {
        return this.equals(new Polynom(new double[]{x}));
    }

    public boolean equals(int x) {
        return this.equals((double) x);
    }

    public boolean isConst() {
        if (this.degree() == 0) {
            return true;
        } else
            return false;
    }

    public Polynom add(Polynom polynom) {
        double[] c1 = this.getCoeffs();
        double[] c2 = polynom.getCoeffs();

        int n = Math.max(c1.length, c2.length);
        int m = Math.min(c1.length, c2.length);

        double[] c = new double[n];

        for (int i = 0; i < m; i++) {
            c[i] = c1[i] + c2[i];
        }

        if (c1.length < c2.length) {
            for (int i = m; i < n; i++) {
                c[i] = c2[i];
            }
        } else {
            for (int i = m; i < n; i++) {
                c[i] = c1[i];
            }
        }

        Polynom p = new Polynom(c);
        p.recheck();

        return p;
    }

    public Polynom subtract(Polynom polynom) {
        return this.add(polynom.multiply(-1));
    }

    public Polynom multiply(Polynom polynom) {
        double[] c1 = this.getCoeffs();
        double[] c2 = polynom.getCoeffs();
        int n1 = c1.length-1;
        int n2 = c2.length-1;
        double[] c = new double[n1 + n2 + 1];
        double[] c1wide = new double[n1 + n2 + 1];
        double[] c2wide = new double[n1 + n2 + 1];

        for (int i = 0; i < c.length; i++) {
            c[i] = 0.0;
            c1wide[i] = 0.0;
            c2wide[i] = 0.0;
        }

        for (int i = 0; i < c1.length; i++) {
            c1wide[i] = c1[i];
        }

        for (int i = 0; i < c2.length; i++) {
            c2wide[i] = c2[i];
        }

        for (int k = 0; k <= n1 + n2; k++) {
            for (int i = 0; i <= k; i++) {
                c[k] = c[k] + c1wide[i]*c2wide[k-i];
            }
        }

        Polynom p = new Polynom(c);
        p.recheck();

        return p;
    }

    public Polynom multiply(double x) {
        double[] c1 = this.getCoeffs();
        double[] c = new double[c1.length];

        for (int i = 0; i < c.length; i++) {
            c[i] = c1[i] * x;
        }

        Polynom p = new Polynom(c);
        p.recheck();

        return p;
    }

    public Polynom power(int n) {
        if (n < 0) {
            System.out.println("Only for non-negative n!");
            return null;
        } else
            if (n == 0) {
                return new Polynom();
            } else
                if (n == 1) {
                    return new Polynom(this.getCoeffs());
                }

        Polynom p = new Polynom(this.getCoeffs());

        int i = n;
        while (i > 1) {
            p = p.multiply(this);
            i--;
        }
        p.recheck();

        return p;
    }

    public Polynom differentiate() {
        double[] c = this.getCoeffs();
        double[] d = new double[c.length];
        Polynom p;

        for (int i = 0; i < d.length; i++) {
            d[i] = 0.0;
        }

        if (c.length < 2) {
            return new Polynom(new double[]{0.0});
        }


        for (int i = 1; i < c.length; i++) {
            d[i-1] = c[i]*i;
        }

        p = new Polynom(d);
        p.recheck();

        return p;
    }

    public Polynom differentiate(int n) {
        if (n < 0) {
            System.out.println("Only for non-negative n!");
            return null;
        }

        if (n == 0) {
            return new Polynom(this.getCoeffs());
        } else
            if (n == 1) {
                return this.differentiate();
            }

        Polynom p = new Polynom(this.getCoeffs());

        int i = n;
        while (i > 0) {
            p = p.differentiate();
            i--;
        }

        return p;
    }

    public double valueOf(double x) {
        double[] c = this.getCoeffs();
        double ans = c[0];

        for (int i = 1; i < c.length; i++) {
            ans += c[i]*Math.pow(x, i);
        }

        return ans;
    }

    public double[] valueOf(double[] x) {
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            y[i] = this.valueOf(x[i]);
        }

        return y;
    }

    public double[] valueOf(int[] x) {
        double[] y = new double[x.length];

        for (int i = 0; i < y.length; i++) {
            y[i] = this.valueOf(x[i]);
        }

        return y;
    }

    public int sign(double x) {
        int sign;

        sign = (int) Math.round(this.valueOf(x) / Math.abs(this.valueOf(x)));

        return sign;
    }

    public int sign(String s) {
        int sign;
        int m = this.degree();
        double a = this.getCoeffs()[m];

        switch (s) {
            case "-Inf":
                sign = (int) Math.round(Math.pow(-1, m) * a / Math.abs(a));
                break;
            case "Inf":
                sign = (int) Math.round(a / Math.abs(a));
                break;
            default:
                sign = 0;
                break;
        }

        return sign;
    }

    public Polynom[] mod(Polynom polynom) {
        if (polynom.equals(0.0)) {
            throw new ArithmeticException("Division by zero.");
        }

        Polynom dividend = new Polynom(this.getCoeffs());
        Polynom divider = new Polynom(polynom.getCoeffs());
        Polynom resultQ = new Polynom(new double[]{0.0});
        double[] dividendCoeffs = dividend.getCoeffs();
        int dividendDegree = dividendCoeffs.length - 1;
        double[] dividerCoeffs = divider.getCoeffs();
        int dividerDegree = dividerCoeffs.length - 1;

        while (dividend.getCoeffs().length >= 1 && dividendDegree - dividerDegree >= 0 && !dividend.equals(0)) {

            dividendCoeffs = dividend.getCoeffs();
            dividendDegree = dividendCoeffs.length - 1;
            dividerCoeffs = divider.getCoeffs();
            dividerDegree = dividerCoeffs.length - 1;

            Polynom quotient = new Polynom(dividendCoeffs[dividendDegree]/dividerCoeffs[dividerDegree], dividendDegree - dividerDegree);
            resultQ = resultQ.add(quotient);
            Polynom residue = new Polynom(quotient.multiply(divider).getCoeffs());
                int check = dividend.degree();
            dividend = new Polynom(dividend.subtract(residue).getCoeffs());
                if (check == dividend.degree()) {
                    double[] c = dividend.getCoeffs();
                    c[check] = 0.0;
                    dividend = new Polynom(c);
                }

            dividendCoeffs = dividend.getCoeffs();
            dividendDegree = dividendCoeffs.length - 1;
        }

        Polynom[] ans = new Polynom[]{resultQ, dividend};

        return ans;
    }

    public double[] solve() {
        Solver solver = new Solver();
        double[] x = solver.solve(this);

        return x;
    }

    public double[] solve(double eps) {
        double min = 1E-15;
        if (eps < min) {
            eps = min;
            System.out.println("Setting EPS to maximal possible accuracy 1E-15");
        }
        Solver solver = new Solver(eps);
        double[] x = solver.solve(this);

        return x;
    }

    public Polynom gcd(Polynom polynom) {
        ArrayList<Polynom> residues = new ArrayList<Polynom>();
        residues.add(this);
        residues.add(polynom);

        int i = 1;
        while (!residues.get(i).equals(0)) {
            Polynom res = residues.get(i-1).mod(residues.get(i))[1];
            residues.add(res);
            i++;
        }

        if (residues.get(i-1).isConst()) {
            return new Polynom();
        } else
            return residues.get(i-1);
    }

    public String toString(){
        sPolynom = "";

        for (int i = 0; i < coeffs.length; i++ ){

            switch (i) {
                case 0: if (coeffs[i] != 0) {

                    if (coeffs[i] < 0) {
                        sPolynom += "- ";
                    }

                    sPolynom += Math.abs(coeffs[i]);
                } else
                    if (coeffs.length == 1) {
                        sPolynom += coeffs[i];
                    }

                    break;
                case 1: if (coeffs[i] != 0) {

                    if (!sPolynom.equals("")){
                        if (coeffs[i] < 0) {
                            sPolynom += " - ";
                        } else {
                            sPolynom += " + ";
                        }

                        sPolynom += Math.abs(coeffs[i]);
                    } else {

                        if (coeffs[i] < 0) {
                            sPolynom += "- ";
                        }

                        sPolynom += Math.abs(coeffs[i]);
                    }

                    sPolynom += "·x";
                }
                    break;
                default: if (coeffs[i] != 0) {

                    if (!sPolynom.equals("")){
                        if (coeffs[i] < 0) {
                            sPolynom += " - ";
                        } else {
                            sPolynom += " + ";
                        }

                        sPolynom += Math.abs(coeffs[i]);
                    } else {

                        if (coeffs[i] < 0) {
                            sPolynom += "- ";
                        }

                        sPolynom += Math.abs(coeffs[i]);
                    }

                    sPolynom += "·x^"+i;
                }
                    break;
            }
        }

        return sPolynom;
    }

}