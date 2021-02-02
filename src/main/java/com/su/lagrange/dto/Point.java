package com.su.lagrange.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Point implements Comparable<Point> {

    public final double x;
    public final double y;

    @Override
    public int compareTo(Point point) {
        return Double.compare(this.x, point.x);
    }
}
