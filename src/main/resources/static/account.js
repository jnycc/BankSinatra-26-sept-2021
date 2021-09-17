let url = new URL(window.location.href);
const transactionsBuyer = []
const transactionsSeller = []
const transactionTableBuyer = document.getElementById("transactionTableBuyer")
const transactionTableSeller = document.getElementById("transactionTableSeller")
const buyer = "buyer"
const seller = "seller"
$(transactionTableBuyer).append("<tr><th>Date</th> <th>Seller</th> <th>Purchase</th> <th>Price</th> <th>Bank fee</th>")
$(transactionTableSeller).append("<tr><th>Date</th> <th>Buyer</th> <th>Purchase</th> <th>Price</th> <th>Bank fee</th>")

window.addEventListener("DOMContentLoaded", setupPage)

async function setupPage(){
    await getAccount()
    await getTransactionsBuyer()
    await getTransactionsSeller()
    await fillTable(transactionsBuyer, transactionTableBuyer, seller)
    await fillTable(transactionsSeller, transactionTableSeller, buyer)
}

async function getAccount(){
    fetch(`${url.origin}/getAccount`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res=> res.json())
        .then(async it => {
            for (const key of Object.keys(it)) {
                if (key === "iban") {
                    let iban = it[key]
                    $("#iban").append(iban);
                } else if (key === "accountId") {
                    $("#name").append(await getName(it[key]))
                } else if (key === "balance") {
                    $("#balance").append(it[key])
                }
            }
        })
}

async function getTransactionsBuyer(){
    await fetch(`${url.origin}/transactionsBuyer`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res=> {
        if (res.status === 200){
            console.log("dit werkt")
        } else if (res.status === 404){
            console.log("hoiiiii")
        }
        return res.json();
    }).then(it =>{
            it.forEach(it => transactionsBuyer.push(it))
        })
    return transactionsBuyer
}

async function getTransactionsSeller(){
    await fetch(`${url.origin}/transactionsSeller`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res=> {
        if (res.status === 200){
            console.log("dit werkt")
        } else if (res.status === 404){
            console.log("hoiiiii")
        }
        return res.json();
    }).then(it =>{
        it.forEach(it => transactionsSeller.push(it))
    })
    return transactionsSeller
}

async function fillTable(array, table, transactionParty) {
    for (let i = 0; i < array.length; i++) {
        const tr = document.createElement("tr")
        tr.id = `tr${i}`
        let transaction = Object.assign({}, array[i])
        let symbol = getSymbol(transaction)

        for (const key of Object.keys(transaction)) {
            const td = document.createElement("td")
            if (key === "transactionDate") {
                td.id = `date${i}`
                let date = new Date(transaction[key])
                td.textContent = date.toLocaleString()
                tr.append(td)
            } else if (key === transactionParty) {
                td.id = `transactionParty${i}`
                td.textContent = await getName(transaction[key])
                tr.append(td)
            } else if (key === "units") {
                td.id = `purchase${i}`
                td.textContent = transaction[key].toFixed(2).concat(" " + symbol)
                tr.append(td)
            } else if (key === "transactionPrice") {
                td.id = `price${i}`
                td.textContent = "$" + transaction[key].toFixed(2)
                tr.append(td)
            } else if (key === "bankCosts") {
                td.id = `fee${i}`
                td.textContent = "$" + transaction[key].toFixed(2)
                tr.append(td)
            }
            $(table).append(tr);
        }
    }
}

function getSymbol(transaction){
    for (const key of Object.keys(transaction)){
        if(!(key === "crypto")){
        } else {
            let symbol = "symbol"
            let crypto = Object.assign({}, transaction[key])
            return crypto[symbol]
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
