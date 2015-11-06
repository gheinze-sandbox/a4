package com.accounted4.assetmgr.error;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.core.ConcurrentDataAccessException;
import com.accounted4.assetmgr.log.Loggable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;

/**
 * General error handler for the application.
 */
@ControllerAdvice
class ExceptionHandler {

    @Loggable
    private Logger log;
    
    
    /**
     * Handle exceptions thrown by handlers.
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ModelAndView exception(Exception exception, WebRequest request) {
        
        Throwable rootThrowable = Throwables.getRootCause(exception);
        
        String msg;
        
        if (exception instanceof DuplicateKeyException) {
            msg = "It seems this record alread existed in the database.<br>" +
                    "Try finding the record rather than creating it.<br>" +
                    "If it can't be found, make sure it hasn't been disabled.<br><br>" +
                       rootThrowable.getMessage();
                    
        } else if (exception instanceof ConcurrentDataAccessException) {
            msg = "The operation was not performed as the record may have been modified in another session.<br>" +
                    "Please reload the record and try again.";
            
        } else {
            msg = "Please notify the administrator that an unexpected exception has been encountered.<br>" +
                       "Please note the sequence of events that led to this exception and whether it is reproducible.<br><br>" +
                       rootThrowable.getMessage();
        }
        
        ModelAndView modelAndView = new ModelAndView(ViewRoute.GENERAL_ERROR);
        modelAndView.addObject("errorMessage", msg);

        log.warn("Generic exception handler captured: ", rootThrowable);
        
        return modelAndView;
        
    }
    
}
