DROP FUNCTION IF EXISTS is_board_active();
CREATE OR REPLACE FUNCTION is_board_active(id_board INTEGER, check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  id_category INTEGER;
  is_active   BOOLEAN;
BEGIN
  IF id_board IS NULL THEN
    RETURN FALSE;
  END IF;

  IF check_parents IS NULL OR NOT check_parents THEN
    SELECT b.is_active INTO is_active FROM boards AS b WHERE b.id=id_board;
    IF NOT FOUND OR NOT is_active THEN
      RETURN FALSE;
    END IF;
    RETURN TRUE;
  END IF;

  -- Go from leaf to root inside board tree and check board activeness
  LOOP
    SELECT b.id_parent, b.is_active, b.id_category INTO id_board, is_active, id_category FROM boards AS b WHERE b.id=id_board;
    IF NOT FOUND OR NOT is_active THEN
      is_active := FALSE;
      EXIT;
    END IF;
    IF id_board IS NULL THEN
      -- Check category activeness if it is available
      IF id_category IS NOT NULL THEN
        SELECT is_category_active(id_category) INTO is_active;
      END IF;
      EXIT;
    END IF;
  END LOOP;
  RETURN is_active;
END;
$$ LANGUAGE plpgsql;
