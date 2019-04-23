                                        <h3 class="font-weight-light pb-2">Security</h3>
                                        <dl class="row mb-0">
                                            <dt class="col-sm-3">Password</dt>
                                            <dd class="col-sm-9">********</dd>

                                            <dt class="col-sm-3">Secret question</dt>
                                            <dd class="col-sm-9">
                                                <#if user.secretQuestion?? && user.secretQuestion?length gt 0>
                                                    ${user.secretQuestion}
                                                <#else>
                                                    Not set
                                                </#if>
                                            </dd>

                                            <dt class="col-sm-3">Answer</dt>
                                            <dd class="col-sm-9">
                                                <#if user.secretAnswer?? && user.secretAnswer?length gt 0>
                                                    Hidden
                                                <#else>
                                                    Not set
                                                </#if>
                                            </dd>
                                        </dl>
                                        <#if isOwnProfile && (hasPermission('EDIT_OWN_PROFILE') || hasPermission('ADMIN'))>
                                            <a href="${currentPageBase}/security/change-password" class="btn btn-primary post-edit-form-submit mt-3">Change password</a>
                                            <a href="${currentPageBase}/security/change-email" class="btn btn-primary post-edit-form-submit ml-2 mt-3">Change email</a>
                                            <a href="${currentPageBase}/security/change-secret-answer" class="btn btn-primary post-edit-form-submit ml-2 mt-3">Change secret answer</a>
                                        </#if>