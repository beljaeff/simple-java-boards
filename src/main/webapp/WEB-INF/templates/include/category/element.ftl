        <div class="card-header alert-primary d-flex justify-content-between align-items-center">
            <div><h5 class="mb-1"><#if (tableLink)?? && tableLink?length gt 0><a href="${tableLink}"></#if>${tableTitle}<#if (tableLink)??></a></#if></h5></div>
            <#if category.id?number gt 0 && (hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY') || hasPermission('DELETE_CATEGORY') || hasPermission('ACTIVATE_CATEGORY') || hasPermission('EDIT_BOARD'))>
                <div class="dropdown">
                    <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-category"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-edit"></i>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="edit-category">
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY')>
                            <a class="dropdown-item" href="${category.link}/edit">Edit</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('DELETE_CATEGORY')>
                            <a class="dropdown-item" href="${category.link}/delete">Delete</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY') || hasPermission('ACTIVATE_CATEGORY')>
                            <a class="dropdown-item" href="${category.link}/change-active">${activeText}</a>
                        </#if>
                        <#if page.entity.list?? && (hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY'))>
                            <a class="dropdown-item" href="${category.link}/up">Move up</a>
                            <a class="dropdown-item" href="${category.link}/down">Move down</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                            <#if hasPermission('ADMIN') || hasPermission('EDIT_CATEGORY') || hasPermission('DELETE_CATEGORY') || hasPermission('ACTIVATE_CATEGORY')>
                                <div class="dropdown-divider"></div>
                            </#if>
                            <a class="dropdown-item" href="${category.link}/add">Add new board</a>
                        </#if>
                    </div>
                </div>
            </#if>
        </div>
