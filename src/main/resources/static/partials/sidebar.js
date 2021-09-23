let firstName;
window.addEventListener("DOMContentLoaded", setupSidebar)

async function setupSidebar(){
    await getFirstName()
    let boldName = firstName.bold()
    $("#intro").append(boldName);
    console.log(firstName)
}

async function getFirstName(){
    await fetch (`${url.origin}/getNameClient`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res => res.text())
        .then(data => firstName = data)
}