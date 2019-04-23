<!doctype html>
<html lang="en">
    <#include "include/common/head.ftl" />
    <body>
        <#include "include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <h2 class="pt-3">${page.title}</h2>
            <#include "include/category/list.ftl" />
        </main>

        <#include "include/common/footer.ftl" />
    </body>
</html>