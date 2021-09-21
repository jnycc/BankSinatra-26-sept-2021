//Page elements
const contentFeature = document.querySelector(".contentFeature");

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
    $(cryptoOverlay).hide()
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
                row.addEventListener('click', () => openDetails(asset.crypto.symbol, asset.crypto.name))
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

function openDetails(symbol, name) {
    $(cryptoName).text(name + " (" + symbol + ")")
    $(cryptoOverlay).show()
}

