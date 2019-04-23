package com.github.beljaeff.sjb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.exception.ForbiddenException;
import com.github.beljaeff.sjb.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Controller
@ControllerAdvice
public class ErrorHandlerController extends BaseController {
    private static final String DEFAULT_ERROR_VIEW = "error";

    private String prepareAndShow(Model model, Object status, Object reason) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("status", status);
        data.put("reason", reason);
        model.addAttribute("data", data);

        return DEFAULT_ERROR_VIEW;
    }

    @RequestMapping("/error-resolver")
    public String handle(HttpServletRequest request, Model model) {
        return prepareAndShow(model,
                              request.getAttribute("javax.servlet.error.status_code"),
                              request.getAttribute("javax.servlet.error.message"));
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Model model, NotFoundException e) {
        String message;
        if(!CollectionUtils.isEmpty(e.getErrors()) && e.getErrors().get(0) != null) {
            message = recordService.getText(e.getErrors().get(0).getCode());
        }
        else if(!isEmpty(e.getMessage())) {
            message = e.getMessage();
        }
        else {
            message = "Not found";
        }
        return prepareAndShow(model, 404, message);
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbidden(Model model, ForbiddenException e) {
        String message;
        if(!CollectionUtils.isEmpty(e.getErrors()) && e.getErrors().get(0) != null) {
            message = recordService.getText(e.getErrors().get(0).getCode());
        }
        else if(!isEmpty(e.getMessage())) {
            message = e.getMessage();
        }
        else {
            message = "Forbidden";
        }
        return prepareAndShow(model, 403, message);
    }
}
