//Create table and add the header row
const assetTable = document.getElementById('assetTable')
$(assetTable).append("<tr><th>Crypto</th> <th>Symbol</th> <th>Units</th> <th>Price</th> <th>Value</th> <th>Delta</th></tr>")
const totalBalance = document.getElementById('totalBalance')
const totalPortfolioValue = document.getElementById('totalPortfolioValue')
const totalPortfolioDelta = document.getElementById('totalPortfolioDelta')

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
            let nrOfCells = assetTable.rows[0].cells.length
            //For every asset, create a row
            for (let asset of json) {
                const row = document.createElement('tr')
                row.id = asset.crypto.symbol
                row.addEventListener('click', () => openDetails(asset.crypto.symbol))
                assetTable.appendChild(row)
                //Prepare the required data-cells
                let cells = []
                for (let i = 0; i < nrOfCells; i++) {
                    cells.push(document.createElement('td'))
                }
                cells[0].append(getCryptoLogo(asset.crypto.symbol), asset.crypto.name)
                cells[1].innerHTML = asset.crypto.symbol
                cells[2].innerHTML = asset.units
                cells[3].innerText = asset.crypto.cryptoPrice
                cells[4].innerHTML = asset.currentValue
                // row.append(cells[0], cells[1], cells[2], cells[3], cells[4])
                cells.forEach(cell => row.appendChild(cell))
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
    alert('My crypto symbol is: ' + symbol + '\nHier komt de overlay met cryptodetails en buy/sell knop.')
}

