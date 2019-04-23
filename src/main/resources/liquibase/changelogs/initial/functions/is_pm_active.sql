DROP FUNCTION IF EXISTS is_pm_active;
CREATE OR REPLACE FUNCTION is_pm_active(id_pm INTEGER, check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  is_active BOOLEAN;
BEGIN
  -- TODO: test this function!
  IF id_pm IS NULL THEN
    RETURN FALSE;
  END IF;

  IF check_parents IS NULL OR NOT check_parents THEN
    SELECT pm.is_active INTO is_active FROM private_messages AS pm WHERE pm.id=id_pm;
    IF NOT FOUND OR NOT is_active THEN
      RETURN FALSE;
    RETURN TRUE;
    END IF;
  END IF;

  -- Go from leaf to root inside board tree and check board activeness
  LOOP
    SELECT pm.id_parent, pm.is_active INTO id_pm, is_active FROM private_messages AS pm WHERE pm.id=id_pm;
    IF NOT FOUND OR NOT is_active THEN
      is_active := FALSE;
      EXIT;
    END IF;
    IF id_pm IS NULL THEN
      is_active := TRUE;
      EXIT;
    END IF;
  END LOOP;
  RETURN is_active;
END;
$$ LANGUAGE plpgsql;