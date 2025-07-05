// Helper functions to query elements
export const elementsLookUp = () => {
  const query = (selector, scope = document) => {
    return scope.querySelector(selector);
  };

  const queryAll = (selector, scope = document) => {
    return scope.querySelectorAll(selector);
  };

  const getParentContainer = (element) => {
    if (element == null) {
      return;
    }
    if (element.classList.contains("todo-container")) {
      return element;
    }

    return getParentContainer(element.parentNode);
  };

  return { query, queryAll, getParentContainer };
};

// Helper functions for building ui components
export const uiBuilder = () => {
  const lookUp = elementsLookUp();
  const form = lookUp.query("#add-task-form");
  const formContainer = lookUp.query("#form-add-container");
  const addTaskBtn = lookUp.query("#add-task-btn");

  // Build ui elements
  const buildUIElement = ({
    element,
    classes = null,
    id = null,
    dataId = null,
    properties = null,
  }) => {
    const uiElement = document.createElement(element);

    if (classes) {
      for (const name of classes) {
        uiElement.classList.add(name);
      }
    }
    if (id) {
      uiElement.id = id;
    }
    if (dataId) {
      uiElement.dataset.id = dataId;
    }

    if (properties) {
      for (const attr in properties) {
        uiElement[attr] = properties[attr];
      }
    }

    return uiElement;
  };

  /* Create todoItemCard Section */

  // Create container for todoItems
  const createTaskContainer = (todoItem) => {
    return buildUIElement({
      element: "div",
      classes: [
        "row",
        "p-3",
        "mb-2",
        "border",
        "border-primary",
        "rounded",
        "todo-container",
      ],
      dataId: todoItem.id,
    });
  };

  // Create edit section in container
  const createEditSection = (todoItem) => {
    const editForm = buildUIElement({
      element: "form",
      classes: ["form-inline", "edit-form"],
    });
    const input = buildUIElement({
      element: "input",
      classes: ["edit-input"],
      id: todoItem.id,
      properties: {
        name: "update",
        placeholder: todoItem.task,
      },
    });

    editForm.append(input);

    return editForm;
  };

  // Create section where task details will be displayed
  const createTaskSection = (todoItem) => {
    const editSecton = createEditSection(todoItem);
    const taskSection = buildUIElement({
      element: "div",
      classes: ["col", "task-section"],
    });
    const task = buildUIElement({
      element: "p",
      classes: ["task-title"],
      properties: { textContent: todoItem.task },
    });

    taskSection.append(task, editSecton);

    return taskSection;
  };

  // Create section for buttons in container
  const createOptionsSection = (todoItem) => {
    const optionsSection = buildUIElement({
      element: "div",
      classes: ["col", "options-section"],
    });
    const completedBtn = buildUIElement({
      element: "input",
      classes: ["complete-task-box"],
      properties: { type: "checkbox", checked: todoItem.completed },
    });
    const editBtn = buildUIElement({
      element: "button",
      classes: ["btn", "btn-outline-primary", "btn-sm", "edit-btn"],
      properties: { textContent: "Edit" },
    });
    const deleteBtn = buildUIElement({
      element: "button",
      classes: ["btn", "btn-outline-danger", "btn-sm", "delete-btn"],
      properties: { textContent: "X" },
    });

    optionsSection.append(completedBtn, editBtn, deleteBtn);

    return optionsSection;
  };

  // Create task card to display
  const createTodoItemCard = (todoItem) => {
    const todoContainer = createTaskContainer(todoItem);
    const taskSection = createTaskSection(todoItem);
    const optionsSection = createOptionsSection(todoItem);
    markContainerAsCompleted(todoContainer, todoItem);

    todoContainer.append(taskSection, optionsSection);

    return todoContainer;
  };

  /* UI UTILITY FUNCTIONS */

  // Add elements to doc fragment to add to the page
  const createContent = (elements) => {
    const content = document.createDocumentFragment();

    for (const element of elements) {
      content.append(element);
    }
    return content;
  };

  // Add array of elements to the page
  const addContentToPage = (elements) => {
    const contentSection = lookUp.query("#project-content");
    if (!Array.isArray(elements)) {
      console.error("Expecting array as argument");
      return;
    }
    contentSection.append(createContent(elements));
  };

  // Remove task container
  const removeTaskContainer = (id) => {
    const todoItem = lookUp.query(`[data-id="${id}"]`);
    console.log(todoItem);
    todoItem.remove();
  };
  // Remove all task containers
  const clearTaskContainers = () => {
    const content = lookUp.query("#project-content");
    content.replaceChildren();
  };

  // Display error messages to the page
  const displayError = (message) => {
    clearTaskContainers();
    const span = buildUIElement({
      element: "span",
      properties: { textContent: message },
    });
    addContentToPage([span]);
  };

  // Populate select menu with projects to choose from
  const populateSelectMenu = (projects) => {
    const select = lookUp.query("#project-select");
    if (projects.length == 0 || !Array.isArray(projects)) {
      displayError("No projects available");
    } else {
      projects.forEach((project) => {
        const option = buildUIElement({
          element: "option",
          properties: { text: project.listTitle, value: project.id },
        });
        select.add(option);
      });
    }
  };

  // Display tasks of project to page
  const displayTasks = (tasks) => {
    clearTaskContainers();
    if (!Array.isArray(tasks) || tasks.length == 0) {
      displayError("No tasks available");
    } else {
      const taskCards = tasks.map(createTodoItemCard);
      addContentToPage(taskCards);
    }
  };

  // Display form to page
  const displayAddTaskForm = () => {
    toggleClass(addTaskBtn, "hide");
    toggleClass(formContainer, "hide");
  };

  // Hide form from page
  const hideAddTaskForm = () => {
    form.reset();
    toggleClass(addTaskBtn, "hide");
    toggleClass(formContainer, "hide");
  };

  // Toggle classes on element
  const toggleClass = (element, state) => {
    element.classList.toggle(state);
  };

  // Mark the containers of completed tasks
  const markContainerAsCompleted = (todoContainer, todoItem) => {
    if (todoItem.completed) {
      toggleClass(todoContainer, "completed");
    }
  };

  return {
    populateSelectMenu,
    clearTaskContainers,
    displayError,
    displayTasks,
    removeTaskContainer,
    displayAddTaskForm,
    hideAddTaskForm,
    toggleClass,
  };
};
