package com.github.beljaeff.sjb.util;

import freemarker.ext.jsp.TaglibFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public class SjbFreeMarkerConfigurer extends FreeMarkerConfigurer {

    /**
     * Override this to resolve ObjectMapper initialization
     */
    @Override
    public TaglibFactory getTaglibFactory() {
        TaglibFactory tagLibFactory = super.getTaglibFactory();
        if (tagLibFactory.getObjectWrapper() == null) {
            tagLibFactory.setObjectWrapper(super.getConfiguration().getObjectWrapper());
        }
        return tagLibFactory;
    }
}
