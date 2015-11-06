package com.accounted4.assetmgr.core;

/**
 * Records based on a db read will have metadata for (ex id and version )
 * which the application needs to carry around in order to support 
 * persistence back to the db.
 * 
 * @author gheinze
 */
public interface RecordMetaDataHolder {
    RecordMetaData getRecordMetaData();
}
