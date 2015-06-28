package com.accounted4.assetmgr.core.party;

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
import org.springframework.stereotype.Repository;


/**
 * Database access layer for query Party (Person/Organization) information
 * from a Postgres database.
 */
@Repository
public class PartyRepositoryImpl implements PartyRepository {
    
    @Loggable
    private Logger LOG;

    private final ResultCheckingJdbcTemplate jdbc;
    private final PartyRowMapper partyRowMapper;

    
    @Autowired
    public PartyRepositoryImpl(ResultCheckingJdbcTemplate jdbc) {
        this.jdbc = jdbc;
        partyRowMapper = new PartyRowMapper();
    }

    

    /*
     * ===================================================================
     */
    private static final String INSERT_PARTY =
            "INSERT INTO party(org_id, party_name, inactive, notes)" +
            "  VALUES(:orgId, :partyName, :inactive, :notes)"
            ;

    /**
     * {@inheritDoc}
     * @param partyForm backing bean of the ui party form
     * @return The id of the newly created record.
     */
    @Override
    public long save(PartyForm partyForm) {
        ExtensibleBeanPropertySqlParameterSource namedParameters =  new ExtensibleBeanPropertySqlParameterSource(partyForm);
        return jdbc.saveAndReturnKey(INSERT_PARTY, namedParameters);
    }


    /*
     * ===================================================================
     */
    private static final String UPDATE_PARTY =
            "UPDATE party SET party_name = :partyName, notes = :notes, inactive = :inactive" +
            "  WHERE org_id = :orgId AND id = :id AND version = :version"
            ;
    
    @Override
    public int update(PartyForm partyForm) {
        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(partyForm);
        return jdbc.updateWithConcurrencyCheck(UPDATE_PARTY, namedParameters);
    }
    

    /*
     * ===================================================================
     */
    private static final String INACTIVATE_PARTY =
            "UPDATE party SET inactive = true" +
            "  WHERE org_id = :orgId AND id = :id"  // TODO: do we care about version on delete??
            ;
    
    @Override
    public int inactivateParty(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        return jdbc.updateWithConcurrencyCheck(INACTIVATE_PARTY, namedParameters);
    }
    
    
    /*
     * ===================================================================
     */
    private static final String GET_PARTY_BY_ID = 
            "SELECT * FROM party WHERE org_id = :orgId AND id = :id";

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyForm getPartyById(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        return jdbc.queryForObject(GET_PARTY_BY_ID, namedParameters, partyRowMapper);
    }

    
    /*
     * ===================================================================
     */
    private static final String GET_PARTY_BY_NAME =
            "SELECT * FROM party WHERE org_id = :orgId AND party_name = :partyName";

    /**
     * {@inheritDoc}
     */
    @Override
    public PartyForm getPartyByKey(String partyName) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("partyName", partyName);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        return jdbc.queryForObject(GET_PARTY_BY_NAME, namedParameters, partyRowMapper);
    }
    

    /*
     * ===================================================================
     */

    private static final String FIND_PARTIES =
            "SELECT * FROM party WHERE org_id = :orgId AND inactive = :inactive AND party_name ILIKE '%' || :partyName || '%'";
    
    @Override
    public List<PartyForm> findParties(PartyForm partyFormTemplate) {
        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(partyFormTemplate);
        return jdbc.query(FIND_PARTIES, namedParameters, partyRowMapper);
    }

    
    /*
     * ===================================================================
     */

    
    private static final String DETACH_ADDRESS_FROM_PARTY =
            "DELETE FROM party_address WHERE org_id = :orgId AND party_id = :partyId AND address_id = :addressId";
    
    @Override
    public void detachAddressFromParty(PartyForm partyForm, long addressId) {
        executeDml(DETACH_ADDRESS_FROM_PARTY, partyForm, addressId);
    }

    
    
    private static final String ATTACH_ADDRESS_TO_PARTY =
            "INSERT INTO party_address(org_id, party_id, address_id) VALUES(:orgId, :partyId, :addressId)";
    
    @Override
    public void attachAddressToParty(PartyForm partyForm, long addressId) {
        executeDml(ATTACH_ADDRESS_TO_PARTY, partyForm, addressId);
    }

    
    
    private void executeDml(String sql, PartyForm partyForm, long addressId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("partyId", partyForm.getRecordMetaData().getId());
        namedParameters.addValue("addressId", addressId);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        jdbc.update(sql, namedParameters);
    }

        
    private enum PartyColumn {
        id, version, inactive, party_name, notes;
    }
    
    private static class PartyRowMapper implements RowMapper<PartyForm> {

        @Override
        public PartyForm mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            RecordMetaData recordMetaData = new RecordMetaData();
            recordMetaData.setId(rs.getLong(PartyColumn.id.name()));
            recordMetaData.setVersion(rs.getInt(PartyColumn.version.name()));
            recordMetaData.setInactive(rs.getBoolean(PartyColumn.inactive.name()));
            recordMetaData.setEditMode(true);
            
            PartyForm partyForm = new PartyForm();
            partyForm.setPartyName(rs.getString(PartyColumn.party_name.name()));
            partyForm.setNotes(rs.getString(PartyColumn.notes.name()));
            
            partyForm.setRecordMetaData(recordMetaData);
            
            return partyForm;
            
        }
        
    }
    
    
}
