package com.accounted4.assetmgr.core;

import java.util.function.Consumer;

/**
 * Create a Functional Reference of {@link Consumer}. Associated a
 * message with the consumer action completion. The example usage is wrapping
 * a call to save or update an object with message of "saved" or "updated".
 * @author gheinze
 * @param <T>
 */
public class ConsumerServiceWrapper<T> {

    private final Consumer<T> action;
    private final String completionMessage;
    
    
    public ConsumerServiceWrapper(Consumer<T> action, String completionMessage) {
        this.action = action;
        this.completionMessage = completionMessage;
    }
    
    public Consumer<T> getServiceAction() {
        return action;
    }
    
    public String getCompletionMessage() {
        return completionMessage;
    }
    
}
