package com.micro.thing.mybatis.model;

import java.io.Serializable;

/**
 * This class corresponds to the database table SPRING_SESSION_ATTRIBUTES
 */
public class SpringSessionAttributes implements Serializable {
    private String sessionPrimaryId;

    private String attributeName;

    private byte[] attributeBytes;

    private static final long serialVersionUID = 1L;

    public String getSessionPrimaryId() {
        return sessionPrimaryId;
    }

    public void setSessionPrimaryId(String sessionPrimaryId) {
        this.sessionPrimaryId = sessionPrimaryId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public byte[] getAttributeBytes() {
        return attributeBytes;
    }

    public void setAttributeBytes(byte[] attributeBytes) {
        this.attributeBytes = attributeBytes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sessionPrimaryId=").append(sessionPrimaryId);
        sb.append(", attributeName=").append(attributeName);
        sb.append(", attributeBytes=").append(attributeBytes);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}