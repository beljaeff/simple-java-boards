DROP FUNCTION IF EXISTS is_post_active();
CREATE OR REPLACE FUNCTION is_post_active(id_post INTEGER, check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  id_topic  INTEGER;
  is_active BOOLEAN;
BEGIN
  IF id_post IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT p.is_active, p.id_topic INTO is_active, id_topic FROM posts AS p WHERE p.id=id_post;
  IF NOT FOUND OR NOT is_active THEN
    RETURN FALSE;
  END IF;

  IF check_parents = TRUE THEN
    SELECT is_topic_active(id_topic, check_parents) INTO is_active;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;
