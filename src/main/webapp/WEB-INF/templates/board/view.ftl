<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            ${page.entity.description}
            <#include "../include/board/single.ftl" />
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>