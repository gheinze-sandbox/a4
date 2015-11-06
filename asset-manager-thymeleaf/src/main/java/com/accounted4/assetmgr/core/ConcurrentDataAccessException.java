package com.accounted4.assetmgr.core;

import org.springframework.dao.DataAccessException;

/**
 * Update and Delete operations should fail to complete and throw a 
 * ConcurrentDataAccessException if the version of the record in the
 * data store does not match the version of the record upon which
 * the operation is being performed.
 * 
 * @author gheinze
 */
public class ConcurrentDataAccessException extends DataAccessException {

    public ConcurrentDataAccessException() {
        super("Operation was not performed as the record may have been modified in another session. Please reload the record and try again.");
    }
    
    public ConcurrentDataAccessException(String msg) {
        super(msg);
    }
    
}
