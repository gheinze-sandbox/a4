package com.accounted4.assetmgr.core.address;

import com.accounted4.assetmgr.core.RecordMetaData;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Backing bean for the Address form.
 * 
 */
public class AddressForm {
    
    private static final String DEFAULT_COUNTRY = "CA";
    private static final String DEFAULT_PROVINCE = "ON";
    private static final String DEFAULT_CITY = "Kitchener";


    @Getter @Setter
    private String line1;

    @Getter @Setter
    private String line2;

    @Getter @Setter
    private String city = DEFAULT_CITY;

    @Getter @Setter
    @Pattern(regexp = "AB|BC|MB|NB|NL|NT|NS|NU|ON|PE|QC|SK|YT", message = "Province code validation failed.")
    private String subdivisionCode = DEFAULT_PROVINCE;

    @Getter @Setter
    @Pattern(regexp = "CA", message = "Country code validation failed.")
    private String countryCode = DEFAULT_COUNTRY;

    @Getter @Setter
    //@Pattern(regexp = "[A-Za-z]\\d[A-Za-z]\\s?\\d[A-Za-z]\\d", message = "Postal code validation failed.")
    private String postalCode;

    @Getter @Setter
    private String note;


    @Getter @Setter
    private RecordMetaData record = new RecordMetaData();

    @Override
    public String toString() {
        
        StringBuilder result = new StringBuilder();
        
        result.append(StringUtils.hasText(line1) ? ", " + line1 : "");
        result.append(StringUtils.hasText(line2) ? ", " + line2 : "");
        result.append(StringUtils.hasText(city) ? ", " + city : "");
        result.append(StringUtils.hasText(subdivisionCode) ? ", " + subdivisionCode : "");
        result.append(StringUtils.hasText(postalCode) ? ", " + postalCode : "");
        
        return result.length() > 0 ? result.substring(2) : "";

    }
 
}
