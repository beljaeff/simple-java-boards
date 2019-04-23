DROP FUNCTION IF EXISTS is_group_active;
CREATE OR REPLACE FUNCTION is_group_active(id_group INTEGER) RETURNS BOOLEAN AS $$
DECLARE
  is_active BOOLEAN;
BEGIN
  IF id_group IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT g.is_active INTO is_active FROM groups AS g WHERE g.id=id_group;
  IF NOT FOUND THEN
    RETURN FALSE;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;