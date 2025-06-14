import dataHelper from "./dataHelper.js";

export const uiHelper = () => {
  const contentPage = document.getElementById("project-content");
  const select = document.querySelector("#project-select");

  const createTodoItemCard = (todoItem) => {
    const todoContainer = document.createElement("div");
    const taskContainer = document.createElement("div");
    const optionsContainer = document.createElement("div");
    const task = document.createElement("p");
    const completed = document.createElement("input");
    const deleteButton = document.createElement("button");

    todoContainer.dataset.id = todoItem.id;

    todoContainer.classList.add(
      "row",
      "p-3",
      "mb-2",
      "border",
      "border-primary",
      "rounded",
      "todo-container"
    );

    taskContainer.classList.add("col", "task-container");
    task.textContent = todoItem.task;
    taskContainer.append(task);

    optionsContainer.classList.add("col", "options-container");
    completed.type = "checkbox";
    completed.checked = todoItem.completed;

    deleteButton.classList.add("btn", "btn-outline-danger");
    deleteButton.dataset.deleteId = todoItem.id;
    deleteButton.textContent = "X";

    optionsContainer.append(completed, deleteButton);

    todoContainer.append(taskContainer, optionsContainer);

    return todoContainer;
  };

  const addContentToPage = (content) => {
    if (!Array.isArray(content)) {
      console.error("Expecting array as argument");
      return;
    }

    const elements = document.createDocumentFragment();

    for (const element of content) {
      elements.append(element);
    }

    contentPage.append(elements);
  };

  const removeTask = (id) => {
    const todoItem = document.querySelector(`[data-id="${id}"]`);
    console.log(todoItem);
    todoItem.remove();
  };

  const clearContentSection = () => {
    const content = document.querySelector("#project-content");
    content.replaceChildren();
  };

  const showError = (message) => {
    clearContentSection();
    const span = document.createElement("span");
    span.textContent = message;
    addContentToPage([span]);
  };

  const addOptionsToSelectMenu = (data) => {
    if (data.length == 0 || !Array.isArray(data)) {
      showError("No projects available");
    } else {
      data.forEach((project) => {
        const option = document.createElement("option");
        option.text = project.listTitle;
        option.value = project.id;
        select.add(option);
      });
    }
  };

  const showTasks = (data) => {
    if (data.length == 0 || !Array.isArray(data)) {
      showError("No tasks available");
    } else {
      data.forEach((task) => {
        const content = createTodoItemCard(task);
        addContentToPage([content]);
      });
    }
  };

  return {
    createTodoItemCard,
    addContentToPage,
    addOptionsToSelectMenu,
    clearContentSection,
    showError,
    showTasks,
    removeTask,
  };
};
