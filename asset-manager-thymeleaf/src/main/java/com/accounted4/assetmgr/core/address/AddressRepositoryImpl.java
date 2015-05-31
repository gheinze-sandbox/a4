package com.accounted4.assetmgr.core.address;

import com.accounted4.assetmgr.core.RecordMetaData;
import com.accounted4.assetmgr.core.ResultCheckingJdbcTemplate;
import com.accounted4.assetmgr.core.SessionUtil;
import com.accounted4.assetmgr.log.Loggable;
import com.accounted4.assetmgr.spring.ExtensibleBeanPropertySqlParameterSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Database access layer for query Address information
 * from a Postgres database.
 *
 * @author gheinze
 */
@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Loggable
    private Logger LOG;

    private final ResultCheckingJdbcTemplate jdbc;
    private final AddressRowMapper addressRowMapper;

    
    @Autowired
    public AddressRepositoryImpl(ResultCheckingJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        addressRowMapper = new AddressRowMapper();
    }

    

    /*
     * ===================================================================
     */
    private static final String INSERT_ADDRESS =
            "INSERT INTO address(org_id, line1, line2, city, subdivision_code, country_code, postal_code, note, inactive)" +
            "  VALUES(:orgId, :line1, :line2, :city, :subdivisionCode, :countryCode, :postalCode, :note, :inactive)"
            ;

    /**
     * {@inheritDoc}
     * @param addressForm backing bean of the ui address form
     * @return 
     */
    @Override
    public long save(AddressForm addressForm) {
        
        // most bind parameters can be retrieved directly from form attributes
        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(addressForm);
        
        // these bind parameters are not available from the form and need to be manually added
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        namedParameters.addValue("inactive", addressForm.getRecord().isInactive());
        
        //TODO: perhaps the return should include version (record meta data?)
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(INSERT_ADDRESS, namedParameters, keyHolder, new String[] {"id"});

        return keyHolder.getKey().longValue();
    
    }

    
    /*
     * ===================================================================
     */
    private static final String GET_ADDRESS_FOR_PARTY =
            "SELECT a.* FROM party_address pa" +
            "  INNER JOIN address a ON (a.org_id = pa.org_id AND a.id = pa.address_id AND pa.party_id = :partyId AND pa.org_id = :orgId)"
            ;

    @Override
    public List<AddressForm> getAddressesForParty(long partyId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("partyId", partyId);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        return jdbc.query(GET_ADDRESS_FOR_PARTY, namedParameters, addressRowMapper);
    }



    private enum AddressColumn {
        id, version, inactive, line1, line2, city, subdivision_code, country_code, postal_code, note;
    }
    
    private static class AddressRowMapper implements RowMapper<AddressForm> {

        @Override
        public AddressForm mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            RecordMetaData recordMetaData = new RecordMetaData();
            recordMetaData.setId(rs.getLong(AddressColumn.id.name()));
            recordMetaData.setVersion(rs.getInt(AddressColumn.version.name()));
            recordMetaData.setInactive(rs.getBoolean(AddressColumn.inactive.name()));
            recordMetaData.setEditMode(true);
            
            AddressForm addressForm = new AddressForm();
            addressForm.setLine1(rs.getString(AddressColumn.line1.name()));
            addressForm.setLine2(rs.getString(AddressColumn.line2.name()));
            addressForm.setCity(rs.getString(AddressColumn.city.name()));
            addressForm.setSubdivisionCode(rs.getString(AddressColumn.subdivision_code.name()));
            addressForm.setCountryCode(rs.getString(AddressColumn.country_code.name()));
            addressForm.setPostalCode(rs.getString(AddressColumn.postal_code.name()));
            addressForm.setNote(rs.getString(AddressColumn.note.name()));
            
            addressForm.setRecord(recordMetaData);
            
            return addressForm;
            
        }
        
    }
    
    
    
}
