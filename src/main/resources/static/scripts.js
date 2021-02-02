function getResponse() {
    var coordinates = document.getElementById("coordinates").value;
    var arr = coordinates.split(", ");
    if (arr.length < 4) {
        alert("Введите больше координат (минимум 4)");
    } else if (arr.length > 10) {
        alert("Введите меньше координат (максимум 10)");
    } else {
        getLagrangePolynomial(coordinates);
        getCharts(coordinates);
        getErrorTable(coordinates);
    }
}

function getLagrangePolynomial(coordinates) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("lagrangePolynomial").innerHTML =
                "Полином Лагранжа: " + this.responseText;
        }
    };
    xhttp.open("POST", "http://localhost:8080/lagrange/polynomial", true);
    xhttp.send(coordinates);
}

function getCharts(coordinates) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var points = [];
            points = JSON.parse(this.responseText);
            drawChart(points);
        }
    };
    xhttp.open("POST", "http://localhost:8080/lagrange/charts", true);
    xhttp.send(coordinates);
}

function drawChart(datasource) {
    const NOT_INTERPOLATION_NODE = 0.1234; // код неинтерполяционного узла (см. ChartElement)
    var chart = $("#chart").dxChart({
        palette: "red",
        dataSource: datasource,
        commonSeriesSettings: {
            type: "spline",
            argumentField: "x"
        },
        commonAxisSettings: {
            grid: {
                visible: true
            }
        },
        margin: {
            bottom: 20
        },
        customizePoint: function(pointInfo) {
            return pointInfo.value === NOT_INTERPOLATION_NODE ? {visible: false} : {visible: true} // Спец. условие, чтобы вывести узлы интерполяции
        },
        series: [
            { valueField: "f", name: "f(x)" },
            { valueField: "polynomialOfX", name: "Q(x)" },
            { valueField: "interpolationNode", name: "узлы интерполяции", type: "scatter"}
        ],
        tooltip:{
            enabled: true
        },
        legend: {
            verticalAlignment: "top",
            horizontalAlignment: "right"
        },
        argumentAxis: {
            label:{
                format: {
                    type: "decimal"
                }
            },
            allowDecimals: false,
            axisDivisionFactor: 60
        },
        title: "Графики функции f(x) и полинома Лагранжа Q(x)"
    }).dxChart("instance");
}

function getErrorTable(coordinates) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var points = [];
            points = JSON.parse(this.responseText);
            document.getElementById("errorTableHeader").innerText = "Погрешности в точках между узлами интерполяции";
            drawTable(points);
        }
    };
    xhttp.open("POST", "http://localhost:8080/lagrange/errors", true);
    xhttp.send(coordinates);
}

function drawTable(points) {
    var html = '<tr>\n' +
        '        <th>x</th>\n' +
        '        <th>f(x)</th>\n' +
        '        <th>Q(x)</th>\n' +
        '        <th>|f(x) - Q(x)|</th>\n' +
        '    </tr>';
    for (var i = 0; i < points.length; i++) {
        var point = points[i];
        html = html + '<tr><td>' + point.x + '</td>\n' +
            '        <td>' + point.f + '</td>\n' +
            '        <td>' + point.polynomialOfX + '</td>\n' +
            '        <td>' + Math.abs(point.polynomialOfX - point.f) + '</td>';

    }
    document.getElementById("errorTable").innerHTML = html;
}