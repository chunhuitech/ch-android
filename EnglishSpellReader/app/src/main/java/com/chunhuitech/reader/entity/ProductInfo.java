package com.chunhuitech.reader.entity;

public class ProductInfo {

    public ProductInfo(String name, String technologyPlatform) {
        this.name = name;
        this.technologyPlatform = technologyPlatform;
    }
    private Long id;

    private String name;

    private String technologyPlatform;

    private String version;

    private Integer versionNum;

    private String downAddress;

    private Long modifyTime;

    private Long createTime;

    private String remarks;

    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTechnologyPlatform() {
        return technologyPlatform;
    }

    public void setTechnologyPlatform(String technologyPlatform) {
        this.technologyPlatform = technologyPlatform == null ? null : technologyPlatform.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public String getDownAddress() {
        return downAddress;
    }

    public void setDownAddress(String downAddress) {
        this.downAddress = downAddress == null ? null : downAddress.trim();
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
