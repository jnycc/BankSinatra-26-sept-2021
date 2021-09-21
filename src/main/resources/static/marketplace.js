let url = new URL(window.location.href);
const assets = [];
const cryptoTable = document.getElementById('cryptoTable');
const currencyFormat = {style: "currency", currency: "USD", minimumFractionDigits: 2}
const cryptosForSaleDiv = document.querySelector('#cryptoDetails');
const cryptoSymbol = document.querySelector("#cryptoSymbol");
const cryptoBuy = $("#cryptoBuy");
const orderForm = document.querySelector("#orderForm");
const buyBtn = document.querySelector("#buy");
const cryptoOverlay = $("#cryptoOverlay");
const cryptosForSale = $("#cryptosForSale");
const purchase = document.querySelector("#purchase");
const cryptoName = $("#cryptoName");
const closeCryptoOverlayBtn = document.querySelector(".closeCryptoOverlay");
let unitsToBuy;
let buyerId;
let purchasePrice;
let cryptoChosen;
let totalPrice = document.querySelector("#totalPrice");
let unitsToBuyInput = document.querySelector("#unitsToBuy");
let isOrderFormEmpty = true;
$(cryptoTable).append("<tr><th>#</th><th>Cryptocurrency</th> <th>Symbol</th> <th>Price</th> <th>Last price update</th><th>24h %</th> <th>7d %</th></tr>");
window.addEventListener("DOMContentLoaded", setupPageWithCryptos);
purchase.addEventListener('click', carryOutTransaction);
buyBtn.addEventListener('click', () => {
    $(cryptoOverlay).hide();
    showCryptosForSale(cryptoChosen);
})
closeCryptoOverlayBtn.addEventListener('click', () => {
    $(cryptoOverlay).hide();
})

//Create table and add the crypto data
function setupPageWithCryptos() {
    $(cryptosForSaleDiv).hide();
    fetch(`/cryptoOverview`,
        {
            method: 'GET',
            headers: { 'Authorization': localStorage.getItem('token') }
        })
        .then(res => res.json())
        .then(json => {
            // voor elk crypto-object: maak 1 klikbare row, vul hem met meerdere datacellen
            let i = 1;
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
                    } else if (key === 'dateRetrieved'){
                        let date = new Date(obj[key]);
                        cell.innerHTML = date.toLocaleString();
                    } else if (key === 'cryptoPrice'){
                        cell.innerHTML = obj.cryptoPrice.toLocaleString("en-US", currencyFormat);
                    } else {
                        cell.innerHTML = obj[key]
                    }
                    row.append(cell)
                }
            }
            let yesterday = new Date()
            yesterday.setDate(yesterday.getDate() - 1)
            setPriceDeltas(yesterday)
            let oneWeekAgo = new Date()
            oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
            setPriceDeltas(oneWeekAgo)
        })
}

function getCryptoLogo(symbol) {
    let logo = document.createElement('img')
    logo.src = `/images/cryptoLogos/logo_${symbol}.png`
    logo.classList.add('cryptoLogo')
    return logo
}

/**
 * Obtains the delta of the current price vs. the price on the selected date of all crypto values.
 * Consequently sets them in the table in the market place page.
 * @param date: the selected date against which a price-delta value is to be calculated.
 */
function setPriceDeltas(date) {
    fetch('/priceDeltas', {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('token'),
            'dateTime': date.toISOString(),
            'Content-Type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(json => {
            // json-object is een map met crypto's met key-values: "symbol: delta%".
            // Voor elk crypto-item in de map: maak 1 td-cell, zet innerHTML value op de delta-waarde. Append de cell aan de bestaande table rows.
            const rows = cryptoTable.rows
            let i = 1 //start vanaf table row 1 (0 = table header)
            for (let key of Object.keys(json)) {
                const cell = document.createElement('td');
                let arrowSrc = json[key] >= 0 ? `images/arrow_up.png` : `images/arrow_down.png`;
                let img = $('<img height="20px" width="10px" style="mix-blend-mode: multiply;">');
                img.attr('src', arrowSrc);
                cell.append(img[0]);
                cell.append(`${json[key].toFixed(2) + '%'}`);
                rows[i].appendChild(cell)
                i++
            }
        })
}

function openDetails(symbol) {
    cryptoChosen = symbol;
    $(cryptoName).text(cryptoChosen);
    $(cryptoOverlay).show();
}

async function showCryptosForSale(symbol) {
    $(cryptoTable).hide();
    const cryptoSymbol = $("#cryptoSymbol").text(symbol);
    cryptoSymbol.css('display', 'inline');
    $("#cryptoImg").attr('src',`/images/cryptoLogos/logo_${symbol}.png`);
    $(cryptosForSaleDiv).show();
    $(cryptosForSale).show();
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
            return res.json();
        } else if (res.status === 404){
            console.log("error")
            return;
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
        let asset = Object.assign({}, assets[i])

        for (const key of Object.keys(asset)) {
            const td = document.createElement("td")
            if (key === "accountId"){
                let accountId = asset[key]
                td.id = `sellerRow${i}`;
                td.textContent = await getName(accountId);
                tr.id = `seller${accountId}`;
                tr.append(td);
                tr.onclick = function () {showOrder(accountId)}
            } else if (key === "unitsForSale") {
                td.id = `units${asset["accountId"]}`;
                td.textContent = asset[key].toFixed(2)
                tr.append(td)
            } else if (key === "salePrice") {
                td.id = `price${asset["accountId"]}`;
                td.textContent = asset[key].toFixed(2);
                tr.append(td);
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

async function showOrder(accountId) {
    $(cryptoBuy).show();

    unitsToBuy = `${$(`#units${accountId}`).text()}`;
    purchasePrice = $(`#price${accountId}`).text();

    if (isOrderFormEmpty) {
        $("#cryptoCoinToBuy").text(cryptoSymbol.innerText);
        $(unitsToBuyInput).attr('max', unitsToBuy);
        $("#pricePerUnit").text(purchasePrice);
        $(totalPrice).text(`0`);
        $(purchase).attr('buyerId', accountId);
        isOrderFormEmpty = false;
    }

}

$(unitsToBuyInput).bind('keyup mouseup', updateTotalPrice);

function updateTotalPrice() {
    $(totalPrice).text(`${$(unitsToBuyInput).val() * $("#pricePerUnit").text()}`);
}

async function carryOutTransaction() {
    buyerId = await getIdCurrentUser();
    let payload = {
        buyer: parseInt(buyerId),
        seller : parseInt($(purchase).attr('buyerId')),
        crypto: {
        symbol: cryptoSymbol.textContent
        },
        units: parseInt($(unitsToBuyInput).val())
    }
    console.log(payload);

    await fetch(`${url.origin}/buy`, {
        method: 'POST',
        headers: { "Authorization": `${localStorage.getItem('token')}`},
        body: JSON.stringify(payload)
    }).then(res => {
        if (res.status === 200) {
            alert("Transaction worked, thank you for your purchase! You will now be redirected to the Marketplace");
            cryptosForSale.hide();
            cryptoBuy.hide();
            window.location.replace(`${url.origin}/marketplace.html`);
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