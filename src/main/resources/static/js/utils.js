window.addErrorMessage = function (errorMessage) {
  const messageElement = document.createElement("div");
  messageElement.textContent = `😢 Une erreur est survenue. ${errorMessage} !`;
  messageElement.style.color = "red";
  messageElement.style.textAlign = "center";
  document.body.appendChild(messageElement);
  setTimeout(() => {
    messageElement.remove();
  }, 5000);
};
