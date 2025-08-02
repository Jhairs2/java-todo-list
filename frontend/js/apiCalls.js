import BASE_API_URL from "/js/url.js";

export const apiCalls = () => {
  /**
   * @typedef {Object} RequestResult
   * @property {Response} response - The raw Fetch API Response object.
   * @property {*} data - The parsed JSON response body.
   */

  // Set up request headers for POST requests
  const requestHeaders = {
    "Content-Type": "application/json",
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
   * @param {string} requestMethod optional type of HTTP request method
   * @param {Object.<string, *} headers optional request headers
   * @param {*} userData optional data to send to the api
   * @returns {Promise<RequestResult>} promise returned from api that can be resolved to get JSON data
   */
  const makeRequest = async (
    url,
    requestMethod = "GET",
    headers = {},
    userData = null
  ) => {
    try {
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
    }
    catch (error) {
      errorHandler(error, "Server error, data issues");
    };
  };

  // PROJECT FETCH

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
      errorHandler(error, "Something has gone wrong");
    }
  };

  // Send POST request to API to add project
  const addProject = async (project) => {
    // Try to send data to API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects`,
        "POST",
        requestHeaders,
        project
      );
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log("Success!", project, `was successfully added to database`);
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status}`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Something has gone wrong");
    }
  };

  const updateProject = async (projectId, project) => {
    // Try to update project data in db through API, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}`,
        "PUT",
        requestHeaders,
        project
      );

      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log(
          "Success!",
          project,
          `${projectId} was successfully updated!`
        );
        return data;
      }

      errorHandler(
        `Server error: Response ${response.status}. Project could not be updated`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Something has gone wrong");
    }
  };

  // Send DELETE request to remove a project to API
  const deleteProject = async (projectId) => {
    // Try to delete project data from API db, throw error if unsuccessful
    try {
      const requestData = await makeRequest(
        `${BASE_API_URL}/projects/${projectId}`,
        "DELETE",
        requestHeaders
      );
      const response = requestData.response;
      const data = requestData.data;

      if (response.ok) {
        console.log("Success! Project", projectId, `deleted from database!`);
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status} could not delete`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Something has gone wrong");
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
      errorHandler(error, "Something has gone wrong");
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
      errorHandler(error, "Something has gone wrong");
    }
  };

  // Send PUT request to api to update task
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
      errorHandler(error, "Something has gone wrong");
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
          taskId,
          `deleted from project ${projectId}`
        );
        return data;
      }
      errorHandler(
        `Server error: Response ${response.status} could not delete`,
        data.error?.message || "Unknown error"
      );
    } catch (error) {
      errorHandler(error, "Something has gone wrong");
    }
  };

  return {
    getProjects,
    getTasks,
    deleteTask,
    deleteProject,
    addTask,
    addProject,
    updateTask,
    updateProject,
  };
};
