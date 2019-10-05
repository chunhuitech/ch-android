package com.chunhuitech.reader.entity;

public class ProductActivity {

    public ProductActivity(Long userId, String clientFlag, String procName, String procVersion, Long procId, String os, String eventName, String ip, String netIp, String area) {
        this.clientFlag = clientFlag;
        this.procName = procName;
        this.procVersion = procVersion;
        this.procId = procId;
        this.ip = ip;
        this.os = os;
        this.eventName = eventName;
        this.userId = userId;
        this.netIp = netIp;
        this.area = area;

    }
    private Long userId;

    private String clientFlag;

    private String procName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String procVersion;

    private Long procId;

    private String os;

    private String eventName;

    private String ip;

    private String netIp;

    private String area;

    public String getClientFlag() {
        return clientFlag;
    }

    public void setClientFlag(String clientFlag) {
        this.clientFlag = clientFlag;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcVersion() {
        return procVersion;
    }

    public void setProcVersion(String procVersion) {
        this.procVersion = procVersion;
    }

    public Long getProcId() {
        return procId;
    }

    public void setProcId(Long procId) {
        this.procId = procId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetIp() {
        return netIp;
    }

    public void setNetIp(String netIp) {
        this.netIp = netIp;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
