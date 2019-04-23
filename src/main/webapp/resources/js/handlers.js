jQuery(document).ready(function ($) {
    $("#sign-in-submit").on("click", function(event) {
        console.log("make submit");
        event.preventDefault();
        var signInData = {
            "username": $('#login').val(),
            "password": $('#password').val()
        };
        $.ajax({
            data: signInData,
            timeout: 1000,
            method: 'POST',
            url: '/login'
        })
        .done(function (data, textStatus, jqXHR) {
            //var preLoginInfo = JSON.parse($.cookie('dashboard.pre.login.request'));
            //window.location = preLoginInfo.url;
            console.log(data);
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
            console.log('Houston...');
        });
    });
});
