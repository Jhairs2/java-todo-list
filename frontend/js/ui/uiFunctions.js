import { createTodoItemCard } from "./todoItemCard.js";
import { query, buildUIElement } from "../utility/utilityFunctions.js";

// Helper functions for building ui components
export const uiFunctions = () => {

  /* UI UTILITY FUNCTIONS */

  // SELECT MENU FUNCTIONS
  /**
   * Function takes an array of projectList objects and populates the options of the 
     selectMenu with className "project-select", with project titles and ids
   * @param {Array} projects Array of projects to be added
   */
  const populateSelectMenu = (projects) => {
    const select = query(".project-select");
    if (!select) {
      return;
    }
    clearSelectMenu(select);
    initSelectMenu(select);
    if (!projects || projects.length == 0 || !Array.isArray(projects)) {
      displayError("No projects available");
    } else {
      projects.forEach((project) => {
        const option = buildUIElement({
          element: "option",
          properties: { textContent: project.listTitle, value: project.id },
        });
        select.add(option);
      });
    }
  };

  /**
   * function will initialize a select menu with a default option
   * @param {HTMLSelectElement} selectMenu select menu to be initialized
   */
  const initSelectMenu = (selectMenu) => {
    const option = buildUIElement({
      element: "option",
      properties: {
        textContent: "Select a project",
        selected: true,
        disabled: true,
      },
    });

    selectMenu.add(option);
  };

  // Clears Select Menu options
  const clearSelectMenu = (selectMenu) => {
    selectMenu.options.length = 0;
  };

  // Add options to select menu
  const addOptionToSelectMenu = (project) => {
    const selectMenu = query(".project-select");
    if (!project || !selectMenu) {
      return;
    }

    const option = buildUIElement({
      element: "option",
      properties: { textContent: project.listTitle, value: project.id },
    });
    selectMenu.add(option);

    selectMenu.selectedIndex = selectMenu.options.length - 1;
    displayTasks([]);
  };

  // remove project option from select menu
  const removeSelectedProjectFromMenu = () => {
    const selectMenu = query(".project-select");
    selectMenu.remove(selectMenu.selectedIndex);
    selectMenu.selectedIndex = 0;
    clearTaskContainers();
  };

  // update the select menu project option text
  const updateProjectTitle = (newTitle) => {
    const selectMenu = query(".project-select");
    selectMenu.options[selectMenu.selectedIndex].textContent = newTitle;
  };

  // Check if default option in project menu is selected, which means project has not been selected
  const isProjectSelected = () => {
    const project = query(".project-select");

    return project.selectedIndex != 0;
  };

  // TASK CONTAINER FUNCTIONS

  // Adds a task container to the page
  const addTaskContainer = (todoItem) => {
    if (query(".error-message")) {
      clearTaskContainers();
    }
    addContentToPage([createTodoItemCard(todoItem)]);
  };
  // Remove task container from page
  const removeTaskContainer = (container) => {
    console.log(container);
    container.remove();
  };

  // Remove all task containers from page
  const clearTaskContainers = () => {
    const content = query(".todo-content-container");
    content.replaceChildren();
  };

  // Display tasks of project to page
  /**
   * Function takes an array of task objects and creates a todoItemCard for each one and then displays it on the page.
   * @param {Array} tasks Array of tasks to be added
   */
  const displayTasks = (tasks) => {
    clearTaskContainers();
    if (!Array.isArray(tasks) || tasks.length == 0) {
      displayError("No tasks available");
    } else {
      const taskCards = tasks.map(createTodoItemCard);
      addContentToPage(taskCards);
    }
  };

  // update the task title of container
  const updateTaskTitle = (container, newTitle) => {
    const title = query(".task-title", container);
    title.textContent = newTitle;
  };

  // CONTENT AND UTILITY FUNCTIONS

  // Add elements to doc fragment to add to the page
  /**
   * Function that takes an array of elements and returns a document fragment that contains
     the elements in the array
   * @param {Array} elements Array of elements 
   * @returns {DocumentFragment} returns document fragment with elements from array
   */
  const createContent = (elements) => {
    const content = document.createDocumentFragment();

    for (const element of elements) {
      content.append(element);
    }
    return content;
  };

  // Add array of elements to the page
  /**
   * Function that takes Array of elements and creates a document fragement that is then appended to the section 
     with the className "todo-content-container". 
   * @param {Array} elements Array of elements
   */
  const addContentToPage = (elements) => {
    const contentSection = query(".todo-content-container");
    if (!Array.isArray(elements)) {
      console.error("Expecting array as argument");
      return;
    }
    contentSection.append(createContent(elements));
  };

  /**
   * This function takes a container element and a type and adds a class with the name "editing" to the container. 
     Then it grabs the input of the type specified and focuses it.
   * @param {HTMLDivElement} container container to toggle editing 
   * @param {String} type type of content that is being edited
   */
  const toggleEditMode = (container, type) => {
    if (!container || !type) {
      return;
    }
    container.classList.toggle("editing");
    const input = query(`.edit-${type}-input`, container);

    if (type === "project") {
      const selectMenu = query(".project-select", container);
      input.placeholder =
        selectMenu.options[selectMenu.selectedIndex].textContent;
    } else if (type === "task") {
      input.placeholder = query(".task-title", container).textContent;
    }

    input.focus();
  };

  /**
   * This function takes a container and adds the class "adding" to it. Then grabs the containers input and focuses it
   * @param {HTMLDivElement} container container to toggle "adding" class on
   */
  const toggleProjectAdd = (container) => {
    if (!container) {
      return;
    }
    container.classList.toggle("adding");
    const input = query(`.add-project-input`, container);
    if (input) {
      input.placeholder = "Add project...";
      input.focus();
    }
  };

  // Mark the containers of completed tasks
  const toggleCompleted = (checkbox, container) => {
    if (!checkbox || !container) {
      return;
    }

    if (checkbox.checked) {
      container.classList.add("completed");
    } else {
      container.classList.remove("completed");
    }
  };

  // Display error messages to the page
  /**
   * Function that displays a message to the page and to the console.
   * @param {String} message String message to be displayed
   */
  const displayError = (message) => {
    clearTaskContainers();
    const span = buildUIElement({
      element: "span",
      attributes: { class: "error-message" },
      properties: { textContent: message },
    });
    console.error(message);
    addContentToPage([span]);
  };

  return {
    populateSelectMenu,
    clearTaskContainers,
    displayError,
    displayTasks,
    removeTaskContainer,
    addContentToPage,
    toggleEditMode,
    toggleCompleted,
    addTaskContainer,
    removeSelectedProjectFromMenu,
    updateProjectTitle,
    updateTaskTitle,
    toggleProjectAdd,
    addOptionToSelectMenu,
    isProjectSelected,
  };
};
