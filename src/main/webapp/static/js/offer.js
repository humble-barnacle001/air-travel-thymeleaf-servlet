(function () {
    $("#until").datepicker({
        autoclose: true,
        format: "dd/mm/yyyy",
        todayBtn: "linked",
        todayHighlight: true,
    });
})();

// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
    "use strict";

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll(".needs-validation");

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener(
            "submit",
            function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }

                form.classList.add("was-validated");
            },
            false
        );
    });
})();

const addOfferModal = new bootstrap.Modal(document.getElementById("addOffer"), {
    keyboard: false,
});
const alertPlaceholder = document.getElementById("alertPlaceholder");
const offersParent = document.getElementById("offers");
const parser = new DOMParser();

const bsAlert = (message, type, timeOut = 5000) => {
    const alertWrapper = document.createElement("div");
    alertWrapper.innerHTML = `
                    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>`;
    alertPlaceholder.append(alertWrapper);
    // Remove alert automatically after 5 seconds
    setTimeout(() => alertPlaceholder.removeChild(alertWrapper), timeOut);
};

$(document).on("submit", "#addOfferForm", function (event) {
    event.preventDefault();
    addOfferModal.hide();
    const $form = $(this);

    $.post($form.attr("action"), $form.serialize())
        .done((response) => {
            const d = parser.parseFromString(response, "text/html");
            const newOffer = document.createElement("div");
            newOffer.classList.add("col-12", "col-md-6", "col-lg-4", "mt-3");
            newOffer.innerHTML = d.body.innerHTML;
            offersParent.appendChild(newOffer);
            bsAlert("Offer added successfully", "success");
            scrollTo(0, 0);
        })
        .fail((_, __, error) => {
            bsAlert(`Error adding offer: ${error}`, "danger");
        });
});

$(document).on("click", ".delete-offer-button", function (event) {
    const $button = $(this);
    const id = $button.attr("delete-id");
    $.ajax({
        url: `/offer?id=${id}`,
        type: "DELETE",
    })
        .done(() => {
            offersParent.removeChild(document.getElementById(id));
            scrollTo(0, 0);
            bsAlert("Successfully deleted the offer", "success");
        })
        .fail((_, __, error) => {
            bsAlert(`Error deleting offer: ${error}`, "danger");
        });
});
