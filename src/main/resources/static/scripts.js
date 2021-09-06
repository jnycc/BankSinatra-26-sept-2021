// Loginsectie
const btnLogin = document.querySelector("#btnLogin")
const btnRegister = document.querySelector("#btnRegister")
const btnSubmitReg = document.querySelector("#submitReg")
const overlay = document.querySelector("#overlay")

btnLogin.addEventListener("click", doLogin);

/*btnRegister.addEventListener("click", () => {
    console.log("clicked register")
    let email = document.querySelector("#email").value;
    fetch(`http://localhost:8080/gegevens/${email}`, {
        method: 'GET',
        headers: {'Authorization':`${localStorage.getItem('token')}`},
    })
        .then(res => res.json())
        .then(it => {
            document.querySelector("h1").innerHTML = `Welcome, ${it.firstName}`
        })
        .catch(console.log('oeps'))

})*/

// Overlay example
btnRegister.addEventListener('click', toggleOverlay);
// overlay.addEventListener('click', toggleOverlay);

// Attempt Registering
btnSubmitReg.addEventListener('click', doRegister);

/*function createRegisterBox() {
    document.querySelector("#overlay").style.display = "block";

    const div = document.createElement("div");
    div.id= "registerFields";
    const divh1 = document.createElement("h1");
    divh1.innerHTML = "Register";
    div.appendChild(divh1);

    if (document.querySelector("#overlay").querySelector("#registerFields")) {
        console.log("register fields already exists")
    } else {
        document.body.querySelector('#overlay').appendChild(div);
    }

    document.querySelector("#overlay").addEventListener('click', toggleOverlay);
}*/



function toggleOverlay() {
    overlay.style.display = overlay.style.display === 'none' ? 'block' : 'none';
}


// Show welcome screen example
function showWelcomeScreen() {
    document.body.removeChild(document.querySelector("#loginForm"));
    document.body.removeChild(document.querySelector("#slogan"));

    const h1 = document.createElement("h1");
    h1.id = "new-header";
    h1.value = "new header";
    // h1.setAttribute("id", "new-header")
    // h1.setAttribute("value", "new header")
    h1.innerHTML = "Welcome to Bank Sinatra"

    document.body.appendChild(h1);
}

function doRegister() {
    let payload =
        {email: `${document.querySelector("#email-reg").value}`,
            password: `${ document.querySelector("#password-reg").value}`,
            firstName: `${document.querySelector("#firstName").value}`,
            prefix: `${document.querySelector("#prefix").value}`,
            lastName: `${document.querySelector("#lastName").value}`,
            dateOfBirth: `${document.querySelector("#dob").value}`,
            bsn: `${document.querySelector("#bsn").value}`,
            address: {
                city: `${document.querySelector("#city").value}`,
                zipCode: `${document.querySelector("#zipcode").value}`,
                street: `${document.querySelector("#street").value}`,
                houseNumber: `${document.querySelector("#houseNumber").value}`,
                houseNumberExtension: `${document.querySelector("#hNrE").value}`
            }
        }

    let jsonString = JSON.stringify(payload);
    console.log(jsonString)

    fetch('http://localhost:8080/register',
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: jsonString
        })
        .then(res => {
            if (res.status === 201) {
                console.log(res.text());
                alert("Thank you. Your requested has been received. We will process it accordingly and come back to you as soon as possible.")
            } else {
                alert("Registration failed");
            }
            return res.text();
        })
        .then(it => {
            console.log(it.statusText);
        })
        .catch()
}

function doLogin(){
    let payload =
        {email: `${document.querySelector("#email").value}`,
        password: `${ document.querySelector("#password").value}`}

    if(payload.email === "" || payload.password === "") {
        alert("Please enter full credentials")
        return;
    }

    let jsonString = JSON.stringify(payload);
    console.log(jsonString)

    fetch('http://localhost:8080/login',
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: jsonString
        })
        .then(res => {
            if (res.status === 200) {
                showWelcomeScreen();
            } else if (res.status === 401) {
                alert("Incorrect login details");
                return;
            } else {
                alert("Your account is blocked. Contact administrator");
                return;
            }
            return res.text();
        })
        .then(it => {
            localStorage.setItem('token', it)
        })
        .catch()

}