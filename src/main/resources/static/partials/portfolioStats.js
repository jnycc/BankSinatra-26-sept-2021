var portMap = null

window.addEventListener("DOMContentLoaded", async () => {
    await getPortfolioStats()
    createPortGraph()
})



async function getPortfolioStats(){
    await fetch(`http://localhost:8080/portfolioStats`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    })
        .then(res => res.json())
        .then (it => {
            portMap = it
            console.log(it)
            //createPortGraph()
        })
}

function createPortGraph() {
    var portDataPoints = []
    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: false,
        //backgroundColor: "rgb(2,34,41)",
        backgroundColor: "transparent",
        toolTip: {
            fontFamily: "tahoma",
            fontColor: "rgb(24,20,1)",
            cornerRadius: 30,
            borderColor: "rgb(81,123,117)",
            backgroundColor: "rgb(81,123,117, .5)",
            contentFormatter: function ( e ) {
                return "$" +  e.entries[0].dataPoint.y;
            },
        },
        toolTipContent: "{y}",
        title:{
            text: "Total portfolio value",
            fontFamily: "tahoma",
            fontColor: "rgb(245,232,204)",
        },
        axisY: {
            valueFormatString: "#,###.##",
            // suffix: ".-",
            prefix: "$",
            labelFontColor: "rgb(24,20,1)",
            labelFontFamily: "tahoma",
            labelFontSize: 12,
            gridColor: "rgb(24,20,1)",
            tickLength: 0,
            lineThickness: 0,
        },
        axisX: {
            valueFormatString: "DD-MM-YY",
            labelAngle: -60,
            interval: 1,
            intervalType: "week",
            labelFontColor: "rgb(24,20,1)",
            labelFontFamily: "tahoma",
            labelFontSize: 12,
            tickLength: 0,
            lineColor: "rgb(24,20,1)",
        },
        data: [{
            type: "area",
            //color: "rgba(81,123,117,.7)",
            color: "rgba(255,165,0,0.6)",
            markerSize: 5,
            //xValueType: "DD-MM-YY",
            xValueFormatString: "DD-MM-YY",
            yValueFormatString: "$#,###.##",
            dataPoints: portDataPoints
        }],
    })
    // for (let key of Object.keys(portMap)) {
    //     portDataPoints.push({
    //         x: new Date(key),
    //         y: Number(portMap[key])
    //     })
    // }
    for (let [key, value] of Object.entries(portMap)) {
        portDataPoints.push({
            x: new Date(key),
            y: Number(value)
        })
    }

    chart.render();

}// end