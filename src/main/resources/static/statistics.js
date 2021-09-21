//JSON-object klaarzetten
var assetStats = null

function getAssetStats() {
    fetch('/', {//vullen met endpoint
        method: 'GET',
        headers: {'Authorization': localStorage.getItem('token')}
    }).then(res => res.json())
        .then(json => {
            assetStats = json
        })
}


window.onload = function () {
    var dataPoints1 = [], dataPoints2 = [];
    var stockChart = new CanvasJS.StockChart("stockChartContainer", {
        animationEnabled: true,
        theme: "light1",
        title: {
            text: "Crypto price chart", //dit vullen met cryptonaam
            fontFamily: "Calibri,Optima,Arial,sans-serif"
        },
        subtitles: [{
            text: "Crypto price chart",//deze mogelijk weghalen
            fontFamily: "Calibri,Optima,Arial,sans-serif"
        }],
        charts: [{
            toolTip: {
                shared: true
            },
            axisY: {
                suffix: " USD"
            },
            data: [{
                name: "Min-Max",
                type: "rangeArea",
                xValueFormatString: "DD-MM-YYYY",
                yValueFormatString: "USD#,###.##",
                dataPoints: dataPoints1
            }, {
                name: "Average",
                type: "line",
                yValueFormatString: "USD#,###.##",
                dataPoints: dataPoints2
            }]
        }],
        navigator: {
            data: [{
                dataPoints: dataPoints2
            }],
            axisX: {
                labelFontColor: "white",
                labelFontWeight: "bolder"
            },
            slider: {
                minimum: new Date(2021, 0o0),
                maximum: new Date(2021, 10)
            }
        },
        rangeSelector: {
            buttons: [{
                label: "1D",
                range: 1,
                rangeType: "day"
            }, {
                label: "7D",
                range: 7,
                rangeType: "day"
            }, {
                label: "1Y",
                range: 1,
                rangeType: "year"
            }, {
                label: "All",
                range: null,
                rangeType: "all"
            }]
        }
    });

    $.getJSON("https://canvasjs.com/data/gallery/stock-chart/weather-india.json", function (data) {
        for (var i = 0; i < data.length; i++) {
            dataPoints1.push({x: new Date(data[i].year, 0o0, 0o0), y: [Number(data[i].min), Number(data[i].max)]});
            dataPoints2.push({x: new Date(data[i].year, 0o0, 0o0), y: Number(data[i].avg)});
        }
        stockChart.render();
    });
}