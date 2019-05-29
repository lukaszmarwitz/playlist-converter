<#include "header.ftlh">

<div class="row">
    <div class="col-12" id="page_result">

    <a class="nav-link font-weight-light" href="search">NEW SEARCH</a>
    <div id="divider"></div>
    <button onclick="topFunction()" id="btn_top" title="Go to top">Top</button>

    <#list videoList as list>

        <p class="font-weight-light">${list.id}. ${list.artist} - ${list.title}</p>
        <iframe src="https://www.youtube.com/embed/${list.token}?rel=0" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

        <#if list_has_next>
            <hr class="my-4">
        </#if>

    </#list>

    </div>
</div>

<script>

    window.onscroll = function() {
        scrollFunction()
    };

    function scrollFunction() {
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
            document.getElementById("btn_top").style.display = "block";
        } else {
            document.getElementById("btn_top").style.display = "none";
        }
    }

    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    }

</script>

<#include "footer.ftlh">