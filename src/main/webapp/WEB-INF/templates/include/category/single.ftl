<#if !category.isActive>
    <#assign inActiveClass=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActiveClass="" />
    <#assign activeText="Deactivate" />
</#if>
<div class="card boards-block mt-4${inActiveClass}">
    <#include "element.ftl" />
    <div class="card-body boards-block p-0">
        <div class="list-group list-group-flush">
            <#if category.boards?? && category.boards?size gt 0>
                <#list category.boards as board>
                    <#assign topicsCount = topicsCount + board.topicsCount />
                    <#assign postsCount = postsCount + board.postsCount />
                    <#include "../board/element.ftl" />
                </#list>
            <#else>
                <p class="m-3">There is no boards here.</p>
            </#if>
        </div>
    </div>
    <#include "stat-topic.ftl" />
</div>
