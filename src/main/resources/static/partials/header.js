$("#header").load("/partials/header.html", function (){
    setUpButtons();
});


function setUpButtons() {
    let url = new URL(window.location.href)

    // Identifying navigation buttons
    const home = document.querySelector("#home")
    const settings = document.querySelector("#settings")
    const account = document.querySelector("#account")
    const portfolio = document.querySelector("#portfolio")
    const marketplace = document.querySelector("#transactions")
    const logout = document.querySelector("#logout")


    // Adding functions to navigation buttons
    home.addEventListener("click", function() {
        window.location.replace("http://localhost:8080/dashboard.html")
    })

    settings.addEventListener("click", function() {
        window.location.replace("http://localhost:8080/settings.html")
    })

    account.addEventListener("click", function() {
        window.location.replace("http://localhost:8080/account.html")
    })

    portfolio.addEventListener("click", function() {
        window.location.replace("http://localhost:8080/portfolio.html")
    })

    marketplace.addEventListener("click", function() {
        window.location.replace("http://localhost:8080/marketplace.html")
    })

    logout.addEventListener("click", function() {
        window.localStorage.clear();
        window.location.replace("/index.html");
    })

}