/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config.bean;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import com.alipay.zdal.client.config.DataSourceConfigType;
import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.lang.StringUtil;
import com.alipay.zdal.rule.config.beans.AppRule;

/**
 * Mapping with Zdal Data Source configuration
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 *
 */
public class AppDataSourceBean implements InitializingBean {

    /** 数据源名称 */
    private String                      appDataSourceName;

    /** 数据库类型 */
    private String                      dataBaseType;

    /** 配置转化后的的dbtype. */
    private DBType                      dbType;

    /** 数据源配置类型 */
    private String                      configType;

    /** 配置转化后的dataSourceConfigType. */
    private DataSourceConfigType        dataSourceConfigType;

    /** 物理数据源列表. */
    private Set<PhysicalDataSourceBean> physicalDataSourceSet;

    /** shard+group类型中的group分组定义. */
    private Map<String, String>         groupDataSourceRuleMap;

    /** shard+failover类型中的group分组定义. */
    private Map<String, String>         failOverGroupRuleMap;

    /**  分库分表的规则.*/
    private AppRule                     appRule;

    /**
     * @return the appRule
     */
    public AppRule getAppRule() {
        return appRule;
    }

    /**
     * @param appRule the appRule to set
     */
    public void setAppRule(AppRule appRule) {
        this.appRule = appRule;
    }

    public String getAppDataSourceName() {
        return appDataSourceName;
    }

    public void setAppDataSourceName(String appDataSourceName) {
        this.appDataSourceName = appDataSourceName;
    }

    public String getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(String dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    /**
     * @return the groupDataSourceRuleMap
     */
    public Map<String, String> getGroupDataSourceRuleMap() {
        return groupDataSourceRuleMap;
    }

    /**
     * @param groupDataSourceRuleMap the groupDataSourceRuleMap to set
     */
    public void setGroupDataSourceRuleMap(Map<String, String> groupDataSourceRuleMap) {
        this.groupDataSourceRuleMap = groupDataSourceRuleMap;
    }

    /**
     * @return the failOverGroupRuleMap
     */
    public Map<String, String> getFailOverGroupRuleMap() {
        return failOverGroupRuleMap;
    }

    /**
     * @param failOverGroupRuleMap the failOverGroupRuleMap to set
     */
    public void setFailOverGroupRuleMap(Map<String, String> failOverGroupRuleMap) {
        this.failOverGroupRuleMap = failOverGroupRuleMap;
    }

    public Set<PhysicalDataSourceBean> getPhysicalDataSourceSet() {
        return physicalDataSourceSet;
    }

    public void setPhysicalDataSourceSet(Set<PhysicalDataSourceBean> physicalDataSourceSet) {
        this.physicalDataSourceSet = physicalDataSourceSet;
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public DataSourceConfigType getDataSourceConfigType() {
        return dataSourceConfigType;
    }

    public void setDataSourceConfigType(DataSourceConfigType dataSourceConfigType) {
        this.dataSourceConfigType = dataSourceConfigType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appDataSourceName == null) ? 0 : appDataSourceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppDataSourceBean other = (AppDataSourceBean) obj;
        if (appDataSourceName == null) {
            if (other.appDataSourceName != null)
                return false;
        } else if (!appDataSourceName.equals(other.appDataSourceName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AppDataSourceBean [appDataSourceName=" + appDataSourceName + ", appRule=" + appRule
               + ", configType=" + configType + ", dataBaseType=" + dataBaseType
               + ", dataSourceConfigType=" + dataSourceConfigType + ", dbType=" + dbType
               + ", failOverGroupRuleMap=" + failOverGroupRuleMap + ", groupDataSourceRuleMap="
               + groupDataSourceRuleMap + ", physicalDataSourceSet=" + physicalDataSourceSet + "]";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isBlank(appDataSourceName)) {
            throw new IllegalArgumentException("ERROR ## the appDataSourceName is null");
        }

        if (StringUtil.isBlank(dataBaseType)) {
            throw new IllegalArgumentException("ERROR ## the dataBaseType is null");
        }
        this.dbType = DBType.convert(dataBaseType);

        if (StringUtil.isBlank(configType)) {
            throw new IllegalArgumentException("ERROR ## the configType is null");
        }
        this.dataSourceConfigType = DataSourceConfigType.typeOf(configType);

        if (physicalDataSourceSet == null || physicalDataSourceSet.isEmpty()) {
            throw new IllegalArgumentException("ERROR ## the physicalDataSourceSet is empty");
        }

        if (this.dataSourceConfigType.isShardGroup()) {
            if (groupDataSourceRuleMap == null || groupDataSourceRuleMap.isEmpty()) {
                throw new IllegalArgumentException(
                    "ERROR ## the dataSourceConfigType is Shard_Group,must have groupDataSourceRuleMap properties");
            }
        }

        if (this.dataSourceConfigType.isShardFailover()) {
            if (failOverGroupRuleMap == null || failOverGroupRuleMap.isEmpty()) {
                throw new IllegalArgumentException(
                    "ERROR ## the dataSourceConfigType is Shard_failover,must have failOverGroupRuleMap properties");
            }
        }

        if (this.dataSourceConfigType.isShard() || this.dataSourceConfigType.isShardFailover()
            || this.dataSourceConfigType.isShardGroup()) {
            if (appRule == null) {
                throw new IllegalArgumentException(
                    "ERROR ## the dataSourceConfigType is Shard,Shard_group,shard_failover must have sharding rule of AppRule properties");
            }
        }
    }

}
