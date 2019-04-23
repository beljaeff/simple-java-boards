<ul class="pagination mb-0">
    <li class="page-item<#if paged.currentPage == 1> disabled</#if>">
        <a class="page-link" href="${linkBase}?page=1">
            <i class="fas fa-angle-double-left" aria-hidden="true"></i>
        </a>
    </li>
    <li class="page-item<#if paged.currentPage == 1> disabled</#if> d-none d-md-block">
        <a class="page-link" href="${linkBase}?page=${paged.currentPage - 1}">
            <i class="fas fa-angle-left" aria-hidden="true"></i>
        </a>
    </li>
    <#include "pages.ftl" />
    <li class="page-item<#if paged.currentPage == paged.totalPages> disabled</#if> d-none d-md-block">
        <a class="page-link" href="${linkBase}?page=${paged.currentPage + 1}">
            <i class="fas fa-angle-right" aria-hidden="true"></i>
        </a>
    </li>
    <li class="page-item<#if paged.currentPage == paged.totalPages> disabled</#if>">
        <a class="page-link" href="${linkBase}?page=${paged.totalPages}">
            <i class="fas fa-angle-double-right" aria-hidden="true"></i>
        </a>
    </li>
</ul>
