<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <form role="document" id="category-form" class="mt-3" method="POST" action="${form.saveUrl}">
                <@spring.formHiddenInput "form.id" />
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
                        <div class="row form-group">
                            <label for="isActive" class="col-form-label col-sm-12 col-md-2">Active:</label>
                            <div class="col-sm-12 col-md-4">
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-fw fa-bolt"></i></div>
                                    </div>
                                    <@spring.formSingleSelect "form.isActive" activeList "class=\"form-control custom-select\"" />
                                </div>
                                <@showValidationErrors "form" "isActive" />
                            </div>
                        </div>
                        <#if form.id gt 0>
                            <div class="row form-group">
                                <label for="position" class="col-form-label col-sm-12 col-md-2">Position:</label>
                                <div class="col-sm-12 col-md-4">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-fw fa-list-ol"></i></div>
                                        </div>
                                        <@spring.formInput "form.position" "class=\"form-control\" placeholder=\"Position\"" />
                                    </div>
                                    <@showValidationErrors "form" "position" />
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
                <@showGlobalErrors "form" />
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary btn-lg category-form-submit">Save</button>
                    <a class="btn btn-primary btn-lg ml-4" href="${form.cancelUrl}">Cancel</a>
                </div>
            </form>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>