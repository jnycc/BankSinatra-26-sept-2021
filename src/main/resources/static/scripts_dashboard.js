
const settings = document.querySelector("#settings")

settings.addEventListener("click", authenticate)

function authenticate() {
    fetch(`http://localhost:8080/authenticate`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            if (it === "true") {
                console.log('yay')
            } else {
                console.log('nay')
            }
        })
        .catch(console.log('oeps'))
}

const emailExample = document.querySelector("#emailExample")

emailExample.appendChild(getEmail());

function getEmail(){
    fetch(`http://localhost:8080/getEmail`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => res.text())
        .then(it => {
            if (it === "") {
                console.log('nay')
            } else {
                console.log(it)
            }
        })
        .catch(console.log('oeps'))
    return it;
}