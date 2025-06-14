const dataHelper = () => {
  const convertToArray = (data) => {
    if (!data || typeof data != "object") {
      throw new Error("Data was not returned from server");
    }
    return Object.values(data);
  };

  return { convertToArray };
};

export default dataHelper;
