<#include "header.ftlh">

<div class="row">
    <div class="col-12" id="main">

        <form method="post" action="result">
            <div class="form-row align-items-center">
                <div class="col-6 my-1">
                    <input type="text" class="form-control" name="url" placeholder="https://itunes.apple.com/../playlist/...">
                </div>
                <div class="col-auto my-1">
                    <button type="submit" class="btn btn-primary">Search</button>
                </div>
            </div>
        </form>

    </div>
</div>

<#include "footer.ftlh">