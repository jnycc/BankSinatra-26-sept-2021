// Loginsectie
const btnLogin = document.querySelector("#btnLogin")
const btnRegister = document.querySelector("#btnRegister")

btnLogin.addEventListener("click", () => {

    console.log("clicked login")
    let payload =
        {email: `${document.querySelector("#email").value}`,
            password: `${ document.querySelector("#password").value}`}

    let jsonString = JSON.stringify(payload);
    console.log(jsonString)

    fetch('http://localhost:8080/login',
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: jsonString
        })
        .then(res => res.text())
        .then(it => {
            localStorage.setItem('Authentication', it)
        })
        .catch(console.log('oeps'))
})

btnRegister.addEventListener("click", () => {

    console.log("clicked register")

})