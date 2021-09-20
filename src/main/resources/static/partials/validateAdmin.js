// checking valid token (with userrole) and loading data)
window.addEventListener("DOMContentLoaded", validateAdmin)

// specific validation of jwt and userrole
function validateAdmin(){
    fetch('/validateAdmin', {
        method: 'POST',
        body: `${localStorage.getItem('token')}`
    })
        .then(res => {
            if (res.status === 200) {
                console.log("no problemo")
            } if (res.status === 403) {
                console.log("you do not have the power of an admin and you shall not pass")
                window.location.replace("/dashboard.html");
            }
            else {
                console.log("you are currently not logged in and will be redirected to the login page")
                window.location.replace("/index.html");
            }
        })
}

