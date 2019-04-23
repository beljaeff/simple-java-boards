package com.github.beljaeff.sjb.model;

public class EntityGraphs {
    public static final String HINT_FETCH = "javax.persistence.fetchgraph";

    public static final String CATEGORIES_WITH_BOARDS = "categoriesWith.boards";
    public static final String BOARDS_WITH_LAST_TOPIC = "boardsWith.lastTopic";
    public static final String BOARD_WITH_CHILD_BOARDS = "boardWith.childBoards";
    public static final String TOPIC_WITH_LAST_POST_AND_AUTHOR = "topicWith.lastPost.author";
    public static final String TOPIC_WITH_BOARD_AND_AUTHOR = "topicWith.board.author";
    public static final String POST_EXCEPT_TOPIC_AND_CHILD_POSTS = "postExcept.topic.childPosts";
    public static final String USERS_WITH_AVATAR = "usersWith.avatar";
    public static final String USERS_WITH_AVATAR_AND_GROUPS = "usersWith.avatar.groups";
    public static final String USERS_WITH_AVATAR_AND_GROUPS_AND_POSTS = "usersWith.avatar.groups.posts";
    public static final String USERS_WITH_GROUPS_WITH_PERMISSIONS = "usersWith.groupsWith.permissions";
    public static final String GROUPS_WITH_PERMISSIONS = "groupsWith.permissions";
    public static final String GROUPS_WITH_OWNERS = "groupsWith.owners";
    public static final String POST_WITH_ATTACHMENTS_AND_TOPIC_WITH_BOARD = "postWithAttachmentsAndTopicWithBoard";
    public static final String POST_WITH_AUTHOR_AND_ATTACHMENTS = "postWithAttachments";
}