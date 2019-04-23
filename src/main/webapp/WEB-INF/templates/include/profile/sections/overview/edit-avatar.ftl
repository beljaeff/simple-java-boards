<#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Overview - edit avatar</h3>
            <form role="document" id="edit-avatar-form" class="mt-3" method="POST" enctype="multipart/form-data" action="${currentPageBase}/overview/edit-avatar">
                <div class="card">
                    <div class="card-body">
                        <#if user.avatar??>
                            <div class="row form-group mb-0">
                                <label class="col-form-label col-sm-12 col-md-2 pb-4">Current avatar:</label>
                                <label class="col-form-label col-sm-12 col-md-10 pb-4">
                                    <a class="fas fa-fw fa-trash-alt remove-avatar" href="${currentPageBase}/overview/remove-avatar"></a>
                                    <a href="/attachment/${user.avatar.id}">${user.avatar.originalName}</a>
                                </label >
                            </div>
                        </#if>
                        <div class="row form-group mb-0">
                            <label class="col-form-label col-sm-12 col-md-2">Set avatar:</label>
                            <div class="col-sm-12 col-md-10">
                                <div class="attachments">
                                    <label class="btn btn-primary" for="avatar">
                                        <input class="attachment-selector" id="avatar" name="avatar" type="file" accept="image/*" style="display:none" /> Browse
                                    </label>
                                    <span id="attachment-info" class="label label-info">File not set</span>
                                </div>
                                <@showValidationErrors "editAvatarForm" "avatar" />
                                <div class="avatar-check-feedback invalid-feedback mt-0">
                                    <b class="avatar-type-incorrect">only images (jpg, png, gif) supported</b>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary edit-avatar-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/overview">Cancel</a>
                </div>
            </form>
        <script type="text/javascript" src="/resources/js/avatar.js" defer></script>
