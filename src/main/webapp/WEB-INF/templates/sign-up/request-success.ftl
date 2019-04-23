<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Sign up</h2>
            <p>
                Congratulations, ${nickName}! <br />
                You successfully registered at Simple Java Boards!<br />
                To activate your account you should read your mailbox ${email} and follow verification link from us.
                Link valid only within next 24 hours.
                If our post is not delivered to you, you can try resent verification code - just authorize and follow instructions.
            </p>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>