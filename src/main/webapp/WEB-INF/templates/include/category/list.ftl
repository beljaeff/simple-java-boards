<#list page.entity.list as category>
    <#assign topicsCount = 0 />
    <#assign postsCount = 0 />
    <#assign tableTitle = category.title />
    <#assign tableLink = category.link />
    <#include "single.ftl" />
</#list>
<#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('EDIT_CATEGORY')>
    <div class="row no-gutters d-flex flex-column flex-sm-row">
        <div class="d-flex flex-grow-1 justify-content-end">
            <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                <a href="/board/add" class="btn btn-primary mt-3 mb-0 mr-3">New board</a>
            </#if>
            <#if hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY')>
                <a href="/category/add" class="btn btn-primary mt-3 mb-0">New category</a>
            </#if>
        </div>
    </div>
</#if>
