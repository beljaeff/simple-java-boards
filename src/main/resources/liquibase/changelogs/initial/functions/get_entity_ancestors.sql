DROP FUNCTION IF EXISTS get_entity_ancestors;
CREATE OR REPLACE FUNCTION get_entity_ancestors(current_entity_id INTEGER, entity_type VARCHAR)
  RETURNS TABLE (
    id INTEGER,
    id_entity INTEGER,
    type VARCHAR,
    title VARCHAR,
    is_active BOOLEAN
  )
AS $$
DECLARE
  category_id INTEGER;
BEGIN
  IF current_entity_id IS NULL THEN
    RETURN;
  END IF;

  id := 1;
  IF entity_type = 'post' THEN
    SELECT p.id, p.id_topic, '', p.is_active, entity_type
        INTO id_entity, current_entity_id, title, is_active, type
    FROM posts AS p WHERE p.id = current_entity_id;
    IF NOT FOUND THEN
      RETURN;
    END IF;
    entity_type := 'topic';
    RETURN NEXT;
    id := id+1;
  END IF;

  IF entity_type = 'topic' THEN
    SELECT t.id, t.id_board, t.title, t.is_active, entity_type
        INTO id_entity, current_entity_id, title, is_active, type
    FROM topics AS t WHERE t.id = current_entity_id;
    IF NOT FOUND THEN
      RETURN;
    END IF;
    entity_type := 'board';
    RETURN NEXT;
    id := id+1;
  END IF;

  IF entity_type = 'board' THEN
    -- Go from leaf to root inside board tree and check board activeness
    LOOP
      SELECT b.id, b.id_parent, b.id_category, b.title, b.is_active, entity_type
          INTO id_entity, current_entity_id, category_id, title, is_active, type
      FROM boards AS b WHERE b.id=current_entity_id;
      IF NOT FOUND THEN
        RETURN;
      END IF;
      RETURN NEXT;
      id := id+1;
      -- Loop exit condition
      IF current_entity_id IS NULL THEN
        -- Get category for top-level board
        IF category_id IS NOT NULL THEN
          entity_type := 'category';
          current_entity_id := category_id;
        END IF;
        EXIT;
      END IF;
    END LOOP;
  END IF;

  IF entity_type = 'category' THEN
    SELECT c.id, c.title, c.is_active, entity_type
        INTO id_entity, title, is_active, type
    FROM categories AS c WHERE c.id=current_entity_id;
    -- If board have no category, adding empty entity
    RETURN NEXT;
  END IF;
END;
$$ LANGUAGE plpgsql;
