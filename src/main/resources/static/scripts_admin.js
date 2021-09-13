// checking valid token (with userrole) and loading data)
window.addEventListener("DOMContentLoaded", validateAdmin)




// FUNCTIONS
function validateAdmin(){
    fetch(`http://localhost:8080/validateAdmin`, { // TODO: ENDPOINT BESTAAT NOG NIET
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