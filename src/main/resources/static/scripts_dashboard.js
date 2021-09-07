
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
balancevalue.addEventListener("click", getBalance)

function getEmail(){
    fetch(`http://localhost:8080/getEmail`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            // if (it === null) {
            //     console.log('no email is found')
            // } else {
            //     console.log(it)
            // }
            balancevalue.innerHTML = it
        })
}

function getBalance(){
    fetch(`http://localhost:8080/getBalance`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            // if (it === null) {
            //     console.log('no email is found')
            // } else {
            //     console.log(it)
            // }
            balancevalue.innerHTML = it
        })
}