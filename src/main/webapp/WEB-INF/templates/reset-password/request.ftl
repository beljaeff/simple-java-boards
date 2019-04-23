<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Reset password</h2>
            Enter your email or your nick name.
            <form role="document" id="reset-password-request-form" class="mt-3" method="POST" action="/reset-password/request">
                <div class="card">
                    <div class="card-body">
                        <h3 class="card-title">Enter credentials</h3>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-user"></i></div>
                                    </div>
                                    <#assign inputMessage = springMacroRequestContext.getMessage("reset.password.request.form.input.incorrect") />
                                    <@spring.formInput "resetPasswordRequestForm.inputId" "class=\"form-control\" data-toggle=\"reset-password-request-form-popover\" placeholder=\"email or nickname\" title=\"Input\" data-content=\"${inputMessage}\"" />
                                </div>
                                <@showValidationErrors "resetPasswordRequestForm" "inputId" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "resetPasswordRequestForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg reset-password-request-submit">Make Request</button>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/resources/js/reset-password-request.js" defer></script>
    </body>
</html>