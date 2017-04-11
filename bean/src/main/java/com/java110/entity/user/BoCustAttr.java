package com.java110.entity.user;

/**
 * 客户属性表（过程表）
 * Created by wuxw on 2016/12/27.
 */
public class BoCustAttr {

    private String boId;

    private String custId;

    private String attrCd;

    private String value;

    private String state;

    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    /**
     * 将过程数据转为实例数据
     * @return
     */
    public CustAttr convert(){
        CustAttr custAttr = new CustAttr();

        custAttr.setCustId(this.getCustId());
        custAttr.setAttrCd(this.getAttrCd());
        custAttr.setValue(this.getValue());
        return custAttr;
    }
}
