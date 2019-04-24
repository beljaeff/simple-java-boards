function updatePopover(field, popoverClass, popoverText) {
    if(typeof popoverText !== undefined) {
        field.attr("data-content", popoverText).data("bs.popover").setContent();
    }
    $("#" + field.attr("aria-describedby")).removeClass("danger success info warning primary").addClass(popoverClass);
}

jQuery(document).ready(function ($) {

    var PATTERN = "^[a-zA-Z0-9_\\.\\-@]{3,}$";

    $("#reset-password-request-form #inputId").on("focus propertychange change keyup paste input", function () {
        var inputId = $(this).val();
        var popoverClass = "danger";

        if(!RegExp(PATTERN).test(inputId)) {
            updatePopover($(this), popoverClass);
            $(this).popover("show");
        }
        else {
            $(this).popover("hide");
        }
    });

    $("input[data-toggle='reset-password-request-form-popover']").popover({
        placement: "top",
        trigger: "focus"
    });

    $("#reset-password-request-form .reset-password-request-submit").on("click", function () {
        var inputId = $("#inputId");

        if(!RegExp(PATTERN).test(inputId.val())) {
            inputId.popover("show");
            updatePopover(inputId, "danger");
            return false;
        }

        return true;
    });
});
