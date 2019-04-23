                                        <h3 class="font-weight-light pb-2">Groups</h3>
                                        <ul class="list-group">
                                            <#list user.groups as group>
                                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                                    ${group.name} (${group.description})
                                                    <#if (hasPermission('ADMIN') || hasPermission('EDIT_USER_GROUP')) && !(user.id == 1 && group.id?number == 7 || user.id == 0 && group.id == 1)>
                                                       <a href="${currentPageBase}/groups/${group.id}/remove" title="remove"><i class="fas fa-trash-alt"></i></a>
                                                    </#if>
                                                </li>
                                            </#list>
                                        </ul>
                                        <#if hasPermission('ADMIN') || hasPermission('EDIT_USER_GROUP')>
                                            <a href="${currentPageBase}/groups/add" class="btn btn-primary post-edit-form-submit mt-3">Add group</a>
                                        </#if>