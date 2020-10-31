package com.github.beljaeff.sjb.controller;

import com.github.beljaeff.sjb.controller.common.BaseController;
import com.github.beljaeff.sjb.dto.dto.attachment.AttachmentDto;
import com.github.beljaeff.sjb.exception.AttachmentException;
import com.github.beljaeff.sjb.exception.NotFoundException;
import com.github.beljaeff.sjb.service.attachment.CommonAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Controller
public class AttachmentController extends BaseController {
    private static final int BUFFER_SIZE = 262144;

    private final CommonAttachmentService commonAttachmentService;

    @Autowired
    public AttachmentController(CommonAttachmentService commonAttachmentService) {
        this.commonAttachmentService = commonAttachmentService;
    }

    @PreAuthorize("hasPermission('VIEW_ATTACHMENTS') && hasPermission('VIEW') || hasPermission('ADMIN')")
    @GetMapping(RoutesHelper.ATTACHMENT_GET)
    public void get(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
        ServletContext servletContext = request.getServletContext();

        AttachmentDto attachment = commonAttachmentService.get(id);
        if(attachment == null) {
            log.error("Attachment id '{}' not found", id);
            throw new NotFoundException();
        }

        String filePath = attachment.getFilePath();
        try {
            File downloadFile = new File(filePath);
            FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = servletContext.getMimeType(filePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", attachment.getOriginalName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        }
        catch (IOException ioe) {
            throw new AttachmentException(ioe);
        }
    }
}