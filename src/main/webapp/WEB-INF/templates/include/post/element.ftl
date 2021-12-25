<#if !post.isApproved>
    <#assign notApproved=" sjb-not-approved" />
<#else>
    <#assign notApproved="" />
</#if>
<#if post.isSticky>
    <#assign sticky=" sjb-sticky" />
    <#assign stickyText="Unsticky" />
<#else>
    <#assign sticky="" />
    <#assign stickyText="Make sticky" />
</#if>
<#if !post.isActive>
    <#assign inActive=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActive="" />
    <#assign activeText="Deactivate" />
</#if>
<div class="card post-block my-3${notApproved}${inActive}${sticky}">
    <div class="post-top-block d-sm-flex flex-sm-row">
        <div class="card-header p-3">
            <div class="d-flex flex-row flex-sm-column">
                <h5 class="d-none d-md-block align-self-center"><a <@printLinkToProfile post.author />>${post.author.nickName}</a></h5>
                <#if (post.author.avatar)??>
                    <a class="align-self-center" <@printLinkToProfile post.author />>
                        <img class="rounded" src="${post.author.avatar.imageUrl}" alt="${post.author.nickName}" title="${post.author.nickName}" />
                    </a>
                <#else>
                    <div class="col d-flex flex-grow-0 align-self-center user-avatar-block user-avatar-block-big">
                        <a <@printLinkToProfile post.author />>
                            <i class="fas fa-user-secret" aria-hidden="true"></i>
                        </a>
                    </div>
                </#if>
                <div class="d-flex flex-column ml-3 ml-sm-0 align-self-sm-center align-items-sm-center">
                    <h5 class="m-0 d-md-none">${post.author.nickName}</h5>
                    <span class="mt-2 badge" style="color:#fff; background-color:#${post.author.groupColor}">${post.author.groupName}</span>
                    <div class="pt-1">
                        <span class="pr-2 text-success"><i class="fas fa-thumbs-up"></i> ${post.author.goodKarma}</span>
                        <span class="text-danger"><i class="fas fa-thumbs-down"></i> ${post.author.badKarma}</span>
                    </div>
                    <div>${post.author.postsCount} posts</div>
                    <#if (post.author.location)??>
                        <div class="text-normal text-muted">${post.author.location}</div>
                    </#if>
                </div>
            </div>
        </div>
        <div class="card-body d-flex flex-column p-3">
            <div class="d-flex justify-content-between">
                <div class="text-muted h7"><i class="far fa-clock"></i> ${post.dateCreate}</div>
                <#assign karmaColor = '' />
                <#if post.karma gt 0>
                    <#assign karmaColor = 'success' />
                <#elseif post.karma lt 0>
                    <#assign karmaColor = 'danger' />
                <#else>
                    <#assign karmaColor = 'secondary' />
                </#if>
                <div><span class="badge badge-${karmaColor}">${post.karma}</span></div>
            </div>
            <p class="card-text">${post.body}</p>
<#--

            (hasPermission('ADMIN') || hasPermission('VIEW_ATTACHMENTS')) &&

-->
            <#if (post.attachments)?? && post.attachments?size gt 0>
                <div class="align-content-end mt-auto pb-3">
                    <hr class="mt-0" />
                    <#assign imagesBegin = 0 />
                    <#assign filesBegin = 0 />
                    <#list post.attachments as attachment>
                        <#if attachment.isImage>
                            <#if imagesBegin == 0>
                                <span class="text-normal text-muted">Gallery:</span>
                                <#assign imagesBegin = 1 />
                            </#if>
                            <img class="rounded mt-2 mr-2" style="cursor:pointer" src="${attachment.previewUrl}" data-toggle="modal" data-target="#attachment-${attachment.id}" />
                            <#include "modal.ftl" />
                        <#else>
                            <#if filesBegin == 0>
                                <#if imagesBegin == 1><br /><br /></#if>
                                <span class="text-normal text-muted">Files: </span>
                                <#assign filesBegin = 1 />
                            </#if>
                            <a href="/attachment/${attachment.id}">${attachment.originalName}</a><#if attachment_has_next>, </#if>
                        </#if>
                    </#list>
                </div>
            </#if>
            <#if (post.author.signature)??>
                <div class="text-normal text-muted align-content-end<#if !((post.attachments)??)> mt-auto</#if>">
                    <hr class="mt-0" />
                    ${post.author.signature}
                </div>
            </#if>
        </div>
    </div>
    <div class="card-footer d-flex justify-content-between p-3">
        <div>
            <#if hasPermission('ADMIN') || hasPermission('MAKE_KARMA_UP')>
                <a href="/post/${post.id}/karma-up${postsPageGetArg}" class="card-link"><i class="far fa-thumbs-up"></i></a>
            </#if>
            <#if hasPermission('ADMIN') || hasPermission('MAKE_KARMA_DOWN')>
                <a href="/post/${post.id}/karma-down${postsPageGetArg}" class="card-link"><i class="far fa-thumbs-down"></i></a>
            </#if>
            <#if hasPermission('ADMIN') || hasPermission('CREATE_POST')>
                <#--TODO: bbcode java and js -->
                <a href="#" class="card-link"><i class="far fa-comment-alt"></i></a>
                <a href="/post/${post.id}/reply${postsPageGetArg}" class="card-link"><i class="fas fa-reply"></i></a>
            </#if>
        </div>
        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || hasPermission('DELETE_POST') || hasPermission('MAKE_STICKY_POST') || hasPermission('ACTIVATE_POST') || !post.isApproved && hasPermission('APPROVE_POST') || post.author.id == userPrincipal.id && hasPermission('EDIT_OWN_POST')>
            <div>
                <div class="dropdown">
                    <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-post-menu"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-edit"></i>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="edit-post-menu">
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || post.author.id == userPrincipal.id && hasPermission('EDIT_OWN_POST')>
                            <a class="dropdown-item" href="/post/${post.id}/edit${postsPageGetArg}">Edit</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('DELETE_POST')>
                            <a class="dropdown-item" href="/post/${post.id}/delete${postsPageGetArg}">Delete</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || hasPermission('MAKE_STICKY_POST') || (hasPermission('MAKE_STICKY_OWN_POST') && hasPermission('EDIT_OWN_POST')) && post.author.id == userPrincipal.id>
                            <a class="dropdown-item" href="/post/${post.id}/change-sticky${postsPageGetArg}">${stickyText}</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || hasPermission('ACTIVATE_POST')>
                            <a class="dropdown-item" href="/post/${post.id}/change-active${postsPageGetArg}">${activeText}</a>
                        </#if>
                        <#if !post.isApproved && (hasPermission('ADMIN') || hasPermission('APPROVE_POST'))>
                            <a class="dropdown-item" href="/post/${post.id}/approve${postsPageGetArg}">Approve</a>
                        </#if>
                    </div>
                </div>
            </div>
        </#if>
    </div>
</div>
