// //JSON-object met daarin een map in een map: "datum: {avg: value, min: value, max: value}" klaarzetten
// let dataMap = null
// // window.onload = getAssetStats('BTC', 7)
// //TODO: eventlistener veranderen in: als overlay opent (functie openDetails) -> creëer graph
//
// window.addEventListener("DOMContentLoaded", async () => {
//     // await getAssetStats('BTC', 365)
//     createGraph('BTC', 300)
// })
//
// async function getAssetStats(symbol, daysBack) {
//     await fetch(`/cryptoStats?symbol=${symbol}&daysBack=${daysBack}`, {//vullen met endpoint
//         method: 'GET',
//         headers: {'Authorization': localStorage.getItem('token')}
//     })
//         .then(res => res.json())
//         .then(json => {
//             dataMap = json
//             console.log("1. Data opgehaald: ")
//             console.log(dataMap)
//         })
// }
//
// async function createGraph(symbol, daysBack) {
//     await getAssetStats(symbol, daysBack)
//     var dataPoints1 = [], dataPoints2 = [];
//     var stockChart = new CanvasJS.StockChart("stockChartContainer", {
//         animationEnabled: true,
//         theme: "light2",
//         title: {
//             text: `${symbol} price chart`, //TODO: dit vullen met cryptonaam ipv symbol
//             fontFamily: "Calibri,Optima,Arial,sans-serif"
//         },
//         charts: [{
//             toolTip: {
//                 shared: true
//             },
//             axisX: {
//                 valueFormatString: "D MMM YYYY"
//             },
//             axisY: {
//                 prefix: "USD "
//             },
//             data: [{
//                 name: "Min-Max",
//                 type: "rangeArea",
//                 xValueFormatString: "DD-MM-YYY",
//                 yValueFormatString: "$#,###.##",
//                 dataPoints: dataPoints1
//             }, {
//                 name: "Average",
//                 type: "line",
//                 yValueFormatString: "$#,###.##",
//                 dataPoints: dataPoints2
//             }]
//         }],
//         navigator: {
//             data: [{
//                 dataPoints: dataPoints2
//             }],
//             axisX: {
//                 labelFontColor: "transparent",
//                 labelFontWeight: "bolder",
//             },
//             slider: {
//                 minimum: new Date(2020, 0o0),
//                 maximum: new Date(2021, 12)
//             }
//         },
//         rangeSelector: {
//             buttons: [{
//                 label: "1D",
//                 range: 1,
//                 rangeType: "day"
//             }, {
//                 label: "7D",
//                 range: 7,
//                 rangeType: "day"
//             }, {
//                 label: "1Y",
//                 range: 1,
//                 rangeType: "year"
//             }, {
//                 label: "All",
//                 range: null,
//                 rangeType: "all"
//             }]
//         }
//     });
//     console.log("Pushmethode runt. 2. Data bestaat uit:")
//     console.log(dataMap)
//     for (let date of Object.keys(dataMap)) {
//         dataPoints1.push({x: new Date(date), y:[Number(dataMap[date].min), Number(dataMap[date].max)]});
//         dataPoints2.push({x: new Date(date), y:Number(dataMap[date].avg)});
//     }
//     stockChart.render();
// }