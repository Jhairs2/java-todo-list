import { uiBuilder, elementsLookUp } from "./uiHelper.js";
import { apiCalls } from "./apiCalls.js";
import eventListeners from "./events.js";

const UI = () => {
  const api = apiCalls();
  const ui = uiBuilder();
  const lookUp = elementsLookUp();
  const events = eventListeners();
  const select = lookUp.query("#project-select");

  const getProjects = async () => {
    return await api.getProjects();
  };

  const loadProjects = async () => {
    const projects = await getProjects();
    ui.populateSelectMenu(projects);
  };

  // Load tasks and menu listener
  const loadTasks = async () => {
    // Add change listener to select menu
    if (!events.handleSelectMenu()) {
      events.handleSelectMenu();
    }
    // Select first project in menu and display it's tasks
    select.value = select.options[1].value;
    events.displayTasks(select.value);
  };

  const runApp = async () => {
    try {
      await loadProjects();
    } catch (error) {
      console.log(error);
      ui.showError("Error: unable to load projects from database ):");
      return;
    }

    try {
      await loadTasks();
    } catch (error) {
      console.log(error);
      ui.showError(
        `Error: unable to load tasks from project ${select.value} ):`
      );
      return;
    }

    events.handlePageListeners();
  };

  return {
    runApp,
  };
};

export default UI;
