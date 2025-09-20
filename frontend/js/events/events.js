import { uiFunctions } from "../ui/uiFunctions.js";
import { apiCalls } from "../api/apiCalls.js";
import { query, queryAll } from "../utility/utilityFunctions.js";
import formEvents from "../events/formEvents.js";

const eventListeners = () => {
  // Initialize imports
  const api = apiCalls();
  const ui = uiFunctions();
  const forms = formEvents();
  const LOGIN_URL = "./index.html";

  // SELECT MENU

  // Add event listener to select menu for changing selections
  const handleSelectMenuEvents = () => {
    const selectMenu = query(".project-select");
    selectMenu.addEventListener("change", handleSelectMenuChanges);
  };

  // handle select menu change,, projects tasks will display whenever option is selected
  const handleSelectMenuChanges = async (e) => {
    try {
      const tasks = await api.getTasks(e.target.value);
      ui.displayTasks(tasks);
    } catch (error) {
      ui.displayError(error);
    }
  };

  // MAIN SECTION

  // handle evenets for the main section
  const handleMainSectionEvents = () => {
    const mainSection = query("#main-section");
    mainSection.addEventListener("click", handleMainSectionClickEvents);
    mainSection.addEventListener("submit", handleMainSectionSubmitEvents);
    mainSection.addEventListener("invalid", inputValidation, true);
    mainSection.addEventListener("focusout", noFocusToggleEdit);
  };

  // Main section click events
  const handleMainSectionClickEvents = async (e) => {
    if (!e.target || !e.target.dataset.action) {
      return;
    }
    const projectId = parseInt(query(".project-select").value);
    const projectContainer = query(".project-select-container");
    const taskContainer = e.target.closest(".task-container");
    let { action } = e.target.dataset;
    action = action.toLowerCase().trim();

    if (action !== "add-project" && !ui.isProjectSelected()) {
      return;
    }

    switch (action) {
      case "add-project":
        ui.toggleProjectAdd(projectContainer);
        break;

      case "edit-task":
        ui.toggleEditMode(taskContainer, "task");
        break;

      case "edit-project":
        ui.toggleEditMode(projectContainer, "project");
        break;

      case "delete-task":
        try {
          await api.deleteTask(projectId, taskContainer.dataset.id);
          ui.removeTaskContainer(taskContainer);
          checkForContent("task");
        } catch (error) {
          ui.displayError(error);
        }
        break;

      case "delete-project":
        try {
          await api.deleteProject(projectId);
          ui.removeSelectedProjectFromMenu();
          checkForContent("project");
        } catch (error) {
          ui.displayError(error);
        }
        break;

      case "complete":
        try {
          await api.updateTask(projectId, taskContainer.dataset.id, {
            completed: e.target.checked,
          });
          ui.toggleCompleted(e.target, taskContainer);
        } catch (error) {
          ui.displayError(error);
        }
        break;

      default:
        break;
    }
  };

  // Main section form submission events
  const handleMainSectionSubmitEvents = async (e) => {
    e.preventDefault();
    if (!e.target || !e.target.dataset.action) {
      return;
    }

    const projectContainer = query(".project-select-container");
    const projectId = parseInt(query(".project-select").value);
    const taskContainer = e.target.closest(".task-container");

    let { action } = e.target.dataset;
    action = action.toLowerCase().trim();

    if (action !== "new-project" && !ui.isProjectSelected()) {
      return;
    }

    switch (action) {
      case "edit-task":
        try {
          const editedTask = await forms.editTaskSubmit(
            e.target,
            projectId,
            taskContainer.dataset.id
          );
          taskContainer.classList.remove("editing");
          ui.updateTaskTitle(taskContainer, editedTask.task);
        } catch (error) {
          ui.displayError(error);
        }
        break;

      case "edit-project":
        try {
          const editedProject = await forms.editProjectSubmit(
            e.target,
            projectId
          );
          projectContainer.classList.remove("editing");
          ui.updateProjectTitle(editedProject.listTitle);
        } catch (error) {
          ui.displayError(error);
        }
        break;

      case "new-task":
        try {
          const addedTask = await forms.addTaskSubmit(e.target, projectId);
          ui.addTaskContainer(addedTask);
        } catch (error) {
          ui.displayError(error);
        }
        break;

      case "new-project":
        try {
          const addedProject = await forms.addProjectSubmit(e.target);
          projectContainer.classList.remove("adding");
          ui.addOptionToSelectMenu(addedProject);
        } catch (error) {
          ui.displayError(error);
        }
        break;

      default:
        break;
    }
    e.target.reset();
  };


  // LOGOUT FUNCTION

  // Handle Logouts
  const clearToken = () => {
    localStorage.removeItem("token");
    window.location.replace(LOGIN_URL);

  }

  const handleLogoutEvent = () => {
    const logoutLink = query(".logout-button");
    logoutLink.addEventListener("click", clearToken);
  }


  // UTILITY FUNCTIONS 

  // Input validation
  const inputValidation = (e) => {
    // empty fields will be invalid
    if (!e.target.value.trim()) {
      e.target.setCustomValidity("Field must not be empty");
    } else {
      e.target.setCustomValidity("");
    }
  };

  // Remove task event classes when inputs are not focused
  const noFocusToggleEdit = (e) => {
    const activeEdit = query(".editing");
    const activeAdding = query(".adding");

    if (
      activeEdit &&
      (!activeEdit.contains(e.relatedTarget) ||
        e.relatedTarget.classList.contains("add-project-btn") ||
        e.relatedTarget.classList.contains("delete-project-btn"))
    ) {
      activeEdit.classList.remove("editing");
    }

    if (
      activeAdding &&
      (!activeAdding.contains(e.relatedTarget) ||
        e.relatedTarget.classList.contains("edit-project-btn") ||
        e.relatedTarget.classList.contains("delete-project-btn"))
    ) {
      activeAdding.classList.remove("adding");
    }
  };

  // Check if tasks are on the page or that projects are in the select menu
  const checkForContent = (type) => {
    if (type === "task") {
      if (queryAll(".task-container").length == 0) {
        ui.displayError("No tasks available.");
      }
    } else if (type === "project") {
      if (query(".project-select").options.length == 1) {
        ui.displayError("No projects available.");
      }
    }
  };

  return {
    handleSelectMenuEvents,
    handleMainSectionEvents,
    handleLogoutEvent
  };
};

export default eventListeners;
