import formEvents from "./formEvents.js";
import { query } from "../utility/utilityFunctions.js";

// Initialize imports
const error = localStorage.getItem("error");
const showPasswordCheckbox = query("#toggle-password");
const password = query("#password");
const statusMessage = query(".status-message");

if (error) {
    statusMessage.textContent = error;
}

// Handle Login submits
document.addEventListener("DOMContentLoaded", () => {
    const forms = formEvents();
    const loginForm = query(".login-form");
    const submitBtn = query(".submit-btn", loginForm)

    loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        try {
            statusMessage.textContent = "Logging in! Please wait..."
            submitBtn.disabled = true;
            const addedUser = await forms.loginUser(loginForm);
            return addedUser;
        }
        catch (error) {
            submitBtn.disabled = false;
            statusMessage.textContent = error;
            localStorage.setItem("error", error);
        }

    })
})


// Enable password visibility
showPasswordCheckbox.addEventListener("change", () => {
    if (password.type === "password") {
        password.type = "text";
    } else {
        password.type = "password";
    }
})

