<#include "header.ftlh">

    <#list urlList as list>

        <iframe width="560" height="315" src="https://www.youtube.com/embed/${list}?rel=0" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

        <#if list_has_next>
            <hr class="my-4">
        </#if>

    </#list>

<#include "footer.ftlh">