package io.nuite.modules.system.utils;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.nuite.modules.sr_base.dao.GoodsShoesDao;
import io.nuite.modules.sr_base.entity.GoodsShoesEntity;
import io.nuite.modules.sr_base.service.GoodsShoesService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
            boolean isCreateHeader, HttpServletResponse response) throws Exception {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);

    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,
            HttpServletResponse response) throws Exception {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response,
            ExportParams exportParams) throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws Exception {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
                throw new Exception(e.getMessage());
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null)
            ;
        downLoadExcel(fileName, response, workbook);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass)
            throws Exception {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new Exception("模板不能为空");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass)
            throws Exception {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new Exception("excel文件不能为空");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return list;
    }

    //货品管理导入
    public static List<Map<String,Object>> uploadPicture(String decpPath, List<MultipartFile> multipartFiles, Integer userSeq,GoodsShoesService GoodsShoesService) {
        List<Map<String,Object>> fileNames = new ArrayList<>(10);
        String message = "";
        for(MultipartFile multipartFile : multipartFiles) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] bytes = new byte[4096];
                String[] filePaths = multipartFile.getOriginalFilename().split("/");
                String filePath = filePaths[filePaths.length - 1];
                //文件名
                String fileName = userSeq + "_" + System.currentTimeMillis() + "_" + filePath;
                String goodId = filePath.split("\\.")[0];
                Integer index = goodId.lastIndexOf("-");
                //去首尾空格
                String good = goodId.trim();
                if(index > 0) {
                    good = goodId.substring(0,index).trim();
                }
                while (good.startsWith("　")){
                    good = good.substring(1, good.length()).trim();
                }
                while (good.endsWith("　")) {
                    good = good.substring(0, good.length() - 1).trim();
                }
                //当货品不存在时不上传图片
                Wrapper<GoodsShoesEntity> wrapper = new EntityWrapper<>();
                wrapper.eq("GoodID",good);
                List<GoodsShoesEntity> goodsShoesEntities = GoodsShoesService.selectList(wrapper);
                if(goodsShoesEntities == null || goodsShoesEntities.size() == 0) {
                    message += good + "货号不存在,上传失败<br>";
                    continue;
                }
                //上传路径
                String imgPath = decpPath + "/" + good + "/";
                File decpPathDir = new File(imgPath);
                if (!decpPathDir.exists()) {
                    decpPathDir.mkdirs();
                }
                File file = new File(imgPath + fileName);
                file.createNewFile();

                outputStream = new FileOutputStream(file);
                inputStream = multipartFile.getInputStream();
                //图片大小压缩到480*480
                //Thumbnails.of(inputStream).size(480,480).toOutputStream(outputStream);
                int len;
                //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
                while ((len = inputStream.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
                //String fileNameLittle = userSeq + "_" + System.currentTimeMillis() + "_" + filePath;
                //File fileLittle = new File(imgPath, fileNameLittle);
                //fileLittle.createNewFile();
                //Thumbnails.of(file).scale(1f).outputQuality(0.75f).outputFormat("jpg").toFile(fileLittle);
                logger.error(filePath + "上传成功");
                Map<String,Object> map = new HashMap<>(10);
                map.put(goodId,fileName);
                fileNames.add(map);
            }catch (Exception e) {
                logger.error(e.getMessage(),e);
                e.printStackTrace();
            }finally {
                try {
                    if(inputStream != null) {
                        inputStream.close();
                    }
                    if(outputStream != null) {
                        outputStream.close();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(),e);
                }
            }

        }
        if(message.length() > 0) {
            Map<String,Object> messageMap = new HashMap<>(1);
            messageMap.put("message",message);
            fileNames.add(messageMap);
        }
        return fileNames;
    }
}
