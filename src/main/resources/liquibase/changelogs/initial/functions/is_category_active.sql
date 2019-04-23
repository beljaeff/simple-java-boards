DROP FUNCTION IF EXISTS is_category_active;
CREATE OR REPLACE FUNCTION is_category_active(id_category INTEGER) RETURNS BOOLEAN AS $$
DECLARE
  is_active BOOLEAN;
BEGIN
  IF id_category IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT c.is_active INTO is_active FROM categories AS c WHERE c.id=id_category;
  IF NOT FOUND THEN
    RETURN FALSE;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;