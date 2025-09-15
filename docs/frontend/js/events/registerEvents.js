import { buildUIElement, query } from "../utility/utilityFunctions.js";
import formEvents from "./formEvents.js";

// Initialize imports
const forms = formEvents();
const registerForm = query(".register-form");
const showPasswordCheckbox = query("#toggle-password");
const password = query("#password");


// Handle register submits
registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
        const addedUser = await forms.re(registerForm);
        completeRegister();
        return addedUser;
    }
    catch (error) {
        console.log(error);
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

// SHow completion message upon successful register
const completeRegister = () => {
    const main = query("main");
    main.replaceChildren();

    const p = buildUIElement({ element: "p", attributes: { class: "register-message" } });
    const a = buildUIElement({ element: "a", properties: { href: "./login.html" } });
    a.textContent = "Login";
    p.textContent = `Successfully Registered! Return to ${a}`;
    main.append(p);
}



