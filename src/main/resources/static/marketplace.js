let url = new URL(window.location.href);
const assets = [];
const cryptoTable = document.getElementById('cryptoTable');
$(cryptoTable).append("<tr><th>#</th><th>Crypto</th> <th>Symbol</th> <th>Price</th> <th>PriceDelta1Day</th></tr>");
const cryptosForSale = $("#cryptosForSale");
window.addEventListener("DOMContentLoaded", setupPageWithCryptos)

//Create table and add the header row
function setupPageWithCryptos() {
    fetch(`/cryptos`,
        {
            method: 'GET',
            headers: { 'Authorization': `${localStorage.getItem('token')}` }
        })
        .then(res => res.json())
        .then(json => {
            // voor elk crypto-object: maak 1 klikbare row, vul hem met meerdere datacellen
            let i = 1;
            for (let obj of json) {
                const row = document.createElement('tr')
                row.id = obj.symbol
                row.onclick = function() {openDetails(obj.symbol)}
                cryptoTable.appendChild(row)
                $(row).append(`<td>${i++}</td>`)
                for (let key of Object.keys(obj)) {
                    const cell = document.createElement('td')
                    if (key === 'name') {
                        cell.append(getCryptoLogo(obj.symbol), obj[key])
                    } else {
                        cell.innerHTML = obj[key]
                    }
                    row.append(cell)
                }
            }
        })
}

function getCryptoLogo(symbol) {
    let logo = document.createElement('img')
    logo.src = `/images/cryptoLogos/logo_${symbol}.png`
    logo.classList.add('cryptoLogo')
    return logo
}

function openDetails(symbol) {
    // alert('My crypto symbol is: ' + symbol + '\nHier komt de overlay met cryptodetails en buy/sell knop.')
    $(cryptoTable).hide();
    showCryptosForSale(symbol);
}


async function showCryptosForSale(symbol) {
    const cryptoSymbol = $("#cryptoSymbol").text(symbol);

    $(cryptosForSale).append("<tr><th>Seller</th><th>Units for sale</th><th>Price per unit</th></tr>");
    $("#cryptoDetails").append(cryptoSymbol).append(cryptosForSale);

    await getCryptosForSale(symbol).then(await fillTable);

}

async function getCryptosForSale(symbol){
    await fetch(`${url.origin}/requestCryptos`,{
        method: 'POST',
        headers: { "Content-Type": "text/plain" ,
            "Authorization": `${localStorage.getItem('token')}`},
        body: symbol
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
    console.log(assets);
    return assets;
}

async function fillTable() {
    for (let i = 0; i < assets.length; i++) {
        const tr = document.createElement("tr")
        tr.id = `tr${i}`
        let asset = Object.assign({}, assets[i])

        for (const key of Object.keys(asset)) {
            const td = document.createElement("td")
            if (key === "account"){
                let accountId = "accountId"
                let account = Object.assign({}, asset[key])
                td.id = `seller${i}`
                td.textContent = await getName(account[accountId])
                tr.append(td)
            } else if (key === "unitsForSale") {
                td.id = `units${i}`
                td.textContent = asset[key].toFixed(2)
                tr.append(td)
            } else if (key === "salePrice") {
                td.id = `price${i}`
                td.textContent = asset[key].toFixed(2)
                tr.append(td)
            }
            $(cryptosForSale).append(tr);
        }
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


