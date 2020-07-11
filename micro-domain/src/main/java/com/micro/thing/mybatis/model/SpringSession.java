package com.micro.thing.mybatis.model;

import java.io.Serializable;

/**
 * This class corresponds to the database table SPRING_SESSION
 */
public class SpringSession implements Serializable {
    private String primaryId;

    private String sessionId;

    private Long creationTime;

    private Long lastAccessTime;

    private Integer maxInactiveInterval;

    private Long expiryTime;

    private String principalName;

    private static final long serialVersionUID = 1L;

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public Long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", primaryId=").append(primaryId);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", creationTime=").append(creationTime);
        sb.append(", lastAccessTime=").append(lastAccessTime);
        sb.append(", maxInactiveInterval=").append(maxInactiveInterval);
        sb.append(", expiryTime=").append(expiryTime);
        sb.append(", principalName=").append(principalName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}