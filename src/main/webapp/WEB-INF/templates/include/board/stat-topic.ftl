        <#assign topicStatPanel = true>
        <div class="card-header alert-primary d-flex justify-content-between">
            <h6 class="mb-0">
                Stat:
                <#if page.entity.topics.total gt 0>
                    ${page.entity.topics.total} topics |
                </#if>
                <#if topicsCount gt 0>
                    ${topicsCount} topics in subboards
                </#if>
                <#if page.entity.topics.total == 0 && postsCount gt 0>
                    <#if topicsCount gt 0> | </#if>
                    ${postsCount} posts in subboards
                </#if>
            </h6>
        </div>
