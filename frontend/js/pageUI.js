import { uiHelper } from "./uiHelper.js";
import { apiCalls } from "./apiCalls.js";
import dataHelper from "./dataHelper.js";
import eventListeners from "./events.js";

const initSelectMenu = async () => {
  try {
    const data = await apiCalls().getProjects();
    const projects = dataHelper().convertToArray(data);
    uiHelper().addOptionsToSelectMenu(projects);
  } catch (error) {
    console.error(error);
    uiHelper().showError(error);
  }
};

const loadTasks = () => {
  eventListeners().showTasksOfSelectedProject();
  eventListeners().deleteButtonListener();
};

initSelectMenu();
loadTasks();
