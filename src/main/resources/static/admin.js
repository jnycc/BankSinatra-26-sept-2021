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

// BASIS
const btnBankfee = document.querySelector("#bankfee")
const feeForm = document.querySelector("#feeForm")
const logout = document.querySelector("#logout")
const btnCloseOverlay = document.querySelector("#close-overlay-btn")
const overlay = document.querySelector("#overlay")

logout.addEventListener("click", function() {
    window.localStorage.clear();
    window.location.replace("/index.html");
})

// CHANGE BANK FEE
$(document).ready(function (){
    $(btnBankfee).click(function (){
        $(overlay).show();
    });

    $(btnCloseOverlay).click(function (){
        $(overlay).hide();
        $(feeForm).trigger("reset");
    })
});

feeForm.addEventListener('submit', function (e) {
    // TODO: wat doet preventdefault?
    e.preventDefault();
    updateFee();
})

function updateFee(){
    let payload =
        {token: `${localStorage.getItem('token')}`,
            fee: `${document.querySelector("#fee-input").value}`}
    console.log(payload);
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
