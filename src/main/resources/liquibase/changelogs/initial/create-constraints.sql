-- TODO: check and test all constaints

ALTER TABLE boards ADD CONSTRAINT fk_boards_parent
                    FOREIGN KEY (id_parent)
                    REFERENCES boards (id) ON DELETE CASCADE;

ALTER TABLE boards ADD CONSTRAINT fk_boards_category
                    FOREIGN KEY (id_category)
                    REFERENCES categories(id) ON DELETE CASCADE;

ALTER TABLE boards ADD CONSTRAINT fk_boards_last_topic
                    FOREIGN KEY (id_last_topic)
                    REFERENCES topics(id) ON DELETE SET NULL;


ALTER TABLE topics ADD CONSTRAINT fk_topics_board
                    FOREIGN KEY (id_board)
                    REFERENCES boards(id) ON DELETE CASCADE;

ALTER TABLE topics ADD CONSTRAINT fk_topics_first_post
                    FOREIGN KEY (id_first_post)
                    REFERENCES posts(id) ON DELETE SET NULL;

ALTER TABLE topics ADD CONSTRAINT fk_topics_last_post
                    FOREIGN KEY (id_last_post)
                    REFERENCES posts(id) ON DELETE SET NULL;

ALTER TABLE topics ADD CONSTRAINT fk_topics_author
                    FOREIGN KEY (id_author)
                    REFERENCES users(id) ON DELETE CASCADE;


ALTER TABLE posts ADD CONSTRAINT fk_posts_topic
                    FOREIGN KEY (id_topic)
                    REFERENCES topics(id) ON DELETE CASCADE;

ALTER TABLE posts ADD CONSTRAINT fk_posts_parent
                    FOREIGN KEY (id_parent)
                    REFERENCES posts(id) ON DELETE SET NULL;

ALTER TABLE posts ADD CONSTRAINT fk_posts_author
                    FOREIGN KEY (id_author)
                    REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE posts ADD CONSTRAINT fk_posts_user_last_update
                    FOREIGN KEY (id_user_last_update)
                    REFERENCES users(id) ON DELETE SET NULL;


ALTER TABLE private_messages ADD CONSTRAINT fk_private_messages_parent
                    FOREIGN KEY (id_parent)
                    REFERENCES private_messages(id) ON DELETE SET NULL;

ALTER TABLE private_messages ADD CONSTRAINT fk_private_messages_sender
                    FOREIGN KEY (id_sender)
                    REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE private_messages ADD CONSTRAINT fk_private_messages_recipient
                    FOREIGN KEY (id_recipient)
                    REFERENCES users(id) ON DELETE CASCADE;


ALTER TABLE post_attachments ADD CONSTRAINT fk_post_attachments_post
                    FOREIGN KEY (post_id)
                    REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE post_attachments ADD CONSTRAINT fk_post_attachments_attachment
                    FOREIGN KEY (attachment_id)
                    REFERENCES attachments (id) ON DELETE CASCADE;

ALTER TABLE private_message_attachments ADD CONSTRAINT fk_private_message_attachments_message
                    FOREIGN KEY (message_id)
                    REFERENCES private_messages (id) ON DELETE CASCADE;

ALTER TABLE private_message_attachments ADD CONSTRAINT fk_private_message_attachments_attachment
                    FOREIGN KEY (attachment_id)
                    REFERENCES attachments (id) ON DELETE CASCADE;


ALTER TABLE users ADD CONSTRAINT fk_user_avatar
                    FOREIGN KEY (avatar)
                    REFERENCES attachments (id) ON DELETE SET NULL;


ALTER TABLE group_permissions ADD CONSTRAINT fk_group_permissions_group
                    FOREIGN KEY (group_id)
                    REFERENCES groups (id) ON DELETE CASCADE;

ALTER TABLE group_permissions ADD CONSTRAINT fk_group_permissions_permission
                    FOREIGN KEY (permission_id)
                    REFERENCES permissions (id) ON DELETE CASCADE;

ALTER TABLE category_moderators ADD CONSTRAINT fk_category_moderators_category
                    FOREIGN KEY (category_id)
                    REFERENCES categories (id) ON DELETE CASCADE;

ALTER TABLE category_moderators ADD CONSTRAINT fk_category_moderators_user
                    FOREIGN KEY (user_id)
                    REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE board_moderators ADD CONSTRAINT fk_board_moderators_board
                    FOREIGN KEY (board_id)
                    REFERENCES boards (id) ON DELETE CASCADE;

ALTER TABLE board_moderators ADD CONSTRAINT fk_board_moderators_user
                    FOREIGN KEY (user_id)
                    REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE topic_moderators ADD CONSTRAINT fk_topic_moderators_topic
                    FOREIGN KEY (topic_id)
                    REFERENCES topics (id) ON DELETE CASCADE;

ALTER TABLE topic_moderators ADD CONSTRAINT fk_topic_moderators_user
                    FOREIGN KEY (user_id)
                    REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_groups ADD CONSTRAINT fk_user_groups_user
                    FOREIGN KEY (user_id)
                    REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE user_groups ADD CONSTRAINT fk_user_groups_group
                    FOREIGN KEY (group_id)
                    REFERENCES groups (id) ON DELETE CASCADE;


ALTER TABLE board_groups ADD CONSTRAINT fk_board_groups_board
                    FOREIGN KEY (board_id)
                    REFERENCES boards (id) ON DELETE CASCADE;

ALTER TABLE board_groups ADD CONSTRAINT fk_board_groups_group
                    FOREIGN KEY (group_id)
                    REFERENCES groups (id) ON DELETE CASCADE;
