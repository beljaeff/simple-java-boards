            <div class="card-header alert-primary d-flex justify-content-between">
                <h6 class="mb-0">
                    Stat:
                    <#if !topicStatPanel>
                        ${page.entity.topics.total} topics |
                    </#if>
                    ${page.entity.postsCount - postsCount} posts
                    <#if (page.entity.boards)?has_content && postsCount gt 0>
                        | ${postsCount} posts in subboards
                    </#if>
                </h6>
            </div>