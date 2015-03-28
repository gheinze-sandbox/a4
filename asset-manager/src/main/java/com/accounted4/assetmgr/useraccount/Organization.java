package com.accounted4.assetmgr.useraccount;

import lombok.Getter;
import lombok.Setter;

/**
 * Bean to represent the attributes of an Organization: the partitioning
 * object of the multi-tenanted db.
 */
@Getter
@Setter
public class Organization {
    
    private long id;
    private String orgName;
    private String emailAddress;

}
