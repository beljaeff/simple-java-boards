<!doctype html>
<html lang="en">
    <#include "include/common/head.ftl" />
    <body class="mt-0">

        <main role="main" class="container container-dark">
            <div class="d-flex align-items-center flex-column">
                <div class="error-block py-4 d-flex flex-column align-items-center">
                    <div class="jumbotron m-0 p-0">
                        <h1><i class="fa fa-spin fa-cog fa-5x"></i></h1>
                    </div>
                    <h2 class="display-1">${data.status}</h2>
                    <h3 class="display-4">ERROR</h3>
                    <div class="d-flex flex-column">
                        <p class="lower-case">${data.reason}</p>
                    </div>
                    <a href="/" class="btn btn-primary">visit the main page</a>
                </div>
            </div>
        </main>

    </body>
</html>