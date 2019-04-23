<#if !topic.isApproved>
    <#assign notApproved=" sjb-not-approved" />
<#else>
    <#assign notApproved="" />
</#if>
<#if topic.isSticky>
    <#assign sticky=" sjb-sticky" />
    <#assign stickyText="Unsticky" />
<#else>
    <#assign sticky="" />
    <#assign stickyText="Make sticky" />
</#if>
<#if topic.isLocked>
    <#assign locked=" sjb-locked" />
    <#assign lockedText="Unlock" />
<#else>
    <#assign locked="" />
    <#assign lockedText="Lock" />
</#if>
<#if !topic.isActive>
    <#assign inActive=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActive="" />
    <#assign activeText="Deactivate" />
</#if>
<div class="list-group-item topic-block px-0${notApproved}${inActive}${locked}${sticky}">
    <div class="row no-gutters mx-2 my-0">
        <div class="col-9 col-md-7 d-flex">
            <div class="row no-gutters">
                <div class="col d-flex flex-grow-0 flex-column mr-2 ml-0 mx-0">
                    <div class="dropdown topic-mark-circle border-muted mt-sm-1">
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('DELETE_TOPIC') || hasPermission('MAKE_STICKY_TOPIC') || hasPermission('LOCK_TOPIC') || hasPermission('ACTIVATE_TOPIC') || !topic.isApproved && hasPermission('APPROVE_TOPIC') || topic.author.id == userPrincipal.id && hasPermission('EDIT_OWN_TOPIC')>
                            <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-forum" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="far fa-circle text-muted"></i>
                            </button>
                            <div class="dropdown-menu dropdown-menu-left" aria-labelledby="edit-forum">
                                <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('EDIT_OWN_TOPIC') && topic.author.id == userPrincipal.id>
                                    <a class="dropdown-item" href="/topic/${topic.id}/edit">Edit</a>
                                </#if>
                                <#if hasPermission('ADMIN') || hasPermission('DELETE_TOPIC')>
                                    <a class="dropdown-item" href="/topic/${topic.id}/delete${topicPageGetArg}">Delete</a>
                                </#if>
                                <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('MAKE_STICKY_TOPIC') || hasPermission('MAKE_STICKY_OWN_TOPIC') && hasPermission('EDIT_OWN_TOPIC') && topic.author.id == userPrincipal.id>
                                    <a class="dropdown-item" href="/topic/${topic.id}/change-sticky${topicPageGetArg}">${stickyText}</a>
                                </#if>
                                <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('LOCK_TOPIC') || hasPermission('LOCK_OWN_TOPIC') && hasPermission('EDIT_OWN_TOPIC') && topic.author.id == userPrincipal.id>
                                    <a class="dropdown-item" href="/topic/${topic.id}/change-lock${topicPageGetArg}">${lockedText}</a>
                                </#if>
                                <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('ACTIVATE_TOPIC')>
                                    <a class="dropdown-item" href="/topic/${topic.id}/change-active${topicPageGetArg}">${activeText}</a>
                                </#if>
                                <#if !topic.isApproved && (hasPermission('ADMIN') || hasPermission('APPROVE_TOPIC'))>
                                    <a class="dropdown-item" href="/topic/${topic.id}/approve${topicPageGetArg}">Approve</a>
                                </#if>
                            </div>
                        <#else>
                            <i class="far fa-circle text-muted"></i>
                        </#if>
                    </div>
                </div>
                <div class="col d-flex flex-column align-self-center">
                    <div class="row no-gutters topic-title-element-block">
                        <div class="col-md-auto"><a href="/topic/${topic.id}"><strong>${topic.title}</strong></a></div>
                        <#if topic.postsCount gt 0 && topic.postsCount gt postsPageSize?number>
                            <#assign totalPages = topic.postsCount / postsPageSize?number />
                            <#if topic.postsCount % postsPageSize?number != 0>
                                <#assign totalPages = totalPages?int + 1 />
                            </#if>
                            <#assign linkBase = "/topic/" + topic.id />
                            <div class="col-md-auto py-1 py-md-0 pl-lg-1">
                                <nav aria-label="Topic posts pages">
                                    <#include "../common/pagination/small.ftl" />
                                </nav>
                            </div>
                        </#if>
                    </div>
                    <span class="text-normal text-muted d-flex flex-column flex-sm-row">
                        <div>Author: <a <@printLinkToProfile topic.author />>${topic.author.nickName}</a><span class="d-none d-sm-inline">,</span></div>
                        <span class="d-block d-sm-inline ml-sm-1">${topic.dateCreate}</span>
                    </span>
                    <span class="text-normal text-muted d-block d-md-none">
                         ${topic.postsCount} posts, ${topic.viewsCount} views
                    </span>
                </div>
            </div>
        </div>
        <div class="col-md-2 text-right topic-stats d-none d-md-block">
            <div class="row no-gutters">
                <div class="col-auto col-md-12 text-normal px-3">
                    ${topic.postsCount} posts
                </div>
                <div class="col-auto col-md-12 text-normal text-muted px-3">
                    ${topic.viewsCount} views
                </div>
            </div>
        </div>
        <#if (topic.lastPost)?? && (((hasPermission('ADMIN') || hasPermission('ACTIVATE_POST')) && !topic.lastPost.isActive || topic.lastPost.isActive) && ((hasPermission('ADMIN') || hasPermission('APPROVE_POST')) && !topic.lastPost.isApproved || topic.lastPost.isApproved))>
            <div class="col-3 topic-last-post">
                <div class="row no-gutters justify-content-center justify-content-md-start">
                    <#if (topic.lastPost.author.avatar)??>
                        <div class="col-md-auto">
                            <a <@printLinkToProfile topic.lastPost.author />>
                                <@printAvatar topic.lastPost.author />
                            </a>
                        </div>
                    <#else>
                        <div class="col d-flex flex-grow-0 align-self-center user-avatar-block user-avatar-block-small">
                            <a <@printLinkToProfile topic.lastPost.author />>
                                <i class="fas fa-user-secret" aria-hidden="true"></i>
                            </a>
                        </div>
                    </#if>
                    <div class="col-md-auto d-block d-md-none text-normal">
                        <a <@printLinkToProfile topic.lastPost.author />>${topic.lastPost.author.nickName}</a>
                    </div>
                    <div class="col-md-auto d-block d-md-none text-normal text-muted">
                        ${topic.lastPost.dateLastUpdate}
                    </div>
                    <div class="col-md-auto last-post-block d-none d-md-block">
                        <div class="d-block text-normal text-muted px-3">
                            Author:
                            <a <@printLinkToProfile topic.lastPost.author />>
                                ${topic.lastPost.author.nickName}
                            </a>
                        </div>
                        <div class="d-block text-normal text-muted px-3">${topic.lastPost.dateLastUpdate}</div>
                    </div>
                </div>
            </div>
        </#if>
    </div>
</div>
