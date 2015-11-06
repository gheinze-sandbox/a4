package com.accounted4.assetmgr.useraccount;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Backing bean representing a user account in application. Meant to be attached
 * to the Spring Authentication object so that the information is available
 * within the session.
 */
@Getter
@Setter
public class UserAccount {

    private long id;
    private int version;
    private int orgId;
    private String orgName;
    private String fullyQualifiedAccountName;
    private String encryptedPassword;
    private String status;
    private String displayName;
    private String emailAddress;
    
    private List<String> authorities = new ArrayList<>();
    
    
    public void addAuthority(String authority) {
        authorities.add(authority);
    }
    
}
