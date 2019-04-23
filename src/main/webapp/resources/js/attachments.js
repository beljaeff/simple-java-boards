jQuery(document).ready(function ($) {

    var maxAttachments = getMaxAttachments();

    $(document).on('click', '.add-attachment', function () {
        var attachmentsCount = $(".attachments div").length;
        if(attachmentsCount < maxAttachments) {
            $(".attachments").append(
                '<div>' +
                '<a class="fas fa-fw fa-trash-alt remove-attachment"></a>' +
                '<label class="btn btn-primary" for="attachment-selector-' + attachmentsCount + '">' +
                '<input class="attachment-selector" id="attachment-selector-' + attachmentsCount + '" name="attachments" type="file" style="display:none" />' +
                'Browse' +
                '</label> ' +
                '<span class="label label-info" id="attachment-info-' + attachmentsCount + '">File not set</span>' +
                '</div>');
        }
    });

    $(document).on('click', '.remove-attachment', function () {
        $(this).parent().remove();
        recalcAttachmentNums();
    });

    $(document).on('change', '.attachment-selector', function () {
        var num = parseInt(this.id.replace("attachment-selector-", ""));
        $('#attachment-info-' + num).html(document.getElementById(this.id).files[0].name);
    });
});

function getMaxAttachments() {
    var max = $("#attachments-block").attr("max-attachments");
    return isInt(max) && max >= 0 ? max : 8;
}

function isInt(val) {
    return !isNaN(val) && parseInt(Number(val)) == val && !isNaN(parseInt(val, 10));
}

function recalcAttachmentNums() {
    var num = 0;
    $(".attachments div").each(function(i) {
        $(this).find("label").attr("for", "attachment-selector-" + num);
        $(this).find("label input").attr("id", "attachment-selector-" + num);
        $(this).find("span").attr("for", "attachment-info-" + num);
        num++;
    });
}
