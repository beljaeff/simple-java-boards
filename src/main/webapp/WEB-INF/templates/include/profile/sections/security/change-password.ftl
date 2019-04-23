<#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Security - change password</h3>
            <form role="document" id="change-password-form" class="mt-3" method="POST" action="${currentPageBase}/security/change-password">
                <div class="card">
                    <div class="card-body pb-0">
                        <div class="row form-group">
                            <label for="password" class="col-form-label col-sm-3">New password:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-fingerprint"></i></div>
                                    </div>
                                    <@spring.formPasswordInput "changePasswordForm.password" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type your new password\" data-toggle=\"change-password-form-popover\" title=\"Password strength\" data-content=\"Enter password...\" data-password-min-strength=\"${passwordMinStrength}\" required" />
                                </div>
                                <@showValidationErrors "changePasswordForm" "password" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="passwordConfirm" class="col-form-label col-sm-3">New password confirm:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-fingerprint"></i></div>
                                    </div>
                                    <@spring.formPasswordInput "changePasswordForm.passwordConfirm" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type your new password again\" data-toggle=\"change-password-form-popover\" title=\"Password matching\" data-content=\"Enter password confirm...\" required" />
                                </div>
                                <@showValidationErrors "changePasswordForm" "passwordConfirm" />
                                <@showGlobalError "changePasswordForm" "PasswordsEqual" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="currentPassword" class="col-form-label col-sm-3">Current password:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-fingerprint"></i></div>
                                    </div>
                                    <@spring.formPasswordInput "changePasswordForm.currentPassword" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type curent password\" required" />
                                </div>
                                <@showValidationErrors "changePasswordForm" "currentPassword" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "changePasswordForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary change-password-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/security">Cancel</a>
                </div>
            </form>
        <script type="text/javascript" src="/webjars/zxcvbn/4.3.0/zxcvbn.js" defer></script>
        <script type="text/javascript" src="/resources/js/change-password.js" defer></script>
