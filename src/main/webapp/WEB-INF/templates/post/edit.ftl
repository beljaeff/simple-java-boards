<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <form role="document" id="post-edit-form" class="mt-3" enctype="multipart/form-data" method="POST" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.id" />
                <@spring.formHiddenInput "form.idTopic" />
                <@spring.formHiddenInput "form.idParent" /><#--TODO: parent post change-->
                <@spring.formHiddenInput "form.fromPage" />
                <@spring.formHiddenInput "form.toPage" />
                <div class="card">
                    <div class="card-body">
                        <div class="row form-group">
                            <label for="post" class="col-form-label col-sm-12 col-md-2">Message:</label>
                            <div class="col-sm-12 col-md-10">
                                <div class="input-group">
                                    <@spring.formTextarea "form.message" "class=\"form-control\" placeholder=\"Message\"" />
                                </div>
                                <@showValidationErrors "form" "message" />
                            </div>
                        </div>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || hasPermission('EDIT_OWN_POST') && hasPermission('MAKE_STICKY_OWN_POST') && authorId == userPrincipal.id>
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
                        <#if !form.isApproved && (hasPermission('ADMIN') || hasPermission('APPROVE_POST'))>
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
                        <#else>
                            <@spring.formHiddenInput "form.isApproved" />
                        </#if>
                        <#if hasPermission('ADMIN') || hasPermission('EDIT_POST') || hasPermission('ACTIVATE_POST')>
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
                        <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS') || page.entity.attachments??>
                                <div class="row form-group mb-0" id="attachments-block" max-attachments="${maxAttachmentsPerPost?number - page.entity.attachments?size}">
                                    <label class="col-form-label col-sm-12 col-md-2">Attachments:</label>
                                    <div class="col-sm-12 col-md-10">
                                        <#if page.entity.attachments?? && page.entity.attachments?size gt 0>
                                            <div class="mb-3 uploaded-attachments">
                                                <#list page.entity.attachments as attachment>
                                                    <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS')>
                                                        <a class="fas fa-fw fa-trash-alt remove-attachment" href="/attachment/${attachment.id}/delete?from=${form.id}&page=${form.fromPage}"></a>
                                                    </#if>
                                                    <a<#if hasPermission('ADMIN') || hasPermission('VIEW_ATTACHMENTS')> href="/attachment/${attachment.id}"</#if>>${attachment.originalName}</a>
                                                    <br />
                                                </#list>
                                            </div>
                                        </#if>
                                        <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS')>
                                            <div class="attachments"></div>
                                            <button type="button" class="btn btn-primary add-attachment">Add attachment</button>
                                            <@showValidationErrors "form" "attachments" />
                                        </#if>
                                    </div>
                                </div>
                        </#if>
                    </div>
                </div>
                <@showGlobalErrors "form" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg post-edit-form-submit">Save</button>
                    <a class="btn btn-primary btn-lg ml-4" href="${form.cancelUrl}">Cancel</a>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/resources/js/attachments.js" defer></script>
    </body>
</html>