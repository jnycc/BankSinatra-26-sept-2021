//Page elements
const contentFeature = document.querySelector(".contentFeature");
const marketBtn = document.querySelector("#market");
let cryptoChosen;
let availableUnits;
let unitsForSale;
let salePrice;
let featureContentIsFilled = false;
let confirmBtn;
let url = new URL(window.location.href);
marketBtn.addEventListener('click', setUpMarketAsset);

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
