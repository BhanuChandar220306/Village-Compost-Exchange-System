document.addEventListener("DOMContentLoaded", () => {

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", loginUser);
    }

    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", registerUser);
    }

});


async function loginUser(event) {

    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;

    const message = document.getElementById("loginMessage");

    message.textContent = "Logging in...";

    try {

        const response = await apiRequest(
            "/api/users/login",
            {
                method: "POST",

                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );

        const user = response.data;

localStorage.setItem("token", user.token);
localStorage.setItem("role", user.role);
localStorage.setItem("userId", user.id);

if (user.fullName) {
    localStorage.setItem("fullName", user.fullName);
}

        message.textContent = "Login successful.";

        setTimeout(() => {

            if (user.role === "HOUSEHOLD") {
                window.location.href = "household.html";

            } else if (user.role === "COLLECTOR") {
                window.location.href = "collector.html";

            } else if (user.role === "FARMER") {
                window.location.href = "farmer.html";

            } else if (user.role === "ADMIN") {
                window.location.href = "admin.html";

            } else {
                message.textContent = "Unknown user role.";
            }

        }, 500);

    } catch (error) {

        console.error("Login error:", error);

        message.textContent = error.message;
    }
}


async function registerUser(event) {

    event.preventDefault();

    const fullName =
        document.getElementById("fullName").value.trim();

    const phone =
        document.getElementById("phone").value.trim();

    const email =
        document.getElementById("registerEmail").value.trim();

    const password =
        document.getElementById("registerPassword").value;

    const role =
        document.getElementById("role").value;

    const message =
        document.getElementById("registerMessage");

    message.textContent = "Creating account...";

    try {

        await apiRequest(
            "/api/users/register",
            {
                method: "POST",

                body: JSON.stringify({
                    fullName: fullName,
                    phone: phone,
                    email: email,
                    password: password,
                    role: role
                })
            }
        );

        message.textContent =
            "Registration successful. Redirecting to login...";

        setTimeout(() => {
            window.location.href = "index.html";
        }, 1500);

    } catch (error) {

        console.error("Registration error:", error);

        message.textContent = error.message;
    }
}