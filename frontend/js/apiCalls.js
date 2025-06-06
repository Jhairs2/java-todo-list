import BASE_API_URL from "/js/url.js";

export const apiCalls = () => {
  async function getProjects() {
    try {
      const response = await fetch(`${BASE_API_URL}/projects`);
      const data = await response.json();
      if (response.status === 200) {
        console.log("Success: ", data);
        return data;
      } else {
        console.log("Server error:", data.error?.message || "Unknown error");
      }
    } catch (error) {
      console.log("Error:", error);
    }
  }

  async function getTasks(id) {
    try {
      const response = await fetch(`${BASE_API_URL}/projects/${id}/todos`);
      const data = await response.json();
      if (response.status === 200) {
        console.log("Success: ", data);
        return data;
      } else {
        console.log("Server error:", data.error?.message || "Unknown error");
      }
    } catch (error) {
      console.log("Error:", error);
    }
  }

  return { getProjects, getTasks };
};
