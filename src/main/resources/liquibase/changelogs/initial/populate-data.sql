DELETE FROM group_permissions;
DELETE FROM user_groups;
DELETE FROM groups;
DELETE FROM permissions;
DELETE FROM users;
DELETE FROM categories;
ALTER SEQUENCE sjb_id_seq RESTART WITH 100;
ALTER SEQUENCE sjb_position_seq RESTART WITH 100;

INSERT INTO categories (id, position, title) VALUES
  (1, 1, 'Main category');

-- password is just word password
INSERT INTO users (id, nick_name, email, password, name, is_hide_email, is_show_online, is_activated, is_system) VALUES
  (0, 'Anonymous', '', '', '', true, false, true, true),
  (1, 'Admin', 'admin@admin.ad', '{bcrypt}$2a$10$8na5IbaQUhPSH2znybDXueklQrud1eQ0RpelTD6TO62J12LXsaduC', 'Administrator', true, false, true, true);

INSERT INTO permissions (id, code, name, is_active, is_system) VALUES
  (1, 'ADMIN', 'Access everything', true, true),
  (2, 'VIEW', 'View categories, boards, topics, posts, user list', true, true),
  (3, 'EDIT_CATEGORY', 'Add and edit category', true, true),
  (4, 'DELETE_CATEGORY', 'Delete any category', true, true),
  (5, 'ACTIVATE_CATEGORY', 'Activate or deactivate category', true, true),
  (6, 'EDIT_BOARD', 'Add and edit board', true, true),
  (7, 'DELETE_BOARD', 'Delete board', true, true),
  (8, 'MOVE_BOARD', 'Move board', true, true),
  (9, 'ACTIVATE_BOARD', 'Activate or deactivate board', true, true),
  (10, 'CREATE_TOPIC', 'Create new topic', true, true),
  (11, 'EDIT_OWN_TOPIC', 'Edit own topic', true, true),
  (12, 'LOCK_OWN_TOPIC', 'Lock own topic so nobody can post there', true, true),
  (13, 'MAKE_STICKY_OWN_TOPIC', 'Stick own topic at board top', true, true),
  (14, 'EDIT_TOPIC', 'Edit any topic', true, true),
  (15, 'DELETE_TOPIC', 'Delete any topic', true, true),
  (16, 'LOCK_TOPIC', 'Lock any topic', true, true),
  (17, 'MAKE_STICKY_TOPIC', 'Stick any topic at board top', true, true),
  (18, 'APPROVE_TOPIC', 'Approve any topic', true, true),
  (19, 'MOVE_TOPIC', 'Move any topic', true, true),
  (20, 'ACTIVATE_TOPIC', 'Activate or deactivate any topic', true, true),
  (21, 'CREATE_POST', 'Create new post', true, true),
  (22, 'EDIT_OWN_POST', 'Edit own post', true, true),
  (23, 'MAKE_STICKY_OWN_POST', 'Stick own post at topic top', true, true),
  (24, 'EDIT_POST', 'Edit any post', true, true),
  (25, 'DELETE_POST', 'Delete any post', true, true),
  (26, 'MAKE_STICKY_POST', 'Stick any post at topic top', true, true),
  (27, 'APPROVE_POST', 'Approve any post', true, true),
  (28, 'MOVE_POST', 'Move any post', true, true),
  (29, 'ACTIVATE_POST', 'Activate or deactivate any post', true, true),
  (30, 'VIEW_ATTACHMENTS', 'View attachments', true, true),
  (31, 'CREATE_ATTACHMENTS', 'Create attachment', true, true),
  (32, 'KARMA_UP_POST', 'Make karma up', true, true),
  (33, 'KARMA_DOWN_POST', 'Make karma down', true, true),
  (34, 'ACTIVATE_USER', 'Make user active/inactive', true, true),
  (35, 'BAN_USER', 'Ban/unban user', true, true),
  (36, 'DELETE_USER', 'Delete user', true, true),
  (37, 'VIEW_PROFILE', 'View user profile', true, true),
  (38, 'EDIT_OWN_PROFILE', 'Edit own profile', true, true),
  (39, 'EDIT_ALL_PROFILES', 'Edit any user profile', true, true),
  (40, 'EDIT_USER_GROUP', 'Edit user group', true, true);

INSERT INTO groups (id, code, name, color, description, weight, is_system) VALUES
  (1, 'UNPRIVILEGED',  'Observers',  '868e96', 'Unprivileged board users', 100, true),
  (2, 'REGISTERED',    'Users',      '28a745', 'Registered board users',   80,  true),
  (3, 'MODERATORS_L3', 'Moderators', '17a2b8', 'Topic moderators',         60,  true),
  (4, 'MODERATORS_L2', 'Moderators', '007bff', 'Board moderators',         40,  true),
  (5, 'MODERATORS_L1', 'Moderators', 'fd7e14', 'Category moderators',      20,  true),
  (6, 'MODERATORS_L0', 'Moderators', 'dc3545', 'Super moderators',         10,  true),
  (7, 'ADMINS',        'Admins',     '6f42c1', 'Board Administators',      1,   true);

INSERT INTO user_groups (user_id, group_id) VALUES
  (0, 1),
  (1, 7);

INSERT INTO group_permissions (group_id, permission_id) VALUES
-- Unprivileged
  (1, 2),
-- Registered
  (2, 2),
  (2, 10),
  (2, 21),
  (2, 22),
  (2, 30),
  (2, 31),
  (2, 32),
  (2, 33),
  (2, 37),
  (2, 38),
-- Topic moderators
  (3, 2),
  (3, 10),
  (3, 11),
  (3, 21),
  (3, 22),
  (3, 23),
  (3, 24), -- inside moderated topic only
  (3, 25), -- inside moderated topic only
  (3, 26), -- inside moderated topic only
  (3, 27), -- inside moderated topic only
  (3, 28), -- inside moderated topic only
  (3, 29), -- inside moderated topic only
  (3, 30),
  (3, 31),
  (3, 32),
  (3, 33),
  (3, 35),
  (3, 37),
  (3, 38),
-- Board moderators + assign and revoke topic moderators
  (4, 2),
  (4, 10),
  (4, 11),
  (4, 12),
  (4, 13),
  (4, 14), -- inside moderated board only
  (4, 15), -- inside moderated board only
  (4, 16), -- inside moderated board only
  (4, 17), -- inside moderated board only
  (4, 18), -- inside moderated board only
  (4, 19), -- inside moderated board only
  (4, 20), -- inside moderated board only
  (4, 21),
  (4, 22),
  (4, 23),
  (4, 24), -- inside moderated board only
  (4, 25), -- inside moderated board only
  (4, 26), -- inside moderated board only
  (4, 27), -- inside moderated board only
  (4, 28), -- inside moderated board only
  (4, 29), -- inside moderated board only
  (4, 30),
  (4, 31),
  (4, 32),
  (4, 33),
  (4, 35),
  (4, 37),
  (4, 38),
-- Global moderators + assign and revoke topic and board moderators
  (5, 2),
  (5, 6),
  (5, 7),
  (5, 8),
  (5, 9),
  (5, 10),
  (5, 11),
  (5, 12),
  (5, 13),
  (5, 14),
  (5, 15),
  (5, 16),
  (5, 17),
  (5, 18),
  (5, 19),
  (5, 20),
  (5, 21),
  (5, 22),
  (5, 23),
  (5, 24),
  (5, 25),
  (5, 26),
  (5, 27),
  (5, 28),
  (5, 29),
  (5, 30),
  (5, 31),
  (5, 32),
  (5, 33),
  (5, 35),
  (5, 37),
  (5, 38),
-- Super moderators + can assign all other moderators
  (6, 2),
  (6, 3),
  (6, 4),
  (6, 5),
  (6, 6),
  (6, 7),
  (6, 8),
  (6, 9),
  (6, 10),
  (6, 11),
  (6, 12),
  (6, 13),
  (6, 14),
  (6, 15),
  (6, 16),
  (6, 17),
  (6, 18),
  (6, 19),
  (6, 20),
  (6, 21),
  (6, 22),
  (6, 23),
  (6, 24),
  (6, 25),
  (6, 26),
  (6, 27),
  (6, 28),
  (6, 29),
  (6, 30),
  (6, 31),
  (6, 32),
  (6, 33),
  (6, 34),
  (6, 35),
  (6, 37),
  (6, 38),
  (6, 40),
-- Admins
  (7, 1),
  (7, 38);
