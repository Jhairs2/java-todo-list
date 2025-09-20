import { apiCalls } from "../api/apiCalls.js";

const formEvents = () => {
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

  // handle registering user
  const registerUser = async (form) => {
    const formData = new FormData(form);

    const addedUser = await api.registerUser(
      {
        username: formData.get("username"),
        password: formData.get("password")
      }
    );
    return addedUser;
  }

  // handle logging in user
  const loginUser = async (form) => {
    const formData = new FormData(form);

    const addedUser = await api.loginUser(
      {
        username: formData.get("username"),
        password: formData.get("password")
      }
    );

    window.location.replace("./todoListUI.html");
    return addedUser;
  }


  return { editTaskSubmit, editProjectSubmit, addProjectSubmit, addTaskSubmit, registerUser, loginUser };
};

export default formEvents;
