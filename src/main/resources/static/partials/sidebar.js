window.addEventListener("DOMContentLoaded", setupSidebar)
let firstName

async function setupSidebar(){
    await getFirstName()
    let boldName = firstName.bold()
    $("#intro").append(boldName);
}

async function getFirstName(){
    await fetch (`${url.origin}/getNameUser`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res => res.text())
        .then(data => firstName = data)
}