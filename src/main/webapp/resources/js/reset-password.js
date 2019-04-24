function updatePopover(field, popoverClass, popoverText) {
    if(typeof popoverText !== undefined) {
        field.attr("data-content", popoverText).data("bs.popover").setContent();
    }
    $("#" + field.attr("aria-describedby")).removeClass("danger success info warning primary").addClass(popoverClass);
}

jQuery(document).ready(function ($) {

    $("#reset-password-form #password").on("propertychange change keyup paste input", function () {
        var password = $(this).val();
        var popoverText = "Enter password...";
        var popoverClass = "";

        switch (zxcvbn(password)["score"]) {
            case 0:
                if (password.length > 0) {
                    popoverText = "Very Weak";
                    popoverClass = "danger";
                }
                break;

            case 1:
                popoverText = "Weak";
                popoverClass = "danger";
                break;

            case 2:
                popoverText = "Medium";
                popoverClass = "warning";
                break;

            case 3:
                popoverText = "Strong";
                popoverClass = "warning";
                break;

            case 4:
                popoverText = "Very Strong";
                popoverClass = "success";
                break;
        }

        updatePopover($(this), popoverClass, popoverText);
    });

    $("#reset-password-form #passwordConfirm").on("propertychange change keyup paste focus input", function () {
        var password = $("#password").val();
        var passwordConfirm = $(this).val();
        var popoverText = "Enter password confirm...";
        var popoverClass = "";

        if (passwordConfirm.length > 0) {
            if (password  !== passwordConfirm) {
                popoverText = "Password does not match";
                popoverClass = "danger";
            }
            else {
                popoverText = "Password match";
                popoverClass = "success";
            }
        }

        updatePopover($(this), popoverClass, popoverText);
    });

    $("input[data-toggle='reset-password-form-popover']").popover({
        placement: "top",
        trigger: "focus"
    });

    $("#reset-password-form .reset-password-submit").on("click", function () {
        var password = $("#password");
        var passwordConfirm = $("#passwordConfirm");

        var state = true;

        if(password.val() !== passwordConfirm.val()) {
            passwordConfirm.popover("show");
            updatePopover(passwordConfirm, "danger", "Password does not match");
            state = false;
        }
        if(password.val().length === 0 && passwordConfirm.val().length === 0) {
            passwordConfirm.popover("show");
            state = false;
        }

        var minStrength = parseInt(password.attr("data-password-min-strength"));
        if(zxcvbn(password.val())["score"] < minStrength) {
            password.popover("show");
            state = false;
        }

        if(!state) {
            return false;
        }
    });
});