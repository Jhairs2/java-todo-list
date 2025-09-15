// QUERY FUNCTIONS
export const query = (selector, scope = document) => {
  return scope.querySelector(selector);
};

export const queryAll = (selector, scope = document) => {
  return scope.querySelectorAll(selector);
};

export const getParentContainerByClass = (element, tagName) => {
  if (element == null) {
    return;
  }
  if (element.classList.contains(tagName)) {
    return element;
  }

  return getParentContainerByClass(element.parentNode, tagName);
};

// CREATE ELEMENTS
/**
 * This function creates the specified element and adds the specified attributes and properties to that element
 * @param {Object.<string, *>} object element, attributes, and properties to create
 * @returns {HTMLElement} HTML element 
 */
export const buildUIElement = (
  { element,
    attributes,
    properties }
) => {
  const uiElement = document.createElement(element);

  if (attributes) {
    setAttributes(uiElement, attributes);
  }

  if (properties) {
    setProperties(uiElement, properties);
  }

  return uiElement;
};

/** This is a function to add attributes to element
 * @param {HTMLElement} element - The element to add attributes too
 * @param {Object.<string, *>} attributes - The attributes to add to the element
 */
export const setAttributes = (element, attributes) => {
  if (typeof attributes === "undefined" || typeof element === "undefined") {
    return console.error("No attributes or element passed in");
  }

  if (element && attributes) {
    Object.entries(attributes).forEach(([attr, value]) => {
      element.setAttribute(attr, value);
    });
  }
};

/** This is a function to add properties to element
 * @param {HTMLElement} element - The element to add properties too
 * @param {Object.<string, *>} properties - The properties to add to the element
 */
export const setProperties = (element, properties) => {
  if (typeof properties === "undefined" || typeof element === "undefined") {
    return console.error("No properties or element passed in");
  }

  if (element && properties) {
    Object.entries(properties).forEach(([prop, value]) => {
      element[prop] = value;
    });
  }
};

