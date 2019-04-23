
<#if user.isBanned>
    <#assign banned=" sjb-banned" />
    <#assign bannedText="Unban" />
<#else>
    <#assign banned="" />
    <#assign bannedText="Ban" />
</#if>
<#if !user.isActivated>
    <#assign notActivated=" sjb-not-activated" />
<#else>
    <#assign notActivated="" />
</#if>
<#if !user.isActive>
    <#assign inActive=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActive="" />
    <#assign activeText="Deactivate" />
</#if>
<div class="card user-block mr-3 my-3${banned}${inActive}${notActivated}">
    <div class="user-top-block">
        <div class="card-header p-3 border-bottom-0">
            <div class="d-flex flex-row flex-sm-column">
                <h5 class="d-none d-md-block align-self-center"><a class="align-self-center" <@printLinkToProfile user />>${user.nickName}</a></h5>
                <#if (user.avatar)??>
                    <a class="align-self-center" <@printLinkToProfile user />>
                        <img class="rounded" src="${user.avatar.imageUrl}" alt="${user.nickName}" title="${user.nickName}" />
                    </a>
                <#else>
                    <div class="col d-flex flex-grow-0 align-self-center user-avatar-block user-avatar-block-big">
                        <a <@printLinkToProfile user />>
                            <i class="fas fa-user-secret" aria-hidden="true"></i>
                        </a>
                    </div>
                </#if>
                <div class="d-flex flex-column ml-3 ml-sm-0 align-self-sm-center align-items-sm-center">
                    <h5 class="m-0 d-md-none"><a <@printLinkToProfile user />>${user.nickName}</a></h5>
                    <span class="mt-2 badge" style="color:#fff; background-color:#${user.groupColor}">${user.groupName}</span>
                    <div class="pt-1">
                        <span class="pr-2 text-success"><i class="fas fa-thumbs-up"></i> ${user.goodKarma}</span>
                        <span class="text-danger"><i class="fas fa-thumbs-down"></i> ${user.badKarma}</span>
                    </div>
                    <div>${user.postsCount} posts</div>
                </div>
            </div>
        </div>
    </div>
</div>
