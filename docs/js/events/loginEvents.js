import formEvents from "./formEvents.js";
import { query } from "../utility/utilityFunctions.js";

// Initialize imports
const forms = formEvents();
const loginForm = query(".login-form");
const showPasswordCheckbox = query("#toggle-password");
const password = query("#password");


// Handle Login submits
loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
        const addedUser = await forms.loginUser(loginForm);
        return addedUser;
    }
    catch (error) {
        const p = query(".error-message");
        p.textContent = error;
    }


})

// Enable password visibility
showPasswordCheckbox.addEventListener("change", () => {
    if (password.type === "password") {
        password.type = "text";
    } else {
        password.type = "password";
    }
})

