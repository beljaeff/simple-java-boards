<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <form role="document" id="create-topic-form" enctype="multipart/form-data" class="mt-3" method="POST" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.idBoard" />
                <input type="hidden" name="icon" value="far fa-circle" />
                <div class="card">
                    <div class="card-body">
                        <div class="row form-group">
                            <label for="title" class="col-form-label col-sm-12 col-md-2">Title:</label>
                            <div class="col-sm-12 col-md-10">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-font"></i></div>
                                    </div>
                                    <@spring.formInput "form.title" "class=\"form-control\" placeholder=\"Title\"" />
                                </div>
                                <@showValidationErrors "form" "title" />
                            </div>
                        </div>
                        <div class="row form-group">
                            <label for="post" class="col-form-label col-sm-12 col-md-2">Message:</label>
                            <div class="col-sm-12 col-md-10">
                                <div class="input-group">
                                    <@spring.formTextarea "form.message" "class=\"form-control\" placeholder=\"Message\"" />
                                </div>
                                <@showGlobalErrors "form" />
                                <@showValidationErrors "form" "message" />
                            </div>
                        </div>
                        <#if hasPermission('ADMIN') || hasPermission('CREATE_ATTACHMENTS')>
                            <div class="row form-group mb-0" id="attachments-block" max-attachments="${maxAttachmentsPerPost}">
                                <label class="col-form-label col-sm-12 col-md-2">Attachments:</label>
                                <div class="col-sm-12 col-md-10">
                                    <div class="attachments"></div>
                                    <button type="button" class="btn btn-primary add-attachment">Add attachment</button>
                                    <@showValidationErrors "form" "attachments" />
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg create-topic-form-submit">Create topic</button>
                    <a class="btn btn-primary btn-lg ml-4" href="${form.cancelUrl}">Cancel</a>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/resources/js/attachments.js" defer></script>
    </body>
</html>