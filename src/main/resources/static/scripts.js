function getResponse() {
    var coordinates = document.getElementById("coordinates").value;
    var arr = coordinates.split(", ");
    if (arr.length < 4) {
        alert("Enter more coordinates (at least 4)");
    } else if (arr.length > 10) {
        alert("Too many coordinates (not more than 10)");
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
                "Lagrange Polynomial: " + this.responseText;
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
    const NOT_INTERPOLATION_NODE = 0.1234; // check (ChartElement)
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
            return pointInfo.value === NOT_INTERPOLATION_NODE ? {visible: false} : {visible: true} // Special condition for interpolation nodes
        },
        series: [
            { valueField: "f", name: "f(x)" },
            { valueField: "polynomialOfX", name: "Q(x)" },
            { valueField: "interpolationNode", name: "Interpolation nodes", type: "scatter"}
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
        title: "Function f(x) and Lagrange polynomial Q(x) graphics"
    }).dxChart("instance");
}

function getErrorTable(coordinates) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var points = [];
            points = JSON.parse(this.responseText);
            document.getElementById("errorTableHeader").innerText = "Errors in the points between the interpolation nodes";
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