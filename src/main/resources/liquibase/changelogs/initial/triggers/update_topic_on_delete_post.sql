DROP TRIGGER IF EXISTS t_update_topic_on_delete_post ON posts;
DROP FUNCTION IF EXISTS update_topic_on_delete_post();

CREATE OR REPLACE FUNCTION update_topic_on_delete_post() RETURNS TRIGGER AS $$
DECLARE
  first_post_id INTEGER;
  last_post_id INTEGER;
BEGIN
  SELECT id_first_post, id_last_post INTO first_post_id, last_post_id FROM topics WHERE id=OLD.id_topic;
  IF first_post_id = OLD.id THEN
    UPDATE topics SET id_first_post = (SELECT id FROM posts WHERE id_topic=OLD.id_topic AND id!=OLD.id ORDER BY date_create ASC LIMIT 1)
                  WHERE id=OLD.id_topic;
  END IF;
  IF last_post_id = OLD.id THEN
    UPDATE topics SET id_last_post = (SELECT id FROM posts WHERE id_topic=OLD.id_topic AND id!=OLD.id ORDER BY date_create DESC LIMIT 1)
                  WHERE id=OLD.id_topic;
  END IF;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_update_topic_on_delete_post
  BEFORE DELETE
  ON posts
  FOR EACH ROW EXECUTE PROCEDURE update_topic_on_delete_post();