                                        <h3 class="font-weight-light pb-2">Statistics</h3>
                                        <dl class="row">
                                            <dt class="col-sm-3">Registered date</dt>
                                            <dd class="col-sm-9">${user.registeredDate}</dd>

                                            <dt class="col-sm-3">Date last login</dt>
                                            <dd class="col-sm-9">
                                                <#if user.lastLoginDate??>
                                                    ${user.lastLoginDate}
                                                <#else>
                                                    Never
                                                </#if>
                                            </dd>

                                            <dt class="col-sm-3">Topics count</dt>
                                            <dd class="col-sm-9">${user.topicsCount}</dd>

                                            <dt class="col-sm-3">Posts count</dt>
                                            <dd class="col-sm-9">${user.postsCount}</dd>

                                            <dt class="col-sm-3">Bad karma</dt>
                                            <dd class="col-sm-9">${user.badKarma}</dd>

                                            <dt class="col-sm-3">Good karma</dt>
                                            <dd class="col-sm-9">${user.goodKarma}</dd>

                                            <dt class="col-sm-3">Time spend</dt>
                                            <dd class="col-sm-9">${user.timeLoggedIn}</dd>
                                        </dl>
