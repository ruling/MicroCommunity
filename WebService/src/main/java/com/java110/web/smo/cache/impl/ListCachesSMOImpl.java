package com.java110.web.smo.cache.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.PrivilegeCodeConstant;
import com.java110.common.constant.ServiceConstant;
import com.java110.common.exception.SMOException;
import com.java110.common.util.Assert;
import com.java110.common.util.BeanConvertUtil;
import com.java110.core.context.IPageData;
import com.java110.entity.component.ComponentValidateResult;
import com.java110.web.core.AbstractComponentSMO;
import com.java110.web.smo.cache.IListCachesSMO;
import com.java110.web.smo.serviceRegister.IListServiceRegistersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 查询serviceRegister服务类
 */
@Service("listCachesSMOImpl")
public class ListCachesSMOImpl extends AbstractComponentSMO implements IListCachesSMO {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> listCaches(IPageData pd) throws SMOException {
        return businessProcess(pd);
    }


    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

        super.validatePageInfo(pd);

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.HAS_LIST_CACHE);
    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) {
        ComponentValidateResult result = super.validateStoreStaffCommunityRelationship(pd, restTemplate);

        Map paramMap = BeanConvertUtil.beanCovertMap(result);
        paramIn.putAll(paramMap);

        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/query.console.caches" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        return responseEntity;
    }


    @Override
    public ResponseEntity<String> flushCache(IPageData pd) throws SMOException {

        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.HAS_LIST_CACHE);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        //根据ID查询缓存ID
        String apiUrl = ServiceConstant.SERVICE_API_URL + "/api/query.console.cache" + mapToUrlParam(paramIn);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                apiUrl,
                HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return responseEntity;
        }

        JSONObject paramOut = JSONObject.parseObject(responseEntity.getBody());

        Assert.hasKey(paramOut, "cache", "查询缓存失败，返回报文中未包含cache节点");

        JSONObject cacheObj = paramOut.getJSONObject("cache");

        Assert.hasKeyAndValue(cacheObj, "url", "查询缓存失败，返回报文中未包含url节点");

        String url = cacheObj.getString("url");

        //调用地址刷新缓存
        //根据ID查询缓存ID
        responseEntity = this.callCenterService(restTemplate, pd, "",
                url,
                HttpMethod.GET);

        return responseEntity;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}