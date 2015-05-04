package com.accounted4.assetmgr.useraccount;

import java.util.Collection;
import java.util.Objects;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Spring Security puts a UserDetails into session as an attribute of the Principal.
 * We override the UserDetails and add some other information which will be useful
 * to have in session (display name of the user, the orgId, etc).
 * 
 * The Principal can be retrieved from the RequestMapping of any controller, or it
 * can be retrieved by a static context call:
 * 
 * (AppUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 * 
 * @author gheinze
 */
public class AppUserDetails extends User {
    
    @Getter private final int orgId;
    @Getter private final String orgName;
    @Getter private final String fullyQualifiedAccountName;
    @Getter private final String displayName;
    @Getter private final String emailAddress;

    public AppUserDetails(UserAccount account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getFullyQualifiedAccountName(), account.getEncryptedPassword(), authorities);
        this.orgId = account.getOrgId();
        this.orgName = account.getOrgName();
        this.fullyQualifiedAccountName = account.getFullyQualifiedAccountName();
        this.displayName = account.getDisplayName();
        this.emailAddress = account.getEmailAddress();
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.orgId;
        hash = 23 * hash + Objects.hashCode(this.fullyQualifiedAccountName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AppUserDetails other = (AppUserDetails) obj;
        if (this.orgId != other.orgId) {
            return false;
        }
        if (!Objects.equals(this.fullyQualifiedAccountName, other.fullyQualifiedAccountName)) {
            return false;
        }
        return true;
    }

}
