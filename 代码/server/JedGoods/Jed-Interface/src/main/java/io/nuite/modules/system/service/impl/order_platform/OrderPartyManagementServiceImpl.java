package io.nuite.modules.system.service.impl.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.nuite.common.utils.PageUtils;
import io.nuite.modules.order_platform_app.utils.RongCloudUtils;
import io.nuite.modules.sr_base.dao.BaseAgentDao;
import io.nuite.modules.sr_base.dao.BaseAreaDao;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.BaseUserDao;
import io.nuite.modules.sr_base.entity.BaseAgentEntity;
import io.nuite.modules.sr_base.entity.BaseAreaEntity;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.system.dao.order_platform.OrderPartyManagementDao;
import io.nuite.modules.system.service.order_platform.OrderPartyManagementService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderPartyManagementServiceImpl implements OrderPartyManagementService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderPartyManagementDao orderPartyManagementDao;

    @Autowired
    private BaseUserDao baseUserDao;

    @Autowired
    private RongCloudUtils rongCloudUtils;

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private BaseAreaDao baseAreaDao;

    @Autowired
    private BaseAgentDao baseAgentDao;

    // 根据当前工厂的用户和订货方类型返回订货方列表
    @Override
    public PageUtils orderPartyList(BaseUserEntity loginUser, Integer saleType, Integer page, Integer limit) {
        List<BaseUserEntity> userList = new ArrayList<BaseUserEntity>();
        Integer totalCount = null;
        Integer start = new Integer(0);
        Integer num = new Integer(0);

        if (page >= 1 && limit > 0) {
            start = (page - 1) * limit;
            num = page * limit;
        }

        userList = baseUserDao.selectBySeqList(loginUser.getCompanySeq(), start, num);
        totalCount = baseUserDao.getTotalCount(loginUser.getCompanySeq());

        if (userList != null && !userList.isEmpty()) {
            for (BaseUserEntity baseUserEntity : userList) {
                if (baseUserEntity != null) {
                    // 去除账号中的密码
                    baseUserEntity.setPassword("");
                }
            }
        }
        return new PageUtils(userList, totalCount, limit, page);
    }

    // 根据用户的序号删除用户
    @Override
    public Integer deleteOrderParty(Integer seq) {
        return orderPartyManagementDao.deleteOrderParty(seq);
    }

    // 根据用户seq获得订货方的具体信息(修改信息)
    @Override
    public BaseUserEntity edit(Integer seq) {
        BaseUserEntity baseUserEntity = new BaseUserEntity();
        baseUserEntity.setSeq(seq);
        baseUserEntity = baseUserDao.selectById(baseUserEntity);
        if (baseUserEntity != null) {
            // 去除账号中的密码
            baseUserEntity.setPassword("");
        }
        return baseUserEntity;
    }

    // 修改账户信息
    @Override
    public int updateOrderParty(BaseUserEntity baseUserEntity, BaseUserEntity userEntity) {
        /*if (baseUserEntity.getSaleType() == 2 || baseUserEntity.getSaleType() == 3) {
            baseUserEntity.setShopSeq("");
        }
        if (baseUserEntity.getAttachType() == 1) {
            baseUserEntity.setAttachSeq(userEntity.getCompanySeq());
        }*/
        if (StringUtils.isNotBlank(baseUserEntity.getPassword())) {
            baseUserEntity.setPassword(DigestUtils.sha256Hex(baseUserEntity.getPassword()));
        }
        return baseUserDao.updateById(baseUserEntity);
    }

    // 添加账户
    @Override
    @Transactional
    public void addOrderParty(BaseUserEntity baseUser, BaseUserEntity baseUserEntity) throws Exception {
        /*if (baseUserEntity.getSaleType() == 1) {
            throw new RuntimeException("不能创建该类型的用户！");
        }
        if (baseUserEntity.getSaleType() == 4 && baseUserEntity.getAttachType() != 3) {
            if (baseUserEntity.getShopSeq() == null || baseUserEntity.getShopSeq().equals("")) {
                throw new RuntimeException("请选择门店！");
            }
        } else {
            baseUserEntity.setShopSeq("");
        }
        if (baseUserEntity.getAttachType() == 1) {
            baseUserEntity.setAttachSeq(null);
        }*/
        baseUserEntity.setCompanySeq(baseUser.getCompanySeq());
        baseUserEntity.setBrandSeq(baseUser.getBrandSeq());
        baseUserEntity.setPassword(DigestUtils.sha256Hex(baseUserEntity.getPassword()));
        baseUserDao.insert(baseUserEntity);

        // 注册融云生成token
        String rongCloudToken = rongCloudUtils.registerUser(baseUserEntity.getSeq(), baseUserEntity.getUserName(),
                "1.jpg");
        if (rongCloudToken == null) {
            throw new RuntimeException("获取融云Token失败");
        }
        baseUserEntity.setRongCloudToken(rongCloudToken);
        baseUserDao.updateById(baseUserEntity);
    }

    // 判断该账号是否存在
    @Override
    public boolean judgeUserOrCompanyExistence(BaseUserEntity baseUserEntity) {
        List<BaseUserEntity> list = baseUserDao.selectList(
                new EntityWrapper<BaseUserEntity>().eq("AccountName", baseUserEntity.getAccountName()).eq("Del", 0));
        if (list != null) {
            for (BaseUserEntity UserEntity : list) {
                if (!UserEntity.getSeq().equals(baseUserEntity.getSeq())) {
                    return true;
                }
            }
        }
        return false;
    }

    // 返回工厂或某一分公司的门店列表
    @Override
    public List<BaseShopEntity> shopList(BaseUserEntity baseUserEntity, Integer attachSeq) {

        //分公司序号
        List<Integer> areaSeqList = new ArrayList<Integer>();

        if (attachSeq == null) {
            //查询工厂所有区域
            List<BaseAreaEntity> areaList = baseAreaDao.selectList(new EntityWrapper<BaseAreaEntity>().eq("brand_Seq", baseUserEntity.getBrandSeq()));
            for (BaseAreaEntity baseAreaEntity : areaList) {
                areaSeqList.add(baseAreaEntity.getSeq());
            }
        } else {
            //指定某一分公司
            areaSeqList.add(attachSeq);
        }
        List<BaseShopEntity> shopList = baseShopDao.selectList(new EntityWrapper<BaseShopEntity>().in("Area_seq", areaSeqList).orderBy("Area_seq ASC"));
        return shopList;
    }

    @Override
    public List<BaseAgentEntity> agentList(Integer brandSeq) {
        return baseAgentDao.selectList(new EntityWrapper<BaseAgentEntity>().eq("Brand_Seq", brandSeq).eq("Del", 0));
    }

    @Override
    public List<BaseAreaEntity> branchOfficeList(Integer brandSeq) {
        return baseAreaDao
                .selectList(new EntityWrapper<BaseAreaEntity>().eq("Brand_Seq", brandSeq).eq("Del", 0));
    }

}
