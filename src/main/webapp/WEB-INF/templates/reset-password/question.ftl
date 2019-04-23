<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Reset password</h2>
            <form role="document" id="secret-question-form" class="mt-3" method="POST" action="/reset-password/question">
                <div class="row">
                    <div class="col-md-6">
                        <div class="card mt-3">
                            <div class="card-body">
                                <h3 class="card-title">Enter secret answer</h3>
                                <div class="form-group">
                                    <label for="secret-answer" class="col-form-label">Question</label>
                                    <div class="input-group">
                                        <span>${secretQuestion}</span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="secretAnswer" class="col-form-label">Your answer</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-question-circle"></i></div>
                                        </div>
                                        <@spring.formInput "secretQuestionForm.secretAnswer" "class=\"form-control\" placeholder=\"your answer\" required" />
                                        <@spring.formHiddenInput "secretQuestionForm.nickName" />
                                    </div>
                                    <p>
                                        You make ${answerTries} answer tries of ${maxSecretAnswerTries}
                                    </p>
                                    <@showValidationErrors "secretQuestionForm" "secretAnswer" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "secretQuestionForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg secret-question-submit">Answer</button>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>