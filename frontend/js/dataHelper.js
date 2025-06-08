import { apiCalls } from "./apiCalls";

const dataHelper = () => {
  const convertProjectDataToArray = async () => {
    const projectData = await apiCalls().getProjects();

    return Object.values(projectData);
  };

  const convertTaskDataToArray = async (projectId) => {
    const taskData = await apiCalls().getTasks(projectId);

    return Object.values(taskData);
  };

  return { convertProjectDataToArray, convertTaskDataToArray };
};

export default dataHelper;
