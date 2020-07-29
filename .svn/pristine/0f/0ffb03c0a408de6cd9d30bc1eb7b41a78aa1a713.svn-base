package io.nuite.modules.order_platform_app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.modules.system.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.dao.AllocateTransferTransferInApplicationDao;
import io.nuite.modules.order_platform_app.entity.AllocateTransferTransferInApplicationEntity;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.service.AllocateTransferFictitiousMallService;
import io.nuite.modules.sr_base.dao.BaseShopDao;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.BaseShopEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.system.entity.order_platform.AllocateTransferFactoryEntity;
import io.nuite.modules.system.service.order_platform.AllocateTransferFactoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


/**
 * App端调拨接口（主要针对门店与门店间的调拨，采用虚拟商城的方式）
 *
 * @author yy
 * @date 2019-09-10 15:46:51
 */
@RestController
@RequestMapping("/order/app/allocateTransfer")
@Api(tags = "调拨接口")
public class AllocateTransferController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AllocateTransferFictitiousMallService allocateTransferFictitiousMallService;

    @Autowired
    private BaseShopDao baseShopDao;

    @Autowired
    private AllocateTransferTransferInApplicationDao allocateTransferTransferInApplicationDao;

    @Autowired
    private GoodsShoesDao goodsShoesDao;

    @Autowired
    private AllocateTransferFactoryService allocateTransferFactoryService;


    /**
     * 获取鞋子各个尺码的 当前库存 + 调出数量（已加入虚拟商城数量）   点击调入
     */
    @Login
    @GetMapping("getShoesStockAndTransferOutNum")
    @ApiOperation("获取鞋子各个尺码的 当前库存 + 调出数量（已加入虚拟商城数量）")
    public R getShoesStockAndTransferOutNum(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                            @ApiParam("鞋子Seq") @RequestParam("shoesSeq") Integer shoesSeq) {
        try {
            List<Map<String, Object>> shoesStockAndTransferOutNumList = allocateTransferFictitiousMallService.getShoesStockAndTransferOutNum(loginUser.getShopSeq(), shoesSeq);

//			// 添加汇总
//			int stockSum = 0;
//			int transferOutNumSum = 0;
//			for(Map<String, Object> map : shoesStockAndTransferOutNumList) {
//				Integer stock = (Integer) map.get("stock");
//				Integer transferOutNum = map.get("transferOutNum") == null ? 0 : (Integer) map.get("transferOutNum");
//				stockSum = stockSum + stock;
//				transferOutNumSum = transferOutNumSum + transferOutNum;
//			}
//			Map<String, Object> sumMap = new HashMap<String, Object>();
//			sumMap.put("size", "汇总");
//			sumMap.put("stock", stockSum);
//			sumMap.put("transferOutNum", transferOutNumSum);
//			shoesStockAndTransferOutNumList.add(sumMap);

            return R.ok(shoesStockAndTransferOutNumList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 确认调出（加入或修改虚拟商城）
     */
    @Login
    @PostMapping("confirmTransferOut")
    @ApiOperation("确认调出（加入或修改虚拟商城）")
    public R confirmTransferOut(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                @ApiParam("鞋子Seq") @RequestParam("shoesSeq") Integer shoesSeq,
                                @ApiParam("每个尺码的调出数量[{size:35,num:2},{size:36,num:3}...]") @RequestParam("sizeTransferOutNum") String sizeTransferOutNum) {
        try {

            allocateTransferFictitiousMallService.confirmTransferOut(loginUser.getShopSeq(), shoesSeq, sizeTransferOutNum);

            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 查询货品多个尺码，所有门店（同一区域的排前面）的当前库存、调出数量（加入虚拟商城的数量）   点击下一步
     */
    @Login
    @GetMapping("getAllShopsStockAndTransferOutNum")
    @ApiOperation("查询货品多个尺码，所有门店的调出数量、当前库存")
    public R getAllShopsStockAndTransferOutNum(@ApiIgnore @LoginUser BaseUserEntity loginUser,
    		@ApiParam("鞋子Seq") @RequestParam("shoesSeq") Integer shoesSeq) {
        try {
            //获取登录门店的区域
            BaseShopEntity baseShopEntity = baseShopDao.selectById(loginUser.getShopSeq());
            Integer shopRegionSeq = baseShopEntity.getRegionSeq();
            //获取货品信息
            GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
            String goodsImgUrl = getGoodsShoesPictureUrl(goodsShoesEntity.getGoodID()) + goodsShoesEntity.getImg1();
            List<Map<String, Object>> allShopsTransferOutNumAndStockList = allocateTransferFictitiousMallService.getAllShopsTransferOutNumAndStock(shoesSeq, shopRegionSeq, loginUser.getShopSeq());
            Map<String,Object> result = new HashMap<>(10);
            result.put("shoesSeq",shoesSeq);
            result.put("goodId",goodsShoesEntity.getGoodID());
            result.put("img",goodsImgUrl);
            result.put("shopSizeList",allShopsTransferOutNumAndStockList);
            return R.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 确认调入
     * （新增调货申请记录、详情， 
	 *	给调出方 （所有门店账号）  -   新增调拨类型消息Message、发送极光推送、保存极光推送。
	 *	给调入方 （所有门店账号）  -   新增调拨类型消息Message、不发送极光推送。）
     */
    @Login
    @PostMapping("confirmTransferIn")
    @ApiOperation("确认调入（新增调货申请记录，新增调拨类型消息）")
    public R confirmTransferIn(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                               @ApiParam("鞋子Seq") @RequestParam("shoesSeq") Integer shoesSeq,
                               @ApiParam("调入各个门店所有尺码对应的数量([{shopSeq:1, sizeAndNum:[{size:35,num:10},{size:36,num:20}...]}, {}])") @RequestParam("sizesShopsTransferInNum") String sizesShopsTransferInNum) {
        try {

            if (loginUser.getShopSeq() == null || loginUser.getShopSeq() == 0) {
                return R.error(403, "只有门店账号可以使用此功能");
            }
            
            allocateTransferFictitiousMallService.confirmTransferIn(loginUser.getShopSeq(), loginUser.getSeq(), shoesSeq, sizesShopsTransferInNum);
            
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 我的调货消息列表
     */
    @Login
    @GetMapping("getMyTransferMessage")
    @ApiOperation("我的调货消息列表")
    public R getMyTransferMessage(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                  @ApiParam("类型 0:全部 1:调入 2:调出") @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
                                  @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                                  @ApiParam("总条数") @RequestParam(value = "num") Integer num) {
        try {
            Page<Map<String, Object>> page = new Page<>(start / num + 1, num);
            List<MessageEntity> messageList = allocateTransferFictitiousMallService.getMyTransferMessage(loginUser.getSeq(), type, page);
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            for (MessageEntity messageEntity : messageList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("seq", messageEntity.getSeq());
                map.put("title", messageEntity.getTitle());
                map.put("content", messageEntity.getContent());
                map.put("inputTime", messageEntity.getInputTime());
                map.put("creator", messageEntity.getCreator());
                map.put("isRead", messageEntity.getIsRead());
                map.put("applicationSeq", messageEntity.getApplicationSeq());
                map.put("applicationType", messageEntity.getApplicationType());

                //添加调货货号，当前状态
                if (messageEntity.getType() == 4) {
                    AllocateTransferTransferInApplicationEntity allocateTransferTransferInApplication = allocateTransferTransferInApplicationDao.selectById(messageEntity.getApplicationSeq());
                    if (allocateTransferTransferInApplication == null) {
                        continue;
                    }
                    Integer shoesSeq = allocateTransferTransferInApplication.getShoesSeq();
                    GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
                    map.put("applicationShoesSeq", shoesSeq);
                    map.put("applicationGoodID", goodsShoesEntity.getGoodID());
                    map.put("applicationState", allocateTransferTransferInApplication.getState());
                    map.put("type", allocateTransferTransferInApplication.getType());
                }
                resultList.add(map);
            }

            return R.ok(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 调货消息详情
     */
    @Login
    @GetMapping("getTransferMessageDetail")
    @ApiOperation("调货消息详情")
    public R getTransferMessageDetail(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                                      @ApiParam("调货申请表Seq") @RequestParam("applicationSeq") Integer applicationSeq) {
        try {

            AllocateTransferTransferInApplicationEntity application = allocateTransferTransferInApplicationDao.selectById(applicationSeq);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            //货号
            Integer shoesSeq = application.getShoesSeq();
            GoodsShoesEntity goodsShoesEntity = goodsShoesDao.selectById(shoesSeq);
            resultMap.put("applicationGoodID", goodsShoesEntity.getGoodID());
            resultMap.put("img", getGoodsShoesPictureUrl(goodsShoesEntity.getGoodID()) + goodsShoesEntity.getImg1());

            //调出方门店名称
            Integer outShopSeq = application.getOutShopSeq();
            if (outShopSeq == 0) {
                resultMap.put("outShopName", "总部");
            } else {
                BaseShopEntity outShopEntity = baseShopDao.selectById(outShopSeq);
                resultMap.put("outShopName", outShopEntity.getName());
            }


            //调入方门店名称
            Integer inShopSeq = application.getInShopSeq();
            if (inShopSeq == 0) {
                resultMap.put("inShopName", "总部");
            } else {
                BaseShopEntity inShopEntity = baseShopDao.selectById(inShopSeq);
                resultMap.put("inShopName", inShopEntity.getName());
            }

            //时间
            resultMap.put("inputTime", application.getInputTime());
            //当前状态
            resultMap.put("state", application.getState());
            //备注
            resultMap.put("remark", application.getRemark());
            //ERP订单号
            resultMap.put("erpOrderNum", application.getErpOrderNum());
            //快递公司序号
            resultMap.put("expressCompanySeq", application.getExpressCompanySeq());
            //快递单号
            resultMap.put("expressNum", application.getExpressNum());
            //处理时间
            resultMap.put("handleTime", application.getHandleTime());

            Integer type = application.getType();
            List<Map<String, Object>> everySizeNumList;
            if (type == 0) {
                //每个尺码的数量详情
                everySizeNumList = allocateTransferFictitiousMallService.getApplicationDetailMapList(applicationSeq);
            } else {
                //每个尺码的数量详情
                everySizeNumList = allocateTransferFactoryService.getApplicationDetailMapList(application.getInShopSeq(), application.getOutShopSeq(), application.getShoesSeq(), application.getInputTime());
            }

            //调货总数
            int totalNum = 0;
            for (Map<String, Object> map : everySizeNumList) {
                totalNum += (Integer) map.get("num");

            }
            resultMap.put("totalNum", totalNum);
            resultMap.put("everySizeNum", everySizeNumList);

            return R.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }


    /**
     * 处理调货申请（同意、拒绝）
     */
    @Login
    @PostMapping("handleTransferApplication")
    @ApiOperation("处理调货申请（同意、拒绝）")
    public R handleTransferApplication(@ApiIgnore @LoginUser BaseUserEntity loginUser,
        @ApiParam("调货申请表Seq") @RequestParam("applicationSeq") Integer applicationSeq,
        @ApiParam("状态 1: 同意 2: 拒绝") @RequestParam(value = "state", required = false) Integer state,
        @ApiParam("ERP单据编号") @RequestParam(value = "erpOrderNum", required = false) String erpOrderNum,
        @ApiParam("快递公司序号") @RequestParam(value = "expressCompanySeq", required = false) Integer expressCompanySeq,
        @ApiParam("快递单号") @RequestParam(value = "expressNum", required = false) String expressNum,
        @ApiParam("原因（备注）") @RequestParam(value = "remark", required = false) String remark) {
        try {

            //判断是否有处理权限
            AllocateTransferTransferInApplicationEntity application = allocateTransferTransferInApplicationDao.selectById(applicationSeq);
            if (!application.getOutShopSeq().equals(loginUser.getShopSeq())) {
                return R.error(403, "您没有处理权限");
            }

            allocateTransferFictitiousMallService.handleTransferApplication(loginUser.getSeq(), loginUser.getShopSeq(), applicationSeq, state, erpOrderNum, expressCompanySeq, expressNum, remark);

            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    
    
    
    
    /**
     * 获取调出货品的门店及尺码数量
     */
    @Login
    @GetMapping("selectExportShop")
    @ApiOperation("获取调出货品的门店及尺码数量")
    public R selectExportShop(@ApiIgnore @LoginUser BaseUserEntity loginUser,
                              @ApiParam("鞋子序号") @RequestParam("shoesSeq") Integer shoesSeq) {
        try {
            return R.ok(allocateTransferFictitiousMallService.selectExportShop(shoesSeq));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return R.error();
    }
    
    
    
    /**
     * 撤销调出
     */
    @Login
    @ApiOperation("撤销调出")
    @GetMapping("/cancelTransferOut")
    public R deleteShopTransferOut(@ApiIgnore @LoginUser BaseUserEntity loginUser, 
    		@ApiParam("鞋子序号") @RequestParam(value = "shoesSeq") Integer shoesSeq) {
    	
    	//如果是自动调出的，不允许撤销
    	if(allocateTransferFictitiousMallService.isAutoTransferOut(loginUser.getShopSeq(), shoesSeq)) {
    		return R.error("滞销品自动调出，不允许撤销");
    	}
    	
    	allocateTransferFictitiousMallService.cancelTransferOut(loginUser.getShopSeq(), shoesSeq);
        return R.ok();
    }
    
    
}
