import { uiHelper } from "./uiHelper.js";
import dataHelper from "./dataHelper.js";
import { apiCalls } from "./apiCalls.js";

const eventListeners = () => {
  const ui = uiHelper();
  const dataHelp = dataHelper();
  const api = apiCalls();
  const select = document.querySelector("#project-select");

  const showTasksOfSelectedProject = async () => {
    select.addEventListener("change", async () => {
      ui.clearContentSection();
      try {
        const data = await api.getTasks(select.value);
        const tasks = dataHelp.convertToArray(data);
        ui.showTasks(tasks);
        deleteButtonListener();
      } catch (error) {
        console.error(error);
        ui.showError(error);
      }
    });
  };

  const deleteButtonListener = () => {
    const deleteButton = document.querySelectorAll("[data-delete-id]");
    console.log(deleteButton);
    deleteButton.forEach((btn) => {
      btn.addEventListener("click", async () => {
        try {
          console.log(btn.dataset.deleteId);
          ui.removeTask(btn.dataset.deleteId);
          await api.deleteTask(select.value, btn.dataset.deleteId);
        } catch (error) {
          console.error(error);
          ui.showError(error);
        }
      });
    });
  };

  return { showTasksOfSelectedProject, deleteButtonListener };
};

export default eventListeners;
