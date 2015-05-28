package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.core.RecordMetaData;
import com.accounted4.assetmgr.core.SessionUtil;
import com.accounted4.assetmgr.spring.ExtensibleBeanPropertySqlParameterSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * Database access layer for query Party (Person/Organization) information
 * from a Postgres database.
 */
@Repository
public class PartyRepositoryImpl implements PartyRepository {
    
    private final NamedParameterJdbcTemplate jdbc;
    private final PartyRowMapper partyRowMapper;

    
    @Autowired
    public PartyRepositoryImpl(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
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
     */
    @Override
    public void save(PartyForm partyForm) {
        
        // most bind parameters can be retrieved directly from form attributes
        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(partyForm);
        
        // these bind parameters are not available from the form and need to be manually added
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        namedParameters.addValue("inactive", partyForm.getRecord().isInactive());
        
        jdbc.update(INSERT_PARTY, namedParameters);
        
        // although we could be more efficient and use a "returning" clause to get the id and version
        // (and using SimpleJdbcInsert), performing an extra query may be more portable across dbs
    }


    /*
     * ===================================================================
     */
    private static final String UPDATE_PARTY =
            "UPDATE party SET party_name = :partyName, notes = :notes, inactive = :inactive" +
            "  WHERE org_id = :orgId AND id = :id AND version = :version"
            ;
    
    @Override
    public void update(PartyForm partyForm) {

        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(partyForm);
        
        // these bind parameters are not available from the form and need to be manually added
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        namedParameters.addValue("inactive", partyForm.getRecord().isInactive());
        namedParameters.addValue("id", partyForm.getRecord().getId());
        namedParameters.addValue("version", partyForm.getRecord().getVersion());
        
        jdbc.update(UPDATE_PARTY, namedParameters);
        
        // TODO: handle concurrent update: ie no rows updated because of version
        
    }
    

    /*
     * ===================================================================
     */
    private static final String DELETE_PARTY =
            "UPDATE party SET inactive = true" +
            "  WHERE org_id = :orgId AND id = :id"  // TODO: do we care about version on delete??
            ;
    
    @Override
    public void deleteParty(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        jdbc.update(DELETE_PARTY, namedParameters);
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
        namedParameters.addValue("orgId", SessionUtil.getSessionOrigId());
        namedParameters.addValue("inactive", partyFormTemplate.getRecord().isInactive());
        return jdbc.query(FIND_PARTIES, namedParameters, partyRowMapper);
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
            
            partyForm.setRecord(recordMetaData);
            
            return partyForm;
            
        }
        
    }
    
    
}
