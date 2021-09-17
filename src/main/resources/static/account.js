let url = new URL(window.location.href);

window.addEventListener("DOMContentLoaded", getAccount)


function getAccount(){
    fetch(`${url.origin}/getAccount`, {
        method: 'GET',
        headers: { "Authorization": `${localStorage.getItem('token')}`}
    }).then(res=> res.json())
        .then(it => {
            for (const key of Object.keys(it)){
                if (key === "iban"){
                    let iban = it[key]
                    $("#iban").append(iban);
                } else if (key === "balance"){
                    $("#balance").append(it[key])
                }
            }
        })
}