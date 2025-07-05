import { uiBuilder, elementsLookUp } from "./uiHelper.js";
import { apiCalls } from "./apiCalls.js";
import formHandler from "./forms.js";

const eventListeners = () => {
  const ui = uiBuilder();
  const api = apiCalls();
  const lookup = elementsLookUp();
  const formHand = formHandler();
  const contentSection = lookup.query("#project-content-container");
  const addTaskSection = lookup.query(".add-task-section");
  const addTaskFormContainer = lookup.query("#form-add-container");
  const select = lookup.query("#project-select");

  /* Handle select menu events */
  // Show tasks of projects when selected
  const handleSelectMenu = () => {
    select.addEventListener("change", async () => {
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
        ui.displayAddTaskForm();
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
      await displayTasks(getCurrentProjectId());
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
      ui.toggleClass(taskContainer, "completed");
    } catch (error) {
      handleError(error);
    }
  };

  // Handle logic for task container edit button
  const handleEditBtns = (taskContainer) => {
    const activeEdit = getActiveEdit();
    if (activeEdit && activeEdit != taskContainer) {
      ui.toggleClass(activeEdit, "editing");
    }
    ui.toggleClass(taskContainer, "editing");
  };

  // Add submit event listener for submit button in task container
  const handleTaskContainerFormSubmits = () => {
    contentSection.addEventListener("submit", async (e) => {
      e.preventDefault();

      if (e.target.classList.contains("edit-form")) {
        await handleEditFormSubmit(
          e.target,
          e.target.closest(".todo-container")
        );
      }
    });
  };

  // Handle logic for edit form submit
  const handleEditFormSubmit = async (form, taskContainer) => {
    try {
      const editedTask = await formHand.submitTaskForm(
        "PUT",
        form,
        getCurrentProjectId(),
        taskContainer.dataset.id
      );
      updateTaskTitle(editedTask.task);
      ui.toggleClass(taskContainer, "editing");
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
  };

  // Delete task from project and remove task container
  const deleteTask = async (projectId, taskId) => {
    const deletedTask = await api.deleteTask(projectId, taskId);
    ui.removeTaskContainer(taskId);

    return deletedTask;
  };

  // Update task in task container with new task
  const updateTaskTitle = (newTask) => {
    const editedTask = lookup.query(".editing .task-section .task-title");
    editedTask.textContent = newTask;
  };

  // Add event listener to page to close active edit when clicking elsewhere
  const hideEditForm = () => {
    document.addEventListener("click", (e) => {
      const activeEdit = getActiveEdit();
      if (activeEdit && !activeEdit.contains(e.target)) {
        const editForm = queryFromContainer(
          activeEdit,
          ".task-section .edit-form"
        );
        if (editForm) {
          editForm.reset();
        }

        ui.toggleClass(activeEdit, "editing");
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
    handleTaskContainerEvents();
    hideEditForm();
  };

  // Get element from task container
  const queryFromContainer = (taskContainer, selector) => {
    return lookup.query(selector, taskContainer);
  };

  // Get active ediit
  const getActiveEdit = () => {
    return lookup.query(".editing");
  };

  // Get current select project from select menu
  const getCurrentProjectId = () => {
    const projectId = select.value;
    return parseInt(projectId);
  };

  return {
    handleSelectMenu,
    handlePageListeners,
    displayTasks,
  };
};

export default eventListeners;
