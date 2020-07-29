package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.binarywang.utils.qrcode.QrcodeUtils;
import io.nuite.common.exception.RRException;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.controller.BaseController;
import io.nuite.modules.sr_base.entity.BaseCompanyEntity;
import io.nuite.modules.sr_base.service.BaseCompanyService;
import io.nuite.modules.system.entity.OrderManageEntity;
import io.nuite.modules.system.service.OrderPlanManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangchuang
 * @since 2019-03-22
 */

@Slf4j
@RestController
@RequestMapping("/sys/orderMeetingManage")
@Api(tags = "后台 - 订货会货品")
public class OrderMeetingGoodsController extends BaseController {
    
    @Autowired
    OrderPlanManageService orderPlanManageService;
    
    @Autowired
    BaseCompanyService baseCompanyService;
    
    @ApiOperation(value = "分页并条件查询")
    @PostMapping("/list")
    public R list(
        @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer page,
        @ApiParam("当前页数量") @RequestParam(defaultValue = "10") Integer limit,
        @ApiParam("排序字段名") @RequestParam(required = false) String order,
        @RequestParam Map<String, Object> params
    ) {
        
        Page<OrderManageEntity> pageCondition = new Page<>(page, limit);
        PageUtils pageUtils = orderPlanManageService.queryPage(pageCondition, params, getUser().getCompanySeq());
        
        return R.ok().put("page", pageUtils);
    }
    
    @ApiOperation(value = "删除指定货号的货品记录")
    @GetMapping("/del/{seq}")
    public R del(@PathVariable Integer seq) {
        orderPlanManageService.deleteById(seq);
        return R.ok();
    }
    
    @PostMapping("uploadGoodsExcel")
    public R uploadExcelFile(MultipartFile goodsExcelFile) {
        
        if (goodsExcelFile.isEmpty()) {
            return R.error("文件不存在！");
        }
        try {
            
            orderPlanManageService.parseJRDExcelToSave(goodsExcelFile, getUser());
/*            if (getUser().getCompanyName() != null && getUser().getCompanyName().contains("吉尔达")) {
                //吉尔达
                orderPlanManageService.parseJRDExcelToSave(goodsExcelFile, getUser());
            } else {
                orderPlanManageService.parseExcelToSave(goodsExcelFile, getUser());
            }*/
            
        } catch (MybatisPlusException e) {
            return R.error("数据保存出错！");
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return R.error("文件解析出错！" + e.getMessage());
        }
        return R.ok();
    }
    
    @ApiOperation(value = "保存或更新货品", notes = "有seq则更新，无seq则保存")
    @PostMapping("/saveOrUpdate")
    public R save(OrderManageEntity orderManageEntity) {
        orderManageEntity.setCompanySeq(getUser().getCompanySeq());
        orderPlanManageService.insertOrUpdate(orderManageEntity);
        return R.ok();
    }
    
    /**
     * 导出excel
     *
     * @param response
     */
    @SuppressWarnings("deprecation")
    @PostMapping("exportExcel")
    public void exportOrderExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        
        try {
            
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("订货会货品-导入模版.xlsx", "UTF-8"));
            
            // 创建excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            
            //单元格样式
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            //设置居中:
            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            
            //设置字体:
            XSSFFont font = workbook.createFont();
            font.setFontName("宋体");
            font.setBold(true);//粗体显示
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);//选择需要用到的字体格式
            
            // 创建sheet页
            XSSFSheet sheet = workbook.createSheet("sheet1");
            //默认宽度和高度
            sheet.setDefaultColumnWidth(15);
            sheet.setDefaultRowHeightInPoints(18);
            //不同列的宽度
            sheet.setColumnWidth(0, 20 * 256);
            
            BaseCompanyEntity baseCompanyEntity = baseCompanyService.selectById(getUser().getCompanySeq());
            
            if (baseCompanyEntity != null
                && baseCompanyEntity.getName() != null
                && baseCompanyEntity.getName().contains("吉尔达")) {
                XSSFRow row1 = sheet.createRow(0);
                XSSFCell cell1 = row1.createCell(0);
                cell1.setCellStyle(cellStyle);
                cell1.setCellValue("9226GD-92310PP米");
                
                XSSFRow row2 = sheet.createRow(1);
                XSSFCell cell2 = row2.createCell(0);
                cell2.setCellStyle(cellStyle);
                cell2.setCellValue("9226GD-92310PP黑");
            } else {
                
                //创建列标题
                String[] title = {"货号", "颜色", "分类", "年份", "季节"};
                XSSFRow row = sheet.createRow(0);
                row.setHeightInPoints(24);
                for (int i = 0; i < title.length; i++) {
                    XSSFCell cell = row.createCell(i);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(title[i]);
                }
            }
            
            // 获取输出流，写入excel并关闭
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 导出订货会商品二维码
     *
     * @param response
     */
    @PostMapping("exportQRCode")
    public void exportQRCode(HttpServletResponse response,
                             @RequestParam("year") Integer year,
                             @RequestParam("seasonSeq") Integer seasonSeq) {
        
        ServletOutputStream out = null;
        HSSFWorkbook wb = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("订货会货号二维码.xls", "UTF-8"));
            // 创建excel
            wb = new HSSFWorkbook();
            
            //列标题单元格样式
            HSSFCellStyle headCellStyle = wb.createCellStyle();
            //设置居中:
            headCellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            //设置字体:
            HSSFFont font = wb.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBold(true);//粗体显示
            font.setFontHeightInPoints((short) 12);
            headCellStyle.setFont(font);//选择需要用到的字体格式
            
            //内容单元格样式
            HSSFCellStyle contentCellStyle = wb.createCellStyle();
            //设置居中:
            contentCellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            
            // 创建sheet页
            HSSFSheet sheet = wb.createSheet("sheet1");
            //默认宽度和高度
            //sheet.setDefaultColumnWidth(15);
            //sheet.setDefaultRowHeightInPoints(18);
            //不同列的宽度
            sheet.setColumnWidth(0, 30 * 256);
            sheet.setColumnWidth(1, 40 * 256);
            //sheet.setColumnWidth(2, 40 * 256);
            
            String[] title = {"货号", "二维码"};
            HSSFRow row = sheet.createRow(0);
            row.setHeightInPoints(24);
            for (int i = 0; i < title.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(headCellStyle);
                cell.setCellValue(title[i]);
            }
            
            Wrapper<OrderManageEntity> wrapper = new EntityWrapper<OrderManageEntity>()
                .eq("Company_Seq", getUser().getCompanySeq());
            
            if (year != null && year != -1) {
                wrapper.eq("Year", year);
            }
            
            if (seasonSeq != null && seasonSeq != -1) {
                wrapper.eq("Season_Seq", seasonSeq);
            }
            
            // 订货会货品列表
            List<OrderManageEntity> goodsList = orderPlanManageService.selectList(wrapper);
            
            // 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            // 循环多少次
            int rowIndex = 1;
            HSSFCell cell;
            for (OrderManageEntity orderManageEntity : goodsList) {
                row = sheet.createRow(rowIndex);// 创建行
                row.setHeightInPoints(210);//行高度
                
                //第一列
                cell = row.createCell(0);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(orderManageEntity.getGoodID());
                
                // anchor主要用于设置图片的属性
                HSSFClientAnchor anchor = new HSSFClientAnchor(10, 10, 1023, 255, (short) 1, rowIndex, (short) 1, rowIndex);
                // 插入图片
                HSSFPicture picture = patriarch.createPicture(anchor,
                    wb.addPicture(QrcodeUtils.createQrcode(orderManageEntity.getGoodID(), 250, null), HSSFWorkbook.PICTURE_TYPE_JPEG));
                picture.resize();
                rowIndex++;
            }
            
            // 获取输出流，写入excel并关闭
            out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("货品二维码导出失败: " + e.getMessage(), e);
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
        
    }
    
    @GetMapping("/seasons")
    @ApiOperation("获取所有存在的季节")
    public R getAllExistSeasons() {
        
        List<Map<String, Object>> allExistSeasons = orderPlanManageService.getAllExistSeasons(getUser().getCompanySeq());
        return R.ok().result(allExistSeasons);
    }
    
    @GetMapping("/years")
    @ApiOperation("获取所有存在的年份")
    public R getAllExistYears() {
        
        List<Integer> allExistYears = orderPlanManageService.getAllExistYears(getUser().getCompanySeq());
        return R.ok().result(allExistYears);
    }
}

