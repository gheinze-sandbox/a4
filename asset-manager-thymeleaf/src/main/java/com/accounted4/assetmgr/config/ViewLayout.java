package com.accounted4.assetmgr.config;

/**
 * The templates used to generate the views returned by the Controllers.
 * Normally, the controller returns a view, but in this application, the
 * controller returns a fragment to be used in a template. In interceptor
 * captures the fragment name and replaces the "content" section of the
 * template with the fragment.
 * See: http://blog.codeleak.pl/2013/11/thymeleaf-template-layouts-in-spring.html
 */
public interface ViewLayout {
    static final String DEFAULT = "core/layouts/default";
    static final String VANILLA = "core/layouts/vanilla";
}
