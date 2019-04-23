<!doctype html>
<html lang="en">
    <#include "include/common/head.ftl" />
    <body>
        <#include "include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <div class="container-sign-in d-flex align-items-center flex-column">
                <div class="card sign-in-block">
                    <div class="sign-in-body">
                        <h5 class="sign-in-title text-center">Sign In</h5>
                        <form class="sign-in-form" method="POST" action="/sign-in">
                            <div class="form-label-group">
                                <input type="text" id="login" name="login" class="form-control" placeholder="Login" required autofocus />
                            </div>

                            <div class="form-label-group">
                                <input type="password" id="password" name="password" class="form-control" placeholder="Password" required />
                            </div>

                            <#if SPRING_SECURITY_LAST_EXCEPTION?? && SPRING_SECURITY_LAST_EXCEPTION.message?has_content>
                                <div class="sign-in-feedback invalid-feedback">
                                    <b>${SPRING_SECURITY_LAST_EXCEPTION.message}</b>
                                </div>
                            </#if>

                            <div class="custom-control custom-checkbox mb-3">
                                <input type="checkbox" class="custom-control-input" id="customCheck1" />
                                <label class="custom-control-label" for="customCheck1">Remember password</label>
                            </div>
                            <button class="btn btn-lg btn-primary btn-block text-uppercase" id="sign-in-submit" type="submit">Sign in</button>
                            <hr class="my-4" />
                            <button class="disabled btn btn-lg btn-google btn-block text-uppercase" type="button"><i class="fab fa-google mr-2"></i> Sign in with Google</button>
                            <button class="disabled btn btn-lg btn-facebook btn-block text-uppercase" type="button"><i class="fab fa-facebook-f mr-2"></i> Sign in with Facebook</button>
                        </form>
                    </div>
                </div>
            </div>
        </main>

        <#include "include/common/footer.ftl" />
    </body>
</html>