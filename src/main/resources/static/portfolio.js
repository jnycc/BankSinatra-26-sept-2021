//Page elements
const contentFeature = document.querySelector(".contentFeature");
const marketBtn = document.querySelector("#market");
const sellBtn = document.querySelector("#sell");
let cryptoChosen;
let availableUnits;
let unitsForSale;
let salePrice;
let featureContentIsFilled = false;
let confirmBtn;
let unitsToSellToBank;
let unitsToSellToBankInput = document.querySelector("#unitsToSellToBank");
let url = new URL(window.location.href);
marketBtn.addEventListener('click', setUpMarketAsset);
sellBtn.addEventListener('click', setupSellAsset);


//Total values
const totalBalance = document.getElementById('totalBalance')
const totalPortfolioValue = document.getElementById('totalPortfolioValue')
const currencyFormat = {style: "currency", currency: "USD", minimumFractionDigits: 2}

//Create table with header row
const assetTable = document.getElementById('assetTable')
$(assetTable).append("<tr><th>Crypto</th> <th>Symbol</th> <th>Units</th> <th>Price</th> <th>Value</th> <th>24h %</th></tr>")

//Modal - overlay with crypto statistics
const cryptoOverlay = document.getElementById('cryptoOverlay')
const closeCryptoOverlay = document.getElementsByClassName('closeCryptoOverlay')[0]
closeCryptoOverlay.addEventListener('click', () => {
    $(cryptoOverlay).hide();
    $(contentFeature).empty();
    featureContentIsFilled = false;
})
window.onclick = function (event) {
    if (event.target === cryptoOverlay) {
        cryptoOverlay.style.display = "none"
    }
}
const cryptoName = document.getElementById('cryptoName')

//Load total portfolio values
window.addEventListener("DOMContentLoaded", () => {
    getBalance();
    getTotalPortfolioValue();
    getAssets();
})

function getBalance() {
    fetch(`/getBalance`, {
        method: 'POST',
        body: localStorage.getItem('token')
    })
        .then(res => res.text())
        .then(it => {
            totalBalance.innerHTML += it
        })
}

function getTotalPortfolioValue() {
    fetch('/portfolio/totalPortfolioValue', {
        method: 'GET',
        headers: {'Authorization': localStorage.getItem('token')}
    })
        .then(res => res.text())
        .then(value => {
            totalPortfolioValue.innerHTML += value
        })
}

function getAssets() {
    fetch('/portfolio/assets', {
        method: 'GET',
        headers: {'Authorization': localStorage.getItem('token')}
    })
        .then(res => res.json())
        .then(json => {
            let nrOfCells = assetTable.rows[0].cells.length - 1
            //For every asset, create a row
            for (let asset of json) {
                const row = document.createElement('tr')
                row.id = asset.crypto.symbol
                row.addEventListener('click', () => openDetails(asset.crypto.symbol, asset.crypto.name, asset.units.toFixed(2)))
                assetTable.appendChild(row)
                //Prepare the required data-cells
                let cells = []
                for (let i = 0; i < nrOfCells; i++) {
                    cells.push(document.createElement('td'))
                }
                cells[0].append(getCryptoLogo(asset.crypto.symbol), asset.crypto.name)
                cells[1].innerHTML = asset.crypto.symbol
                cells[2].innerHTML = asset.units.toLocaleString("en-US", {style: 'decimal', minimumFractionDigits: 2})
                cells[3].innerText = asset.crypto.cryptoPrice.toLocaleString('en-US', currencyFormat)
                cells[4].innerHTML = asset.currentValue.toLocaleString('en-US', currencyFormat)
                // row.append(cells[0], cells[1], cells[2], cells[3], cells[4])
                cells.forEach(cell => row.appendChild(cell))
            }
        })
}

function setAssetValueDeltas() {
    fetch('/assetValueDeltas', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('token'),
            'dateTime': date.toISOString(),
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(json => {
            //TODO:
            // uit sql map van "symbol: delta" krijgen.
            // historische units x historische prijs voor een bepaalde dateTime, voor alle cryptos
        })

}

function getCryptoLogo(symbol) {
    let logo = document.createElement('img')
    logo.src = `/images/cryptoLogos/logo_${symbol}.png`
    logo.classList.add('cryptoLogo')
    return logo
}

function openDetails(symbol, name, units) {
    $(cryptoName).text(name + " (" + symbol + ")")
    $(cryptoOverlay).show()
    // TODO toon crypto grafiek in contentFeature div
    cryptoChosen = symbol;
    availableUnits = units;
}

function setUpMarketAsset() {
    if (!featureContentIsFilled) {
        const table = $('<table class="marketTable" style="margin-left: auto; margin-right: auto"></table>');
        $(table).append("<tr><th>Available Units</th><th>Units for sale</th><th>Price per unit</th></tr>");
        const tr = document.createElement("tr");
        const units = document.createElement("td");
        units.id = "unitsAvailable";
        units.innerText = `${availableUnits}`;
        tr.append(units);
        $(tr).append(`<td><input id='unitsToSell' type='number' min='0'></td>`);
        $(tr).append("<td>$<input id='pricePerUnit' type='number' min='0'></td>");
        $(table).append(tr);
        $(contentFeature).append(table).append('<br>').append(`<button id="confirm" class="market" onclick="marketAsset()">Confirm</button>`);
        featureContentIsFilled = true;
    }
}

function marketAsset() {
    unitsForSale = $("#unitsToSell").val();
    salePrice = $("#pricePerUnit").val();

    if (!(unitsForSale > 0)) {
        alert("Units for sale must be larger than 0");
        return;
    } else if (!(salePrice > 0)) {
        alert("Price per unit must be larger than 0");
        return;
    }

    let payload = {
        crypto: {
            symbol: cryptoChosen
        },
        units: parseFloat(availableUnits),
        unitsForSale: parseFloat(unitsForSale),
        salePrice: parseFloat(salePrice),
        symbol: cryptoChosen
    }
    fetch(`${url.origin}/marketAsset`, {
        method: 'PUT',
        headers: {
            'Authorization': localStorage.getItem('token'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    }).then(response => {
        if (response.status === 200) {
            return response.text().then( text => {
                alert(text);
                window.location.replace(`${url.origin}/portfolio.html`);
            });
        }
        return response.text.then(text => {
            alert(text);
        })
    })
}

async function setupSellAsset() {
    const header = $('<h3>Sell your units to Bank Sinatra for their current market value*</h3>')
    const footnote = $('<p>*Bank fees apply, lolz</p>')
    const table = $('<table class="sellTable"></table>')
    const sellBankbtn = $('<button class="sellToBankButton" onclick="sellToBank()">Sell</button>')
    $(table).append("<tr><th>Total units</th><th>Current value</th><th>Units to sell</th></tr>")
    const tr = document.createElement("tr")
    const units = document.createElement("td")
    const value = document.createElement("td")
    units.id = "unitsAvailable"
    value.id = "currentValue"
    units.innerText = availableUnits
    value.innerText = "$" + await getCurrentValue(cryptoChosen)
    tr.append(units)
    tr.append(value)
    $(tr).append("<td><input id='unitsToSellToBank' type='number' max={availableUnits} min='0'></td>")
    unitsToSellToBank = `${$(`#unitsToSellToBank`).text()}`; //TODO: geeft ingevuld int nog niet goed door help
    $(table).append(tr)
    $(contentFeature).append(header, table, footnote, sellBankbtn);
    console.log(getCurrentValue(cryptoChosen))
}

async function getCurrentValue(symbol) {
    let value;
    await fetch(`${url.origin}/latestPrice`, {
        method: 'POST',
        headers: {"Authorization": `${localStorage.getItem('token')}`},
        body: symbol
    }).then(res => res.json())
        .then(data => value = data)
    return value
}

async function sellToBank(){
    let sellerId = await getIdCurrentUser();
    let payload ={
        buyer: 1, //dit werkt
        seller: parseInt(sellerId), //dit werkt ook
        crypto: {
            symbol: cryptoChosen.textContent
        },
        units: parseInt(unitsToSellToBank) //TODO: werkt nog niet
    }
    console.log(payload);

    await fetch(`${url.origin}/buy`, {
        method: 'POST',
        headers: {"Authorization": `${localStorage.getItem('token')}`},
        body: JSON.stringify(payload)
    }).then(res=> {
        if(res.status === 200){
            alert("Yesss sell us your assets, bitch")
        } else {
            return res.text().then(it => alert(it));
        }
    })
}

async function getIdCurrentUser() {
    let userId;
    await fetch(
        `${url.origin}/getUserId`, {
            method: 'POST',
            headers: { "Authorization": `${localStorage.getItem('token')}`}
        }).then(res => {
        if (res.status === 200) {
            return res.text().then(it => { userId = it;})
        } else {
            alert("Not a valid token anymore");
        }
    })
    return userId;
}
