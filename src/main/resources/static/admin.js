// PAGE SETUP
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

const logout = document.querySelector("#logout")

const btnBankfee = document.querySelector("#bankfee")
const feeForm = document.querySelector("#feeForm")
const btnCloseOverlay = document.querySelector("#close-overlay-btn")
const overlay = document.querySelector("#overlay")

let user = null;
const findUserForm = document.querySelector("#findUserForm")
const userTable = $("#userTable");

const btnToggleBlock = document.querySelector("#toggleBlock");
$(btnToggleBlock).hide()

// LOGOUT
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
    e.preventDefault();
    updateFee();
})

function updateFee(){
    let payload =
        {token: `${localStorage.getItem('token')}`,
            fee: `${document.querySelector("#fee-input").value}`}
    console.log(payload);
    fetch(`http://localhost:8080/admin/updateFee`,
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

// LOAD USER
findUserForm.addEventListener('submit', function (e) {
    e.preventDefault();
    user = document.querySelector("#email-input").value;
    loadUser(user);
})

function loadUser(user){
    if ($('#userTable tr').length !== 0) {
        userTable.empty();
    }
    fetch(`http://localhost:8080/admin/getUserData?email=${user}`,
        {
            method: 'GET',
            headers: { "Authorization": localStorage.getItem('token') }
        })
        .then(res => res.json())
        .then(it => {                        // TODO: dit moet mooier en met minder complexiteit kunnen
            for (const key in it) {          // loop through all properties of the imported user json
                if (it[key] !== null) {
                    if (key === "address") { // "address" contains properties of its own, so needs to be looped through separately
                        let obj = it["address"];
                        for (const key in obj) {
                            $(userTable).append("<tr><th>" + key + "</th><th>" + obj[key] + "</th></tr>");
                        }
                    } else {
                        $(userTable).append("<tr><th>" + key + "</th><th>" + it[key] + "</th></tr>");
                    }
                }
            }
            $("#userData").append(userTable)
            $(btnToggleBlock).show()
        })
}

// BLOCK/UNBLOCK USER
btnToggleBlock.addEventListener("click", function() {
    blockUser(user)
})

function blockUser(user) {
    fetch(`http://localhost:8080/admin/toggleBlock?email=${user}`,
        {
            method: 'POST',
            headers: { "Authorization": localStorage.getItem('token') }
        }).then(res => {
        if (res.status === 200) {
            loadUser(user) // reload user user after block is toggled to update info
            console.log("Block status changed.")
        } else {
            res.json().then(it => {
                alert(it.message);
            })
        }
    })
}