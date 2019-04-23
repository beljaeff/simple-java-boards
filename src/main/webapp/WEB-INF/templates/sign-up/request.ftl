<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

    <#--TODO: posts.properties to frotend -->
        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Sign up</h2>
            <form role="document" id="sign-up-form" class="mt-3" method="POST" action="/sign-up/request">
                <div class="card">
                    <div class="card-body">
                        <h3 class="card-title">Who are you?</h3>
                        <div class="row">
                            <div class="form-group col-md-2">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-venus-mars"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "signUpForm.gender" genderList "class=\"form-control custom-select\"" />
                                </div>
                            </div>
                            <div class="form-group col-md-5">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-font"></i></div>
                                    </div>
                                    <@spring.formInput "signUpForm.name" "class=\"form-control\" placeholder=\"Name\"" />
                                </div>
                                <@showValidationErrors "signUpForm" "name" />
                            </div>
                            <div class="form-group col-md-5">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-font"></i></div>
                                    </div>
                                    <@spring.formInput "signUpForm.surname" "class=\"form-control\" placeholder=\"Surname\"" />
                                </div>
                                <@showValidationErrors "signUpForm" "surname" />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="card mt-3">
                            <div class="card-body">
                                <h3 class="card-title">How to contact you?</h3>
                                <div class="form-group">
                                    <label for="nick-name" class="col-form-label">Nick name (login)</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-user"></i></div>
                                        </div>
                                        <#assign nickNameMessage = springMacroRequestContext.getMessage("sign.up.form.nick.name.incorrect") />
                                        <@spring.formInput "signUpForm.nickName" "class=\"form-control\" placeholder=\"Your nick name\" minlength=\"3\" maxlength=\"64\" data-toggle=\"sign-up-form-popover\" title=\"Nick name\" data-content=\"${nickNameMessage}\" required" />
                                    </div>
                                    <@showValidationErrors "signUpForm" "nickName" />
                                </div>
                                <div class="form-group">
                                    <label for="email" class="col-form-label">Email</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-envelope"></i></div>
                                        </div>
                                        <#assign emailMessage = springMacroRequestContext.getMessage("sign.up.form.email.incorrect") />
                                        <@spring.formInput "signUpForm.email" "class=\"form-control\" placeholder=\"your@email.com\" minlength=\"5\" maxlength=\"64\" data-toggle=\"sign-up-form-popover\" title=\"Email\" data-content=\"${emailMessage}\" required" />
                                    </div>
                                    <@showValidationErrors "signUpForm" "email" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card mt-3">
                            <div class="card-body">
                                <h3 class="card-title">Securize your account!</h3>
                                <div class="form-group">
                                    <label for="password" class="col-form-label">Password</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fingerprint"></i></div>
                                        </div>
                                        <@spring.formPasswordInput "signUpForm.password" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type your password\" data-toggle=\"sign-up-form-popover\" title=\"Password strength\" data-content=\"Enter password...\" data-password-min-strength=\"${passwordMinStrength}\" required" />
                                    </div>
                                    <@showValidationErrors "signUpForm" "password" />
                                </div>
                                <div class="form-group">
                                    <label for="password-confirm" class="col-form-label">Password confirm</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fingerprint"></i></div>
                                        </div>
                                        <@spring.formPasswordInput "signUpForm.passwordConfirm" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type your password again\" data-toggle=\"sign-up-form-popover\" title=\"Password matching\" data-content=\"Enter password confirm...\" required" />
                                    </div>
                                    <@showValidationErrors "signUpForm" "passwordConfirm" />
                                    <@showGlobalError "signUpForm" "PasswordsEqual" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "signUpForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg sign-up-submit">Sign up</button>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/webjars/zxcvbn/4.3.0/zxcvbn.js" defer></script>
        <script type="text/javascript" src="/resources/js/sign-up.js" defer></script>
    </body>
</html>