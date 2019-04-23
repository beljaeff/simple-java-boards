<#if !board.isActive>
    <#assign inActiveClass=" sjb-inactive" />
    <#assign activeText="Activate" />
<#else>
    <#assign inActiveClass="" />
    <#assign activeText="Deactivate" />
</#if>
<#if (hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('ACTIVATE_BOARD')) && !board.isActive || board.isActive>
                <div class="list-group-item board-block px-0${inActiveClass}">
                    <div class="row no-gutters align-self-center mx-3 my-0">
                        <div class="col-md-6 align-self-center">
                            <div class="row no-gutters">
                                <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('DELETE_BOARD') || hasPermission('ACTIVATE_BOARD')>
                                    <div class="col dropdown d-flex flex-grow-0 align-self-center mr-3">
                                        <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-forum" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <span class="board-icon border-muted text-muted">
                                                <i class="${board.icon}"></i>
                                            </span>
                                        </button>
                                        <div class="dropdown-menu dropdown-menu-left" aria-labelledby="edit-board">
                                            <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                                                <a class="dropdown-item" href="${board.link}/edit">Edit</a>
                                                <a class="dropdown-item" href="${board.link}/up">Move up</a>
                                                <a class="dropdown-item" href="${board.link}/down">Move down</a>
                                            </#if>
                                            <#if hasPermission('ADMIN') || hasPermission('DELETE_BOARD')>
                                                <a class="dropdown-item" href="${board.link}/delete">Delete</a>
                                            </#if>
                                            <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('ACTIVATE_BOARD')>
                                                <a class="dropdown-item" href="${board.link}/change-active">${activeText}</a>
                                            </#if>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="col d-flex flex-grow-0 align-self-center mr-3">
                                        <span class="board-icon border-muted text-muted">
                                            <i class="${board.icon}"></i>
                                        </span>
                                    </div>
                                </#if>
                                <div class="col">
                                    <a href="${board.link}"><h5 class="list-group-item-heading">${board.title}</h5></a>
                                    <span class="text-normal">${board.description}</span>
                                    <div class="d-block d-md-none">
                                        <span><strong>${board.postsCount}</strong> posts posted for now</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 text-center align-self-center px-3 d-none d-md-block">
                            <h5 class="d-block m-0">${board.postsCount}</h5>
                            <span class="text-normal">posts</span>
                        </div>
                        <#if (board.lastTopic)?? && (((hasPermission('ADMIN') || hasPermission('ACTIVATE_TOPIC')) && !board.lastTopic.isActive || board.lastTopic.isActive) && ((hasPermission('ADMIN') || hasPermission('APPROVE_TOPIC')) && !board.lastTopic.isApproved || board.lastTopic.isApproved))>
                            <div class="col-md-4 subboard-last-topic align-self-center pl-0 pt-2 mt-2 pt-md-0 mt-md-0">
                                <div class="row no-gutters">
                                    <#if (board.lastTopic.author.avatar)??>
                                        <div class="col d-flex flex-grow-0 align-self-center mr-3">
                                            <a <@printLinkToProfile board.lastTopic.author />>
                                                <@printAvatar board.lastTopic.author />
                                            </a>
                                        </div>
                                    <#else>
                                        <div class="col d-flex flex-grow-0 align-self-center mr-3 user-avatar-block user-avatar-block-small">
                                            <a <@printLinkToProfile board.lastTopic.author />>
                                                <i class="fas fa-user-secret" aria-hidden="true"></i>
                                            </a>
                                        </div>
                                    </#if>
                                    <div class="col text-normal text-truncate">
                                        <div class="d-block d-md-none">Last topic:</div>
                                        <a href="${board.lastTopic.link}"><h6>${board.lastTopic.title}</h6></a>
                                        <div>
                                            Author:
                                            <a <@printLinkToProfile board.lastTopic.author />>
                                                ${board.lastTopic.author.nickName}
                                            </a>
                                        </div>
                                        <div class="text-muted">${board.lastTopic.dateCreate}</div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
</#if>