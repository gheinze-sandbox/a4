package com.accounted4.assetmgr.core.address;

import java.util.List;

/**
 *
 * @author gheinze
 */
public interface AddressService {

    void updateAddress(AddressForm addressForm);
  
    String getAddressList(String filter);
    
}
