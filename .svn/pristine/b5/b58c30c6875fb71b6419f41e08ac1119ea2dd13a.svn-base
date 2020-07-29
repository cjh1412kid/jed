package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.FileUtils;
import io.nuite.modules.order_platform_app.service.BaseService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class BaseController extends AbstractController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected BaseService baseService;

    @Autowired
    protected ConfigUtils configUtils;

    //基础库访问目录
    private String baseDir() {
        return configUtils.getPictureBaseUrl() + "/" + configUtils.getBaseDir() + "/";
    }

    //基础库上传目录
    private String baseUploadDir(HttpServletRequest request) {
//		//tomcat实际路径
//		String path = request.getSession().getServletContext().getRealPath("/");
//		//上传项目路径
//		String uploadProject1 = path.substring(0, path.length() - 11) + configUtils.getPictureBaseUploadProject() +"/";
        String uploadProject = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + "/";
        return uploadProject + configUtils.getBaseDir() + "/";
    }

    //订货平台访问目录
    private String orderPlatformDir() {
        return configUtils.getPictureBaseUrl() + "/" + configUtils.getOrderPlatformApp().getOrderPlatformDir() + "/";
    }

    //订货平台上传目录
    private String orderPlatformUploadDir(HttpServletRequest request) {
//		//tomcat实际路径
//		String path = request.getSession().getServletContext().getRealPath("/");
//		//上传项目路径
//		String uploadProject1 = path.substring(0, path.length() - 11) + configUtils.getPictureBaseUploadProject() +"/";
        String uploadProject = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + "/";
        return uploadProject + configUtils.getOrderPlatformApp().getOrderPlatformDir() + "/";
    }

    /**
     * 品牌图片路径
     *
     * @param folder
     * @return
     */
    protected String getBaseBrandPictureUrl(String folder) {
        return baseDir() + configUtils.getBaseBrand() + "/" + folder + "/";
    }

    /**
     * 门店图片路径
     *
     * @param folder
     * @return
     */
    protected String getBaseShopPictureUrl(String folder) {
        return baseDir() + configUtils.getShopImage() + "/" + folder + "/";
    }
    
    
    /**
     * 鞋子基本信息图片路径
     *
     * @param folder
     * @return
     */
    protected String getGoodsShoesPictureUrl(String folder) {
        return baseDir() + configUtils.getGoodsShoes() + "/" + folder + "/";
    }

    /**
     * 用户信息图片路径
     *
     * @param folder
     * @return
     */
    protected String getBaseUserPictureUrl(String folder) {
        return baseDir() + configUtils.getBaseUser() + "/" + folder + "/";
    }

    /**
     * 主页展示图图片路径
     *
     * @param folder
     * @return
     */
    protected String getPublicityPicPictureUrl(String folder) {
        return orderPlatformDir() + configUtils.getOrderPlatformApp().getPublicityPic() + "/" + folder + "/";
    }

    /**
     * 社区图片路径
     *
     * @param folder
     * @return
     */
    protected String getCommunityCONTENTPictureUrl(String folder) {
        return orderPlatformDir() + configUtils.getOrderPlatformApp().getCommunityContent() + "/" + folder + "/";
    }

    /**
     * 在线沟通图片路径
     *
     * @param folder
     * @return
     */
    protected String getOnlineMessagePictureUrl(String folder) {
        return orderPlatformDir() + configUtils.getOrderPlatformApp().getOnlineMessage() + "/" + folder + "/";
    }

    /**
     * 订单的图片路径
     *
     * @param folder
     * @return
     */
    protected String getOrderPictureUrl(String folder) {
        return orderPlatformDir() + configUtils.getOrderPlatformApp().getOrder() + "/" + folder + "/";
    }

    /**
     * 订单快递的图片路径
     *
     * @param folder
     * @return
     */
    protected String getOrderExpressPictureUrl(String folder) {
        return orderPlatformDir() + configUtils.getOrderPlatformApp().getOrderExpress() + "/" + folder + "/";
    }

    /**
     * 轮播图图片路径
     *
     * @param folder
     * @return
     */
    protected String getHomeCarouselPictureUrl(String folder) {
        return baseDir() + configUtils.getSowingMap() + "/" + folder + "/";
    }

    //上传路径

    /**
     * 用户图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getBaseUserUploadUrl(HttpServletRequest request, String folder) {
        return baseUploadDir(request) + configUtils.getBaseUser() + "/" + folder + "/";
    }

    /**
     * 社区图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getCommunityCONTENTUploadUrl(HttpServletRequest request, String folder) {
        return orderPlatformUploadDir(request) + configUtils.getOrderPlatformApp().getCommunityContent() + "/" + folder + "/";
    }

    /**
     * 在线沟通图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getOnlineMessageUploadUrl(HttpServletRequest request, String folder) {
        return orderPlatformUploadDir(request) + configUtils.getOrderPlatformApp().getOnlineMessage() + "/" + folder + "/";
    }

    /**
     * 订单图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getOrderUploadUrl(HttpServletRequest request, String folder) {
        return orderPlatformUploadDir(request) + configUtils.getOrderPlatformApp().getOrder() + "/" + folder + "/";
    }

    /**
     * 货品图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getGoodsUploadUrl(HttpServletRequest request, String folder) {
        return baseUploadDir(request) + configUtils.getGoodsShoes() + "/" + folder + "/";
    }

    /**
     * 快递单图片上传路径
     *
     * @param request
     * @param folder
     * @return
     */
    protected String getOrderExpressUploadUrl(HttpServletRequest request, String folder) {
        return orderPlatformUploadDir(request) + configUtils.getOrderPlatformApp().getOrderExpress() + "/" + folder + "/";
    }

    /**
     * 上传文件，返回文件名
     *
     * @param userSeq
     * @param imgDir
     * @param img
     * @return
     * @throws IOException
     */
    protected String upLoadFile(Integer userSeq, String imgDir, MultipartFile img) throws IOException {

        // 存放目录
        File fileDir = new File(imgDir);
        // 如果目录不存在就创建
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 原文件名
        String originalFilename = img.getOriginalFilename();
        logger.info("originalFilename:" + originalFilename);
        // 重新定义文件名
        String fileName = userSeq + "_" + System.currentTimeMillis() + "_" + originalFilename;
        logger.info("fileName:" + fileName);
        // 上传文件
        File file = new File(fileDir, fileName);
        file.createNewFile();
        img.transferTo(file);
        logger.info("AbsolutePath:" + file.getAbsolutePath());

        return fileName;
    }

    /**
     * 文件持久化本地基础路径
     *
     * @return
     */
    protected String localUploadDir() {
        String uploadProject = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator;
        return uploadProject;
    }
}
