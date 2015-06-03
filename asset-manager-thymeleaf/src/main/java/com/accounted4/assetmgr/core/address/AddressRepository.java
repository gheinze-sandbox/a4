package com.accounted4.assetmgr.core.address;

import java.util.List;


/**
 *
 */
public interface AddressRepository {
 
    long save(AddressForm addressForm);
    
    List<AddressForm> getAddressesForParty(long partyId);

    void update(AddressForm addressForm);

}
