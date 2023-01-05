package com.github.beljaeff.sjb.util;

import com.github.beljaeff.sjb.enums.ErrorCode;
import com.github.beljaeff.sjb.model.Attachment;
import com.github.beljaeff.sjb.model.Group;
import com.github.beljaeff.sjb.model.Post;
import com.github.beljaeff.sjb.model.User;
import com.github.beljaeff.sjb.service.RecordService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

//TODO: tests
public class CommonUtils {

    private static final String JPG = "image/jpeg";
    private static final String GIF = "image/gif";
    private static final String PNG = "image/png";

    public static void addError(BindingResult bindingResult, String field, String errorText) {
        if(field != null) {
            bindingResult.rejectValue(field, null, errorText);
        }
        else {
            bindingResult.reject(null, errorText);
        }
    }

    public static void addErrors(BindingResult result, RecordService service, List<ErrorCode> errors) {
        for(ErrorCode error : errors) {
            addError(result, error.getField(), service.getText(error.getCode()));
        }
    }

    public static Map<String, String> getActiveValues(RecordService recordService) {
        Map<String, String> ret = new HashMap<>();
        ret.put("true", recordService.getText("active.text"));
        ret.put("false", recordService.getText("inactive.text"));
        return ret;
    }

    /**
     * Maps list of attachment with posts they belongs to
     * to map with key is post id and value is a list of attachments belongs to post
     * @param attachments given
     * @return resulting map
     */
    public static Map<Integer, List<Attachment>> mapAttachmentsByPostId(List<Attachment> attachments) {
        if(CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyMap();
        }
        Map<Integer, List<Attachment>> ret = new HashMap<>();
        for(Attachment attachment : attachments) {
            if(attachment.getPosts() == null) {
                continue;
            }
            for(Post post : attachment.getPosts()) {
                if(!ret.containsKey(post.getId())) {
                    ret.put(post.getId(), new ArrayList<>());
                }
                List<Attachment> aList = ret.get(post.getId());
                aList.add(attachment);
            }
            attachment.setPosts(null);
        }
        return ret;
    }

    /**
     * Maps list of groups with users to map with key is user id and value is a list of groups user in
     * @param groups given
     * @return resulting map
     */
    public static Map<Integer, Set<Group>> mapGroupsByUserId(List<Group> groups) {
        if(CollectionUtils.isEmpty(groups)) {
            return Collections.emptyMap();
        }
        Map<Integer, Set<Group>> ret = new HashMap<>();
        for(Group group : groups) {
            if(group.getOwners() == null) {
                continue;
            }
            for(User user : group.getOwners()) {
                if(!ret.containsKey(user.getId())) {
                    ret.put(user.getId(), new TreeSet<>(Comparator.comparingInt(Group::getWeight)));
                }
                Set<Group> gSet = ret.get(user.getId());
                gSet.add(group);
            }
            group.setOwners(null);
        }
        return ret;
    }

    public static boolean isAttachmentsEmpty(MultipartFile[] attachments) {
        if(attachments == null) {
            return true;
        }
        boolean ret = true;
        for(MultipartFile attachment : attachments) {
            if(!(attachment == null || attachment.isEmpty())) {
                ret = false;
            }
        }
        return ret;
    }

    public static boolean isImage(String contentType) {
        return JPG.equals(contentType) || PNG.equals(contentType) || GIF.equals(contentType);
    }
}
