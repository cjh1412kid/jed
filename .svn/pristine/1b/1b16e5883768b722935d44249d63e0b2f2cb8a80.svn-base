package io.nuite.modules.job.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.job.service.SalesTopThreeService;
import io.nuite.modules.order_platform_app.dao.MessageDao;
import io.nuite.modules.order_platform_app.dao.SaleShoesDetailDao;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.entity.SaleShoesDetailEntity;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: 销售TOP3Service实现类
 * @author: jxj
 * @create: 2019-05-07 20:15
 */
@Service
public class SalesTopThreeServiceImpl implements SalesTopThreeService {
    @Autowired
    private SaleShoesDetailDao saleShoesDetailDao;

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private JPushUtils jPushUtils;

    @Value("${jrd.companySeq}")
    private Integer companySeq;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectSaleGoodsTopThree() throws Exception {
        Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq",companySeq);
        List<BaseShopEntity> list = baseShopDao.selectList(wrapper);
        Map<String,Object> map = new HashMap<>(10);
        for(int i = 0;i < list.size();i++) {
            List<MessageEntity> messageEntities = new ArrayList<>(30);
            map.put("shopSeq",list.get(i).getSeq());
            List<SaleShoesDetailEntity> saleShoesDetailEntities =  saleShoesDetailDao.selectSaleGoodsTopThree(map);
            String content = "";
            for(int j = 0;j < saleShoesDetailEntities.size();j++) {
                content = content + "昨天销售第"+ (j+1) +"名的商品货号为:" + saleShoesDetailEntities.get(j).getShoeID() + ",共卖出" + saleShoesDetailEntities.get(j).getSaleCount() + "双,销售金额为:" + saleShoesDetailEntities.get(j).getRealPrice() + "元<br>";
            }
            Wrapper<BaseUserEntity> baseUserEntityWrapper = new EntityWrapper<>();
            baseUserEntityWrapper.eq("Shop_Seq",list.get(i).getSeq());
            List<BaseUserEntity> userEntities = baseUserDao.selectList(baseUserEntityWrapper);
            List<String> nameList = new ArrayList<>(20);
            for(int j = 0;j < userEntities.size();j++) {
                MessageEntity messageEntity = new MessageEntity();
                nameList.add(userEntities.get(j).getAccountName());
                messageEntity.setUserSeq(userEntities.get(j).getSeq());
                messageEntity.setType(3);
                messageEntity.setDel(0);
                messageEntity.setTitle("昨天销售前三名商品货号信息");
                messageEntity.setContent(content);
                messageEntity.setIsRead(0);
                messageEntity.setInputTime(new Date());
                messageEntities.add(messageEntity);
            }
            if(nameList.size() > 0) {
                jPushUtils.sendPush(nameList,content,"0");
            }
            if(messageEntities.size() > 0) {
                messageDao.insertMessage(messageEntities);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectSaleCategoryTopThree() throws Exception {
        Wrapper<BaseShopEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("Company_Seq",companySeq);
        List<BaseShopEntity> list = baseShopDao.selectList(wrapper);
        Map<String,Object> map = new HashMap<>(10);
        for(int i = 0;i < list.size();i++) {
            List<MessageEntity> messageEntities = new ArrayList<>(30);
            map.put("shopSeq",list.get(i).getSeq());
            List<SaleShoesDetailEntity> saleShoesDetailEntities =  saleShoesDetailDao.selectSaleCategoryTopThree(map);
            String content = "";
            for(int j = 0;j < saleShoesDetailEntities.size();j++) {
                content = content + "昨天销售第"+ (j+1) +"名的商品品类为:" + saleShoesDetailEntities.get(j).getCategoryName() + ",共卖出" + saleShoesDetailEntities.get(j).getSaleCount() + "双,销售金额为:" + saleShoesDetailEntities.get(j).getRealPrice() + "元<br>";
            }
            Wrapper<BaseUserEntity> baseUserEntityWrapper = new EntityWrapper<>();
            baseUserEntityWrapper.eq("Shop_Seq",list.get(i).getSeq());
            List<BaseUserEntity> userEntities = baseUserDao.selectList(baseUserEntityWrapper);
            List<String> nameList = new ArrayList<>(20);
            for(int j = 0;j < userEntities.size();j++) {
                MessageEntity messageEntity = new MessageEntity();
                nameList.add(userEntities.get(j).getAccountName());
                messageEntity.setUserSeq(userEntities.get(j).getSeq());
                messageEntity.setType(3);
                messageEntity.setDel(0);
                messageEntity.setTitle("昨天销售前三名商品品类信息");
                messageEntity.setContent(content);
                messageEntity.setIsRead(0);
                messageEntity.setInputTime(new Date());
                messageEntities.add(messageEntity);
            }
            if(nameList.size() > 0) {
                jPushUtils.sendPush(nameList,content,"0");
            }
            if(messageEntities.size() > 0) {
                messageDao.insertMessage(messageEntities);
            }
        }
    }
}
