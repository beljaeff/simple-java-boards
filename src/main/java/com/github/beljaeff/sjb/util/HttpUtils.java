package com.github.beljaeff.sjb.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.github.beljaeff.sjb.controller.RoutesHelper.ROOT_URL;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class HttpUtils {

    public static final String DELIMITER = "/";

    public static String getRootPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(sra == null || sra.getRequest() == null || isEmpty(sra.getRequest().getContextPath())) {
            return ROOT_URL;
        }
        return sra.getRequest().getContextPath();
    }

    public static String getBasePath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(sra == null || sra.getRequest() == null) {
            log.error("Can not get HttpServletContext from holder");
            return ROOT_URL;
        }
        HttpServletRequest request = sra.getRequest();
        String port = request.getServerPort() != 80 ? ":" + request.getServerPort() : "";
        return new StringBuilder()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(port)
                .append(request.getContextPath())
                .toString();
    }

    public static String makeLink(String type, Integer idEntity, String...suffixes) {
        StringBuilder ret = new StringBuilder(getRootPath());
        if(!isEmpty(type)) {
            ret.append(type);
        }
        if(idEntity != null && idEntity >= 0) {
            ret.append(DELIMITER).append(idEntity);
        }
        if(suffixes != null) {
            for(String suffix : suffixes) {
                if(suffix != null) {
                    ret.append(DELIMITER).append(suffix);
                }
            }
        }
        return ret.toString();
    }
}
