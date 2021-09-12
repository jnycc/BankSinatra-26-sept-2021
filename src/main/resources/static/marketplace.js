$("#header").load("/partials/header.html", function (){
        setUpButtons();
});

function setUpButtons() {
    let url = new URL(window.location.href)

    const logout = document.querySelector("#logout");
    logout.addEventListener('click', function() {
        if (localStorage.getItem('token') != null) {
            localStorage.clear();
        }
        window.location.replace(`${url.origin}/index.html`);
    })


}
