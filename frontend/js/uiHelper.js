import dataHelper from "./dataHelper";

export const uiHelper = () => {
  const contentPage = document.getElementById("project-content");

  const createTodoItemCard = (todoItem) => {
    const todoContainer = document.createElement("div");
    const task = document.createElement("div");
    const completed = document.createElement("div");

    todoContainer.dataset.id = todoItem.id();

    todoContainer.classList.add(
      "row",
      "p-3",
      "mb-2",
      "border",
      "border-primary",
      "rounded",
      "todo-container"
    );

    task.classList.add("col", "todo-title");

    completed.classList.add("col", "todo-completed");

    todoContainer.append(task, completed);

    return todoContainer;
  };

  const fillSelectMenuWithProjects = async () => {
    const projectData = dataHelper().convertProjectDataToArray();
    const select = document.querySelector("#project-select");

    projectData.forEach((project) => {
      let option = document.createElement("option");
      option.text = project.listTitle;
      option.value = project.id;

      select.add(option);
    });
  };

  const addContentToPage = (content) => {
    if (!Array.isArray(content)) {
      throw new TypeError(
        "Error expecting Array as argument type not: " + typeof content
      );
    }

    const elements = document.createDocumentFragment();

    for (const element of content) {
      elements.append(element);
    }

    contentPage.append(elements);
  };

  return { createTodoItemCard, addContentToPage };
};
