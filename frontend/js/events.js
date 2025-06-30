import { uiHelper } from "./uiHelper.js";
import { apiCalls } from "./apiCalls.js";
import formHandler from "./forms.js";

const eventListeners = () => {
  const ui = uiHelper();
  const api = apiCalls();
  const formHand = formHandler();
  const contentSection = ui.getElement("#project-content-container");
  const addTaskSection = ui.getElement(".add-task-section");
  const addTaskFormContainer = ui.getElement("#form-add-container");
  const select = ui.getElement("#project-select");

  /* Handle select menu events */
  // Show tasks of projects when selected
  const showTasksOfSelectedProject = () => {
    select.addEventListener("change", async () => {
      ui.clearTaskContainers();
      try {
        await displayTasks(select.value);
      } catch (error) {
        handleError(error);
      }
    });
  };

  /* Handle add task form events */
  // Add click event listner to divs that contains buttons to show and hide task form
  const handleAddTaskSection = () => {
    addTaskSection.addEventListener("click", (e) => {
      if (e.target.matches("#add-task-btn")) {
        ui.showAddTaskForm();
      }
    });

    addTaskFormContainer.addEventListener("click", (e) => {
      if (e.target.matches("#cancel-add-btn")) {
        ui.hideAddTaskForm();
      }
    });
  };

  // Add submit listener to div that contains task form
  const handleAddTaskFormSubmit = () => {
    addTaskFormContainer.addEventListener("submit", async (e) => {
      e.preventDefault();
      if (e.target.matches("#add-task-form")) {
        await handleAddTaskForm(e.target);
      }
    });
  };

  // handle logic for task form submit
  const handleAddTaskForm = async (form) => {
    try {
      const addedTask = await formHand.submitTaskForm(
        "POST",
        form,
        getCurrentProjectId()
      );
      ui.hideAddTaskForm();
      displayTasks(getCurrentProjectId());
      console.log(addedTask);
    } catch (error) {
      handleError(error);
      ui.hideAddTaskForm();
    }
  };

  /* Handle task container events */
  // Add click event listener to task container
  const handleTaskContainerClickEvents = () => {
    contentSection.addEventListener("click", async (e) => {
      if (e.target.classList.contains("edit-btn")) {
        handleEditBtns(e.target.closest(".todo-container"));
      } else if (e.target.classList.contains("delete-btn")) {
        await handleDeleteBtns(e.target.closest(".todo-container"));
      } else if (e.target.classList.contains("complete-task-box")) {
        console.log("im running");
        await handleCheckboxes(e.target.closest(".todo-container"), e.target);
      }
    });
  };

  // Handle logic for task container checkbox
  const handleCheckboxes = async (taskContainer, checkbox) => {
    try {
      const updatedTask = await api.updateTask(
        getCurrentProjectId(),
        taskContainer.dataset.id,
        { completed: checkbox.checked }
      );
      console.log(updatedTask);
    } catch (error) {
      handleError(error);
    }
  };

  // Handle logic for task container edit button
  const handleEditBtns = (taskContainer) => {
    const activeEdit = getActiveEdit();
    if (activeEdit && activeEdit != taskContainer) {
      activeEdit.classList.toggle("editing");
    }
    taskContainer.classList.toggle("editing");
  };

  // Add submit event listener for submit button in task container
  const handleTaskContainerFormSubmits = () => {
    contentSection.addEventListener("submit", async (e) => {
      e.preventDefault();

      if (e.target.classList.contains("edit-form")) {
        await handleEditFormSubit(
          e.target,
          e.target.closest(".todo-container")
        );
      }
    });
  };

  // Handle logic for edit form submit
  const handleEditFormSubit = async (form, taskContainer) => {
    try {
      const editedTask = await formHand.submitTaskForm(
        "PUT",
        form,
        getCurrentProjectId(),
        taskContainer.dataset.id
      );
      updateTaskTitle(editedTask.task);
      taskContainer.classList.toggle("editing");
      console.log(editedTask);
    } catch (error) {
      handleError(error);
    }
  };

  // Handle logic for task container delete button
  const handleDeleteBtns = async (taskContainer) => {
    try {
      const deletedTask = await deleteTask(
        getCurrentProjectId(),
        taskContainer.dataset.id
      );
      console.log(deletedTask);
    } catch (error) {
      handleError(error);
    }
  };

  /* Helper functions */

  // Display tasks to screen
  const displayTasks = async (projectId) => {
    const tasksArr = await api.getTasks(projectId);
    ui.displayTasks(tasksArr);
    if (tasksArr.length > 0) {
      handleTaskContainerEvents();
    }
  };

  // Delete task from project and remove task container
  const deleteTask = async (projectId, taskId) => {
    const deletedTask = await api.deleteTask(projectId, taskId);
    ui.removeTaskContainer(taskId);

    return deletedTask;
  };

  // Update task in task container with new task
  const updateTaskTitle = (newTask) => {
    const editedTask = ui.getElement(".editing .task-section .task-title");
    editedTask.textContent = newTask;
  };

  // Add event listener to page to close active edit when clicking elsewhere
  const hideEditForm = () => {
    document.addEventListener("click", (e) => {
      const activeEdit = getActiveEdit();
      if (activeEdit && !activeEdit.contains(e.target)) {
        const editForm = getElementFromContainer(
          activeEdit,
          ".task-section .edit-form"
        );
        if (editForm) {
          editForm.reset();
        }

        activeEdit.classList.toggle("editing");
      }
    });
  };
  // Handle errors and display error on page and coonsole
  const handleError = (error) => {
    console.error(error);
    ui.showError(error);
  };

  // Add all task container event listeners
  const handleTaskContainerEvents = () => {
    handleTaskContainerClickEvents();
    handleTaskContainerFormSubmits();
  };

  // Add page listeners
  const handlePageListeners = () => {
    handleAddTaskSection();
    handleAddTaskFormSubmit();
    hideEditForm();
  };

  // Get element from task container
  const getElementFromContainer = (taskContainer, selector) => {
    return ui.getElement(selector, taskContainer);
  };

  // Get active ediit
  const getActiveEdit = () => {
    return ui.getElement(".editing");
  };

  // Get current select project from select menu
  const getCurrentProjectId = () => {
    const projectId = select.value;
    return parseInt(projectId);
  };

  return {
    showTasksOfSelectedProject,
    handlePageListeners,
  };
};

export default eventListeners;
