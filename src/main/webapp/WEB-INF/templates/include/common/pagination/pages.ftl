        <#list [-100, -50, -25, -10, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 10, 25, 50, 100] as i>
            <#if i lt 0>
                <#if paged.currentPage + i gt 0>
                    <li class="page-item<#if i lt -2> d-none d-md-block</#if>">
                        <a class="page-link" href="${linkBase}?page=${paged.currentPage + i}">
                            ${paged.currentPage + i}
                        </a>
                    </li>
                </#if>
            <#elseif i gt 0>
                <#if paged.currentPage + i lte paged.totalPages>
                    <li class="page-item<#if i gt 2> d-none d-md-block</#if>">
                        <a class="page-link" href="${linkBase}?page=${paged.currentPage + i}">
                            ${paged.currentPage + i}
                        </a>
                    </li>
                </#if>
            <#else>
                <li class="page-item active">
                    <span class="page-link">${paged.currentPage}<span class="sr-only">(current)</span></span>
                </li>
            </#if>
        </#list>