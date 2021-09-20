//Create table and add the header row
const cryptoTable = document.getElementById('cryptoTabel')
$(cryptoTable).append("<tr><th>#</th><th>Crypto</th> <th>Symbol</th> <th>Price</th> <th>PriceDelta1Day</th></tr>")
const deltas = document.getElementById('deltas')

window.addEventListener("DOMContentLoaded", getAvailableCryptos)
window.addEventListener("DOMContentLoaded", getPriceDeltas)

function getAvailableCryptos() {
    fetch(`/cryptos`, {
        method: 'GET',
        headers: {'Authorization': localStorage.getItem('token')}
    })
        .then(res => res.json())
        .then(json => {
            // voor elk crypto-object: maak 1 klikbare row, vul hem met meerdere datacellen
            let i = 1
            for (let obj of json) {
                const row = document.createElement('tr')
                row.id = obj.symbol
                row.addEventListener('click', () => openDetails(obj.symbol))
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

function getPriceDeltas() {
    let yesterday = new Date()
    yesterday.setDate(yesterday.getDate() - 1)
    console.log(yesterday.toISOString())
    fetch('/priceDeltas', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('token'),
            'dateTime': `${yesterday.toISOString()}`,
            'Content-Type': 'application/json'
        },
    })
        .then(res => res.text())
        .then(it => {
            //it is een map met "symbol: delta"
            // voor elk crypto-item in de map: maak 1 td-cell, zet innerHTML value op de delta-waarde.
            // Append de cell aan de bestaande row.
            // Hiervoor cryptoTabel.rows gebruiken en door elke row loopen
            console.log("resultaat:" + it)
            deltas.innerHTML = it
        })
}


function openDetails(symbol) {
    alert('My crypto symbol is: ' + symbol + '\nHier komt de overlay met cryptodetails en buy/sell knop.')
}
