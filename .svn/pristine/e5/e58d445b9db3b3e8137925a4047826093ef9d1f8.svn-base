package io.nuite.modules.order_platform_app.service.impl;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.ImportMultipartExcelUtil;
import io.nuite.modules.order_platform_app.dao.AdjustPriceDao;
import io.nuite.modules.order_platform_app.entity.AdjustPriceEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.service.AdjustPriceService;
import io.nuite.modules.order_platform_app.service.MessageService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.BaseShopService;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.model.AdjustPriceForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description: 调价Service实现类
 * @author: jxj
 * @create: 2019-03-30 13:16
 */

@Slf4j
@Service
public class AdjustPriceServiceImpl extends ServiceImpl<AdjustPriceDao, AdjustPriceEntity> implements AdjustPriceService {
    @Autowired
    private ConfigUtils configUtils;
    
    @Autowired
    AdjustPriceDao adjustPriceDao;
    
    @Autowired
    BaseShopService shopService;
    
    @Autowired
    GoodsShoesService goodsShoesService;
    
    @Autowired
    MessageService messageService;
    
    @Autowired
    BaseUserService baseUserService;
    
    @Autowired
    JPushUtils jPushUtils;
    
    @Override
    public List<AdjustPriceEntity> selectAdjustPriceByMessageSeq(Integer messageSeq) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("messageSeq", messageSeq);
        return baseMapper.selectAdjustPriceByMessageSeq(map);
    }
    
    @Override
    public List queryAdjustPriceTable() {
        return null;
    }
    
    @Override
    public List<Map<String, Object>> queryAdjustPriceTree(Integer companySeq) {
        return adjustPriceDao.selectAdjustPriceTree(companySeq);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> parseExcelToSave(MultipartFile excelFile, BaseUserEntity user, String flag) throws Exception {
        List<List<Object>> rows = ImportMultipartExcelUtil.importExcel(excelFile);
        
        if (rows.isEmpty()) {
            throw new RRException("Excel内容为空");
        }
        
        //缓存表格数据
        Map<Integer, List<AdjustPriceForm>> excelMap = new HashMap<>();
        
        /*门店	             货号	        现价  	原价
         温州中山东路1号店	   873BS-851209黑	88	    118
        */
        List row = null;
        for (int i = 1; i < rows.size(); i++) {
            row = rows.get(i);
            //门店
            Object shopNameObj = row.get(0);
            if (StringUtils.isBlank(shopNameObj.toString())) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行门店为空！");
            }
            
            String shopName = shopNameObj.toString().trim();
            BaseShopEntity baseShopEntity = shopService.selectOne(new EntityWrapper<BaseShopEntity>()
                .eq("Company_Seq", user.getCompanySeq())
                .eq("Name", shopName));
            
            if (baseShopEntity == null) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行门店数据库不存在！");
            }
            
            //货号
            Object goodIDObj = row.get(1);
            if (StringUtils.isBlank(goodIDObj.toString())) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行货号为空！");
            }
            
            String goodID = goodIDObj.toString().trim();
            GoodsShoesEntity goodsShoesEntity = goodsShoesService.selectOne(new EntityWrapper<GoodsShoesEntity>()
                .eq("Company_Seq", user.getCompanySeq())
                .eq("GoodID", goodID));
            if (goodsShoesEntity == null) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行货号鞋子数据库不存在！");
            }
            
            //现价
            Object currentPriceObj = row.get(2);
            if (StringUtils.isBlank(currentPriceObj.toString())) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行现价为空！");
            }
            
            BigDecimal currentPrice = null;
            try {
                currentPrice = new BigDecimal(currentPriceObj.toString());
            } catch (Exception e) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行现价解析出错，请检查是否合法！");
            }
            
            //原价
            Object prevPriceObj = row.get(3);
            if (StringUtils.isBlank(prevPriceObj.toString())) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行原价为空！");
            }
            
            BigDecimal prevPrice = null;
            try {
                prevPrice = new BigDecimal(prevPriceObj.toString());
            } catch (Exception e) {
                throw new RRException("文件解析失败：第" + (i + 1) + "行原价解析出错，请检查是否合法！");
            }
            
            AdjustPriceForm form = new AdjustPriceForm();
            form.setShopName(shopName);
            form.setGoodID(goodID);
            form.setShoeSeq(goodsShoesEntity.getSeq());
            form.setCurrentPrice(currentPrice);
            form.setPreviousPrice(prevPrice);
            
            //缓存数据
            if (excelMap.containsKey(baseShopEntity.getSeq())) {
                List<AdjustPriceForm> adjustPriceForms = excelMap.get(baseShopEntity.getSeq());
                adjustPriceForms.add(form);
            } else {
                List<AdjustPriceForm> formList = new ArrayList<>();
                formList.add(form);
                excelMap.put(baseShopEntity.getSeq(), formList);
            }
            
        }
        
        //excel解析结果打印,测试
       /* for (Map.Entry<Integer, List<AdjustPriceForm>> entry : excelMap.entrySet()) {
            System.out.println("门店： " + entry.getKey());
            for (AdjustPriceForm form : entry.getValue()) {
                System.out.println(form);
            }
        }*/
        
        List<String> shopNameList = new ArrayList<>();
        for (Map.Entry<Integer, List<AdjustPriceForm>> entry : excelMap.entrySet()) {
            //查询门店下所有用户
            List<BaseUserEntity> shopUsers = baseUserService.selectList(new EntityWrapper<BaseUserEntity>()
                .eq("Shop_Seq", entry.getKey()));
            
            if (shopUsers != null && shopUsers.size() > 0) {
                for (BaseUserEntity shopUser : shopUsers) {
                    //给每个门店用户发送一条消息
                    MessageEntity message = new MessageEntity();
                    message.setTitle("您有一条调价消息");
                    message.setContent("有" + entry.getValue().size() + "件商品调价信息");
                    message.setUserSeq(shopUser.getSeq());
                    message.setInputTime(new Date());
                    message.setDel(0);
                    message.setCreator(user.getSeq());
                    message.setType(2);
                    message.setIsRead(0);//未读
                    message.setFlag(flag);//调价事件标记
                    
                    messageService.insert(message);
                    
                    //每条消息对象N条货品调价记录
                    for (AdjustPriceForm form : entry.getValue()) {
                        AdjustPriceEntity adjustPriceEntity = new AdjustPriceEntity();
                        adjustPriceEntity.setShoesSeq(form.getShoeSeq());
                        adjustPriceEntity.setMessageSeq(message.getSeq());
                        adjustPriceEntity.setCurrentPrice(form.getCurrentPrice());
                        adjustPriceEntity.setPreviousPrice(form.getPreviousPrice());
                        
                        super.insert(adjustPriceEntity);
                    }
                }
                
                //发出通知操作
                
                //发送手机推送消息
                try {
                    //给接单员推送
                    List<String> aliasList = new ArrayList<String>();
                    for (BaseUserEntity baseUserEntity : shopUsers) {
                        aliasList.add(baseUserEntity.getAccountName());
                    }
                    jPushUtils.sendPush(aliasList, "您有" + entry.getValue().size() + "件商品调价信息", "0");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("发送手机调价推送消息失败" + e.getMessage(), e);
                }
                
            } else {
                //门店下无用户情况
                shopNameList.add(entry.getValue().get(0).getShopName());
            }
        }
        
        //返回无用户的门店列表
        return shopNameList;
    }
    
    @Override
    public List<Map<String, Object>> queryExcelDataByFlag(String flag) {
        
        List<MessageEntity> messageEntities = adjustPriceDao.selectAdjustPriceDataByFlag(flag);
        
        //返回数据打印测试
        for (MessageEntity messageEntity : messageEntities) {
            System.out.println("消息：" + messageEntity.getSeq());
            for (AdjustPriceEntity detail : messageEntity.getDetails()) {
                System.out.println(detail);
            }
        }
    
        /*门店	             货号	        现价  	原价
         温州中山东路1号店	   873BS-851209黑	88	    118
        */
        List<Map<String, Object>> list = new ArrayList<>();
        if (messageEntities != null && messageEntities.size() > 0) {
            for (MessageEntity messageEntity : messageEntities) {
                for (AdjustPriceEntity detail : messageEntity.getDetails()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("shopName", messageEntity.getShopName());
                    row.put("goodID", detail.getGoodID());
                    row.put("currentPrice", detail.getCurrentPrice());
                    row.put("previousPrice", detail.getPreviousPrice());
                    
                    list.add(row);
                }
            }
        }
        
        return list;
    }
}
