package com.accounted4.assetmgr.support.web;

import lombok.Getter;


/**
 * A message to be displayed in web context styled to message type.
 */
public class Message implements java.io.Serializable {

    /**
     * Name of the flash attribute.
     */
    public static final String MESSAGE_ATTRIBUTE = "message";

    /**
     * The type of the message to be displayed. The type is used to show message in a different style.
     */
    public static enum Type {

        DANGER, WARNING, INFO, SUCCESS;
    }
    

    @Getter private final String message;
    @Getter private final Type type;
    @Getter private final Object[] args;

    public Message(String message, Type type) {
        this.message = message;
        this.type = type;
        this.args = null;
    }

    public Message(String message, Type type, Object... args) {
        this.message = message;
        this.type = type;
        this.args = args;
    }

}
