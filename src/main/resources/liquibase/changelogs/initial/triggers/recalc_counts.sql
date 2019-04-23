DROP TRIGGER IF EXISTS t_boards_recalc_counts ON boards;
DROP TRIGGER IF EXISTS t_topics_recalc_counts ON topics;
DROP TRIGGER IF EXISTS t_posts_recalc_counts ON posts;

DROP FUNCTION IF EXISTS recalc_counts;
CREATE OR REPLACE FUNCTION recalc_counts() RETURNS TRIGGER AS $$
DECLARE
  board_id INTEGER;
  topics_cnt INTEGER;
  posts_cnt INTEGER;
  ret RECORD;
BEGIN
  IF TG_OP = 'INSERT' AND TG_TABLE_NAME = 'topics' THEN
    board_id := NEW.id_board;
    topics_cnt := 1;
    posts_cnt := 0;
    ret := NEW;
  END IF;
  IF TG_OP = 'INSERT' AND TG_TABLE_NAME = 'posts' THEN
    SELECT id_board INTO board_id FROM topics WHERE id=NEW.id_topic;
    topics_cnt := 0;
    posts_cnt := 1;
    ret := NEW;
  END IF;
  IF TG_OP = 'DELETE' AND TG_TABLE_NAME = 'boards' THEN
    board_id := OLD.id_parent;
    topics_cnt := -1 * OLD.topics_count;
    posts_cnt := -1 * OLD.posts_count;
    ret := OLD;
  END IF;
  IF TG_OP = 'DELETE' AND TG_TABLE_NAME = 'topics' THEN
    board_id := OLD.id_board;
    topics_cnt := -1;
    posts_cnt := -1 * OLD.posts_count;
    ret := OLD;
  END IF;
  IF TG_OP = 'DELETE' AND TG_TABLE_NAME = 'posts' THEN
    SELECT id_board INTO board_id FROM topics WHERE id=OLD.id_topic;
    topics_cnt := 0;
    posts_cnt := -1;
    ret := OLD;
  END IF;
  LOOP
    UPDATE boards SET
                      topics_count = topics_count + topics_cnt,
                      posts_count = posts_count + posts_cnt
                  WHERE id = board_id;
    SELECT id_parent INTO board_id FROM boards WHERE id=board_id;
    IF NOT FOUND THEN
      EXIT;
    END IF;
  END LOOP;
  RETURN ret;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_boards_recalc_counts
  AFTER DELETE
  ON boards
  FOR EACH ROW EXECUTE PROCEDURE recalc_counts();

CREATE TRIGGER t_topics_recalc_counts
  AFTER INSERT OR DELETE
  ON topics
  FOR EACH ROW EXECUTE PROCEDURE recalc_counts();

CREATE TRIGGER t_posts_recalc_counts
  AFTER INSERT OR DELETE
  ON posts
  FOR EACH ROW EXECUTE PROCEDURE recalc_counts();