
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


const balancevalue = document.querySelector("#balanceValue")

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

//TODO: omzetten in return statement?
const portfolioValue = document.querySelector("#portfolioValue")
portfolioValue.addEventListener("click", getPortfolioValue)

function getPortfolioValue(){
    fetch(`http://localhost:8080/getPortfolioValue`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            portfolioValue.innerHTML = it
        })
}