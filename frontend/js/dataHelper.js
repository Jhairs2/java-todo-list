const dataHelper = () => {
  const convertToArray = (data) => {
    if (!data || typeof data != "object") {
      throw new Error("Something went wrong! Retry request");
    }

    return Object.values(data);
  };

  return { convertToArray };
};

export default dataHelper;
