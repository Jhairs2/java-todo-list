import BASE_API_URL from "/js/url.js";

export const apiCalls = () => {
  async function getProjects() {
    try {
      const response = await fetch(`${BASE_API_URL}/projects`);
      const data = await response.json();
      if (response.ok) {
        console.log("Success: ", data);
        return data;
      } else {
        console.log(
          `Server error: Response ${response.status}`,
          data.error?.message || "Unknown error"
        );
        throw new Error(`Response failure: ${response.status}`);
      }
    } catch (error) {
      console.log("Error:", error);
    }
  }

  async function getTasks(id) {
    try {
      const response = await fetch(`${BASE_API_URL}/projects/${id}/todos`);
      const data = await response.json();
      if (response.ok) {
        console.log("Success: ", data);
        return data;
      } else {
        console.log("Server error:", data.error?.message || "Unknown error");
        throw new Error(`Response failure: ${response.status}`);
      }
    } catch (error) {
      console.log("Error:", error);
    }
  }

  const deleteTask = async (projectId, todoId) => {
    try {
      const response = await fetch(
        `${BASE_API_URL}/projects/${projectId}/todos/${todoId}`,
        {
          method: "DELETE",
        }
      );
      if (response.ok) {
        console.log(
          `Success! Todo ${todoId} deleted from project ${projectId}`
        );
        return;
      } else {
        console.log(`Error: ${response.status}`);
        throw new Error(`Response failure: ${response.status}`);
      }
    } catch (error) {
      console.log("Error:", error);
    }
  };

  return { getProjects, getTasks, deleteTask };
};
