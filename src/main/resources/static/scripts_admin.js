// AUTHENTICATION
window.addEventListener("DOMContentLoaded", validateAdmin)

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

// HEADER
const bankfee = document.querySelector("#bankfee")
const logout = document.querySelector("#logout")

bankfee.addEventListener("click", function() {
    window.alert("click") // functionaliteit hier
})

logout.addEventListener("click", function() {
    window.localStorage.clear();
    window.location.replace("/index.html");
})

