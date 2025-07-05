import { apiCalls } from "./apiCalls.js";

const formHandler = () => {
  const api = apiCalls();

  const editFormSubmit = async (form, projectId, taskId) => {
    const formData = new FormData(form);
    const editedTask = await api.updateTask(projectId, taskId, {
      task: formData.get("update"),
    });

    return editedTask;
  };

  const addTaskFormSubmit = async (form, projectId) => {
    const formData = new FormData(form);

    const addedTask = await api.addTask(projectId, {
      task: formData.get("add-task"),
    });

    return addedTask;
  };

  const submitTaskForm = async (
    requestType,
    form,
    projectId,
    taskId = null
  ) => {
    switch (requestType) {
      case "PUT":
        return await editFormSubmit(form, projectId, taskId);

      case "POST":
        return addTaskFormSubmit(form, projectId);

      default:
        console.error("Incorrect requestType");
        break;
    }
  };

  return { submitTaskForm };
};

export default formHandler;
