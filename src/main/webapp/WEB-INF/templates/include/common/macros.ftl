<#macro printAvatar user>
    <img class="rounded" src="${user.avatar.previewUrl}" alt="${user.nickName}" title="${user.nickName}" />
</#macro>

<#--
 * printBreadcrumbs
 *
 * Prints breadcrumbs from simple hash.
 *
 * @param link - url given.
 * @param title - title for the url.
-->
<#macro printBreadcrumbs breadcrumbs>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb pl-0 pr-0 mb-0">
            <#if (breadcrumbs)??>
                <#list breadcrumbs as bc>
                    <li class="breadcrumb-item"><a href="${bc.link}">${bc.title}</a></li>
                </#list>
            </#if>
        </ol>
    </nav>
</#macro>

<#--
 * showValidationErrors
 *
 * Show validation errors for the given field of the object with name resolved by given path.
 *
 * @param path - name of the validation object.
 * @param field - name of the checked field.
-->
<#macro showValidationErrors path field>
    <div class="${field}-feedback invalid-feedback">
        <#assign errors = springMacroRequestContext.getErrors(path).getFieldErrors(field) />
        <#list errors as error>
            <b>${error.defaultMessage}</b>
            <#if error_has_next><br /></#if>
        </#list>
    </div>
</#macro>

<#--
 * showGlobalError
 *
 * Find and show global error (by given constraint) for the object with name resolved by given path.
 *
 * @param path - name of the validation object.
 * @param constraintName - name of the constraint to find error.
-->
<#macro showGlobalError path constraintName>
    <div class="${constraintName}-feedback invalid-feedback">
        <#assign errors = springMacroRequestContext.getErrors(path).getGlobalErrors() />
        <#list errors as error>
            <#if error.code?starts_with(constraintName)>
                <b>${error.defaultMessage}</b>
                <#if error_has_next><br /></#if>
            </#if>
        </#list>
    </div>
</#macro>

<#--
 * showGlobalErrors
 *
 * Show all global errors for the object with name resolved by given path.
 *
 * @param path - name of the validation object.
-->
<#macro showGlobalErrors path>
    <div class="invalid-feedback">
        <#assign errors = springMacroRequestContext.getErrors(path).getGlobalErrors() />
        <#list errors as error>
            <b>${error.defaultMessage}</b>
        </#list>
    </div>
</#macro>

<#--
 * printList
 *
 * Prints <ul> from given list.
 *
 * @param list - given list.
-->
<#macro printList list>
    <ul>
        <#list list as element>
            <li>${element}</li>
        </#list>
    </ul>
</#macro>

<#--
 * printLinkToProfile
 *
 * Prints href='...' for uset profile or nothing in case insufficient permissions.
 *
 * @param userId
-->
<#macro printLinkToProfile user>
    <#if hasPermission('VIEW_PROFILE') || hasPermission('ADMIN')>
        href="${user.linkToProfile}"
    </#if>
</#macro>

<#--
 * hasPermission
 *
 * Function return true if curent user has given permission, false otherwise.
 *
 * @param permission to check
-->
<#function hasPermission permission>
    <#return userPermissions?seq_contains(permission)>
</#function>

<#--
 * groupSelect
 *
 * Show a selectbox (dropdown) input element allowing a single value to be chosen
 * from a list of options.
 *
 * @param path the name of the field to bind to
 * @param options a map (value=label) of all the available options
 * @param attributes any additional attributes for the element
 *    (such as class or CSS styles or size)
-->
<#macro groupSelect path groups attributes="">
    <@spring.bind path/>
    <select id="${spring.status.expression?replace('[','')?replace(']','')}" name="${spring.status.expression}" ${attributes}>
        <#list groups as group>
            <option value="${group.id}"<@spring.checkSelected group.id/>>${group.name} (${group.description})</option>
        </#list>
    </select>
</#macro>
