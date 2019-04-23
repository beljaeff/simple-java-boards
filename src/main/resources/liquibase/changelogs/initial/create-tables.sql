CREATE SEQUENCE sjb_id_seq START 100;
CREATE SEQUENCE sjb_position_seq START 100;

CREATE TABLE categories (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  position                     INTEGER NOT NULL DEFAULT nextval('sjb_position_seq'),
  title                        VARCHAR(128) NOT NULL,
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE boards (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  id_category                  INTEGER,
  id_parent                    INTEGER,
  position                     INTEGER NOT NULL DEFAULT nextval('sjb_position_seq'),
  title                        VARCHAR(128) NOT NULL,
  description                  TEXT,
  icon                         VARCHAR(32) NOT NULL DEFAULT 'fas fa-comments',
  id_last_topic                INTEGER,
  topics_count                 INTEGER NOT NULL DEFAULT '0',
  posts_count                  INTEGER NOT NULL DEFAULT '0',
  is_active                    BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT board_unique_idx UNIQUE (title, id_category, id_parent, is_active)
);

CREATE TABLE topics (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  id_board                     INTEGER NOT NULL,
  title                        VARCHAR(128) NOT NULL,
  is_sticky                    BOOLEAN NOT NULL DEFAULT FALSE,
  id_first_post                INTEGER,
  id_last_post                 INTEGER,
  id_author                    INTEGER NOT NULL,
  icon                         VARCHAR(32) NOT NULL DEFAULT 'fa fa-circle',
  date_create                  TIMESTAMP NOT NULL DEFAULT NOW(),
  posts_count                  INTEGER NOT NULL DEFAULT '0',
  views_count                  INTEGER NOT NULL DEFAULT '0',
  is_locked                    BOOLEAN NOT NULL DEFAULT FALSE,
  is_approved                  BOOLEAN NOT NULL DEFAULT FALSE,
  is_active                    BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE posts (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  id_topic                     INTEGER NOT NULL,
  id_parent                    INTEGER,
  is_sticky                    BOOLEAN NOT NULL DEFAULT FALSE,
  ip_create                    VARCHAR(15) NOT NULL,
  id_author                    INTEGER NOT NULL,
  date_create                  TIMESTAMP NOT NULL DEFAULT NOW(),
  ip_last_update               VARCHAR(15),
  id_user_last_update          INTEGER,
  date_last_update             TIMESTAMP NOT NULL DEFAULT NOW(),
  body                         TEXT NOT NULL,
  karma_bad                    INTEGER NOT NULL DEFAULT '0',
  karma_good                   INTEGER NOT NULL DEFAULT '0',
  is_approved                  BOOLEAN NOT NULL DEFAULT FALSE,
  is_active                    BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE private_messages (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  id_parent                    INTEGER,
  id_sender                    INTEGER NOT NULL,
  id_recipient                 INTEGER NOT NULL,
  is_seen                      BOOLEAN NOT NULL DEFAULT FALSE,
  ip_create                    VARCHAR(15) NOT NULL,
  date_create                  TIMESTAMP NOT NULL DEFAULT NOW(),
  title                        VARCHAR(128) NOT NULL,
  body                         TEXT NOT NULL,
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE attachments (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  original_name                VARCHAR(128) NOT NULL,
  file_name                    VARCHAR(128) NOT NULL,
  date_upload                  TIMESTAMP NOT NULL DEFAULT NOW(),
  type                         VARCHAR(16) NOT NULL,
  content_type                 VARCHAR(128) NOT NULL,
  description                  VARCHAR(128),
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE post_attachments (
  post_id                      INTEGER NOT NULL,
  attachment_id                INTEGER NOT NULL,
  CONSTRAINT post_attachments_idx UNIQUE (post_id, attachment_id)
);

CREATE TABLE private_message_attachments (
  message_id                   INTEGER NOT NULL,
  attachment_id                INTEGER NOT NULL,
  CONSTRAINT private_message_attachments_idx UNIQUE (message_id, attachment_id)
);

CREATE TABLE users (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  nick_name                    VARCHAR(64) UNIQUE NOT NULL,
  email                        VARCHAR(64) UNIQUE NOT NULL,
  password                     VARCHAR(128) NOT NULL,
  date_registered              TIMESTAMP DEFAULT now() NOT NULL,
  date_last_login              TIMESTAMP,
  posts_count                  INTEGER NOT NULL default '0',
  name                         VARCHAR(128) NOT NULL,
  surname                      VARCHAR(128),
  gender                       BOOLEAN,
  date_birth                   DATE,
  location                     VARCHAR(128),
  site                         VARCHAR(128),
  is_hide_email                BOOLEAN NOT NULL DEFAULT FALSE,
  is_hide_date_birth           BOOLEAN NOT NULL DEFAULT FALSE,
  is_show_online               BOOLEAN NOT NULL DEFAULT TRUE,
  signature                    VARCHAR(128),
  avatar                       INTEGER,
  karma_bad                    INTEGER NOT NULL DEFAULT '0',
  karma_good                   INTEGER NOT NULL DEFAULT '0',
  secret_question              VARCHAR(128) NOT NULL default '',
  secret_answer                VARCHAR(128) NOT NULL default '',
  secret_answer_tries          INTEGER NOT NULL DEFAULT '0',
  is_banned                    BOOLEAN NOT NULL DEFAULT FALSE,
  is_activated                 BOOLEAN NOT NULL DEFAULT FALSE,
  validation_code              VARCHAR(128) NOT NULL default '',
  last_validation_request_date TIMESTAMP DEFAULT now() NOT NULL,
  time_logged_in               BIGINT NOT NULL default '0',
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE,
  is_system                    BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE groups (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  code                         VARCHAR(32) UNIQUE NOT NULL,
  name                         VARCHAR(128) NOT NULL,
  description                  VARCHAR(128),
  color                        VARCHAR(6) NOT NULL,
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE,
  weight                       INTEGER NOT NULL DEFAULT 1,
  is_system                    BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE permissions (
  id                           INTEGER PRIMARY KEY DEFAULT nextval('sjb_id_seq'),
  code                         VARCHAR(32) UNIQUE NOT NULL,
  name                         VARCHAR(128) NOT NULL,
  description                  VARCHAR(128),
  is_active                    BOOLEAN NOT NULL DEFAULT TRUE,
  is_system                    BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE group_permissions (
  group_id                     INTEGER NOT NULL,
  permission_id                INTEGER NOT NULL,
  CONSTRAINT group_permissions_idx UNIQUE (group_id, permission_id)
);

CREATE TABLE category_moderators (
  category_id                  INTEGER NOT NULL,
  user_id                      INTEGER NOT NULL,
  CONSTRAINT category_moderators_idx UNIQUE (category_id, user_id)
);

CREATE TABLE board_moderators (
  board_id                     INTEGER NOT NULL,
  user_id                      INTEGER NOT NULL,
  CONSTRAINT board_moderators_idx UNIQUE (board_id, user_id)
);

CREATE TABLE topic_moderators (
  topic_id                     INTEGER NOT NULL,
  user_id                      INTEGER NOT NULL,
  CONSTRAINT topic_moderators_idx UNIQUE (topic_id, user_id)
);

CREATE TABLE user_groups (
  user_id                      INTEGER NOT NULL,
  group_id                     INTEGER NOT NULL,
  CONSTRAINT user_groups_idx UNIQUE (user_id, group_id)
);

-- To restrict board access for a group
CREATE TABLE board_groups (
  board_id                     INTEGER NOT NULL,
  group_id                     INTEGER NOT NULL,
  CONSTRAINT board_groups_idx UNIQUE (board_id, group_id)
);

CREATE TABLE karma_log (
  sender_id                    INTEGER NOT NULL,
  target_id                    INTEGER NOT NULL,
  is_positive                  BOOLEAN NOT NULL,
  type                         VARCHAR(8) NOT NULL,
  CONSTRAINT karma_log_idx UNIQUE (sender_id, target_id, type)
);
