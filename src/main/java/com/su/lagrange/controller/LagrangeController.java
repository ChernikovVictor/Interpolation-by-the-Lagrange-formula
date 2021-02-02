package com.su.lagrange.controller;

import com.su.lagrange.dto.ChartElement;
import com.su.lagrange.dto.Point;
import com.su.lagrange.entity.Polynomial;
import com.su.lagrange.service.LagrangePolynomialSupplier;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@RestController
@RequestMapping("/lagrange")
public class LagrangeController {

    private final LagrangePolynomialSupplier polynomialSupplier;

    private static final Function<Double, Double> f = x -> {
        double a = Math.pow(Math.E, -1 * Math.atan(x));
        double b = Math.pow(Math.E, x);
        double c = Math.pow(Math.E, -1 * x);
        return a * (b - c) * Math.sin(2 * x);
    };

    @PostMapping("/polynomial")
    public String getLagrangePolynomial(@RequestBody String coordinates) {
        Point[] points = getSortedPointsFromCoordinates(coordinates);
        Polynomial polynomial = LagrangePolynomialSupplier.provide(points);
        return polynomial.toHTML();
    }

    @PostMapping("/errors")
    public List<ChartElement> getPointsForError (@RequestBody String coordinates) {
        Point[] points = getSortedPointsFromCoordinates(coordinates);
        Polynomial polynomial = LagrangePolynomialSupplier.provide(points);
        return getChartElementsForErrorTable(points, polynomial);
    }

    @PostMapping("/charts")
    public List<ChartElement> getCharts(@RequestBody String coordinates) {
        Point[] points = getSortedPointsFromCoordinates(coordinates);
        Polynomial polynomial = LagrangePolynomialSupplier.provide(points);
        ChartElement[] chartElements1 = getChartElementsForInterpolationNodes(points, polynomial);
        ChartElement[] chartElements2 = getChartElementsForOtherNodes(points, polynomial);
        return Stream.concat(Arrays.stream(chartElements1), Arrays.stream(chartElements2))
                .collect(Collectors.toList());
    }

    private Point[] getSortedPointsFromCoordinates(String coordinates) {
        String[] arguments = coordinates.split(", ");
        Point[] points = new Point[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            double x = Double.parseDouble(arguments[i]);
            double y = f.apply(x);
            points[i] = new Point(x, y);
        }

        Arrays.sort(points);
        return points;
    }

    private List<ChartElement> getChartElementsForErrorTable(Point[] points, Polynomial polynomial) {
        List<ChartElement> chartElements = new ArrayList<>(points.length - 1);
        for (int i = 0; i < points.length - 1; i++) {
            double x = points[i].x + (points[i + 1].x - points[i].x) / 2;
            double y = f.apply(x);
            double p = polynomial.valueAt(x);
            chartElements.add(new ChartElement(x, y, p, ChartElement.NOT_INTERPOLATION_NODE));
        }
        return chartElements;
    }

    private ChartElement[] getChartElementsForInterpolationNodes(Point[] points, Polynomial polynomial) {
        ChartElement[] chartElements = new ChartElement[points.length];
        for (int i = 0; i < points.length; i++) {
            chartElements[i] = new ChartElement(points[i].x, points[i].y,
                    polynomial.valueAt(points[i].x), points[i].y);
        }
        return chartElements;
    }

    private ChartElement[] getChartElementsForOtherNodes(Point[] points, Polynomial polynomial) {
        ChartElement[] chartElements = new ChartElement[1000];
        double minX = points[0].x;
        double maxX = points[points.length - 1].x;
        double step = (maxX - minX) / 1000;
        for (int i = 0; i < 1000; i++) {
            double x = minX + i * step;
            double y = f.apply(x);
            double p = polynomial.valueAt(x);
            chartElements[i] = new ChartElement(x, y, p, ChartElement.NOT_INTERPOLATION_NODE);
        }

        return chartElements;
    }
}
