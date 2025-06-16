import BASE_API_URL from "/js/url.js";

export const apiCalls = () => {
  // Set up request headers for POST requests
  const requestHeaders = {
    "Content-Type": "application/json",
  };

  // Handle errors when they occur
  const errorHandler = (
    consoleMessage = "Something went wrong",
    errorMessage = consoleMessage
  ) => {
    console.error(consoleMessage);
    throw new Error(errorMessage);
  };

  // Make private method to handle fetch requests
  const makeRequest = async (
    url,
    requestMethod = "GET",
    headers = {},
    userData = null
  ) => {
    // If header is not an object or null set to empty object
    if (typeof headers !== "object" || !headers) {
      headers = {};
    }

    let options = {
      method: requestMethod,
      headers: headers,
    };

    if (userData) {
      options.body = JSON.stringify(userData);
    }

    const response = await fetch(url, options);
    const data = await response.json();

    return { response, data };
  };

  // Request projects from API
  const getProjects = async () => {
    // Try to get data from API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(`${BASE_API_URL}/projects`);
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          `Success! Request status was ${response.status}. Data:`,
          data
        );
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status}`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Could not connect to server");
    }
  };

  // Request project's tasks from API
  const getTasks = async (projectId) => {
    // Try to get data from API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}/todos`
      );
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          `Success! Request status was ${response.status}. Data:`,
          data
        );
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status}`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Could not connect to server");
    }
  };

  // Send POST request to API to add task to project
  const addTask = async (projectId, task) => {
    // Try to send data to API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}/todos`,
        "POST",
        requestHeaders,
        task
      );
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          "Success!",
          task,
          `was successfully added to project ${projectId}`
        );
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status}`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Could not connect to server");
    }
  };

  const updateTask = async (projectId, taskId, task) => {
    // Try to update task data in db through API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}/todos/${taskId}`,
        "PUT",
        requestHeaders,
        task
      );

      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          "Success!",
          task,
          `task in project ${projectId} was successfully updated!`
        );
        return data;
      }

      errorHandler(
        `Server error: Response ${response.status}. Task could not be updated`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Could not connect to server");
    }
  };

  // Send DELETE request to remove a task to API
  const deleteTask = async (projectId, taskId) => {
    // Try to delete task data from API db, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}/todos/${taskId}`,
        "DELETE",
        requestHeaders
      );
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          "Success! Todo",
          todoId,
          `deleted from project ${projectId}`
        );
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status} could not delete`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Could not connect to server");
    }
  };

  return { getProjects, getTasks, deleteTask, addTask, updateTask };
};
