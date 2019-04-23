<#assign paged = page.entity.list />
<#assign linkBase = '/profile' />
<#if paged.total gt 0 && paged.total gt paged.pageSize && paged.currentPage lte paged.totalPages>
    <nav class="users-pages mt-3" aria-label="Users pages">
        <#include "../common/pagination/big.ftl" />
    </nav>
</#if>

<div class="card border-0">
    <div class="card-body users-block d-flex flex-wrap p-0">
        <#list page.entity.list.list as user>
            <#include "element.ftl" />
        </#list>
    </div>
</div>

<#if paged.total gt 0 && paged.total gt paged.pageSize && paged.currentPage lte paged.totalPages>
    <nav class="users-pages pb-3" aria-label="Users pages">
        <#include "../common/pagination/big.ftl" />
    </nav>
</#if>