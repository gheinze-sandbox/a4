package com.accounted4.assetmgr.core.address;

/**
 *
 * @author gheinze
 */
public interface AddressService {

    void updateAddress(AddressForm addressForm);
  
    String getAddressList(String filter);
    
}
