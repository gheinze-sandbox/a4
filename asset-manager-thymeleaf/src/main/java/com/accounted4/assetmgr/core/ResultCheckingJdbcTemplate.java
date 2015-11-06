package com.accounted4.assetmgr.core;

import com.accounted4.assetmgr.log.Loggable;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/**
 * JdbcTemplate which performs some boilerplate error checking that
 * exactly one row is updated in a standard update.
 * 
 * @author gheinze
 */
@Component
public class ResultCheckingJdbcTemplate extends NamedParameterJdbcTemplate{

    @Loggable
    private Logger LOG;

    @Autowired
    public ResultCheckingJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }
    

    /**
     * Call for an insert, have the key returned.
     * 
     * @param sql
     * @param paramSource
     * @return
     * @throws DataAccessException 
     */
    public long saveAndReturnKey(String sql, SqlParameterSource paramSource) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        update(sql, paramSource, keyHolder, new String[] {RecordMetaData.KEY_COLUMN_NAME});
        return keyHolder.getKey().longValue();
    }
    
     /**
      * Perform the normal jdbc update, but expect exactly one row to be affected.
      * @param sql
      * @param paramSource
      * @return
      * @throws DataAccessException 
      */   
    public int updateWithConcurrencyCheck(String sql, SqlParameterSource paramSource) throws DataAccessException {
        
        int rowsUpdated = super.update(sql, paramSource);
        
        switch (rowsUpdated) {
            
            case 0:
                throw new ConcurrentDataAccessException();
                
            case 1:
                break;  // There should be exactly 1 record updated or deleted
                
            default:
                StringBuilder msg = new StringBuilder("Expected only one row to be changed, found " + rowsUpdated );
                msg.append("\n").append(sql);
                msg.append("\n").append(paramSource.toString());
                LOG.error(msg.toString());
                
        }
        
        return rowsUpdated;
        
    }
    
}
