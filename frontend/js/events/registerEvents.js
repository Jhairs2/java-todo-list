import { buildUIElement, query } from "../utility/utilityFunctions.js";
import formEvents from "./formEvents.js";

// Initialize imports
const showPasswordCheckbox = query("#toggle-password");
const password = query("#password");

// Handle register submits
document.addEventListener("DOMContentLoaded", () => {
    const forms = formEvents();
    const registerForm = query(".register-form");
    const submitBtn = query(".submit-btn", registerForm)
    const statusMessage = query(".status-message");

    registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        try {
            statusMessage.textContent = "Registering! Please wait..."
            submitBtn.disabled = true;
            const addedUser = await forms.registerUser(registerForm);
            completeRegister();
            return addedUser;
        }
        catch (error) {
            submitBtn.disabled = false;
            statusMessage.textContent = error;
            console.log(error);
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

// Show completion message upon successful register
const completeRegister = () => {
    const main = query("main");
    main.replaceChildren();

    const p = buildUIElement({ element: "p", attributes: { class: "register-message" } });
    p.textContent = "Successfully Registered! Return to ";

    const a = buildUIElement({ element: "a" });
    a.href = "./index.html";
    a.textContent = "Login";

    p.append(a);
    main.append(p);
}



