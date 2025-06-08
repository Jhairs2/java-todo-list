import { uiHelper } from "./uiHelper.js";
const select = document.querySelector("#project-select");
uiHelper().fillSelectMenuWithProjects();

select.addEventListener("change", () => {
  const data = getTasks(select.value);
  console.log(data);
});
