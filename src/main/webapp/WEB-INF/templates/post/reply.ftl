<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <form role="document" id="reply-post-form" class="mt-3" method="POST" enctype="multipart/form-data" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.idTopic" />
                <@spring.formHiddenInput "form.idParent" />
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
                <@showGlobalErrors "form" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg create-topic-form-submit">Reply</button>
                    <a class="btn btn-primary btn-lg ml-4" href="${form.cancelUrl}">Cancel</a>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
        <script type="text/javascript" src="/resources/js/attachments.js" defer></script>
    </body>
</html>