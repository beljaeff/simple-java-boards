            <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('DELETE_BOARD') || hasPermission('ACTIVATE_BOARD')>
                <div class="dropdown">
                    <button class="btn btn-link dropdown-toggle p-0 card-link" type="button" id="edit-board"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fas fa-edit"></i>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="edit-board-one">
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                            <a class="dropdown-item" href="${page.entity.link}/edit">Edit</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('DELETE_BOARD')>
                            <a class="dropdown-item" href="${page.entity.link}/delete">Delete</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD') || hasPermission('ACTIVATE_BOARD')>
                            <a class="dropdown-item" href="${page.entity.link}/change-active">${activeText}</a>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_BOARD')>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${page.entity.link}/add">Add new board</a>
                        </#if>
                    </div>
                </div>
            </#if>
