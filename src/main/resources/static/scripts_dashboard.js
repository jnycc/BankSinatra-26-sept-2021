const settings = document.querySelector("#settings")
settings.addEventListener("click", authenticate)

function authenticate() {
    fetch(`http://localhost:8080/authenticate`, {
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


// document.getElementById("balanceValue").innerHTML = getBalance();


//TODO: omzetten in return statement?
function getBalance(){
    fetch(`http://localhost:8080/getBalance`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            balancevalue.innerHTML = it
        })
}

// const portfolioData = '{"voorbeelddata":"hierkomt crypto", "date":"1986-12-14", "city":"New York"}';
// const portfolioString = JSON.parse(portfolioData);
// portfolioString.date = new Date(portfolioString.date);
// document.getElementById("portfolioValue").innerHTML = portfolioString.name + ", " + portfolioString.date;
//
// //TODO: omzetten in return statement?
// const portfolioValue = document.querySelector("#portfolioValue")
// portfolioValue.addEventListener("click", getPortfolioValue)
//
// function getPortfolioValue(){
//     fetch(`http://localhost:8080/portfolio`, {
//         method: 'POST',
//         body: `${localStorage.getItem('token')}`
//     })
//         .then(res => res.text())
//         .then(it => {
//             portfolioValue.innerHTML = it
//         })
// }
//
// // Uitprobeersel met hele portfolio opphalen als json string
// //TODO: hoe door te geven aan html/forntend??? via const??
// function getPortfolio(){
//     fetch(`http://localhost:8080/getPortfolio`, {
//         method: 'POST',
//         body: `${localStorage.getItem('token')}`
//     })
//         .then(res => res.text())
//         .then(it => {
//             const portfolio = it
//         })
// }




// Navigation script aanmaken voor alle navigation functies hieronder???

const home = document.querySelector("#home")
// const settings = document.querySelector("#settings")
const account = document.querySelector("#account")
const portfolio = document.querySelector("#portfolio")
const marketplace = document.querySelector("#transactions")
const logout = document.querySelector("#logout")


home.addEventListener("click", goHome)
// settings.addEventListener("click", goToSettings)
account.addEventListener("click", goToAccount)
portfolio.addEventListener("click", goToPortfolio)
marketplace.addEventListener("click", goToMarketplace)
// logout.addEventListener("click", doLogout)


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
        })
}


function goToSettings(){
}

function goToAccount(){
}

function goToPortfolio(){
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
            })
}


