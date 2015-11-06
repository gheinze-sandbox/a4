package com.accounted4.assetmgr.core;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Allows a RecordMetaData parameter as a controller request handler and
 * defines how to extract such an object out of the request parameters.
 * This is tightly coupled with the html fragment "metaDataFields.html".
 *
 * @author gheinze
 */
public class RecordMetaDataResolver implements HandlerMethodArgumentResolver {


    private static final String ID = "metaData.id";
    private static final String VERSION = "metaData.version";
    private static final String INACTIVE = "metaData.inactive";
    private static final String EDITMODE = "metaData.editMode";


    @Override
    public boolean supportsParameter(MethodParameter mp) {
        return mp.getParameterType().equals(RecordMetaData.class);
    }


    @Override
    public Object resolveArgument(
            MethodParameter mp
            ,ModelAndViewContainer mavc
            ,NativeWebRequest nwr
            ,WebDataBinderFactory wdbf
    ) throws Exception {

        if (!mp.getParameterType().equals(RecordMetaData.class)) {
            return null;
        }

        RecordMetaData recordMetaData = new RecordMetaData();

        String paramValue = (String) nwr.getParameter(ID);
        if (null != paramValue) {
            recordMetaData.setId(Long.parseLong(paramValue));
        }

        paramValue = (String) nwr.getParameter(VERSION);
        if (null != paramValue) {
            recordMetaData.setVersion(Integer.parseInt(paramValue));
        }

        paramValue = (String) nwr.getParameter(INACTIVE);
        if (null != paramValue) {
            recordMetaData.setInactive(Boolean.parseBoolean(paramValue));
        }

        paramValue = (String) nwr.getParameter(EDITMODE);
        if (null != paramValue) {
            recordMetaData.setEditMode(Boolean.parseBoolean(paramValue));
        }

        return recordMetaData;

    }

}
