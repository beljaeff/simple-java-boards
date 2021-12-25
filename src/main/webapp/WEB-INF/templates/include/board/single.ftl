<#if !page.entity.isActive>
    <#assign inActiveClass=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActiveClass="" />
    <#assign activeText="Deactivate" />
</#if>
<#if page.entity.topics.currentPage gt 1>
    <#assign topicPageGetArg="?page=${page.entity.topics.currentPage}" />
<#else>
    <#assign topicPageGetArg="" />
</#if>

<#assign topicsCount = 0 />
<#assign postsCount = 0 />
<#assign topicStatPanel = false />
<div class="card boards-block mt-4${inActiveClass}">
    <#if (page.entity.boards)?has_content>
        <div class="card-header alert-primary d-flex justify-content-between align-items-center">
            <div><h5 class="mb-1">Boards</h5></div>
            <#include "edit-dropdown.ftl" />
        </div>
        <div class="card-body boards-block p-0">
            <div class="list-group list-group-flush">
                <#list page.entity.boards as board>
                    <#assign topicsCount = topicsCount + board.topicsCount />
                    <#assign postsCount = postsCount + board.postsCount />
                    <#include "element.ftl" />
                </#list>
            </div>
        </div>
        <#if topicsCount gt 0 || postsCount gt 0>
            <#include "stat-topic.ftl" />
        <#else>
            <div class="card-header alert-primary d-flex justify-content-between">
                <h6 class="mb-0">Stat: totally empty</h6>
            </div>
        </#if>
    <#else>
        <div class="card-header alert-primary d-flex justify-content-between align-items-center">
            <div><h5 class="mb-1">Topics</h5></div>
            <#include "edit-dropdown.ftl" />
        </div>
    </#if>

    <div class="card-body topics-block p-0">
        <div class="list-group list-group-flush">
            <#list page.entity.topics.list as topic>
                <#include "../topic/element.ftl" />
            </#list>
        </div>
        <#if !(page.entity.boards)?has_content && page.entity.topics.total == 0>
            <p class="mx-3 mt-3">There is no topics here.</p>
        </#if>
        <#if page.entity.topics.total gt 0>
            <#include "stat-post.ftl" />
        </#if>

        <div class="row no-gutters d-flex flex-column flex-sm-row">
            <#assign paged = page.entity.topics />
            <#assign linkBase = page.entity.link />
            <#if paged.total gt 0 && paged.total gt paged.pageSize && paged.currentPage lte paged.totalPages>
                <nav class="topics-pages m-3 mr-auto" aria-label="Topics pages">
                    <#include "../common/pagination/big.ftl" />
                </nav>
            </#if>
            <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('CREATE_TOPIC')>
                <div class="d-flex flex-grow-1 justify-content-end">
                    <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                        <a href="/board/${page.entity.id}/add" class="btn btn-primary m-3">New board</a>
                    </#if>
                    <#if hasPermission('ADMIN') || hasPermission('CREATE_TOPIC')>
                        <a href="/board/${page.entity.id}/create-topic" class="btn btn-primary m-3">Create topic</a>
                    </#if>
                </div>
            </#if>
        </div>
    </div>
</div>