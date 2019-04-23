<#assign currentPageBase="/profile/" + user.id />
<#if user.id == userPrincipal.id>
    <#assign isOwnProfile=true />
<#else>
    <#assign isOwnProfile=false />
</#if>
<#if user.isBanned>
    <#assign bannedText="Unban" />
<#else>
    <#assign bannedText="Ban" />
</#if>
<#if user.isActive>
    <#assign activateText="Deactivate" />
<#else>
    <#assign activateText="Activate" />
</#if>
<#if profileAction??>
    <#assign profileAction="/" + profileAction />
<#else>
    <#assign profileAction="" />
</#if>
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl">
    <body>
        <#include "../include/common/top.ftl">

            <main role="main" class="container pb-3">
                <@printBreadcrumbs breadcrumbs />
                <h2>${header}</h2>

                <div class="card border-0">
                    <div class="card-body profile-block p-0">
                        <div class="card user-block my-3">
                            <div class="user-top-block d-sm-flex flex-sm-row">
                                <div class="card-header p-3">
                                    <div class="d-flex flex-column align-items-center">
                                        <div class="d-flex flex-sm-column">
                                            <h5 class="d-none d-md-block align-self-center">${user.nickName}</h5>
                                            <#if (user.avatar)??>
                                                <img class="rounded" src="${user.avatar.imageUrl}" alt="${user.nickName}" title="${user.nickName}" />
                                            <#else>
                                                <div class="col d-flex flex-grow-0 align-self-center user-avatar-block user-avatar-block-big">
                                                    <i class="fas fa-user-secret" aria-hidden="true"></i>
                                                </div>
                                            </#if>
                                            <#include "../include/profile/menu-text.ftl" />
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body d-flex flex-column p-3">
                                    <#include "../include/profile/menu-icon.ftl" />
                                    <div>
                                        <#include "../include/profile/sections/${profileSection}${profileAction}.ftl" />
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer d-flex justify-content-between p-3">
                                <div>
                                    <a class="card-link" title="Write private message to ${user.nickName}"><i class="far fa-comment-alt"></i></a>
                                </div>
                                <div>
                                    <#if !user.isSystem && !isOwnProfile && (hasPermission('ADMIN') || hasPermission('DELETE_USER') || hasPermission('ACTIVATE_USER') || hasPermission('BAN_USER'))>
                                        <div class="dropdown">
                                            <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-profile"
                                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <i class="fas fa-edit"></i>
                                            </button>
                                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="edit-profile`">
                                                <#if hasPermission('ADMIN') || hasPermission('DELETE_USER')>
                                                    <a class="dropdown-item" href="${currentPageBase}/${profileSection}/delete">Delete</a>
                                                </#if>
                                                <#if hasPermission('ADMIN') || hasPermission('ACTIVATE_USER')>
                                                    <a class="dropdown-item" href="${currentPageBase}/${profileSection}/activate">${activateText}</a>
                                                </#if>
                                                <#if hasPermission('ADMIN') || hasPermission('BAN_USER')>
                                                    <a class="dropdown-item" href="${currentPageBase}/${profileSection}/ban">${bannedText}</a>
                                                </#if>
                                            </div>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>

        <#include "../include/common/footer.ftl">
    </body>
</html>