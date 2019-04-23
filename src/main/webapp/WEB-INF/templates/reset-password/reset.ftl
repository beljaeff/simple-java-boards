<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Reset password</h2>
                <form role="document" id="reset-password-form" class="mt-3" method="POST" action="/reset-password/reset">
                <div class="row">
                    <div class="col-md-6">
                        <div class="card mt-3">
                            <div class="card-body">
                                <h3 class="card-title">Input new password</h3>
                                <div class="form-group">
                                    <label for="password" class="col-form-label">Password</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fingerprint"></i></div>
                                        </div>
                                        <@spring.formPasswordInput "resetPasswordForm.password" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type new password\" data-toggle=\"reset-password-form-popover\" title=\"Password strength\" data-content=\"Enter password...\" data-password-min-strength=\"${passwordMinStrength}\" required" />
                                    </div>
                                    <@showValidationErrors "resetPasswordForm" "password" />
                                </div>
                                <div class="form-group">
                                    <label for="password-confirm" class="col-form-label">Password confirm</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fingerprint"></i></div>
                                        </div>
                                        <@spring.formPasswordInput "resetPasswordForm.passwordConfirm" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type new password again\" data-toggle=\"reset-password-form-popover\" title=\"Password matching\" data-content=\"Enter password confirm...\" required" />
                                        <@spring.formHiddenInput "resetPasswordForm.validationCode" />
                                    </div>
                                    <@showValidationErrors "resetPasswordForm" "passwordConfirm" />
                                    <@showGlobalError "resetPasswordForm" "PasswordsEqual" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "resetPasswordForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg reset-password-submit">Reset password</button>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/webjars/zxcvbn/4.3.0/zxcvbn.js" defer></script>
        <script type="text/javascript" src="/resources/js/reset-password.js" defer></script>
    </body>
</html>