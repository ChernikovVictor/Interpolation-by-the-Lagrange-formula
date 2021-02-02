package com.su.lagrange.entity;

public class Polynomial {

    private final Monomial[] monomials = new Monomial[11];

    public static boolean isZero(Polynomial polynomial) {
        for (Monomial monomial : polynomial.monomials) {
            if (!Monomial.isZero(monomial)) {
                return false;
            }
        }
        return true;
    }

    public Polynomial() {
        for (int i = 0; i < monomials.length; i++) {
            monomials[i] = new Monomial(i, 0);
        }
    }

    public void addMonomial(Monomial monomial) {
        monomials[monomial.getDegree()] = monomials[monomial.getDegree()].add(monomial);
    }

    public void addPolynomial(Polynomial polynomial) {
        for (int i = 0; i < monomials.length; i++) {
            monomials[i] = monomials[i].add(polynomial.monomials[i]);
        }
    }

    public void multiplyByNumber(double number) {
        for (int i = 0; i < monomials.length; i++) {
            monomials[i] = monomials[i].multiplyByNumber(number);
        }
    }

    public Polynomial multiplyByMonomial(Monomial monomial) {
        Polynomial polynomial = new Polynomial();
        for (Monomial value : monomials) {
            if (!Monomial.isZero(value)) {
                polynomial.addMonomial(value.multiplyByMonomial(monomial));
            }
        }
        return polynomial;
    }

    public Polynomial multiplyByPolynomial(Polynomial polynomial) {
        Polynomial result = new Polynomial();
        for (Monomial monomial : polynomial.monomials) {
            if (!Monomial.isZero(monomial)) {
                result.addPolynomial(this.multiplyByMonomial(monomial));
            }
        }
        return result;
    }

    public double valueAt(double x) {
        double value = 0;
        for (Monomial monomial : monomials) {
            value += monomial.valueAt(x);
        }
        return value;
    }

    public String toHTML() {
        int i = getFirstDegreeOfNonZeroMonomial();
        if (i < 0) {
            return "0";
        } else {
            StringBuilder s = new StringBuilder(monomials[i].toHTML());
            getOtherMonomialsAsHTML(s, i - 1);
            return s.toString();
        }
    }

    private int getFirstDegreeOfNonZeroMonomial() {
        int i = monomials.length - 1;
        while (i >= 0 && Monomial.isZero(monomials[i])) {
            i--;
        }
        return i;
    }

    private void getOtherMonomialsAsHTML(StringBuilder s, int i) {
        while (i >= 0) {
            if (!Monomial.isZero(monomials[i])) {
                if (monomials[i].getCoefficient() > 0) {
                    s.append(" + ").append(monomials[i].toHTML());
                } else {
                    s.append(" ").append(monomials[i].toHTML());
                }
            }
            i--;
        }
    }
}
