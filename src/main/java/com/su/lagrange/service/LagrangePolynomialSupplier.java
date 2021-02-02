package com.su.lagrange.service;

import com.su.lagrange.dto.Point;
import com.su.lagrange.entity.Monomial;
import com.su.lagrange.entity.Polynomial;
import org.springframework.stereotype.Component;

@Component
public class LagrangePolynomialSupplier {

    public static Polynomial provide(Point[] points) {

        Polynomial lagrangePolynomial = new Polynomial();

        for (int k = 0; k < points.length; k++) {
            Polynomial polynomial = getNextPolynomial(k, points);
            polynomial.multiplyByNumber(points[k].y);
            lagrangePolynomial.addPolynomial(polynomial);
        }

        return lagrangePolynomial;
    }

    private static Polynomial getNextPolynomial(int k, Point[] points) {

        Polynomial result = new Polynomial();
        for (int j = 0; j < points.length; j++) {
            if (j != k) {
                Polynomial polynomial = createBasePolynomial(j, k, points);
                if (Polynomial.isZero(result)) {
                    result.addPolynomial(polynomial);
                } else {
                    result = result.multiplyByPolynomial(polynomial);
                }
            }
        }

        return result;
    }

    private static Polynomial createBasePolynomial(int j, int k, Point[] points) {
        Monomial x = new Monomial(1, 1);
        Monomial x_j = new Monomial(0, -1 * points[j].x);
        Polynomial polynomial = new Polynomial();
        polynomial.addMonomial(x);
        polynomial.addMonomial(x_j);
        polynomial.multiplyByNumber(1 / (points[k].x - points[j].x));
        return polynomial;
    }

}
