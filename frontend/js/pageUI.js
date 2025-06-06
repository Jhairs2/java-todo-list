import { getProjects, getTasks } from "./api.js";
import { uiHelper } from "./uiHelper.js";

const select = document.querySelector("#project-select");

const addProjectsToSelectMenu = async () => {
  const data = await getProjects();
  console.log(data);
  Object.values(data).forEach((project) => {
    console.log(project);
    let option = document.createElement("option");
    option.text = project.listTitle;
    option.value = project.id;

    select.add(option);
  });
};

addProjectsToSelectMenu();

select.addEventListener("change", () => {
  const data = getTasks(select.value);
  console.log(data);
});
