//JSON-object met daarin een map in een map. "datum: klaarzetten
let dataMap = null
// window.onload = getAssetStats('BTC', 7)
//TODO: eventlistener veranderen in: als overlay opent (functie openDetails) -> creëer graph
window.addEventListener("DOMContentLoaded", () => {
    // await getAssetStats('BTC', 7)
    getAssetStats('BTC', 7)
})
// window.onload = async () => {
//     await getAssetStats('BTC', 7)
//     await createGraph()
// }

async function setupStats() {
    await getAssetStats('BTC', 7)
    createGraph()
}

async function getAssetStats(symbol, daysBack) {
    await fetch(`/cryptoStats?symbol=${symbol}&daysBack=${daysBack}`, {//vullen met endpoint
        method: 'GET',
        headers: {'Authorization': localStorage.getItem('token')}
    })
        .then(res => res.json())
        .then(json => {
            dataMap = json
            console.log("1. Data opgehaald: ")
            console.log(dataMap)
            createGraph(symbol)//TODO: dit apart zetten/aanroepen
        })
}


function createGraph(symbol) {
    var dataPoints1 = [], dataPoints2 = [];
    var stockChart = new CanvasJS.StockChart("stockChartContainer", {
        animationEnabled: true,
        theme: "light2",
        title: {
            text: `${symbol} price chart`, //TODO: dit vullen met cryptonaam ipv symbol
            fontFamily: "Calibri,Optima,Arial,sans-serif"
        },
        // subtitles: [{
        //     text: "Crypto price chart",//TODO: deze mogelijk weghalen
        //     fontFamily: "Calibri,Optima,Arial,sans-serif"
        // }],
        charts: [{
            toolTip: {
                shared: true
            },
            axisX: {
                valueFormatString: "D MMM YYYY"
            },
            axisY: {
                prefix: "USD "
            },
            data: [{
                name: "Min-Max",
                type: "rangeArea",
                xValueFormatString: "DD-MM-YYY",
                yValueFormatString: "$#,###.##",
                dataPoints: dataPoints1
            }, {
                name: "Average",
                type: "line",
                yValueFormatString: "$#,###.##",
                dataPoints: dataPoints2
            }]
        }],
        navigator: {
            data: [{
                dataPoints: dataPoints2
            }],
            axisX: {
                labelFontColor: "transparent",
                labelFontWeight: "bolder",
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
    console.log("Pushmethode runt. 2. Data bestaat uit:")
    console.log(dataMap)
    for (let date of Object.keys(dataMap)) {
        dataPoints1.push({x: new Date(date), y:[Number(dataMap[date].min), Number(dataMap[date].max)]});
        dataPoints2.push({x: new Date(date), y:Number(dataMap[date].avg)});
    }


// $.getJSON("https://canvasjs.com/data/gallery/stock-chart/weather-india.json", function (dummyData) {
//     console.log("pushmethode runt")
//     for (var i = 0; i < dummyData.length; i++) {
//         dataPoints1.push({x: new Date(dummyData[i].year, 0o0, 0o0), y: [Number(dummyData[i].min), Number(dummyData[i].max)]});
//         dataPoints2.push({x: new Date(dummyData[i].year, 0o0, 0o0), y: Number(dummyData[i].avg)});
//     }
//     stockChart.render();
// })

    stockChart.render();
}