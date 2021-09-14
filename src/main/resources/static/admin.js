// AUTHENTICATION
window.addEventListener("DOMContentLoaded", validateAdmin)

function validateAdmin(){
    fetch(`http://localhost:8080/validateAdmin`, {
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
    updateFee();
})

logout.addEventListener("click", function() {
    window.localStorage.clear();
    window.location.replace("/index.html");
})

function updateFee(){
    let payload =
        {token: `${localStorage.getItem('token')}`,
            fee: `0.02`}

    fetch(`http://localhost:8080/updateFee`,
        {
            method: 'PUT',
            header: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        })
        .then(res => {
        if (res.status === 200) {
            window.alert("yay")
        } else {
            res.json().then(it => {
                window.alert("update failed")
            })
        }
    })
}

