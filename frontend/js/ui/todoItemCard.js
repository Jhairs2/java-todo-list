import { uiFunctions } from "./uiFunctions.js";
import { buildUIElement } from "../utility/utilityFunctions.js";

/* Create todoItemCard Section */

// Create container for todoItems
const createTaskContainer = (todoItem) => {
    return buildUIElement({
        element: "div",
        attributes: { class: "task-container", "data-id": todoItem.id },
    });
};

// Create task edit section
const createEditSection = (todoItem) => {
    const editForm = buildUIElement({
        element: "form",
        attributes: { class: "edit-task-form", "data-action": "edit-task" },
        properties: {
            maxLength: "70", required: true, placeholder: todoItem.task
        }
    })
    const input = buildUIElement({
        element: "input",
        attributes: { class: "edit-task-input", },
        properties: {
            type: "text",
            name: "update-task-input",
            maxLength: "70",
            required: true,
        },
    });

    editForm.append(input);

    return editForm;
};

// create checkbox for completing task
const createTaskCheckbox = (todoItem) => {
    const checkbox = buildUIElement({
        element: "input",
        attributes: { class: "task-checkbox", "data-action": "complete" },
        properties: { type: "checkbox", checked: todoItem.completed },
    });

    return checkbox;
};

// Create section where task details will be displayed
const createTaskTitleSection = (todoItem) => {
    const editSection = createEditSection(todoItem);
    const taskSection = buildUIElement({
        element: "div",
        attributes: { class: "task-title-section" },
    });
    const task = buildUIElement({
        element: "p",
        attributes: { class: "task-title" },
        properties: { textContent: todoItem.task },
    });

    taskSection.append(task, editSection);

    return taskSection;
};

// Create section for edit btn in container
const createEditBtn = () => {
    const editBtn = buildUIElement({
        element: "button",
        attributes: { class: "edit-task-btn", "data-action": "edit-task" },
        properties: { textContent: "Edit" },
    });
    return editBtn;
};

// Create section for delete btn in container
const createDeleteBtn = () => {
    const deleteBtn = buildUIElement({
        element: "button",
        attributes: { class: "delete-task-btn", "data-action": "delete-task" },
        properties: { textContent: "X" },
    });

    return deleteBtn;
};

// Create task card to display
/**
 * This function takes a todoItem object and creates a container card 
 * @param {Object} todoItem todoItem object 
 * @returns {HTMLDivElement} container card returned
 */
export const createTodoItemCard = (todoItem) => {
    const todoContainer = createTaskContainer(todoItem);
    const checkbox = createTaskCheckbox(todoItem);
    const taskSection = createTaskTitleSection(todoItem);
    const editBtn = createEditBtn();
    const deleteBtn = createDeleteBtn();

    uiFunctions().toggleCompleted(checkbox, todoContainer);
    todoContainer.append(checkbox, taskSection, editBtn, deleteBtn);

    return todoContainer;
};

