<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <form role="document" id="topic-edit-form" class="mt-3" method="POST" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.id" />
                <@spring.formHiddenInput "form.idBoard" />
                <input type="hidden" name="icon" value="far fa-circle" />
                <div class="card">
                    <div class="card-body">
                        <div class="row form-group">
                            <label for="title" class="col-form-label col-sm-12 col-md-2">Title:</label>
                            <div class="col-sm-12 col-md-4">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-font"></i></div>
                                    </div>
                                    <@spring.formInput "form.title" "class=\"form-control\" placeholder=\"Title\"" />
                                </div>
                                <@showValidationErrors "form" "title" />
                            </div>
                        </div>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('EDIT_OWN_TOPIC') && hasPermission('MAKE_STICKY_OWN_TOPIC') && authorId == userPrincipal.id>
                            <div class="row form-group">
                                <label for="isSticky" class="col-form-label col-sm-12 col-md-2">Sticky:</label>
                                <div class="col-sm-12 col-md-4">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fw fa-bolt"></i></div>
                                        </div>
                                        <@spring.formSingleSelect "form.isSticky" activeList "class=\"form-control custom-select\"" />
                                    </div>
                                </div>
                            </div>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('EDIT_OWN_TOPIC') && hasPermission('LOCK_OWN_TOPIC') && authorId == userPrincipal.id>
                            <div class="row form-group">
                                <label for="isLocked" class="col-form-label col-sm-12 col-md-2">Locked:</label>
                                <div class="col-sm-12 col-md-4">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fw fa-bolt"></i></div>
                                        </div>
                                        <@spring.formSingleSelect "form.isLocked" activeList "class=\"form-control custom-select\"" />
                                    </div>
                                </div>
                            </div>
                        </#if>
                        <#if form.isApproved?? && !form.isApproved && (hasPermission('ADMIN') || hasPermission('APPROVE_TOPIC'))>
                            <div class="row form-group">
                                <label for="isApproved" class="col-form-label col-sm-12 col-md-2">Approved:</label>
                                <div class="col-sm-12 col-md-4">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fw fa-bolt"></i></div>
                                        </div>
                                        <@spring.formSingleSelect "form.isApproved" activeList "class=\"form-control custom-select\"" />
                                    </div>
                                </div>
                            </div>
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_TOPIC') || hasPermission('ACTIVATE_TOPIC')>
                            <div class="row form-group">
                                <label for="isActive" class="col-form-label col-sm-12 col-md-2">Active:</label>
                                <div class="col-sm-12 col-md-4">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fw fa-bolt"></i></div>
                                        </div>
                                        <@spring.formSingleSelect "form.isActive" activeList "class=\"form-control custom-select\"" />
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
                <@showGlobalErrors "form" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg topic-edit-form-submit">Save</button>
                    <a class="btn btn-primary btn-lg ml-4" href="${form.cancelUrl}">Cancel</a>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>