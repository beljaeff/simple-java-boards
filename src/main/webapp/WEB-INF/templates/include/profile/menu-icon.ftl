<div class="menu-block mb-3 d-flex flex-row d-sm-none align-self-center border-0">
    <ul class="nav d-flex flex-row nav-pills">
        <li class="nav-item"><a href="${currentPageBase}/overview" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "overview"> active</#if>"><i class="fas fa-user-circle fa-fw mr-sm-2"></i></a></li>
<#if isOwnProfile>
        <li class="nav-item"><a href="${currentPageBase}/security" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "security"> active</#if>"><i class="fas fa-lock fa-fw mr-sm-2"></i></a></li>
        <li class="nav-item"><a class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "private-messages"> active</#if>"><i class="fas fa-envelope fa-fw mr-sm-2"></i></a></li>
</#if>
        <li class="nav-item"><a href="${currentPageBase}/groups" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "groups"> active</#if>"><i class="fas fa-users fa-fw mr-sm-2"></i></a></li>
        <li class="nav-item"><a class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "posts"> active</#if>"><i class="fas fa-comment fa-fw mr-sm-2"></i></a></li>
        <li class="nav-item"><a class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "topics"> active</#if>"><i class="fas fa-comments fa-fw mr-sm-2"></i></a></li>
        <li class="nav-item"><a href="${currentPageBase}/statistics" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "statistics"> active</#if>"><i class="fas fa-chart-pie fa-fw mr-sm-2"></i></a></li>
    </ul>
</div>
