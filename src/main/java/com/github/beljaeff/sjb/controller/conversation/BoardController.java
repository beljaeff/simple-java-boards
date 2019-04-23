package com.github.beljaeff.sjb.controller.conversation;

import com.github.beljaeff.sjb.controller.Routes;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.ActionStatusDto;
import com.github.beljaeff.sjb.dto.dto.BaseDto;
import com.github.beljaeff.sjb.dto.dto.page.FormPageDto;
import com.github.beljaeff.sjb.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.github.beljaeff.sjb.dto.form.BaseForm;
import com.github.beljaeff.sjb.dto.form.conversation.BoardForm;
import com.github.beljaeff.sjb.service.conversation.BoardService;

import javax.validation.Valid;

@Controller
public class BoardController extends AbstractController {

    private static final String BOARD_VIEW_TPL = "board/view";
    private static final String BOARD_EDIT_TPL = "board/edit";

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PreAuthorize("hasAnyPermissions('VIEW', 'ADMIN')")
    @GetMapping(Routes.BOARD_VIEW)
    public String get(@PathVariable int id, @RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute(BaseController.ATTR_PAGE, boardService.getBoard(id, page));
        return BOARD_VIEW_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_EDIT)
    public String edit(@PathVariable int id, Model model) {
        BoardForm form = getFormFromModel(model, BoardForm::new);
        FormPageDto<? extends BaseDto, BoardForm> page = boardService.getEditForm(id, form);
        setFormAttributes(page, model);
        return BOARD_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_ADD_FROM_PARENT)
    public String addFromBoard(@PathVariable int id, Model model) {
        BoardForm form = getFormFromModel(model, BoardForm::new);
        form.setParentBoard(id);
        FormPageDto<? extends BaseDto, BoardForm> page = boardService.getAddForm(form);
        setFormAttributes(page, model);
        return BOARD_EDIT_TPL;
    }

    @PreAuthorize("hasPermission('EDIT_CATEGORY') && hasPermission('EDIT_BOARD') || hasPermission('ADMIN')")
    @GetMapping(Routes.BOARD_ADD_FROM_CATEGORY)
    public String addFromCategory(@PathVariable int id, Model model) {
        BoardForm form = getFormFromModel(model, BoardForm::new);
        form.setCategory(id);
        FormPageDto<? extends BaseDto, BoardForm> page = boardService.getAddForm(form);
        setFormAttributes(page, model);
        return BOARD_EDIT_TPL;
    }

    @PreAuthorize("hasPermission('EDIT_CATEGORY') && hasPermission('EDIT_BOARD') || hasPermission('ADMIN')")
    @GetMapping(Routes.BOARD_ADD_FROM_INDEX)
    public String addFromIndex(Model model) {
        BoardForm form = getFormFromModel(model, BoardForm::new);
        FormPageDto<? extends BaseDto, BoardForm> page = boardService.getAddForm(form);
        setFormAttributes(page, model);
        return BOARD_EDIT_TPL;
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @PostMapping(Routes.BOARD_SAVE)
    public String save(@ModelAttribute(BaseController.ATTR_FORM) @Valid BoardForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        return saveForm(form, bindingResult, redirectAttributes, form.getId() == 0 ? boardService::create : boardService::edit);
    }

    void setFormUrls(BaseForm baseform) {
        BoardForm form = (BoardForm) baseform;
        // Edit board
        if(form.getId() > 0) {
            form.setFormUrl(buildSegmentsPath(Routes.BOARD_EDIT, form.getId()));
            form.setSaveUrl(Routes.BOARD_SAVE);
            form.setCancelUrl(buildSegmentsPath(Routes.BOARD_VIEW, form.getId()));
            form.setFinishUrl(buildSegmentsPath(Routes.BOARD_VIEW, form.getId()));
        }
        // Add from index
        else if(form.getParentBoard() == null && form.getCategory() == null) {
            form.setFormUrl(Routes.BOARD_ADD_FROM_INDEX);
            form.setSaveUrl(Routes.BOARD_SAVE);
            form.setCancelUrl(Routes.ROOT_URL);
            form.setFinishUrl(Routes.ROOT_URL);
        }
        // Add from category
        else if(form.getParentBoard() == null && form.getCategory() != null) {
            form.setFormUrl(buildSegmentsPath(Routes.BOARD_ADD_FROM_CATEGORY, form.getCategory()));
            form.setSaveUrl(Routes.BOARD_SAVE);
            form.setCancelUrl(buildSegmentsPath(Routes.CATEGORY_VIEW, form.getCategory()));
            form.setFinishUrl(buildSegmentsPath(Routes.CATEGORY_VIEW, form.getCategory()));
        }
        // Add from board
        else if(form.getParentBoard() != null && form.getCategory() == null) {
            form.setFormUrl(buildSegmentsPath(Routes.BOARD_ADD_FROM_PARENT, form.getParentBoard()));
            form.setSaveUrl(Routes.BOARD_SAVE);
            form.setCancelUrl(buildSegmentsPath(Routes.BOARD_VIEW, form.getParentBoard()));
            form.setFinishUrl(buildSegmentsPath(Routes.BOARD_VIEW, form.getParentBoard()));
        }
    }

    @Override
    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_SAVE)
    public String redirect(Model model) {
        return super.redirect(model);
    }

    @PreAuthorize("hasAnyPermissions('DELETE_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_DELETE)
    public String delete(@PathVariable int id) {
        return "redirect:" + getLinkForRedirect(boardService.delete(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ACTIVATE_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_CHANGE_ACTIVE)
    public String changeActive(@PathVariable int id) {
        return "redirect:" + getLinkForRedirect(boardService.changeActive(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_UP)
    public String up(@PathVariable int id) {
        return "redirect:" + getLinkForRedirect(boardService.up(id));
    }

    @PreAuthorize("hasAnyPermissions('EDIT_BOARD', 'ADMIN')")
    @GetMapping(Routes.BOARD_DOWN)
    public String down(@PathVariable int id) {
        return "redirect:" + getLinkForRedirect(boardService.down(id));
    }

    private String getLinkForRedirect(ActionStatusDto<Board> actionStatus) {
        if(actionStatus == null) {
            return Routes.ROOT_URL;
        }
        Board board = actionStatus.getEntity();
        if(board != null && board.getCategory() != null) {
            return buildSegmentsPath(Routes.CATEGORY_VIEW, board.getCategory().getId());
        }
        else if(board != null && board.getParentBoard() != null) {
            return buildSegmentsPath(Routes.BOARD_VIEW, board.getParentBoard().getId());
        }
        else {
            return Routes.ROOT_URL;
        }
    }
}