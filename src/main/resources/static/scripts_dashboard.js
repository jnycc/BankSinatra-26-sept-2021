
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