DROP FUNCTION IF EXISTS is_user_active;
CREATE OR REPLACE FUNCTION is_user_active(id_user INTEGER) RETURNS BOOLEAN AS $$
DECLARE
  is_active BOOLEAN;
BEGIN
  IF id_user IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT u.is_active INTO is_active FROM users AS u WHERE u.id=id_user;
  IF NOT FOUND THEN
    RETURN FALSE;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;