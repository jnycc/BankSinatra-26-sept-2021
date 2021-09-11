// checking valid token (with userrole) and loading data)
window.addEventListener("DOMContentLoaded", validateClient)
window.addEventListener("DOMContentLoaded", getBalance)
//window.addEventListener("DOMContentLoaded", getPortfolio)




// Navigation script aanmaken voor alle navigation functies hieronder???

const balance = document.querySelector("#balanceValue")
const portfolioValue = document.querySelector("#portfolioValue")

// Identifying navigation buttons
const home = document.querySelector("#home")
const settings = document.querySelector("#settings")
const account = document.querySelector("#account")
const portfolio = document.querySelector("#portfolio")
const marketplace = document.querySelector("#transactions")
const logout = document.querySelector("#logout")


// Adding functions to navigation buttons
home.addEventListener("click", goHome)
settings.addEventListener("click", goToSettings)
account.addEventListener("click", goToAccount)
portfolio.addEventListener("click", goToPortfolio)
marketplace.addEventListener("click", goToMarketplace)
logout.addEventListener("click", doLogOut)


// FUNCTIONS:

// specific validation of jwt and userrole
function validateClient(){
    fetch(`http://localhost:8080/validateClient`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => {
            if (res.status === 200) {
                console.log("no problemo")
            } else {
                console.log("your token is bad and you should feel bad")
                window.location.replace("/index.html");
            }
        })
}


function getBalance(){
    fetch(`http://localhost:8080/getBalance`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            balance.innerHTML = it
        })
}

// TODO: Json string ophalen en omzetten in html
// const portfolioData = '{"voorbeelddata":"hierkomt crypto", "date":"1986-12-14", "city":"New York"}';
// const portfolioString = JSON.parse(portfolioData);
// portfolioString.date = new Date(portfolioString.date);
// document.getElementById("portfolioValue").innerHTML = portfolioString.name + ", " + portfolioString.date;



// // TODO: totaalwaarde uit Json string halen
function getPortfolio(){
    fetch(`http://localhost:8080/portfolio`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            portfolioValue.innerHTML = it
        })
}




// function getPortfolioValue(){
//     fetch(`http://localhost:8080/getPortfolio`, {
//         method: 'POST',
//         body: `${localStorage.getItem('token')}`
//     })
//         .then(res => res.text())
//         .then(it => {
//             const portfolio = it
//         })
// }



// NAVIGATION FUNCTIONS
// TODO: naar aparte navigation js file?

function goHome(){
    fetch(`http://localhost:8080/validateClient`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res)
        .then(it => {
            if (it.status === 200) {
                window.location.replace("http://localhost:8080/dashboard.html")
            }
            else {
                console.log("your token is bad and you should feel bad")
                window.location.replace("/index.html");
            }
        })
}


function goToSettings(){
    fetch(`http://localhost:8080/validateClient`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res)
        .then(it => {
            if (it.status === 200) {
                window.location.replace("http://localhost:8080/Settings.html")
            }
            else{
                console.log("your token is bad and you should feel bad")
                window.location.replace("/index.html");
            }
        })
}

function goToAccount(){
    fetch(`http://localhost:8080/validateClient`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res)
        .then(it => {
            if (it.status === 200) {
                window.location.replace("http://localhost:8080/Account.html")
            }
            else {
                console.log("your token is bad and you should feel bad")
                window.location.replace("/index.html");
            }

        })
}

function goToPortfolio(){
    fetch(`http://localhost:8080/validateClient`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res)
        .then(it => {
            if (it.status === 200) {
                window.location.replace("http://localhost:8080/Portfolio.html")
            }
            else {
                console.log("your token is bad and you should feel bad")
                window.location.replace("/index.html");
            }

        })
}


function goToMarketplace(){
        fetch(`http://localhost:8080/validateClient`, {
            method: 'POST',
            body: `${localStorage.getItem('token')}`
        })
            .then(res => res)
            .then(it => {
                if (it.status === 200) {
                    window.location.replace("http://localhost:8080/marketplace.html")
                }
                else {
                    console.log("your token is bad and you should feel bad")
                    window.location.replace("/index.html");
                }
            })
}

function doLogOut(){
    window.localStorage.clear();
    window.location.replace("/index.html");
}


