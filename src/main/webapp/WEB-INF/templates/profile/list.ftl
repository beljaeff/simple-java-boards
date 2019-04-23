<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs page.breadcrumbs />
            <h2>User profiles</h2>
            <#include "../include/profile/list.ftl" />
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>