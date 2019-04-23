                                        <h3 class="font-weight-light pb-2">Overview</h3>
                                        <dl class="row mb-0">
                                            <dt class="col-sm-3">Nick name</dt>
                                            <dd class="col-sm-9">${user.nickName}</dd>

                                            <dt class="col-sm-3">Email</dt>
                                            <dd class="col-sm-9">
                                                <#if isOwnProfile || !isOwnProfile && !user.hideEmail || hasPermission('ADMIN')>
                                                    <a href="mailto:${user.email}">${user.email}</a>
                                                <#else>
                                                    Hidden
                                                </#if>
                                            </dd>

                                            <dt class="col-sm-3">Name</dt>
                                            <dd class="col-sm-9">${user.name}</dd>

                                            <#if user.surname??>
                                                <dt class="col-sm-3">Surname</dt>
                                                <dd class="col-sm-9">${user.surname}</dd>
                                            </#if>

                                            <#if user.birthDate??>
                                                <dt class="col-sm-3">Birthdate</dt>
                                                <dd class="col-sm-9">
                                                    <#if isOwnProfile || !isOwnProfile && !user.hideBirthdate || hasPermission('ADMIN')>
                                                        ${user.birthDate}
                                                    <#else>
                                                        Hidden
                                                    </#if>
                                                </dd>
                                            </#if>

                                            <#if user.gender??>
                                                <dt class="col-sm-3">Gender</dt>
                                                <dd class="col-sm-9">${user.gender.text}</dd>
                                            </#if>

                                            <#if user.location??>
                                                <dt class="col-sm-3">Location</dt>
                                                <dd class="col-sm-9">${user.location}</dd>
                                            </#if>

                                            <#if user.site??>
                                                <dt class="col-sm-3">Site</dt>
                                                <dd class="col-sm-9"><a href="${user.site}" target="_blank">${user.site}</a></dd>
                                            </#if>

                                            <#if user.signature??>
                                                <dt class="col-sm-3">Signature</dt>
                                                <dd class="col-sm-9">${user.signature}</dd>
                                            </#if>

                                            <dt class="col-sm-3">Banned?</dt>
                                            <dd class="col-sm-9"><#if user.isBanned><span class="font-weight-bold text-danger">Yes</span><#else>No</#if></dd>

                                            <#if hasPermission('EDIT_ALL_PROFILES') || hasPermission('ADMIN')>
                                                <dt class="col-sm-3">Activated?</dt>
                                                <dd class="col-sm-9"><#if !user.isActivated><span class="font-weight-bold text-danger">No</span><#else>Yes</#if></dd>

                                                <dt class="col-sm-3">Active?</dt>
                                                <dd class="col-sm-9"><#if !user.isActive><span class="font-weight-bold text-danger">No</span><#else>Yes</#if></dd>

                                                <dt class="col-sm-3">System?</dt>
                                                <dd class="col-sm-9"><#if user.isSystem><span class="font-weight-bold text-danger">Yes</span><#else>No</#if></dd>
                                            </#if>
                                        </dl>
                                        <#if isOwnProfile && (hasPermission('EDIT_OWN_PROFILE') || hasPermission('ADMIN'))>
                                            <a href="${currentPageBase}/overview/edit" class="btn btn-primary post-edit-form-submit mt-3">Edit</a>
                                            <a href="${currentPageBase}/overview/edit-avatar" class="btn btn-primary post-edit-form-submit ml-2 mt-3">Edit avatar</a>
                                        </#if>