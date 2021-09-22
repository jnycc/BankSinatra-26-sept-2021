

// checking valid token (with userrole) and loading data)
window.addEventListener("DOMContentLoaded", validateClient)



window.addEventListener("DOMContentLoaded", getBalance)
window.addEventListener("DOMContentLoaded", getPortfolio)


const balance = document.querySelector("#balanceValue")
const portfolioValue = document.querySelector("#portfolioValue")
const nav1 = document.querySelector("#FS1")
const nav2 = document.querySelector("#FS2")
const nav3 = document.querySelector("#FS3")

nav1.addEventListener("click", function() {
    document.location.href ="marketplace.html";
})



// FUNCTIONS:

function getBalance(){
    fetch(`http://localhost:8080/getBalance`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
         // .then(it => parseFloat("it").toFixed(2))
        .then (it => {
            balance.innerHTML = it
        })
}

// // TODO: totaalwaarde uit Json string halen
function getPortfolio(){
    fetch(`http://localhost:8080/portfolio/totalValue`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    })
        .then(res => res.text())
        .then(it => {
            portfolioValue.innerHTML = it
            console.log(it)
        })
}