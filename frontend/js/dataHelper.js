import { apiCalls } from "./apiCalls";

const dataHelper = () => {
  async function convertProjectDataToArray() {
    const projectData = await apiCalls().getProjects();

    return Object.values(projectData);
  }

  async function convertTaskDataToArray(projectId) {
    const taskData = await apiCalls().getTasks(projectId);

    return Object.values(taskData);
  }

  return { convertProjectDataToArray, convertTaskDataToArray };
};

export default dataHelper;
