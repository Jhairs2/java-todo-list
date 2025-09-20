import BASE_API_URL from "./url.js";

export const apiCalls = () => {

  // Set up request headers for POST requests
  const requestHeaders = {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${localStorage.getItem("token")}`
  };

  // Handle errors when they occur
  const errorHandler = (
    consoleMessage = "Server or connection error",
    errorMessage = consoleMessage
  ) => {
    console.error(consoleMessage);
    throw new Error(errorMessage);
  };

  // Make private method to handle fetch requests
  /**
   * Function that makes a fetch call to the specified url and returns the response data
   * @param {string} url url to make fetch call to
   * @param {Object} options config options to add to fetch call
   * @returns {Promise<RequestResult>} promise returned from api that can be resolved to get JSON data
   */
  const makeRequest = async ({ url, options }) => {

    const response = await fetch(url, options);
    return handleFetchResponse(response);
  }


  const handleFetchResponse = async (response) => {
    if (response.status === 401 || response.status === 403) {
      if (window.location.pathname != "index.html") {
        window.location.replace("./index.html");
      }
      errorHandler("Invalid credentials");
    }
    else if (response.ok) {
      console.log(`Success! Request status was ${response.status}.`);
      return await response.json();
    }
    else {
      const data = await response.json();
      errorHandler(
        data?.message || "server error"
      );
    }
  }

  // Request projects from API
  const getProjects = async () => {
    return makeRequest({ url: `${BASE_API_URL}/projects`, options: { method: "GET", headers: requestHeaders } })
  };

  // Send POST request to API to add project
  const addProject = async (project) => {
    return makeRequest({ url: `${BASE_API_URL}/projects`, options: { method: "POST", headers: requestHeaders, body: JSON.stringify(project) } });
  };

  // Send PUT request to API to update project
  const updateProject = async (projectId, project) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}`, options: { method: "PUT", headers: requestHeaders, body: JSON.stringify(project) } });
  };

  // Send DELETE request to API to remove a project 
  const deleteProject = async (projectId) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}`, options: { method: "DELETE", headers: requestHeaders } });
  };

  // Request project's tasks from API
  const getTasks = async (projectId) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}/todos`, options: { method: "GET", headers: requestHeaders } });
  };

  // Send POST request to API to add task to project
  const addTask = async (projectId, task) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}/todos`, options: { method: "POST", headers: requestHeaders, body: JSON.stringify(task) } });
  };

  // Send PUT request to API to update task
  const updateTask = async (projectId, taskId, task) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}/todos/${taskId}`, options: { method: "PUT", headers: requestHeaders, body: JSON.stringify(task) } });
  };

  // Send DELETE request to remove a task to API
  const deleteTask = async (projectId, taskId) => {
    return makeRequest({ url: `${BASE_API_URL}/projects/${projectId}/todos/${taskId}`, options: { method: "DELETE", headers: requestHeaders } });
  };

  // Send POST request to register user in API DB
  const registerUser = async (registerRequest) => {
    return makeRequest({
      url: `${BASE_API_URL}/accounts/register`,
      options: { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(registerRequest) }
    })
  }

  // Send POST request to receive JWT token from API
  const loginUser = async (loginRequest) => {
    // Try to delete task data from API db, throw error if unsuccessful
    const requestData = await makeRequest({
      url: `${BASE_API_URL}/accounts/login`,
      options: { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(loginRequest) }
    }
    )

    localStorage.setItem("token", requestData.token);
  }

  return {
    getProjects,
    getTasks,
    deleteTask,
    deleteProject,
    addTask,
    addProject,
    updateTask,
    updateProject,
    registerUser,
    loginUser
  };
}

