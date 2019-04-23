<#if !page.entity.isApproved>
    <#assign topicNotApproved=" sjb-not-approved" />
<#else>
    <#assign topicNotApproved="" />
</#if>
<#if page.entity.isSticky>
    <#assign topicSticky=" sjb-sticky" />
<#else>
    <#assign topicSticky="" />
</#if>
<#if page.entity.isLocked>
    <#assign topicLocked=" sjb-locked" />
<#else>
    <#assign topicLocked="" />
</#if>
<#if !page.entity.isActive>
    <#assign topicInActive=" sjb-inactive" />
<#else>
    <#assign topicInActive="" />
</#if>
<#if page.entity.posts.currentPage gt 1>
    <#assign postsPageGetArg="?page=${page.entity.posts.currentPage}" />
<#else>
    <#assign postsPageGetArg="" />
</#if>
<div class="card card-header mt-3 topic-header-block p-3${topicNotApproved}${topicInActive}${topicLocked}">
    <div class="d-flex justify-content-between align-items-center">
        <div class="d-flex justify-content-between align-items-center">
            <div class="mr-2">
                <#if (page.entity.author.avatar)??>
                    <a <@printLinkToProfile page.entity.author />>
                        <@printAvatar page.entity.author />
                    </a>
                <#else>
                    <div class="col d-flex flex-grow-0 align-self-center user-avatar-block user-avatar-block-small">
                        <a <@printLinkToProfile page.entity.author />>
                            <i class="fas fa-user-secret" aria-hidden="true"></i>
                        </a>
                    </div>
                </#if>
            </div>
            <div class="ml-2">
                <div class="h5 m-0"><a <@printLinkToProfile page.entity.author />>${page.entity.author.nickName}</a></div>
                <div class="h7 text-muted">${page.entity.dateCreate}</div>
            </div>
        </div>
        <div class="d-none d-md-block">
            Total ${page.entity.postsCount} posts here<br />
            <#if page.entity.isLocked>
                This topic is locked. Can not post here.
            </#if>
        </div>
    </div>
    <div class="d-block d-md-none">
        Total ${page.entity.postsCount} posts here<br />
        <#if page.entity.isLocked>
            This topic is locked. Can not post here.
        </#if>
    </div>
</div>

<#assign paged = page.entity.posts />
<#assign linkBase = '/topic/' + page.entity.id />
<#if paged.total gt 0 && paged.total gt paged.pageSize && paged.currentPage lte paged.totalPages>
    <nav class="posts-pages mt-3" aria-label="Posts pages">
        <#include "../common/pagination/big.ftl" />
    </nav>
</#if>
<div class="card border-0">
    <div class="card-body posts-block p-0${topicNotApproved}${topicInActive}${topicLocked}">
        <#list page.entity.posts.list as post>
            <#include "../post/element.ftl" />
        </#list>
        <#if !page.entity.isLocked && (hasPermission('ADMIN') || hasPermission('CREATE_POST'))>
            <form id="quick-post-form" method="POST" enctype="multipart/form-data" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.idTopic" />
                <@spring.formHiddenInput "form.fromPage" />
                <div class="card post-block my-3">
                    <div class="card-header">
                        <ul class="nav nav-tabs card-header-tabs" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" id="reply-tab" data-toggle="tab" href="#reply" role="tab" aria-controls="reply"
                                   aria-selected="true">Message</a>
                            </li>
                            <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS')>
                                <li class="nav-item">
                                    <a class="nav-link" id="uploads-tab" data-toggle="tab" role="tab" aria-controls="uploads" aria-selected="false"
                                       href="#uploads">Uploads</a>
                                </li>
                            </#if>
                        </ul>
                    </div>
                    <div class="card-body">
                        <div class="tab-content" id="myTabContent">
                            <div class="tab-pane fade show active" id="reply" role="tabpanel" aria-labelledby="reply-tab">
                                <div class="form-group">
                                    <label class="sr-only" for="post">post</label>
                                    <@spring.formTextarea "form.message" "class=\"form-control\" rows=\"3\" placeholder=\"Message\"" />
                                    <@showValidationErrors "form" "message" />
                                </div>
                                <div class="btn-group">
                                    <button type="submit" class="btn btn-primary">Quick reply</button>
                                </div>
                            </div>
                            <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS')>
                                <div class="tab-pane fade" id="uploads" role="tabpanel" aria-labelledby="uploads-tab">
                                    <div class="row form-group mb-0" id="attachments-block" max-attachments="${maxAttachmentsPerPost}">
                                        <label class="col-form-label col-sm-12 col-md-2">Attachments:</label>
                                        <div class="col-sm-12 col-md-10">
                                            <div class="attachments"></div>
                                            <button type="button" class="btn btn-primary add-attachment">Add attachment</button>
                                            <@showValidationErrors "form" "attachments" />
                                        </div>
                                    </div>
                                    <div class="py-4"></div>
                                </div>
                            </#if>
                            <@showValidationErrors "form" "idTopic" />
                            <@showValidationErrors "form" "idParent" />
                            <@showGlobalErrors "form" />
                        </div>
                    </div>
                </div>
            </form>
        </#if>
    </div>
</div>

<#if paged.total gt 0 && paged.total gt paged.pageSize && paged.currentPage lte paged.totalPages>
    <nav class="posts-pages pb-3" aria-label="Posts pages">
        <#include "../common/pagination/big.ftl" />
    </nav>
</#if>