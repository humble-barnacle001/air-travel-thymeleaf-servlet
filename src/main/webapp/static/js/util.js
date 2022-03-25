export function bsAlert(placeholder, message, type, timeOut = 5000) {
    const alertWrapper = document.createElement("div");
    alertWrapper.innerHTML = `
                    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>`;
    placeholder.append(alertWrapper);
    // Remove alert automatically after 5 seconds
    setTimeout(() => placeholder.removeChild(alertWrapper), timeOut);
}

const domParser = new DOMParser();

export function getParser() {
    return domParser;
}
