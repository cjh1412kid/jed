package io.nuite.modules.system.controller.order_platform;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.ShoesArrivedEntity;
import io.nuite.modules.system.entity.ShoesReplenishDetailEntity;
import io.nuite.modules.system.entity.ShoesReplenishEntity;
import io.nuite.modules.system.service.ShoesArrivedService;
import io.nuite.modules.system.service.ShoesReplenishDetailService;
import io.nuite.modules.system.service.ShoesReplenishService;

@RestController
@RequestMapping("/order/replenish")
public class OrderReplenishController extends AbstractController {

	@Autowired
	private ShoesReplenishService shoesReplenishService;
	
	@Autowired
	private ShoesArrivedService ShoesArrivedService;
	
	@Autowired
	private GoodsShoesService goodsShoesService;
	
	@Autowired
	private ShoesReplenishDetailService shoesReplenishDetailService;
	
	
	@GetMapping("replenishDetail")
	public R replenishDetail(@RequestParam(value = "shoesSeq", required = false)Integer shoesSeq) {
		//根据shoeSeq 查询次补单详情
		List<ShoesReplenishEntity> shoesReplenishEntities=shoesReplenishService.queryReplenishPlan(shoesSeq);
		ShoesReplenishEntity shoesReplenishEntity=null;
		if(shoesReplenishEntities.size()>0) {
			shoesReplenishEntity=shoesReplenishEntities.get(0);
		}
		//根据shoeSeq 查询库存结构
		//尺码
		List<Map<String, Object>> list=goodsShoesService.getSizeMap(shoesSeq);
		for (Map<String, Object> map : list) {
			map.put("replenish", 0);
		}
		return R.ok().put("shoesReplenishEntity",shoesReplenishEntity).put("sizes", list);
		}
	
	@GetMapping("replenishNear")
	public R replenishNear(@RequestParam(value = "shoesSeq", required = false)Integer shoesSeq) {
		List<ShoesReplenishEntity> shoesReplenishEntities=shoesReplenishService.queryReplenishPlan(shoesSeq);
		ShoesReplenishEntity shoesReplenishEntity=shoesReplenishEntities.get(0);
		//根据ReplenishSeq查询
		List<ShoesReplenishDetailEntity> list=shoesReplenishDetailService.getListByReplenishSeq(shoesReplenishEntity.getSeq());
		return R.ok(list);
	}
	
	@GetMapping("replenishNearOne")
	public R replenishNearOne(@RequestParam(value = "replenishSeq", required = false)Integer replenishSeq) {
		ShoesReplenishEntity shoesReplenishEntity=shoesReplenishService.selectById(replenishSeq);
		GoodsShoesEntity goodsShoesEntity=goodsShoesService.selectById(shoesReplenishEntity.getShoesSeq());
		shoesReplenishEntity.setGoodID(goodsShoesEntity.getGoodID());
		List<ShoesReplenishDetailEntity> list=shoesReplenishDetailService.getListByReplenishSeq(replenishSeq);
		return R.ok(list).put("shoesReplenishEntity", shoesReplenishEntity);
	}
	
	
	@PostMapping("submitReplenish")
	public R submitReplenish( @RequestParam("allSizes")String allSizes, @RequestParam("replenishCount")Integer replenishCount, @RequestParam("shoesSeq")Integer shoesSeq,
			@RequestParam("remark")String remark) {
		 ShoesReplenishEntity shoesReplenishEntity=new ShoesReplenishEntity();
		 shoesReplenishEntity.setShoesSeq(shoesSeq);
		 shoesReplenishEntity.setRemark(remark);
		 shoesReplenishEntity.setReplenishNum(replenishCount);
		 shoesReplenishEntity.setCompanySeq(getUser().getCompanySeq());
		 shoesReplenishService.insert(shoesReplenishEntity);
		Integer shoesReplenishSeq=shoesReplenishEntity.getSeq();
		JSONArray  sizeArray=JSONArray.parseArray(allSizes);
		for (Object sizeObject : sizeArray) {
			JSONObject sizeJson=JSONObject.parseObject(sizeObject.toString());
			Integer replenishNum=sizeJson.getInteger("replenish");
			if(replenishNum!=0) {
				Integer sizeSeq=sizeJson.getInteger("sizeSeq");
				ShoesReplenishDetailEntity shoesReplenishDetailEntity=new ShoesReplenishDetailEntity();
				shoesReplenishDetailEntity.setReplenishSeq(shoesReplenishSeq);
				shoesReplenishDetailEntity.setSizeSeq(sizeSeq);
				shoesReplenishDetailEntity.setReplenishNum(replenishNum);
				shoesReplenishDetailEntity.setDel(0);
				shoesReplenishDetailService.insert(shoesReplenishDetailEntity);
			}
		}
		
		return R.ok();
	}
	
	/**
	 * 获取所有补货日期
	 * @return
	 */
	@RequestMapping("/daysList")
	public R getDaysList() {
		List<String> days=shoesReplenishService.getDaysList(getUser().getCompanySeq());
		return R.ok(days);
	}
	
	/**
	 * 根据日期查询所有补货消息
	 * @return
	 */
	@RequestMapping("replenishList")
	public R getReplenishList(@RequestParam(value = "inputTime", required = false)String inputTime,@RequestParam Integer page,
	        @RequestParam Integer limit) {
			PageUtils replenishList=shoesReplenishService.getGoodsList(new Page<Map<String, Object>>(page, limit),inputTime,getUser().getCompanySeq());	
			 return R.ok().put("page", replenishList);
	}
	
	/**
	 * 根据补货seq获取到货列表，剩余补货量
	 * @return
	 */
	@RequestMapping("arrivedList")
	public R getArrivedList(@RequestParam(value = "replenishSeq", required = false)Integer replenishSeq) {
		List<ShoesArrivedEntity> arriveds=	ShoesArrivedService.getListByReplenishSeq(replenishSeq);
		return R.ok(arriveds);
	}
	
	@RequestMapping("save")
	public R save(@RequestParam(value = "replenishSeq")Integer replenishSeq,@RequestParam(value = "replenishTime")String replenishTime,@RequestParam(value = "replenishNum")Integer replenishNum) {
		ShoesArrivedEntity shoesArrivedEntity=new ShoesArrivedEntity();
		shoesArrivedEntity.setArrivedNum(replenishNum);
		shoesArrivedEntity.setDel(0);
		shoesArrivedEntity.setReplenishSeq(replenishSeq);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info(replenishNum+""+replenishSeq+""+replenishTime);
		Date date=new Date();
		try {
			 date=sdf.parse(replenishTime);
		} catch (ParseException e) {
			return R.error();
		}
		shoesArrivedEntity.setArrivedTime(date);
		ShoesArrivedService.insert(shoesArrivedEntity);
		return R.ok();
	}
	
	@RequestMapping("add")
	public R add(@RequestBody ShoesReplenishEntity shoesReplenishEntity) {
		String goodsId=shoesReplenishEntity.getGoodID();
		GoodsShoesEntity goodsShoesEntity=goodsShoesService.goodsList(goodsId);
		if(goodsShoesEntity==null) {
			return R.error("货品名称输入有误");
		}
		Integer goodSeq=goodsShoesEntity.getSeq();
		shoesReplenishEntity.setShoesSeq(goodSeq);
		shoesReplenishService.insert(shoesReplenishEntity);
		return R.ok();
		
	}
	@RequestMapping("delete")
	public R delete(@RequestParam(value = "seq")Integer seq) {
		shoesReplenishService.deleteById(seq);
		return R.ok();
	}
	
	@GetMapping("exportRecord")
	public R exportRecord(HttpServletResponse response,@RequestParam(value = "seqs")String seqs) {
		JSONArray seqArray=JSONArray.parseArray(seqs);
		 ServletOutputStream out = null;
	        HSSFWorkbook wb = null;
	        
		try {
		List<Map<String, Object>> mapList=new ArrayList<Map<String,Object>>();
		for (Object replenishSeqObject : seqArray) {
			Integer replenishSeq=Integer.parseInt(replenishSeqObject.toString());
			ShoesReplenishEntity shoesReplenishEntity=shoesReplenishService.selectById(replenishSeq);
			GoodsShoesEntity goodsShoesEntity=goodsShoesService.selectById(shoesReplenishEntity.getShoesSeq());
			List<ShoesReplenishDetailEntity> shoesReplenishDetailEntities=shoesReplenishDetailService.getListByReplenishSeq(replenishSeq);
			for (ShoesReplenishDetailEntity shoesReplenishDetailEntity : shoesReplenishDetailEntities) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("goodID", goodsShoesEntity.getGoodID());
				map.put("size", shoesReplenishDetailEntity.getSizeName());
				map.put("num", shoesReplenishDetailEntity.getReplenishNum());
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				map.put("inputTime", sdf.format(shoesReplenishDetailEntity.getInputTime()));
				mapList.add(map);
			}
		}
	            response.setCharacterEncoding("UTF-8");
	            response.setHeader("content-Type", "application/vnd.ms-excel");
	            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("补单记录.xls", "UTF-8"));
	            // 创建excel
	            wb = new HSSFWorkbook();

	            //列标题单元格样式
	            HSSFCellStyle headCellStyle = wb.createCellStyle();
	            //设置居中:
	            headCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
	            headCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
	            //设置字体:
	            HSSFFont font = wb.createFont();
	            font.setFontName("仿宋_GB2312");
	            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
	            font.setFontHeightInPoints((short) 12);
	            headCellStyle.setFont(font);//选择需要用到的字体格式

	            //内容单元格样式
	            HSSFCellStyle contentCellStyle = wb.createCellStyle();
	            //设置居中:
	            contentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
	            contentCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

	            // 创建sheet页
	            HSSFSheet sheet = wb.createSheet("sheet1");
	            //默认宽度和高度
	            sheet.setDefaultColumnWidth(15);
	            sheet.setDefaultRowHeightInPoints(15);
	            //不同列的宽度
	            sheet.setColumnWidth(0, 18 * 256);
	            sheet.setColumnWidth(1, 18 * 256);
	            sheet.setColumnWidth(2, 40 * 256);


	            String[] title = {"货品名称", "尺码", "补单数量","补单时间"};
	            HSSFRow row = sheet.createRow(0);
	            row.setHeightInPoints(24);
	            for (int i = 0; i < title.length; i++) {
	                HSSFCell cell = row.createCell(i);
	                cell.setCellStyle(headCellStyle);
	                cell.setCellValue(title[i]);
	            }
	            int h = 1;
	                  // 循环多少次
	                  HSSFCell cell;
	                  for (int i = 0; i < mapList.size(); i++) {
	                      row = sheet.createRow(h++);// 创建行
	                      row.setHeightInPoints(100);//行高度
	                      Map<String, Object> recordEntity = mapList.get(i);// 获得行对象

	                      cell = row.createCell(0);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("goodID").toString());

	                      cell = row.createCell(1);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("size").toString());
	                      
	                      cell = row.createCell(2);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("num").toString());
	                      
	                      
	                      cell = row.createCell(3);
	                      cell.setCellStyle(contentCellStyle);
	                      cell.setCellValue(recordEntity.get("inputTime").toString());
	                  }
				
	            // 获取输出流，写入excel并关闭
	            out = response.getOutputStream();
	            wb.write(out);
	            out.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("导出失败: " + e.getMessage(), e);
	        } finally {
	            try {
	                if (out != null) {
	                    out.close();
	                }
	                if (wb != null) {
	                    wb.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        }
		
		
		return R.ok();
	}
}
