package com.accounted4.assetmgr.support.web;

import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Support for a ThymeLeaf "templating" layout structure. We intercept the view name
 * returned from the Controller and replace it with the view name of the layout. The
 * original view name is added to the pages backing bean so that the template can 
 * reference the view name as the content fragment replacement target.
 * 
 * See: http://blog.codeleak.pl/2013/11/thymeleaf-template-layouts-in-spring.html
 */
public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

    private static final String DEFAULT_LAYOUT = "core/layouts/default";
    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";
    
    private String defaultLayout = DEFAULT_LAYOUT;
    private String viewAttributeName = DEFAULT_VIEW_ATTRIBUTE_NAME;

    
    public void setDefaultLayout(String defaultLayout) {
        Assert.hasLength(defaultLayout);
        this.defaultLayout = defaultLayout;
    }

    
    public void setViewAttributeName(String viewAttributeName) {
        Assert.hasLength(defaultLayout);
        this.viewAttributeName = viewAttributeName;
    }

    
    @Override
    public void postHandle(
            HttpServletRequest request
            ,HttpServletResponse response
            ,Object handler
            ,ModelAndView modelAndView
    ) throws Exception {
        
        if (modelAndView == null || !modelAndView.hasView()) {
            return;
        }
        
        String originalViewName = modelAndView.getViewName();
        
        if (isRedirectOrForward(originalViewName)) {
            return;
        }
        
        String layoutName = getLayoutName(handler);
        modelAndView.setViewName(layoutName);
        modelAndView.addObject(this.viewAttributeName, originalViewName);
    }

    
    
    private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }

    
    private String getLayoutName(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Layout layout = getMethodOrTypeAnnotation(handlerMethod);
        return (null == layout ? this.defaultLayout : layout.value());
    }

    
    /**
     * Determine the value of the Layout annotation with which the controller may 
     * have been tagged. The annotation can be at the method level or the class level.
     * 
     * @param handlerMethod The Spring Controller handling the request.
     * @return The Layout with which the Controller was annotated.
     */
    private Layout getMethodOrTypeAnnotation(HandlerMethod handlerMethod) {
        
        // Try determining the layout from the method level annotation
        Layout layout = handlerMethod.getMethodAnnotation(Layout.class);
        
        if (layout == null) {
            // Try determining the layout from the class level annotation
            layout = handlerMethod.getBeanType().getAnnotation(Layout.class);
        }
        
        return layout;
    }
    
}
