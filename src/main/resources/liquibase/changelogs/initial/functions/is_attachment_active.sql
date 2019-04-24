DROP FUNCTION IF EXISTS is_attachment_active();
CREATE OR REPLACE FUNCTION is_attachment_active(id_attachment INTEGER, check_parents BOOLEAN) RETURNS BOOLEAN AS $$
DECLARE
  id_pm       INTEGER;
  id_post     INTEGER;
  id_user     INTEGER;
  is_active   BOOLEAN;
  user_active BOOLEAN;
BEGIN
  IF id_attachment IS NULL THEN
    RETURN FALSE;
  END IF;

  SELECT a.is_active, pma.message_id, pa.post_id, u.id, u.is_active INTO is_active, id_pm, id_post, id_user, user_active
        FROM      attachments                 AS a
        LEFT JOIN private_message_attachments AS pma ON a.id = pma.attachment_id
        LEFT JOIN post_attachments            AS pa  ON a.id = pa.attachment_id
        LEFT JOIN users                       AS u   ON a.id = u.avatar
  WHERE a.id=id_attachment;

  IF NOT FOUND OR is_active = FALSE THEN
    RETURN FALSE;
  END IF;

  IF check_parents = TRUE THEN
    IF     id_user IS NOT NULL THEN
      is_active := user_active;
    ELSEIF id_post IS NOT NULL THEN
      SELECT is_post_active(id_post, check_parents) INTO is_active;
    ELSEIF id_pm   IS NOT NULL THEN
      SELECT is_pm_active(id_pm) INTO is_active;
    END IF;
  END IF;

  RETURN is_active;
END;
$$ LANGUAGE plpgsql;