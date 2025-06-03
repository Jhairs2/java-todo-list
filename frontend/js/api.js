import BASE_API_URL from "/js/url.js";

export async function getProjects() {
  try {
    const response = await fetch(`${BASE_API_URL}/projects`);
    const data = await response.json();
    if (response.status === 200) {
      console.log("Success: ", data);
    } else {
      console.log("Server error:", data.error?.message || "Unknown error");
    }
  } catch (error) {
    console.log("Error:", error);
  }
}

export async function getTasks(id) {
  try {
    const response = await fetch(`${BASE_API_URL}/projects/${id}/todos`);
    const data = await response.json();
    if (response.status === 200) {
      console.log("Success: ", data);
    } else {
      console.log("Server error:", data.error?.message || "Unknown error");
    }
  } catch (error) {
    console.log("Error:", error);
  }
}
