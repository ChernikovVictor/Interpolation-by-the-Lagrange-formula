package com.su.lagrange.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChartElement {

    /* код неинтерполяционного узла */
    public static final double NOT_INTERPOLATION_NODE = 0.1234;

    public final double x;
    public final double f;
    public final double polynomialOfX;
    public final double interpolationNode;

}
