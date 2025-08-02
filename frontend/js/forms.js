import { apiCalls } from "./apiCalls.js";

const formHandler = () => {
  const api = apiCalls();

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
