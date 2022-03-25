import { bsAlert, getParser } from "./util.js";

(function () {
    $("#until").datepicker({
        autoclose: true,
        format: "dd/mm/yyyy",
        todayBtn: "linked",
        todayHighlight: true,
    });
})();

const addOfferModal = new bootstrap.Modal(document.getElementById("addOffer"), {
    keyboard: false,
});
const offerAlertPlaceholder = document.getElementById("offerAlertPlaceholder");
const offersParent = document.getElementById("offers");

$(document).on("submit", "#addOfferForm", function (event) {
    event.preventDefault();
    addOfferModal.hide();
    const $form = $(this);

    $.post($form.attr("action"), $form.serialize())
        .done((response) => {
            const d = getParser().parseFromString(response, "text/html");
            const newOffer = document.createElement("div");
            newOffer.innerHTML = d.body.innerHTML.trim();
            offersParent.appendChild(newOffer.firstChild);
            bsAlert(
                offerAlertPlaceholder,
                "Offer added successfully",
                "success"
            );
        })
        .fail((_, __, error) => {
            bsAlert(
                offerAlertPlaceholder,
                `Error adding offer ${error}`,
                "danger"
            );
        })
        .always(() => {
            offerAlertPlaceholder.parentElement.scrollIntoView();
            $form.get(0).reset();
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
            offersParent.removeChild(document.getElementById(`offer-${id}`));
            bsAlert(
                offerAlertPlaceholder,
                "Successfully deleted the offer",
                "success"
            );
        })
        .fail((_, __, error) => {
            bsAlert(
                offerAlertPlaceholder,
                `Error deleting offer ${error}`,
                "danger"
            );
        })
        .always(() => {
            offerAlertPlaceholder.parentElement.scrollIntoView();
        });
});
