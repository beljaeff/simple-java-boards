jQuery(document).ready(function ($) {

    var EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    var NICK_NAME_PATTERN = "^[a-zA-Z0-9_\\.\\-]{3,}$";

    $('#sign-up-form #password').on('propertychange change keyup paste input', function () {
        var password = $(this).val();
        var popoverText = 'Enter password...';
        var popoverClass = '';

        switch (zxcvbn(password)['score']) {
            case 0:
                if (password.length > 0) {
                    popoverText = 'Very Weak';
                    popoverClass = 'danger';
                }
                break;

            case 1:
                popoverText = 'Weak';
                popoverClass = 'danger';
                break;

            case 2:
                popoverText = 'Medium';
                popoverClass = 'warning';
                break;

            case 3:
                popoverText = 'Strong';
                popoverClass = 'warning';
                break;

            case 4:
                popoverText = 'Very Strong';
                popoverClass = 'success';
                break;
        }

        updatePopover($(this), popoverClass, popoverText);
    });

    $('#sign-up-form #passwordConfirm').on('propertychange change keyup paste input', function () {
        var password = $('#password').val();
        var passwordConfirm = $(this).val();
        var popoverText = 'Enter password confirm...';
        var popoverClass = '';

        if (passwordConfirm.length > 0) {
            if (password  !== passwordConfirm) {
                popoverText = 'Password does not match';
                popoverClass = 'danger';
            }
            else {
                popoverText = 'Password match';
                popoverClass = 'success';
            }
        }

        updatePopover($(this), popoverClass, popoverText);
    });

    $('#sign-up-form #nickName').on('focus propertychange change keyup paste input', function () {
        var nickName = $(this).val();
        var popoverClass = 'danger';

        if(!RegExp(NICK_NAME_PATTERN).test(nickName)) {
            updatePopover($(this), popoverClass);
            $(this).popover('show');
        }
        else {
            $(this).popover('hide');
        }
    });

    $('#sign-up-form #email').on('focus propertychange change keyup paste input', function () {
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

    $('input[data-toggle="sign-up-form-popover"]').popover({
        placement: 'top',
        trigger: 'focus'
    });

    $('#sign-up-form .sign-up-submit').on('click', function () {
        var nickName = $('#nickName');
        var email = $('#email');
        var password = $('#password');
        var passwordConfirm = $('#passwordConfirm');

        var state = true;

        if(!RegExp(NICK_NAME_PATTERN).test(nickName.val())) {
            nickName.popover('show');
            updatePopover(nickName, 'danger');
            state = false;
        }

        if(!RegExp(EMAIL_PATTERN).test(email.val())) {
            email.popover('show');
            updatePopover(email, 'danger');
            state = false;
        }

        if(password.val() !== passwordConfirm.val()) {
            passwordConfirm.popover('show');
            updatePopover(passwordConfirm, 'danger', 'Password does not match');
            state = false;
        }
        if(password.val().length === 0 && passwordConfirm.val().length === 0) {
            passwordConfirm.popover('show');
            state = false;
        }

        var minStrength = parseInt(password.attr('data-password-min-strength'));
        if(zxcvbn(password.val())['score'] < minStrength) {
            password.popover('show');
            state = false;
        }

        if(!state) {
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
