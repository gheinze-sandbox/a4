package com.accounted4.assetmgr.core;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gheinze
 */
public class RecordMetaData {

    public static final String[] keyColumn = new String[] {"id"};
    
    
    @Getter @Setter
    private long id;
    
    @Getter @Setter
    private int version;

    @Getter @Setter
    private boolean inactive;
    
    @Getter @Setter
    private boolean editMode = false;

    public boolean isInsertMode() {
        return !editMode;
    }

    @Override
    public String toString() {
        return "RecordMetaData{" + "id=" + id + ", version=" + version + ", inactive=" + inactive + ", editMode=" + editMode + '}';
    }
    
}
