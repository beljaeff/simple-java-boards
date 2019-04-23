<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Reset password</h2>
            <p>Can not proceed reset password.</p>
            <@printList resetPasswordErrors />
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>