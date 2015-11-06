package com.accounted4.assetmgr.core.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author gheinze
 */
@Controller
public class AddressController {

    @Autowired
    AddressService addressService;
    
    
    @RequestMapping(value = "core/addressSearch/{filter}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAddresses(@PathVariable String filter) {
        String result = addressService.getAddressList(filter);
        return result;
    }
    
}
