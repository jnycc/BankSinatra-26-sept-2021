// Loginsectie
const btnRegister = document.querySelector("#btnRegister")
const overlay = document.querySelector("#overlay")
const loginForm = document.querySelector("#loginForm")
const registerForm = document.querySelector("#registerFields")
const closeOverlayBtn = document.querySelector("#close-overlay-btn")
const adminBtn = document.querySelector("#admin")
const clientBtn = document.querySelector("#client")

// TODO pas linkjes aan naar remote
const userLogin = 'http://localhost:8080/login';
const adminLogin = 'http://localhost:8080/admin/login';
const userRegister = 'http://localhost:8080/register';
const adminRegister = 'http://localhost:8080/admin/register';
let currentLogin = 'http://localhost:8080/login';
let currentRegister = 'http://localhost:8080/register';

btnRegister.addEventListener('click', toggleOverlay);
closeOverlayBtn.addEventListener('click', closeOverlay);
adminBtn.addEventListener('click', switchToAdmin);
clientBtn.addEventListener('click', switchToClient);

loginForm.addEventListener('submit', function(e) {
    if(loginForm.children.namedItem("email").validity.valid && loginForm.children.namedItem("password").validity.valid) {
        e.preventDefault();
        doLogin(currentLogin);
    }
});

registerForm.addEventListener('submit', function (e) {
    if (checkRegistrationFields()) {
        e.preventDefault();
        doRegister(currentRegister);
    }
})

function toggleOverlay() {
    overlay.style.display = 'block';
}
// TODO invulvelden leeg maken ?
function closeOverlay() {
    overlay.style.display = 'none';
}

// TODO haal deze uit
function showWelcomeScreen() {
    document.body.removeChild(document.querySelector("#loginForm"));
    document.body.removeChild(document.querySelector("#slogan"));

    const h1 = document.createElement("h1");
    h1.id = "new-header";
    h1.value = "new header";
    h1.innerHTML = "Welcome to Bank Sinatra"

    document.body.appendChild(h1);
}

function doRegister(currentRegister) {
    let payload =
        {email: `${document.querySelector("#email-reg").value}`,
            password: `${ document.querySelector("#password-reg").value}`,
            firstName: `${document.querySelector("#firstName").value}`,
            prefix: `${document.querySelector("#prefix").value}`,
            lastName: `${document.querySelector("#lastName").value}`,
            dateOfBirth: `${document.querySelector("#dob").value}`,
            bsn: parseInt(`${document.querySelector("#bsn").value}`),
            address: {
                city: `${document.querySelector("#city").value}`,
                zipCode: `${document.querySelector("#zipcode").value}`,
                street: `${document.querySelector("#street").value}`,
                houseNumber: `${document.querySelector("#houseNumber").value}`,
                houseNumberExtension: `${document.querySelector("#hNrE").value}`
            }
        }

    let jsonString = JSON.stringify(payload);

    fetch(`${currentRegister}`,
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: jsonString
        })
        .then(res => {
            if (res.status === 201) {
                console.log(res.text());
                alert("Thank you. Your requested has been received. We will process it accordingly and come back to you as soon as possible.")
            } else if (res.status === 409) {
                alert("Registration failed. User with this email address already exists")
                return;
            } else {
                alert("Registration failed");
                return;
            }
            return res.text();
        })
        .then(it => {
            console.log(it.statusText);
        })
        .catch()
}

function doLogin(currentLogin){
    let payload =
        {email: `${document.querySelector("#email").value}`,
        password: `${ document.querySelector("#password").value}`}

    let jsonString = JSON.stringify(payload);

    fetch(`${currentLogin}`,
        {
            method: 'POST',
            header: { "Content-Type": "application/json" },
            body: jsonString
        })
        .then(res => {
            if (res.status === 200) {
                window.location.replace("http://localhost:8080/dashboard.html")
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

function switchToAdmin(){
    currentLogin = adminLogin;
    currentRegister = adminRegister;
}

function switchToClient(){
    currentLogin = userLogin;
    currentRegister = userRegister;
}

function checkRegistrationFields() {
    return registerForm.children.namedItem("email-reg").validity.valid
    && registerForm.children.namedItem("password-reg").validity.valid
    && registerForm.children.namedItem("firstName-reg").validity.valid
    && registerForm.children.namedItem("prefix-reg").validity.valid
    && registerForm.children.namedItem("lastName-reg").validity.valid
    && registerForm.children.namedItem("bsn-reg").validity.valid
    && registerForm.children.namedItem("city-reg").validity.valid
    && registerForm.children.namedItem("zipcode-reg").validity.valid
    && registerForm.children.namedItem("street-reg").validity.valid
    && registerForm.children.namedItem("housenumber-reg").validity.valid
    && registerForm.children.namedItem("extension-reg").validity.valid
}

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