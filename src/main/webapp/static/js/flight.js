import { bsAlert, getParser } from "./util.js";

const addFlightModal = new bootstrap.Modal(
    document.getElementById("addFlight"),
    {
        keyboard: false,
    }
);
const flightAlertPlaceholder = document.getElementById(
    "flightAlertPlaceholder"
);
const flightsParent = document.getElementById("flights");
const flightChooser = document.getElementById("chooseFlight");

$(document).on("submit", "#addFlightForm", function (event) {
    event.preventDefault();
    addFlightModal.hide();
    const $form = $(this);

    $.post($form.attr("action"), $form.serialize())
        .done((response) => {
            const d = getParser().parseFromString(response, "text/html");
            const newFlight = document.createElement("div");
            newFlight.innerHTML = d.body.innerHTML.trim();
            flightsParent.appendChild(newFlight.firstChild);
            const fopt = document.createElement("option");
            fopt.setAttribute(
                "value",
                d.body.firstElementChild.id.substring(7)
            );
            fopt.innerText =
                d.body.firstElementChild.getAttribute("travel-plan");
            flightChooser.appendChild(fopt);
            bsAlert(
                flightAlertPlaceholder,
                "Flight added successfully",
                "success"
            );
        })
        .fail((_, __, error) => {
            bsAlert(
                flightAlertPlaceholder,
                `Error adding flight ${error}`,
                "danger"
            );
        })
        .always(() => {
            flightAlertPlaceholder.parentElement.scrollIntoView();
            $form.get(0).reset();
        });
});
