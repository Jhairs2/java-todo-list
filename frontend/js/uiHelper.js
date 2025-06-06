export const domUtility = () => {
  const contentPage = document.getElementById("project-content");

  const createTodoItemCard = () => {
    const div = document.createElement("div");
    const task = document.createElement("div");
    const completed = document.createElement("div");

    div.classList.add(
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

    div.append(task, completed);

    return div;
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
