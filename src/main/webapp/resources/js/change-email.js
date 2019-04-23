jQuery(document).ready(function ($) {

    var EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    $('#change-email-form #email').on('focus propertychange change keyup paste input', function () {
        var email = $(this).val();
        var popoverClass = 'danger';

        if(!RegExp(EMAIL_PATTERN).test(email)) {
            updatePopover($(this), popoverClass);
            $(this).popover('show');
        }
        else {
            $(this).popover('hide');
        }
    });

    $('input[data-toggle="change-email-form-popover"]').popover({
        placement: 'top',
        trigger: 'focus'
    });

    $('#change-email-form .change-email-form-submit').on('click', function () {
        var email = $('#email');

        if(!RegExp(EMAIL_PATTERN).test(email.val())) {
            email.popover('show');
            updatePopover(email, 'danger');
            return false;
        }
    });
});

function updatePopover(field, popoverClass, popoverText) {
    if(typeof popoverText !== undefined) {
        field.attr('data-content', popoverText).data('bs.popover').setContent();
    }
    $('#' + field.attr('aria-describedby')).removeClass('danger success info warning primary').addClass(popoverClass);
}
