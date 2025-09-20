import { uiFunctions } from "./uiFunctions.js";
import { apiCalls } from "../api/apiCalls.js";
import { query } from "../utility/utilityFunctions.js";
import eventListeners from "../events/events.js";

const UI = () => {
  // initialize imports
  const api = apiCalls();
  const ui = uiFunctions();
  const events = eventListeners();
  const select = query(".project-select");

  // Load projects into select menu
  const loadProjects = async () => {
    const projects = await api.getProjects();
    ui.populateSelectMenu(projects);
  };

  // Load tasks and menu listener
  const loadTasks = async () => {
    // Select first project in menu and display it's tasks
    if (select.options.length > 1) {
      select.value = select.options[1].value;
      const tasks = await api.getTasks(select.value);
      ui.displayTasks(tasks);
    }

  };

  // Add event listeners
  const addFunctionality = () => {
    if (events.handleSelectMenuEvents) {
      events.handleSelectMenuEvents();
    }

    if (events.handleMainSectionEvents) {
      events.handleMainSectionEvents();
    }

    if (events.handleLogoutEvent) {
      events.handleLogoutEvent();
    }

  }

  // initialize app
  const runApp = async () => {
    try {
      addFunctionality();
      await loadProjects();
    } catch (error) {
      console.log(error);

      if (error == "TypeError: Failed to fetch") {
        window.location.replace("./index.html");
        return;
      }
      ui.displayError("Error: unable to load projects from database ):");
      return;
    }

    try {
      await loadTasks();
    } catch (error) {
      console.log(error);
      if (error == "TypeError: Failed to fetch") {
        window.location.replace("./index.html");
        return;
      }
      ui.displayError(
        `Error: unable to load tasks from project ${select.value} ):`
      );
      return;
    }


  };

  return {
    runApp,
  };
};

export default UI;
