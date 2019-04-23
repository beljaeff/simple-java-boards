    <ul class="pagination pagination-sm m-0">
        <#list [1, 2, 5, 10, 59, 100] as i>
            <#if i lte totalPages>
                <li class="page-item">
                    <a class="page-link" href="${linkBase}?page=${i}">${i}</a>
                </li>
            </#if>
        </#list>
        <li class="page-item">
            <a class="page-link" href="${linkBase}?page=${totalPages}"><i class="fas fa-angle-double-right" aria-hidden="true"></i></a>
        </li>
    </ul>