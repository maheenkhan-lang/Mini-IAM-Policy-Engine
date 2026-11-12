const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const errorMessage = document.getElementById("errorMessage");
    const loginButton = document.getElementById("loginButton");

    errorMessage.textContent = "";
    loginButton.disabled = true;
    loginButton.textContent = "Signing in...";

    try {

        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "Invalid username or password");
        }

        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("username", data.username);
        sessionStorage.setItem("email", data.email);
        sessionStorage.setItem("department", data.department);

        window.location.href = "dashboard.html";

    } catch (error) {

        errorMessage.textContent = error.message;

    } finally {

        loginButton.disabled = false;
        loginButton.textContent = "Sign In";
    }
});