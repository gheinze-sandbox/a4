package com.accounted4.assetmgr.useraccount;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * This is the Spring Authentication User service: a DAO for retrieving user data.
 * It supplies user data to other components within the Spring framework. In particular,
 * it does not authenticate the user, which is done by the AuthenticationManager. But it
 * retrieves a UserDetails object which the AuthenticationManager uses to check authentication.
 */
public class UserService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        String fullyQualifiedUsername = (new AccountName(username).getFullyQualifiedName());
        
        UserAccount userAccount = userAccountRepository.findByFullyQualifiedAccountName(fullyQualifiedUsername);
        
        if (userAccount == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return createUser(userAccount);
        
    }

    
    public void signin(UserAccount userAccount) {
        Authentication authenticationToken = createAuthenticationToken(userAccount);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    

    private Authentication createAuthenticationToken(UserAccount account) {
        return new UsernamePasswordAuthenticationToken(
                createUser(account)
                ,null
                ,createAuthorities(account)
        );
    }
    

    private User createUser(UserAccount account) {
        return new AppUserDetails(account, createAuthorities(account));
    }

    
    private List<GrantedAuthority> createAuthorities(UserAccount account) {
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        account.getAuthorities().stream().forEach((authority) -> {
            authorities.add(new SimpleGrantedAuthority(authority));
        });
        
        return authorities;
        
    }

    
    
    
    private static final String DEFAULT_ACCOUNT_STATUS = "ACTIVE";
    
    // Pull from properties file?
    private static final String ORG_SPLIT_TOKEN = "@";
    private static final String DEFAULT_ORG_NAME = "Public";
    private static final String DEFAULT_ORG_SUFFIX = ORG_SPLIT_TOKEN + DEFAULT_ORG_NAME;
 


    public UserAccount createPublicUserAccount(String providedAccountName, String unEncryptedPassword) {
        String qualifiedAccountName = providedAccountName.endsWith(DEFAULT_ORG_SUFFIX) ?
                providedAccountName :
                providedAccountName + DEFAULT_ORG_SUFFIX;
        return createUserAccount(qualifiedAccountName, unEncryptedPassword);
    }
    
    
    public UserAccount createUserAccount(String providedAccountName, String unEncryptedPassword) {
        AccountName accountName = new AccountName(providedAccountName);
        UserAccount userAccount = new UserAccount();
        userAccount.setOrgName(accountName.getOrgName());
        userAccount.setFullyQualifiedAccountName(accountName.getFullyQualifiedName());
        userAccount.setEncryptedPassword(passwordEncoder.encode(unEncryptedPassword));
        userAccount.setStatus(DEFAULT_ACCOUNT_STATUS);
        userAccount.setDisplayName(accountName.getFullyQualifiedName());
        // TODO: email
        userAccount.addAuthority("ROLE_USER");
        
        // id to be generated
        // version to be generated
        // org_id to be looked up
        
        return userAccountRepository.save(userAccount);

    }
        

    /**
     * An account name is expected to be in the form of: username@organization,
     * but a default organization will be provided if not specified.
     * 
     */
    public static class AccountName {
        
        @Getter private final String unqualifiedName;
        @Getter private final String orgName;
        
        
        public AccountName(String accountName) {
            
            // TODO: might need to do some validation here if the username is length 1
            // ends in a "@", is an "@", etc.  For now, a length validator added to
            // backing input bean, but this could be called from other places. Needs testing.
            String strippedAccountName = accountName.endsWith(ORG_SPLIT_TOKEN) ?
                    accountName.substring(0, accountName.length() - 1) :
                    accountName;
            
            
            int splitLocation = strippedAccountName.lastIndexOf(ORG_SPLIT_TOKEN);
            unqualifiedName = (splitLocation < 0) ? strippedAccountName : strippedAccountName.substring(0, splitLocation);
            orgName = (splitLocation < 0) ? DEFAULT_ORG_NAME : strippedAccountName.substring(splitLocation + 1);
        }
     
        
        public String getFullyQualifiedName() {
            return unqualifiedName + ORG_SPLIT_TOKEN + orgName;
        }
        
    }
    
    
}
