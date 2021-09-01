// Loginsectie
const btnLogin = document.querySelector("#btnLogin")
const btnRegister = document.querySelector("#btnRegister")

btnLogin.addEventListener("click", () => {

    console.log("clicked login")
    let payload =
        `{ email: "${ document.querySelector("#email").nodeValue }", 
        password: "${ document.querySelector("#password").nodeValue }" }`
    fetch('localhost:8080/login',
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: `${ payload }`
        })
        .then(res => res.json())
        .then(it => {
            console.log(it)
        })
        .catch(console.log('oeps'))
})

btnRegister.addEventListener("click", () => {

    console.log("clicked register")

})