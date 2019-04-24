DROP FUNCTION IF EXISTS is_topic_active();
CREATE OR REPLACE FUNCTION is_topic_active(id_topic INTEGER, check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  id_board  INTEGER;
  is_active BOOLEAN;
BEGIN
  IF id_topic IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT t.is_active, t.id_board INTO is_active, id_board FROM topics AS t WHERE t.id=id_topic;
  IF NOT FOUND OR NOT is_active THEN
    RETURN FALSE;
  END IF;

  IF check_parents = TRUE THEN
    SELECT is_board_active(id_board, check_parents) INTO is_active;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;
