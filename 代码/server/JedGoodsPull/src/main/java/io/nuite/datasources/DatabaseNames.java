package io.nuite.datasources;

/**
 * 同数据源下不同库
 */
public interface DatabaseNames {
    //基础数据源
    String SR_BASE = "JedSmartRetail.dbo.";
    //订货平台数据源
    String SR_ORDER_PLATFORM = "JedOrderPlatform.dbo.";
}
