package com.accounted4.assetmgr.core;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Backing bean for the Party form.
 */
public class PartyForm {
    
    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    
    @Getter @Setter
    @NotBlank(message = PartyForm.NOT_BLANK_MESSAGE)
    @Size(min = 2)
    private String partyName;

    @Getter @Setter
    private String notes;    
    
    @Getter @Setter
    private RecordMetaData record = new RecordMetaData();
    
    @Getter @Setter
    private String address;
    
}
