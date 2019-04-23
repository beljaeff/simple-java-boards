package com.github.beljaeff.sjb.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.github.beljaeff.sjb.dto.dto.conversation.BoardDto;
import com.github.beljaeff.sjb.dto.dto.pagination.PaginatedDto;
import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;
import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.model.Board;
import com.github.beljaeff.sjb.model.Category;

import java.util.ArrayList;
import java.util.List;

import static com.github.beljaeff.sjb.util.HttpUtils.makeLink;

@Component
public class BoardMapper extends AbstractMapper {

    private TopicMapper topicMapper;

    @Autowired
    public void setTopicMapper(TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public void updateBoardFromForm(Board board, Category category, Board parent, BoardForm form) {
        if(form == null || board == null || !isLoaded(board)) {
            return;
        }
        if(parent != null) {
            board.setParentBoard(parent);
        }
        if(category != null) {
            board.setCategory(category);
        }
        board.setTitle(form.getTitle());
        board.setDescription(form.getDescription());
        board.setIcon(form.getIcon());
        board.setIsActive(form.getIsActive());
        board.setPosition(form.getPosition() == null ? 0 : Integer.parseInt(form.getPosition()));
    }

    public BoardForm boardToBoardForm(Board board) {
        if(board == null || !isLoaded(board)) {
            return null;
        }
        BoardForm form = new BoardForm();
        form.setId(board.getId());
        if(isLoaded(board.getParentBoard()) && board.getParentBoard() != null) {
            form.setParentBoard(board.getParentBoard().getId());
        }
        if(isLoaded(board.getCategory()) && board.getCategory() != null) {
            form.setCategory(board.getCategory().getId());
        }
        form.setTitle(board.getTitle());
        form.setDescription(board.getDescription());
        form.setIsActive(board.getIsActive());
        form.setPosition(String.valueOf(board.getPosition()));

        return form;
    }

    public List<BoardDto> boardToBoardDto(List<Board> boards) {
        List<BoardDto> result = new ArrayList<>();
        if(!isLoaded(boards)) {
            return result;
        }
        if(!CollectionUtils.isEmpty(boards)) {
            for(Board board : boards) {
                BoardDto dto = boardToBoardDto(board);
                if(dto != null) {
                    result.add(dto);
                }
            }
        }
        return result;
    }

    public BoardDto boardToBoardDto(Board board) {
        if(board == null || !isLoaded(board)) {
            return null;
        }
        BoardDto result = new BoardDto();
        result.setId(board.getId());
        result.setTitle(board.getTitle());
        result.setLink(board.getId() > 0 ? makeLink(EntityType.BOARD.getType(), board.getId()) : "");
        result.setDescription(board.getDescription());
        result.setIsActive(board.getIsActive());
        result.setIcon(board.getIcon());
        if(isLoaded(board.getChildBoards())) {
            result.setBoards(boardToBoardDto(board.getChildBoards()));
        }
        result.setPostsCount((int)board.getPostsCount());
        result.setTopicsCount((int)board.getTopicsCount());
        if(isLoaded(board.getLastTopic())) {
            result.setLastTopic(topicMapper.topicToTopicDto(board.getLastTopic()));
        }
        if(isLoaded(board.getTopics())) {
            result.setTopics(topicMapper.topicToTopicDto(board.getTopics()));
        }
        if(result.getTopics() == null) {
            result.setTopics(new PaginatedDto<>());
        }
        return result;
    }
}
