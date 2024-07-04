package com.smeup.kokos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class A37Configuration {
    @JsonProperty("id")
    private String id;
    @JsonProperty("listenerCfg")
    private Map<String, String> properties;
    @JsonProperty("evtTTL")
    private long evtTTL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public long getEvtTTL() {
        return evtTTL;
    }

    public void setEvtTTL(long evtTTL) {
        this.evtTTL = evtTTL;
    }
}
