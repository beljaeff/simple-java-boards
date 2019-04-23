<div class="modal fade" id="attachment-${attachment.id}" tabindex="-1" role="dialog" aria-labelledby="attachment-label-${attachment.id}" aria-hidden="true">
    <div class="modal-dialog attachment-modal modal-dialog-login modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <img src="${attachment.imageUrl}" class="rounded">
                <#if attachment.description??>${attachment.description}</#if>
            </div>
        </div>
    </div>
</div>