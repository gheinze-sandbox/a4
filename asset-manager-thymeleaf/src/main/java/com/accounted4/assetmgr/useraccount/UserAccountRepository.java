package com.accounted4.assetmgr.useraccount;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;


/**
 * Perform db interfacing activities for UserAccounts.  If the activity is
 * not doing db io, then it probably belongs in the UserService.
 * 
 */
@Repository
public class UserAccountRepository {

    
    private final NamedParameterJdbcTemplate jdbc;


    @Autowired
    public UserAccountRepository(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
    }

    
    
    public UserAccount save(UserAccount accountToSave) {
        saveUserAccount(accountToSave);
        saveUserAccountAuthorities(accountToSave);
        return findByFullyQualifiedAccountName(accountToSave.getFullyQualifiedAccountName());
    }


    /*
     * ===================================================================
     */
    private static final String FIND_BY_FULLY_QUALIFIED_ACCOUNT_NAME =
            "SELECT * FROM user_account WHERE fully_qualified_account_name = :fullyQualifiedAccountName"
            ;
    //TODO: Populate org_name??
    
    public UserAccount findByFullyQualifiedAccountName(String fullyQualifiedAccountName) {
        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("fullyQualifiedAccountName", fullyQualifiedAccountName);
        
        // TODO: add org name to UserAccount?
        
        UserAccount result = jdbc.queryForObject(
                FIND_BY_FULLY_QUALIFIED_ACCOUNT_NAME
                ,namedParameters
                ,new BeanPropertyRowMapper<UserAccount>(UserAccount.class)
        );
        
        return addAuthoritiesToAccount(result);
    }
    
    
    private UserAccount addAuthoritiesToAccount(UserAccount account) {
        if (null != account) {
            account.setAuthorities(getAuthoritiesForAccount(account.getOrgId(), account.getId()));
        }
        return account;
    }
    
    
    /*
     * ===================================================================
     */
    private static final String GET_AUTHORITIES =
            "SELECT authority FROM user_account_authority WHERE org_id = :orgId AND user_account_id = :userAccountId"
            ;
    
    private List<String> getAuthoritiesForAccount(int orgId, long userAccountId) {
        
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("orgId", orgId);
        namedParameters.addValue("userAccountId", userAccountId);
        return jdbc.query(GET_AUTHORITIES, namedParameters, new SingleColumnRowMapper(String.class));
    }
    
    
    /*
     * ===================================================================
     */
    private static final String INSERT_USER_ACCOUNT =
            "INSERT INTO user_account(org_id, fully_qualified_account_name, encrypted_password, status, display_name, email_address)" +
            "  SELECT id, :fullyQualifiedAccountName, :encryptedPassword, :status ::user_account_status, :displayName, :emailAddress" +
            "    FROM organization" +
            "    WHERE org_name = :orgName"
            ;

    private void saveUserAccount(UserAccount account) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(account);
        jdbc.update(INSERT_USER_ACCOUNT, namedParameters);
    }


    /*
     * ===================================================================
     */
    private static final String INSERT_USER_ACCOUNT_AUTHORITIES =
        "INSERT INTO user_account_authority(org_id, user_account_id, authority)" +
        "  SELECT u.org_id, u.id, :authority" +
        "    FROM user_account u" +
        "    WHERE u.fully_qualified_account_name = :fullQualifiedAccountName"
            ;
    
    private void saveUserAccountAuthorities(UserAccount account) {
        
        List<SqlParameterSource> parameterBatch = new ArrayList<>();
        
        account.getAuthorities().stream().map((authority) -> {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("fullQualifiedAccountName", account.getFullyQualifiedAccountName());
            namedParameters.addValue("authority", authority);
            return namedParameters;
        }).forEach((namedParameters) -> {
            parameterBatch.add(namedParameters);
        });
        
        if (!parameterBatch.isEmpty()) {
            jdbc.batchUpdate(INSERT_USER_ACCOUNT_AUTHORITIES, parameterBatch.toArray(new SqlParameterSource[0]));
        }
        
    }
    
//    private class UserAccountRowMapper implements RowMapper<UserAccount> {
//
//        @Override
//        public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
//            UserAccount account = new UserAccount();
//            account.setOrgId(rs.getInt("org_id"));
//            account.setId(rs.getLong("id"));
//            account.setVersion(rs.getInt("version"));
//            account.setAccountName(rs.getString("account_name"));
//            account.setEncryptedPassword(rs.getString("encrypted_password"));
//            account.setStatus(rs.getString("status"));
//            account.setDisplayName(rs.getString("display_name"));
//            account.setEmailAddress(rs.getString("email_address"));
//            return account;
//        }
//        
//    }

}
