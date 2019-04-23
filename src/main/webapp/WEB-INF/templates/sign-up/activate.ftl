<#import "/spring.ftl" as spring />
<!doctype html>
<html lang="en">
    <#include "../include/common/head.ftl" />
    <body>
        <#include "../include/common/top.ftl" />

        <main role="main" class="container pb-3">
            <@printBreadcrumbs breadcrumbs />
            <h2>Activate account</h2>
            <p>
                <#if (activationError)??>
                    Activation unsuccessful.
                    <ul>
                        <#if activationError.isBadToken?? && activationError.isBadToken()>
                            <li><@spring.post "user.activation.token.empty" /></li>
                        <#elseif activationError.isNull()>
                            <li><@spring.post "user.activation.not.found" /></li>
                        <#else>
                            <#if activationError.isActivated()>
                                <li><@spring.post "user.activation.already.activated" /></li>
                            </#if>
                            <#if activationError.isAfter()>
                                <li><@spring.post "user.activation.time.exceeded" /></li>
                            </#if>
                        </#if>
                    </ul>
                <#else>
                    Congratulations, ${nickName}! <br />
                    You successfully activated your account at Simple Java Boards!<br />
                    Now you can log in using your account.
                </#if>
            </p>
        </main>

        <#include "../include/common/footer.ftl" />
    </body>
</html>