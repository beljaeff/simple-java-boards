<div class="d-none d-sm-flex flex-column ml-3 ml-sm-0 align-self-sm-center align-items-sm-center">
    <div class="menu-block pt-sm-0 mt-sm-3 d-none d-sm-block pt-3">
        <ul class="nav flex-column nav-pills">
            <li class="nav-item"><a href="${currentPageBase}/overview" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "overview"> active</#if>"><i class="fas fa-user-circle fa-fw mr-sm-2"></i>Overview</a></li>
<#if isOwnProfile>
            <li class="nav-item"><a href="${currentPageBase}/security" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "security"> active</#if>"><i class="fas fa-lock fa-fw mr-sm-2"></i>Security</a></li>
            <li class="nav-item"><a class="disabled long nav-link d-flex flex-row align-items-center px-2<#if profileSection == "private-messages"> active</#if>"><i class="fas fa-envelope fa-fw mr-sm-2"></i>Private<br />messages</a></li>
</#if>
            <li class="nav-item"><a href="${currentPageBase}/groups" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "groups"> active</#if>"><i class="fas fa-users fa-fw mr-sm-2"></i>Groups</a></li>
            <li class="nav-item"><a class="disabled nav-link d-flex flex-row align-items-center px-2<#if profileSection == "posts"> active</#if>"><i class="fas fa-comment fa-fw mr-sm-2"></i>Posts</a></li>
            <li class="nav-item"><a class="disabled nav-link d-flex flex-row align-items-center px-2<#if profileSection == "topics"> active</#if>"><i class="fas fa-comments fa-fw mr-sm-2"></i>Topics</a></li>
            <li class="nav-item"><a href="${currentPageBase}/statistics" class="nav-link d-flex flex-row align-items-center px-2<#if profileSection == "statistics"> active</#if>"><i class="fas fa-chart-pie fa-fw mr-sm-2"></i>Statistics</a></li>
        </ul>
    </div>
</div>
