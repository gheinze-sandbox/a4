package com.accounted4.assetmgr.auth;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;


/**
 * The backing bean for the Signup page.
 */
public class SignupForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    
    @Getter @Setter
    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    @Size(min = 2)
    private String accountName;

    @Getter @Setter
    @NotBlank(message = SignupForm.NOT_BLANK_MESSAGE)
    private String password;
    
}
