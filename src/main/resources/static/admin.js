// PAGE SETUP
window.addEventListener("DOMContentLoaded", validateAdmin)

function validateAdmin(){
    fetch(`http://localhost:8080/validateAdmin`, {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => {
            if (res.status === 200) {
                console.log("Admin validated")
            } else {
                console.log("Admin validation failed, redirecting to login page")
                window.location.replace("/index.html")
            }
        })
}

let url = new URL(window.location.href);

const logout = document.querySelector("#logout")

const btnBankfee = document.querySelector("#bankfee")
const feeForm = document.querySelector("#feeForm")
const btnCloseOverlay = document.querySelector("#close-overlay-btn")
const overlay = document.querySelector("#overlay")

let user = null
const findUserForm = document.querySelector("#findUserForm")
const userTable = $("#userTable")
const btnToggleBlock = document.querySelector("#toggleBlock")
$(btnToggleBlock).hide()

const portfolioData = $("#portfolioData")
const assetTable = $("#assetTable")
let assets = {}
const btnSubmitChanges = document.querySelector("#submitChanges")



// CONFIRMATION PROMPT
function confirmationPrompt(action) {
    return confirm(`Are you sure you want to ${action}?`)
}

// LOGOUT
logout.addEventListener("click", function() {
    if (confirmationPrompt("logout")) {
        window.localStorage.clear();
        window.location.replace("/index.html");
    }
})

// CHANGE BANK FEE
$(document).ready(function (){
    $(btnBankfee).click(function (){
        getCurrentFee()
        $(overlay).show()
    });

    $(btnCloseOverlay).click(function (){
        $(overlay).hide();
        $(feeForm).trigger("reset")
    })
});

feeForm.addEventListener('submit', function (e) {
    if (confirmationPrompt("change the bank costs")) {
        e.preventDefault()
        updateFee()
    }
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
                console.log("Bank costs updated.")
            } else {
                window.alert(`Error: ${res.statusText}`)
            }
        })
}

function getCurrentFee(){
    fetch(`http://localhost:8080/admin/getBankFee`,
        {
            method: 'GET',
            headers: {"Authorization": localStorage.getItem('token')}
        })
        .then(res => res.json())
        .then(it => {
            console.log(it)
            document.getElementById("fee-input").placeholder = `Current fee: ${it}`
        })
}

// LOAD USER
findUserForm.addEventListener('submit', function (e) {
    e.preventDefault()
    user = document.querySelector("#email-input").value
    loadUser(user)
})

function loadUser(user){
    if ($('#userTable tr').length !== 0) {
        userTable.empty()
    }
    if ($('#assetTable tr').length !== 0) {
        assetTable.empty()
    }

    fetch(`http://localhost:8080/admin/getUserData?email=${user}`,
        {
            method: 'GET',
            headers: { "Authorization": localStorage.getItem('token') }
        })
        .then(res => {
            if (res.status === 200) {
                res.json().then(it => {
                    fillUserTable(it)
                    $(btnToggleBlock).show()
                    if (it["dateOfBirth"] != null) { // only show assets when loading a client; admins don't have assets
                        showPortfolio(user)
                    } else {
                        $(portfolioData).hide()
                    }
                })
            } else {
                res.json().then(it => {
                    alert(it.message);
                })
            }
        })
}

function fillUserTable(it) {
    for (const key in it) {          // loop through all properties of the imported user json
        if (it[key] !== null && key !== "account") {
            if (key === "address") { // "address" contains properties of its own, so needs to be looped through separately
                let obj = it["address"];
                for (const key in obj) {
                    $(userTable).append(`<tr><td>${key}</td><td>${obj[key]}</td></tr>`)
                }
            } else {
                $(userTable).append(`<tr><td>${key}</td><td>${it[key]}</td></tr>`)
            }
        }
    }
}
// BLOCK/UNBLOCK USER
btnToggleBlock.addEventListener("click", function() {
    if (confirmationPrompt("change this user's block status")) {
        blockUser(user)
    }
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
                alert(it.message)
            })
        }
    })
}

// ADD OR REMOVE ASSETS
btnSubmitChanges.addEventListener("click", function() {
    if (confirmationPrompt("modify this user's assets")) {
        applyAssetChanges()
    }
})

async function showPortfolio(user) {
    await getAssets(user)
    $(portfolioData).show()
}

async function getAssets(user) {
    await fetch(`${url.origin}/admin/getAssets?email=${user}`, {
        method: 'GET',
        headers: {"Authorization": `${localStorage.getItem('token')}`},
    }).then(res => {
        if (res.status === 200) {
            console.log("ok")
        } else {
            console.log("error")
        }
        return res.json().then(it => {
            assets = it
            fillAssetTable()
        })
    })
}

function fillAssetTable() {
    for (const key in assets) {
        const tr = document.createElement("tr")
        const td1 = document.createElement("td")
        td1.innerText = key;
        const td2 = document.createElement("td")
        td2.innerText = parseFloat(assets[key]).toFixed(2)
        const td3 = document.createElement("input")
        td3.type = "number"
        td3.step = "0.01"
        td3.value = "0.00"
        td3.id = `${key}input`
        tr.append(td1, td2, td3)
        if (key === 'USD') { // move USD to top of table
            $(assetTable).prepend(tr)
        } else {
            $(assetTable).append(tr)
        }
    }
    $(assetTable).prepend("<tr><th>Symbol</th> <th>Amount owned</th> <th>Add/subtract amount</th></tr>"); // place header
}

async function applyAssetChanges() {
    let changes = {}
    for (const key in assets) {
        changes[key] = document.querySelector(`#${key}input`).value
    }

    console.log(changes)
    fetch(`http://localhost:8080/admin/updateAssets?email=${user}`,
        {
            method: 'PUT',
            headers: {  "Content-Type": "application/json",
                        "Authorization": `${localStorage.getItem('token')}`},
            body: JSON.stringify(changes)
        })
        .then(res => {
            if (res.status === 200) {
                console.log("Assets updated.")
            } else {
                window.alert(`Error: ${res.statusText}`) // TODO: werkt dit nou eigenlijk echt?
            }
        })
    loadUser(user) // reload user to reflect changes
}
