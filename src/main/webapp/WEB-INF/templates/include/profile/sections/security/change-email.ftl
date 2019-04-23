<#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Security - change email</h3>
            <form role="document" id="change-email-form" class="mt-3" method="POST" action="${currentPageBase}/security/change-email">
                <div class="card">
                    <div class="card-body pb-0">
                        <div class="row form-group">
                            <label for="email" class="col-form-label col-sm-3">Email:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-envelope"></i></div>
                                    </div>
                                    <#assign emailMessage = springMacroRequestContext.getMessage("change.email.form.email.incorrect") />
                                    <@spring.formInput "changeEmailForm.email" "class=\"form-control\" placeholder=\"your@email.com\" minlength=\"5\" maxlength=\"64\" data-toggle=\"change-email-form-popover\" title=\"Email\" data-content=\"${emailMessage}\" required" />
                                </div>
                                <@showValidationErrors "changeEmailForm" "email" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="currentPassword" class="col-form-label col-sm-3">Current password:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-fingerprint"></i></div>
                                    </div>
                                    <@spring.formPasswordInput "changeEmailForm.currentPassword" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type curent password\" required" />
                                </div>
                                <@showValidationErrors "changeEmailForm" "currentPassword" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "changeEmailForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary change-email-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/security">Cancel</a>
                </div>
            </form>
        <script type="text/javascript" src="/resources/js/change-email.js" defer></script>
