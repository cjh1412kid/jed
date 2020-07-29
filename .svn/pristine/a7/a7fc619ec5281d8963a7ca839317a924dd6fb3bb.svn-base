package io.nuite.modules.system.controller.order_platform;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.entity.OrderEntity;
import io.nuite.modules.order_platform_app.entity.OrderProductsEntity;
import io.nuite.modules.order_platform_app.service.PushRecordService;
import io.nuite.modules.order_platform_app.utils.JPushUtils;
import io.nuite.modules.order_platform_app.utils.OrderStatusEnum;
import io.nuite.modules.sr_base.entity.BaseBrandEntity;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.sr_base.service.GoodsSXService;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.from.PartShippedForm;
import io.nuite.modules.system.from.ShippedProductForm;
import io.nuite.modules.system.service.order_platform.OrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订货平台订单管理
 *
 * @author fl
 * @date 2018-04-28
 */
@RestController
@RequestMapping("/order")
@Api(tags = "后台 - 订单管理相关接口", description = "订单的状态跟进和删除")
public class OrderManageController extends AbstractController {

    @Autowired
    private OrderManageService orderManageService;
    
	@Autowired
	private BaseUserService baseUserService;
	
	@Autowired
	private JPushUtils jPushUtils;
	
    @Autowired
    private PushRecordService pushRecordService;
    
    @Autowired
    private GoodsSXService goodsSXService;

    
    
    /**
     * 订单列表
     */
    @GetMapping("/orderlist")
    public R orderlist(@RequestParam Map<String, Object> params) {
        try {
            PageUtils page = orderManageService.orderlist(getUser(), params);
            return R.ok().put("page", page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    
    
    /**
     * 订单所有货号(一级子表格)
     */
    @GetMapping("/orderGoodsIds")
    @ApiOperation("订单所有货号")
    public R orderGoodsIds(@ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq) {
        try {
        	//订单货号列表
        	List<Map<String, Object>> list = orderManageService.getOrderGoodIds(orderSeq);
        	
            PageUtils page = new PageUtils(list, list.size(), list.size(), 1);
            return R.ok().put("page", page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    

    
    /**
     * 每个货号对应商品列表(二级子表格)
     */
    @GetMapping("/goodsIdProducts")
    @ApiOperation("订单中鞋子的列表")
    public R goodsIdProducts(@ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
    						@ApiParam("货号") @RequestParam("goodId") String goodId) {
        try {
        	//货号不同颜色尺码列表
        	List<Map<String, Object>> list = orderManageService.getGoodsIdProducts(orderSeq, goodId);
        	
        	PageUtils page = new PageUtils(list, list.size(), list.size(), 1);
            return R.ok().put("page", page);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }
    
    

    /**
     * 根据分公司序号/代理序号获取所有用户（用于select下拉框数据）
     */
    @GetMapping("/userList")
    @ApiOperation("根据分公司序号/代理序号获取所有用户")
    public R userList(@ApiParam("所属类型") @RequestParam(value = "attachType", required = false) Integer attachType,
                      @ApiParam("所属类型序号") @RequestParam(value = "attachSeq", required = false) Integer attachSeq) {
        try {
            List<Map<String, Object>> list = orderManageService.getUserList(getUser().getCompanySeq(), getUser().getBrandSeq(), attachType, attachSeq);
            return R.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }

    }

    
    /**
     * 订单商品列表（修改价格 + 发货页面用）
     */
    @GetMapping("/orderGoods")
    public R orderGoods(Integer seq) {
        try {
        	OrderEntity orderEntity = orderManageService.getOrderBySeq(seq);
        	orderEntity.setStatusName(OrderStatusEnum.get(orderEntity.getOrderStatus()).getFactoryName());
        	orderEntity.setOrderingparty(baseUserService.selectById(orderEntity.getUserSeq()).getUserName());
        	List<Map<String, Object>> orderProductsList = orderManageService.getOrderProductsList(seq);
            return R.ok().put("order", orderEntity).put("list", orderProductsList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("服务器异常");
        }
    }

    
    /**
     * 删除订单
     */
    @GetMapping("/deleteOrder")
    @ApiOperation("删除订单 ")
    public R deleteOrder(@ApiParam("订单序号") @RequestParam("seq") Integer seq) {
        try {
            orderManageService.deleteOrder(seq);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("删除失败");
        }
    }

    
    /**
     * 接单
     */
    @PostMapping("/confirmOrder")
    @ApiOperation("接单")
    @RequiresPermissions("order:receiveOrder")
    public R confirmOrder(@ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
    		@ApiParam("要求到货时间") @RequestParam("requireTime") Date requireTime) {
        try {
			OrderEntity orderEntity = orderManageService.getOrderBySeq(orderSeq);
	        if(orderEntity.getOrderStatus() != 0) {
	        	return R.error(HttpStatus.SC_FORBIDDEN, "当前订单状态不可接单，请刷新后再试");
	        }
            orderManageService.confirmOrder(orderSeq, requireTime, getUser());
            return R.ok("接单成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("接单失败");
        }
    }
    
    
    /**
     * 入库
     */
    @PostMapping("/updateOrderStatus")
    @ApiOperation("入库")
    @RequiresPermissions("order:storeOrder")
    public R updateOrderStatus(@ApiParam("类别序号") @RequestParam("orderStatus") Integer orderStatus,
                               @ApiParam("订单序号") @RequestParam("seq") Integer seq) {
        try {
            return orderManageService.updateOrderStatus(orderStatus, seq, getUser());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("操作失败");
        }
    }
    
    
	/**
	 * 取消订单
	 */
	@PostMapping("/cancelOrder")
	@ApiOperation("取消订单")
	@RequiresPermissions("order:cancelOrder")
	public R cancelOrder(@ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
			@ApiParam("取消备注") @RequestParam("remark") String remark) {
		try {
			/*取消条件判断*/
			OrderEntity orderEntity = orderManageService.getOrderBySeq(orderSeq);
	        if(orderEntity.getOrderStatus() != 0 && orderEntity.getOrderStatus() != 1 && orderEntity.getOrderStatus() != 2 && orderEntity.getOrderStatus() != 3) {
	        	return R.error(HttpStatus.SC_FORBIDDEN, "当前订单状态不可取消");
	        }
	        
			/**涉及到需要修改的表：
			 YHSR_OP_ShoesData(订货平台鞋子数据表) 	修改库存
			 YHSR_OP_Order (订单信息表)				修改订单状态
			 **/
			//根据订单编号，查询订单的购买的商品列表
			List<OrderProductsEntity> orderProductsList = orderManageService.getOrderProductsListByOrderSeq(orderSeq);
			List<Map<String, Integer>> stockChangeList = new ArrayList<Map<String, Integer>>();
			Map<String, Integer> stockChangeMap;
			for(OrderProductsEntity orderProducts : orderProductsList) {
				//库存变动
				stockChangeMap = new HashMap<String, Integer>();
				stockChangeMap.put("shoesDataSeq", orderProducts.getShoesDataSeq());
				stockChangeMap.put("changNum", orderProducts.getBuyCount());
				stockChangeList.add(stockChangeMap);
			}
	        
			//取消订单
	        try {
	        	orderManageService.cancelOrder(orderSeq, remark, stockChangeList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
	    		return R.error(HttpStatus.SC_FORBIDDEN, "取消订单失败");
			}

			//发送手机推送消息
	        BaseUserEntity submitUser = null;  //提交订单人
			try {
				//给用户推送
				submitUser = baseUserService.getBaseUserBySeq(orderEntity.getUserSeq());
				List<String> aliasList = new ArrayList<String>();
				aliasList.add(submitUser.getAccountName());
				jPushUtils.sendPush(aliasList, "您的订单：" + orderEntity.getOrderNum() + "已被商家取消，备注：" + remark + "。 如有疑问请及时联系客服！", "7");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("取消订单后，发送手机推送消息失败" + e.getMessage(), e);
			}
			
			//保存推送消息
			try {
				//保存用户推送
				pushRecordService.addPushRecord(getUserSeq().intValue(), submitUser.getSeq(), submitUser.getAccountName(), 1, orderSeq, "您的订单：" + orderEntity.getOrderNum() + "已被商家取消，备注：" + remark + "。 如有疑问请及时联系客服！");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("取消订单后，保存推送消息失败" + e.getMessage(), e);
			}
			
			return R.ok("取消成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return R.error("取消失败");
		}
    }
	

    /**
     * 根据订单的Seq查询订单状态
     */
    @GetMapping("/getOrder")
    @ApiOperation("根据订单的Seq查询订单状态")
    public R getOrder(@ApiParam("订单序号") @RequestParam("seq") Integer seq) {
        try {
            return R.ok().put("order", orderManageService.getOrderBySeq(seq));
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("查询失败");
        }
    }

    /**
     * 修改订单价格（支持修改某些商品单价）
     * @param seq
     * @param price
     * @return
     */
    @PostMapping("changePrice")
	@RequiresPermissions("order:checkOrder")
    public R changePrice(@ApiParam("订单序号") @RequestParam("orderSeq") Integer orderSeq,
    		@ApiParam("订单总价") @RequestParam("orderTotalPrice") BigDecimal orderTotalPrice,
    		@ApiParam("商品seq价格jsonArr") @RequestParam("seqPriceList") String seqPriceList) {
        try {
            OrderEntity orderEntity = orderManageService.getOrderBySeq(orderSeq);
            if (orderEntity.getOrderStatus() != 1) {
                return R.error("当前状态不可以修改金额！");
            }
            
        	if(orderTotalPrice != null) { //修改订单总价
        		orderManageService.changeOrderTotalPrice(orderSeq, orderTotalPrice);
        	} else { //修改某些商品单价
        		orderManageService.changeProductsPrice(orderSeq, seqPriceList);
        	}
            return R.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("修改订单金额失败");
        }
    }

    
    
    /**
     * 所有拆单方式（分类 + 自定义属性）
     * @param seq
     * @param price 本次支付金额
     * @return
     */
    @GetMapping("sXIdNameList")
    public R sXIdNameList() {
        try {
        	//获取工厂的所有自定义属性 + category
            List<Map<String, Object>> list = goodsSXService.selectSXIdNameByCompanySeq(getUser().getCompanySeq());
            Map<String, Object> category =  new HashMap<String, Object>();
            category.put("id", "CategorySeq");
            category.put("name", "分类");
            list.add(0, category);
            return R.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("获取拆单方式失败");
        }
    }
    
    
    /**
     * 拆单
     * @param seq
     * @return
     */
    @PostMapping("splitOrder")
	@RequiresPermissions("order:splitOrder")
    public R splitOrder(@ApiParam("订单序号") @RequestParam("orderSeqArr") Integer[] orderSeqArr,
    					@ApiParam("拆单依据的SXId(或category)") @RequestParam("splitSXId") String splitSXId) {
        try {
        	if(orderSeqArr.length == 0) {
        		return R.error("请选择订单！");
        	}
            if(StringUtils.isBlank(splitSXId)){
            	return R.error("请选择拆单依据！");
            }
            
            for(Integer orderSeq : orderSeqArr) {
	            OrderEntity orderEntity = orderManageService.getOrderBySeq(orderSeq);
	            if (orderEntity.getOrderStatus() != 1) {
	                return R.error("订单" + orderEntity.getOrderNum() + "状态不可以拆单！");
	            }
            }
            
        	orderManageService.splitOrder(orderSeqArr, splitSXId);
        	
            return R.ok("拆单成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("拆单失败");
        }
    }
    
    
    
    /**
     * 归类拆单合并成新订单，并往erp同步
     * @return
     */
    @Deprecated
    @PostMapping("classifySplitOrder")
	@RequiresPermissions("order:splitOrder")
    public R classifySplitOrder(@ApiParam("时间段") @RequestParam("classifySplitTime") String classifySplitTime) {
        try {
            if(StringUtils.isBlank(classifySplitTime)){
            	return R.error("请选择时间段！");
            }
            
            String[] start_endTime = classifySplitTime.split("~");
            String startTime = start_endTime[0];
            String endTime = start_endTime[1];
        	orderManageService.classifySplitOrder(getUser().getCompanySeq(), startTime, endTime);
            return R.ok("归类拆单合并同步成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("失败");
        }
    }
    
    
    /**
     * 按模板归类拆单合并成新订单，并往erp同步
     * @return
     */
    @PostMapping("splitOrderByModel")
	@RequiresPermissions("order:splitOrder")
    public R splitOrderByModel(@ApiParam("模板seq") @RequestParam("modelSeq") Integer modelSeq,
    		@ApiParam("时间段") @RequestParam("classifySplitTime") String classifySplitTime) {
        try {
            if(StringUtils.isBlank(classifySplitTime)){
            	return R.error("请选择时间段！");
            }
            
            String[] start_endTime = classifySplitTime.split("~");
            String startTime = start_endTime[0];
            String endTime = start_endTime[1];
        	orderManageService.splitOrderByModel(getUser().getCompanySeq(), modelSeq, startTime, endTime);
            return R.ok("归类拆单合并同步成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("失败");
        }
    }
    
    
    /**
     * 审核、修改订单已付金额
     * @param seq
     * @param price 本次支付金额
     * @return
     */
    @PostMapping("payPrice")
	@RequiresPermissions("order:checkOrder")
    public R payPrice(Integer seq, BigDecimal price) {
        try {
            return orderManageService.payOrder(seq, price, getUser());
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("操作失败");
        }
    }

    @GetMapping("expressCompany")
    public R expressCompany() {
        List<Map<String, Object>> list = orderManageService.getExpressCompanyList();
        return R.ok().put("list", list);
    }

    // 根据状态返回关联类型list(分公司/代理)
    @GetMapping("attach")
    public R attach(Integer attachType) {
        try {
            if (attachType == 2) {// 分公司
                List<BaseCompanyEntity> companyList = orderManageService.attachCompany(getUser());
                return R.ok().put("list", companyList);
            }
            if (attachType == 3) {// 代理
                List<BaseBrandEntity> agentList = orderManageService.attachAgent(getUser());
                return R.ok().put("list", agentList);
            }
            return R.error("没有指定类型!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("列表查询数据问题！");
        }
    }

    /**
     * 部分发货接口
     */
    @PostMapping("partShipped")
    @RequiresPermissions("order:deliverOrder")
    public R partShipped(PartShippedForm partShippedForm) {
        if (partShippedForm.getOrderSeq() == null || partShippedForm.getList() == null) {
            return R.ok("参数错误");
        }
        if (partShippedForm.getExpressCompanySeq() == null && partShippedForm.getExpressFile() == null) {
            return R.ok("快递信息不完整");
        }
        if (partShippedForm.getList() == null || partShippedForm.getList().isEmpty()) {
            return R.ok("快递信息不完整");
        }
        for (ShippedProductForm shippedProductForm : partShippedForm.getList()) {
            if (shippedProductForm.getSeq() == null) {
                return R.ok("参数错误");
            }
        }
        try {
            orderManageService.partShipped(partShippedForm, getUser());
            return R.ok();
        } catch (IOException e) {
        	e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("附件处理失败");
        }
    }

    
    /**
     * 延长收货
     * @param seq
     * @param price
     * @return
     */
    @PostMapping("changeReceiveTime")
    public R changeReceiveTime(Integer seq, Date receiveTime) {
        try {
        	OrderEntity orderEntity = orderManageService.getOrderBySeq(seq);
        	if(orderEntity.getOrderStatus() != 5) {
        		return R.error("只有已发货的订单可以延长收货");
        	}
        	if(orderEntity.getReceiveTime().after(receiveTime)) {
        		return R.error("只能选择之后的日期");
        	}
            orderManageService.changeReceiveTime(seq, receiveTime);
            return R.ok("延长收货成功");
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error(e.getMessage(), e);
            return R.error("延长收货失败");
        }
    }
    

    
    /**
     * 导出excel
     * @param response
     */
    @SuppressWarnings("deprecation")
	@PostMapping("exportExcel")
    public void exportOrderExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("订单表.xlsx", "UTF-8"));
            // 创建excel
            XSSFWorkbook wb = new XSSFWorkbook();

            //单元格样式
            XSSFCellStyle cellStyle = wb.createCellStyle();
            	//设置居中:
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
            	//设置字体:
            XSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);//选择需要用到的字体格式
            

            // 创建sheet页
            XSSFSheet sheet = wb.createSheet("sheet1");
            //默认宽度和高度
            sheet.setDefaultColumnWidth(15);
            sheet.setDefaultRowHeightInPoints(18);
            //不同列的宽度
            sheet.setColumnWidth(0, 20 * 256);
//            sheet.setColumnWidth(5, 25 * 256);
//            sheet.setColumnWidth(6, 25 * 256);

            //创建列标题
            String[] title = {"订单号", "已付金额", "订单价格", "订单状态", "订货方", "备注", "留言"};
            XSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(24);
            for (int i = 0; i < title.length; i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(title[i]);
            }
            String[] titleMsgValue = {"货号", "颜色", "尺码", "价格", "数量", "已发"};
            row = sheet.createRow(1);
            row.setHeightInPoints(24);
            for (int i = 0; i < titleMsgValue.length; i++) {
                XSSFCell cell = row.createCell(i + 1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(titleMsgValue[i]);
            }


            // 获取list对象
//          List<OrderEntity> dataset = orderManageService.getList(getUser().getCompanySeq());
            PageUtils companyBrandPage = orderManageService.orderlist(getUser(), params);
            @SuppressWarnings("unchecked")
			List<OrderEntity> dataset = (List<OrderEntity>) companyBrandPage.getList();
            
            // 循环插入数据
            int h = 2;
            for (int i = 0; i < dataset.size(); i++) {
                row = sheet.createRow(h++);// 创建行
                row.setHeightInPoints(20);//行高度
                OrderEntity orderEntity = dataset.get(i);// 获得行对象
                row.createCell(0).setCellValue(orderEntity.getOrderNum());
                row.createCell(1).setCellValue(orderEntity.getPaid().doubleValue());
                row.createCell(2).setCellValue(orderEntity.getOrderPrice().doubleValue());
                row.createCell(3).setCellValue(orderEntity.getStatusName());
                row.createCell(4).setCellValue(orderEntity.getOrderingparty());
                row.createCell(5).setCellValue(orderEntity.getRemark());
                row.createCell(6).setCellValue(orderEntity.getSuggestion());
                List<Map<String, Object>> goodsList = orderManageService.getOrderProductsList(orderEntity.getSeq());
                if (goodsList != null && !goodsList.isEmpty()) {
                    for (Map<String, Object> map : goodsList) {
                        row = sheet.createRow(h++);// 创建行
                        row.createCell(0).setCellValue(orderEntity.getOrderNum());
                        row.createCell(1).setCellValue(map.get("goodId").toString());
                        row.createCell(2).setCellValue(map.get("color").toString());
                        row.createCell(3).setCellValue(map.get("shoesSize").toString());
                        row.createCell(4).setCellValue(Double.valueOf(map.get("productPrice").toString()));
                        row.createCell(5).setCellValue(Integer.parseInt(map.get("buyCount").toString()));
                        row.createCell(6).setCellValue(Integer.parseInt(map.get("deliverNum").toString()));
                    }
                }
                sheet.createRow(h++);
            }
            // 获取输出流，写入excel并关闭
            ServletOutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("订单导出失败: " + e.getMessage(), e);
        }

    }

}
