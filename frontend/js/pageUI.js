import { getProjects, getTasks } from "./api.js";

const select = document.querySelector("#project-select");

select.addEventListener("change", () => {
  const data = getTasks(select.value);
  console.log(data);
});

console.log(getProjects());
console.log("YOOOO");
