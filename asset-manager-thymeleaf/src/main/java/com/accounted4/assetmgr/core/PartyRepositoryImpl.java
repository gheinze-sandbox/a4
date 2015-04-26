package com.accounted4.assetmgr.core;

import com.accounted4.assetmgr.spring.ExtensibleBeanPropertySqlParameterSource;
import com.accounted4.assetmgr.useraccount.AppUserDetails;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public class PartyRepositoryImpl implements PartyRepository {
    
    private final NamedParameterJdbcTemplate jdbc;


    @Autowired
    public PartyRepositoryImpl(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    

    /*
     * ===================================================================
     */
    private static final String INSERT_PARTY =
            "INSERT INTO party(org_id, display_name, inactive, notes)" +
            "  VALUES(:orgId, :partyName, :inactive, :notes)"
            ;

    @Override
    public void save(PartyForm partyForm) {
        ExtensibleBeanPropertySqlParameterSource namedParameters = new ExtensibleBeanPropertySqlParameterSource(partyForm);
        AppUserDetails user = (AppUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        namedParameters.addValue("orgId", user.getOrgId());
        namedParameters.addValue("inactive", partyForm.getRecord().isInactive());
        jdbc.update(INSERT_PARTY, namedParameters);
    }


    
}
