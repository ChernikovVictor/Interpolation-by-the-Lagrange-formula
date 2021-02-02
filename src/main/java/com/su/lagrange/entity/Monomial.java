package com.su.lagrange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Monomial {

    private final int degree;
    private final double coefficient;

    public static boolean isZero(Monomial monomial) {
        return Math.abs(monomial.getCoefficient()) < 1e-9;
    }

    public Monomial add(Monomial monomial) {
        return new Monomial(this.getDegree(), this.getCoefficient() + monomial.getCoefficient());
    }

    public Monomial multiplyByNumber(double number) {
        return new Monomial(this.getDegree(), this.getCoefficient() * number);
    }

    public Monomial multiplyByMonomial(Monomial monomial) {
        return new Monomial(this.getDegree() + monomial.getDegree(),
                this.getCoefficient() * monomial.getCoefficient());
    }

    public double valueAt(double x) {
        return Math.pow(x, this.getDegree()) * this.getCoefficient();
    }

    public String toHTML() {
        if (degree == 0) {
            return monomialWithZeroDegreeToHTML();
        } else if (degree == 1) {
            return monomialWithUnoDegreeToHTML();
        } else {
            return (coefficient < 0) ?
                    String.format("&ndash; %fx<sup>%d</sup>", Math.abs(coefficient), degree)
                    :  String.format("%fx<sup>%d</sup>", coefficient, degree);
        }
    }

    private String monomialWithZeroDegreeToHTML() {
        return coefficient < 0 ? String.format("&ndash; %f", Math.abs(coefficient)) : String.format("%f", coefficient);
    }

    private String monomialWithUnoDegreeToHTML() {
        return coefficient < 0 ? String.format("&ndash; %fx", Math.abs(coefficient)) : String.format("%fx", coefficient);
    }
}
