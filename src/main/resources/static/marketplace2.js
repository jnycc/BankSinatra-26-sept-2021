//Create table and add the header row
const cryptoTable = document.getElementById('cryptoTabel')
$(cryptoTable).append("<tr><th>#</th><th>Crypto</th> <th>Symbol</th> <th>Price</th> <th>PriceDelta1Day</th></tr>")

window.addEventListener("DOMContentLoaded", getAvailableCryptos)

function getAvailableCryptos() {
    fetch(`/cryptos`,
        {
            method: 'GET',
            headers: { 'Authorization': `${localStorage.getItem('token')}` }
        })
        .then(res => res.json())
        .then(json => {
            // voor elk crypto-object: maak 1 klikbare row, vul hem met meerdere datacellen
            let i = 1
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
    alert('My crypto symbol is: ' + symbol + '\nHier komt de overlay met cryptodetails en buy/sell knop.')
}
