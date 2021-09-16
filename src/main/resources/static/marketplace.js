let url = new URL(window.location.href);
const assets = [];
const cryptoTable = $("#cryptoTable");
setupPage();

// marketplace functions:
async function setupPage() {
    const cryptoSymbol = $("#cryptoSymbol").text("BTC");
    $("#cryptoOverview").append(cryptoSymbol);

    $(cryptoTable).append("<tr><th>Seller</th><th>Units for sale</th><th>Price per unit</th></tr>");
    $("#cryptoOverview").append(cryptoTable);

    await getCryptosForSale().then(await fillTable);

}
async function getCryptosForSale(){
    await fetch(`${url.origin}/requestCryptos`,{
        method: 'POST',
        headers: { "Content-Type": "text/plain" ,
        "Authorization": `${localStorage.getItem('token')}`},
        body: "BTC"
        }).then(res => {
            if (res.status === 200){
                console.log("dit werkt")
            } else if (res.status === 404){
                console.log("hoiiiii")
            }
            return res.json();
        }).then(it => {
            //for loop: for each object, get waarde
            it.forEach(it => assets.push(it))
    })
    return assets;
}

async function fillTable() {
    for (let i = 0; i < assets.length; i++) {
        const tr = document.createElement("tr");
        tr.id = `tr${i + 1}`;
        let asset = Object.assign({}, assets[i]);

        for (const key of Object.keys(asset)) {
            const td = document.createElement("td");
            if (key === "account") {
                let accountId = "accountId";
                let account = Object.assign({}, asset[key])
                td.id = `seller${i++}`;
                td.textContent = await getName(account[accountId]);
                tr.append(td);
            } else if (key === "unitsForSale") {
                td.id = `units${i++}`;
                td.textContent = asset[key].toFixed(2);
                tr.append(td);
            } else if (key === "salePrice") {
                td.id = `price${i++}`;
                td.textContent = asset[key];
                tr.append(td);
            }

        }
        $(cryptoTable)
        $(cryptoTable).append(tr);
    }
}

async function getName(accountId){
    let name;
    await fetch(`${url.origin}/requestName`, {
        method: 'POST',
        headers: { "Content-Type": "text/plain"},
        body: accountId
    }).then(res => res.json())
        .then(data => name = data)
    return name
}

