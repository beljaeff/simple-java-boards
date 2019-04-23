<#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Security - change secret answer</h3>
            <form role="document" id="change-secret-answer-form" class="mt-3" method="POST" action="${currentPageBase}/security/change-secret-answer">
                <div class="card">
                    <div class="card-body pb-0">
                        <div class="row form-group">
                            <label for="secretQuestion" class="col-form-label col-sm-3">Question</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-question-circle"></i></div>
                                    </div>
                                    <#--TODO: remove hardcode -->
                                    <select name="secretQuestion" id="secretQuestion" class="form-control custom-select">
                                        <option<#if changeSecretAnswerForm.secretQuestion?? && changeSecretAnswerForm.secretQuestion == "What is the name of a college you applied to but didn't attend?"> selected="selected"</#if> value="What is the name of a college you applied to but didn't attend?">What is the name of a college you applied to but didn't attend?</option>
                                        <option<#if changeSecretAnswerForm.secretQuestion?? && changeSecretAnswerForm.secretQuestion == "In what city or town was your first job?"> selected="selected"</#if> value="In what city or town was your first job?">In what city or town was your first job?</option>
                                        <option<#if changeSecretAnswerForm.secretQuestion?? && changeSecretAnswerForm.secretQuestion == "What is the name of your favorite childhood friend?"> selected="selected"</#if> value="What is the name of your favorite childhood friend?">What is the name of your favorite childhood friend?</option>
                                        <option<#if changeSecretAnswerForm.secretQuestion?? && changeSecretAnswerForm.secretQuestion == "What is the middle name of your youngest child?"> selected="selected"</#if> value="What is the middle name of your youngest child?">What is the middle name of your youngest child?</option>
                                        <option<#if changeSecretAnswerForm.secretQuestion?? && changeSecretAnswerForm.secretQuestion == "What was the last name of your third grade teacher?"> selected="selected"</#if> value="What was the last name of your third grade teacher?">What was the last name of your third grade teacher?</option>
                                    </select>
                                </div>
                                <@showValidationErrors "changeSecretAnswerForm" "secretQuestion" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="secretAnswer" class="col-form-label col-sm-3">Your answer</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-font"></i></div>
                                    </div>
                                    <@spring.formInput "changeSecretAnswerForm.secretAnswer" "class=\"form-control\" placeholder=\"your answer\" required" />
                                </div>
                                <@showValidationErrors "changeSecretAnswerForm" "secretAnswer" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="currentPassword" class="col-form-label col-sm-3">Current password:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-fingerprint"></i></div>
                                    </div>
                                    <@spring.formPasswordInput "changeSecretAnswerForm.currentPassword" "class=\"form-control\" minlength=\"5\" maxlength=\"64\" placeholder=\"Type curent password\" required" />
                                </div>
                                <@showValidationErrors "changeSecretAnswerForm" "currentPassword" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showGlobalErrors "changeSecretAnswerForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary change-secret-answer-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/security">Cancel</a>
                </div>
            </form>
