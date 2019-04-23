                                        <#import "/spring.ftl" as spring />
                                        <h3 class="font-weight-light pb-2">Groups - add new</h3>
            <form role="document" id="add-user-group-form" class="mt-3" method="POST" action="${currentPageBase}/groups/add">
                <@spring.formHiddenInput "addUserGroupForm.idUser" />
                <div class="card">
                    <div class="card-body pb-0">
                        <div class="row form-group">
                            <label for="title" class="col-form-label col-sm-3">Group:</label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-users"></i></div>
                                    </div>
                                    <@groupSelect "addUserGroupForm.idGroup" groups "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "addUserGroupForm" "idGroup" />
                            </div>
                        </div>
                    </div>
                </div>
                <@showValidationErrors "addUserGroupForm" "idUser" />
                <@showGlobalErrors "addUserGroupForm" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary add-user-group-form-submit">Save</button>
                    <a class="btn btn-primary ml-2" href="${currentPageBase}/groups">Cancel</a>
                </div>
            </form>
