<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>${page.title}</h2>
            <#include "../include/topic/single.ftl" />
        </main>

        <#include "../include/common/footer.ftl" />
    <script type="text/javascript" src="/resources/js/attachments.js" defer></script>
    </body>
</html>