package com.indiepost.v8view;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * Created by jake on 17. 2. 23.
 */

/**
 * Created on 2016-12-03
 *
 * @author Patrick
 */
public class V8ScriptTemplateViewResolver extends UrlBasedViewResolver {
    public V8ScriptTemplateViewResolver() {
        setViewClass(requiredViewClass());
    }

    public V8ScriptTemplateViewResolver(String prefix, String suffix) {
        this();
        setPrefix(prefix);
        setSuffix(suffix);
    }


    @Override
    protected Class<?> requiredViewClass() {
        return V8ScriptTemplateView.class;
    }
}