jQuery(document).ready(function ($) {
    $(document).on("change", ".attachment-selector", function () {
        $(".avatar-check-feedback").hide();
        $(".avatar-check-feedback b").hide();

        var file = document.getElementById(this.id).files[0];
        if(!(file.type === "image/jpeg" || file.type === "image/png" || file.type === "image/gif")){
            $(".avatar-check-feedback").show();
            $(".avatar-check-feedback b.avatar-type-incorrect").show();
            return false;
        }

        $("#attachment-info").html(file.name);
    });
});