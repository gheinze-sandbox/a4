package com.accounted4.assetmgr.spring;

import com.accounted4.assetmgr.core.RecordMetaData;
import com.accounted4.assetmgr.core.RecordMetaDataHolder;
import com.accounted4.assetmgr.core.SessionUtil;
import java.util.Map.Entry;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

/**
 * A SqlParameterSource which can be used to pull parameters from a bean
 * (i.e. the BeanPropertySqlParameterSource) but also provides a facility for adding
 * some extra custom mappings via a MapSqlParameterSource.
 * 
 * @author gheinze
 */
public final class ExtensibleBeanPropertySqlParameterSource extends AbstractSqlParameterSource {

    
    private final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    private final BeanPropertySqlParameterSource beanPropertySqlParameterSource;

    
    /**
     * Create a map of parameters to bind to a jdbc call based on a form object
     * but also allowing the addition of more properties if desired.
     * orgId will always be added to the map from session.
     * RecordMetaData holders will automatically have meta data added to parameter map.
     * @param object 
     */
    public ExtensibleBeanPropertySqlParameterSource(Object object) {
        this.beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(object);
        addValue("orgId", SessionUtil.getSessionOrigId());
        if (object instanceof RecordMetaDataHolder) {
            RecordMetaData metaData = ((RecordMetaDataHolder) object).getRecordMetaData();
            addValue("inactive", metaData.isInactive());
            addValue("id", metaData.getId());
            addValue("version", metaData.getVersion());

        }
    }

    public ExtensibleBeanPropertySqlParameterSource addValue(String paramName, Object value) {
        mapSqlParameterSource.addValue(paramName, value);
        return this;
    }

    @Override
    public boolean hasValue(String paramName) {
        return beanPropertySqlParameterSource.hasValue(paramName) ||
                mapSqlParameterSource.hasValue(paramName);
    }

    @Override
    public Object getValue(String paramName) {
        return beanPropertySqlParameterSource.hasValue(paramName) ?
                beanPropertySqlParameterSource.getValue(paramName) :
                mapSqlParameterSource.getValue(paramName);
    }

    @Override
    public int getSqlType(String paramName) {
        return beanPropertySqlParameterSource.hasValue(paramName) ?
                beanPropertySqlParameterSource.getSqlType(paramName) :
                mapSqlParameterSource.getSqlType(paramName);
    }

    
    // --------------------------------------------
    
    
    
    // --------------------------------------------
    
    @Override
    public String toString() {
        return getMappedParameters()+ " " + getBeanParameters();
    }
    
    
    private String getMappedParameters() {
        
        StringBuilder msg = new StringBuilder();
        
        msg.append("Mapped parameters: {");
        
        for (Entry<String, Object> entry : mapSqlParameterSource.getValues().entrySet()) {
            msg.append("[");
            msg.append(entry.getKey());
            msg.append(", ");
            msg.append(entry.getValue());
            msg.append("]");
        }
        
        msg.append("}");
        
        return msg.toString();
        
    }
    
    private String getBeanParameters() {
        
        StringBuilder msg = new StringBuilder();
        
        msg.append("Bean Property Parameters: {");
        
        String[] readablePropertyNames = beanPropertySqlParameterSource.getReadablePropertyNames();
        for (String property : readablePropertyNames) {
            msg.append("[");
            msg.append(property);
            msg.append(", ");
            msg.append(beanPropertySqlParameterSource.getValue(property));
            msg.append("]");
        }
        
        msg.append("}");
        
        return msg.toString();
        
    }
    
    
}
