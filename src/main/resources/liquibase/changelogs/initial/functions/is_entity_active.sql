DROP FUNCTION IF EXISTS is_entity_active;
CREATE OR REPLACE FUNCTION is_entity_active(entity_id     INTEGER,
                                            entity_type   VARCHAR,
                                            check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  is_active BOOLEAN;
BEGIN
  is_active := FALSE;
  IF entity_type = 'category' THEN
    SELECT is_category_active(entity_id) INTO is_active;
  END IF;
  IF entity_type = 'board' THEN
    SELECT is_board_active(entity_id, check_parents) INTO is_active;
  END IF;
  IF entity_type = 'topic' THEN
    SELECT is_topic_active(entity_id, check_parents) INTO is_active;
  END IF;
  IF entity_type = 'post' THEN
    SELECT is_post_active(entity_id, check_parents) INTO is_active;
  END IF;
  IF entity_type = 'profile' THEN
    SELECT is_user_active(entity_id) INTO is_active;
  END IF;
  IF entity_type = 'group' THEN
    SELECT is_group_active(entity_id) INTO is_active;
  END IF;
  IF entity_type = 'attachment' THEN
    SELECT is_attachment_active(entity_id, check_parents) INTO is_active;
  END IF;
  RETURN is_active;
END;
$$ LANGUAGE plpgsql;
