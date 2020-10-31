package com.github.beljaeff.sjb.controller;

import com.github.beljaeff.sjb.enums.EntityType;
import com.github.beljaeff.sjb.enums.ProfileSection;
import com.github.beljaeff.sjb.util.HttpUtils;

public final class RoutesHelper {
    
    public static final String SIGN_IN                           = "/sign-in";
    public static final String SIGN_UP                           = "/sign-up";
    public static final String SIGN_UP_REQUEST                   = "/sign-up/request";
    public static final String SIGN_UP_REQUEST_SUCCESS           = "/sign-up/request-success";
    public static final String SIGN_UP_ACTIVATE                  = "/sign-up/activate";
    public static final String RESET_PASSWORD                    = "/reset-password";
    public static final String RESET_PASSWORD_REQUEST            = "/reset-password/request";
    public static final String RESET_PASSWORD_ERROR              = "/reset-password/error";
    public static final String RESET_PASSWORD_QUESTION           = "/reset-password/question";
    public static final String RESET_PASSWORD_REQUEST_SUCCESS    = "/reset-password/request-success";
    public static final String RESET_PASSWORD_RESET              = "/reset-password/reset";
    public static final String RESET_PASSWORD_RESET_SUCCESS      = "/reset-password/reset-success";

    public static final String ATTACHMENT                        = "/attachment";
    public static final String ATTACHMENT_GET                    = ATTACHMENT   + "/{id}";
    public static final String ATTACHMENT_DELETE                 = ATTACHMENT   + "/{id}/delete";

    public static final String ROOT_URL                          = "/";

    public static final String CATEGORY                          = ROOT_URL + EntityType.ConstantsHelper.CATEGORY;
    public static final String CATEGORY_VIEW                     = CATEGORY     + "/{id}";
    public static final String CATEGORY_ADD                      = CATEGORY     + "/add";
    public static final String CATEGORY_EDIT                     = CATEGORY     + "/{id}/edit";
    public static final String CATEGORY_SAVE                     = CATEGORY     + "/save";
    public static final String CATEGORY_DELETE                   = CATEGORY     + "/{id}/delete";
    public static final String CATEGORY_CHANGE_ACTIVE            = CATEGORY     + "/{id}/change-active";
    public static final String CATEGORY_UP                       = CATEGORY     + "/{id}/up";
    public static final String CATEGORY_DOWN                     = CATEGORY     + "/{id}/down";

    public static final String BOARD                             = ROOT_URL     + EntityType.ConstantsHelper.BOARD;
    public static final String BOARD_VIEW                        = BOARD        + "/{id}";
    public static final String BOARD_ADD_FROM_CATEGORY           = CATEGORY     + "/{id}/add";
    public static final String BOARD_ADD_FROM_PARENT             = BOARD        + "/{id}/add";
    public static final String BOARD_ADD_FROM_INDEX              = BOARD        + "/add";
    public static final String BOARD_EDIT                        = BOARD        + "/{id}/edit";
    public static final String BOARD_SAVE                        = BOARD        + "/save";
    public static final String BOARD_DELETE                      = BOARD        + "/{id}/delete";
    public static final String BOARD_CHANGE_ACTIVE               = BOARD        + "/{id}/change-active";
    public static final String BOARD_UP                          = BOARD        + "/{id}/up";
    public static final String BOARD_DOWN                        = BOARD        + "/{id}/down";

    public static final String TOPIC                             = ROOT_URL     + EntityType.ConstantsHelper.TOPIC;
    public static final String TOPIC_VIEW                        = TOPIC        + "/{id}";
    public static final String TOPIC_CREATE                      = BOARD        + "/{id}/create-topic";
    public static final String TOPIC_SAVE_NEW                    = BOARD        + "/save-new-topic";
    public static final String TOPIC_EDIT                        = TOPIC        + "/{id}/edit";
    public static final String TOPIC_SAVE                        = TOPIC        + "/save";
    public static final String TOPIC_DELETE                      = TOPIC        + "/{id}/delete";
    public static final String TOPIC_CHANGE_ACTIVE               = TOPIC        + "/{id}/change-active";
    public static final String TOPIC_CHANGE_LOCK                 = TOPIC        + "/{id}/change-lock";
    public static final String TOPIC_CHANGE_STICKY               = TOPIC        + "/{id}/change-sticky";
    public static final String TOPIC_APPROVE                     = TOPIC        + "/{id}/approve";

    public static final String POST                              = ROOT_URL     + EntityType.ConstantsHelper.POST;
    public static final String POST_REPLY                        = POST         + "/{id}/reply";
    public static final String POST_SAVE_REPLY                   = POST         + "/save-reply";
    public static final String POST_EDIT                         = POST         + "/{id}/edit";
    public static final String POST_SAVE                         = POST         + "/save";
    public static final String POST_DELETE                       = POST         + "/{id}/delete";
    public static final String POST_CHANGE_ACTIVE                = POST         + "/{id}/change-active";
    public static final String POST_CHANGE_STICKY                = POST         + "/{id}/change-sticky";
    public static final String POST_APPROVE                      = POST         + "/{id}/approve";
    public static final String POST_REMOVE_ATTACHMENT            = POST         + "/{id}/remove-attachment/{aid}";

    public static final String PROFILE                           = ROOT_URL     + EntityType.ConstantsHelper.PROFILE;
    public static final String PROFILE_LIST                      = PROFILE;
    public static final String USER_PROFILE                      = PROFILE      + "/{id}";
    public static final String USER_PROFILE_OVERVIEW             = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW;
    public static final String USER_PROFILE_SECURITY             = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY;
    public static final String USER_PROFILE_GROUPS               = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.GROUPS;
    public static final String USER_PROFILE_POSTS                = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.POSTS;
    public static final String USER_PROFILE_TOPICS               = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.TOPICS;
    public static final String USER_PROFILE_STATISTICS           = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.STATISTICS;
    public static final String USER_DELETE                       = USER_PROFILE + "/{section}/delete";
    public static final String USER_ACTIVATE                     = USER_PROFILE + "/{section}/activate";
    public static final String USER_BAN                          = USER_PROFILE + "/{section}/ban";
    public static final String USER_PROFILE_EDIT_OVERVIEW        = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW + "/edit";
    public static final String USER_PROFILE_SAVE_OVERVIEW        = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW + "/save";
    public static final String USER_PROFILE_EDIT_AVATAR          = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW + "/edit-avatar";
    public static final String USER_PROFILE_SAVE_AVATAR          = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW + "/save-avatar";
    public static final String USER_PROFILE_REMOVE_AVATAR        = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.OVERVIEW + "/remove-avatar";
    public static final String USER_PROFILE_CHANGE_EMAIL         = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/change-email";
    public static final String USER_PROFILE_SAVE_EMAIL           = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/save-email";
    public static final String USER_PROFILE_CHANGE_PASSWORD      = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/change-password";
    public static final String USER_PROFILE_SAVE_PASSWORD        = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/save-password";
    public static final String USER_PROFILE_CHANGE_SECRET_ANSWER = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/change-secret-answer";
    public static final String USER_PROFILE_SAVE_SECRET_ANSWER   = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.SECURITY + "/save-secret-answer";
    public static final String USER_PROFILE_ADD_GROUP            = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.GROUPS + "/add";
    public static final String USER_PROFILE_SAVE_GROUP           = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.GROUPS + "/save";
    public static final String USER_PROFILE_REMOVE_GROUP         = USER_PROFILE + HttpUtils.DELIMITER + ProfileSection.ConstantsHelper.GROUPS + "/{idGroup}/remove";

    public static final String USER_PROFILE_COMMON               = USER_PROFILE + "/{section}";

    public static final String PAGE_QUERY = "page={page}";
}
