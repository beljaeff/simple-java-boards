<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Reset password</h2>
            <p>
                Reset password request performed for user ${nickName}.
                To reset password you should read your mailbox and follow instructions inside letter from us.
            </p>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>