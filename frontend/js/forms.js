import { apiCalls } from "./apiCalls.js";

const formHandler = () => {
  const api = apiCalls();

  // Handles updating projects submissions
  const editProjectSubmit = async (form, projectId) => {
    if (!form.checkValidity()) {
      return null;
    }

    const formData = new FormData(form);

    const editProject = await api.updateProject(projectId, {
      listTitle: formData.get("update-project-input")
    })

    return editProject;
  }

  // handle editing tasks submissions
  const editTaskSubmit = async (form, projectId, taskId) => {
    if (!form.checkValidity()) {
      return null;
    }

    const formData = new FormData(form);

    const editedTask = await api.updateTask(projectId, taskId, {
      task: formData.get("update-task-input"),
    });

    return editedTask;
  };

  // handle adding task submissions
  const addTaskSubmit = async (form, projectId) => {
    if (!form.checkValidity()) {
      return null;
    }
    const formData = new FormData(form);

    const addedTask = await api.addTask(projectId, {
      task: formData.get("add-task-input"),
    });

    return addedTask;
  };

  // handle adding project submissions
  const addProjectSubmit = async (form) => {
    if (!form.checkValidity()) {
      return null;
    }
    const formData = new FormData(form);

    const addedProject = await api.addProject({
      listTitle: formData.get("add-project-input"),
    });

    return addedProject;
  };


  return { editTaskSubmit, editProjectSubmit, addProjectSubmit, addTaskSubmit };
};

export default formHandler;
