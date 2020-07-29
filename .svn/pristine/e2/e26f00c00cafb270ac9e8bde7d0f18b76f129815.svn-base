package io.nuite.modules.system.controller.order_platform;

import com.alibaba.fastjson.JSON;
import io.nuite.common.utils.FileUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.UEditorContentEntity;
import io.nuite.modules.sr_base.service.BaseCompanyService;
import io.nuite.modules.sr_base.utils.ConfigUtils;
import io.nuite.modules.system.controller.AbstractController;
import io.nuite.modules.system.entity.UEditor;
import io.nuite.modules.system.service.order_platform.UEditorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/7/4 18:12
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/editor")
public class UEditorController extends AbstractController {
    private final static Logger logger = LoggerFactory.getLogger(UEditorController.class);

    @Autowired
    BaseCompanyService baseCompanyService;

    @Autowired
    UEditorService uEditorService;

    @Autowired
    private ConfigUtils configUtils;

    /**
     * 图片上传
     *
     * @param action
     * @param upfile
     * @return
     */
    @RequestMapping(value = "/imgUpload")
    public String imgUpload(@RequestParam("action") String action, MultipartFile upfile) {

        UEditor UEditor = new UEditor();
        if (action == null || upfile == null) {
            UEditor.setState("上传失败");
            return JSON.toJSONString(UEditor);
        }

        if (action.equals("uploadimage") || action.equals("uploadscrawl")) {
            if (upfile != null) {
                Integer company_Seq = getUser().getCompanySeq();
                String commonPath = "ueditor" + File.separator + company_Seq + File.separator + "image" + File.separator;
                /*图片存放地址*/
                String imgDir = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator + commonPath;
                String filename = null;
                try {
                    filename = FileUtils.upLoadFile(imgDir, null, upfile);
                    /*图片读取地址*/
                    UEditor.setUrl(configUtils.getPictureBaseUrl() + File.separator + commonPath + filename);
                    UEditor.setState("SUCCESS");
                    UEditor.setOriginal(filename);
                    UEditor.setTitle(filename);
                    return JSON.toJSONString(UEditor);
                } catch (IOException e) {
                    e.printStackTrace();
                    UEditor.setState("ERROR");
                    return JSON.toJSONString(UEditor);
                }
            } else {
                UEditor.setState("上传文件为null");
            }
        }
        return JSON.toJSONString(UEditor);
    }

    /**
     * 视频上传
     *
     * @param action
     * @param upfile
     * @return
     */
    @RequestMapping(value = "/videoUpload")
    public String videoUpload(@RequestParam("action") String action, MultipartFile upfile) {

        UEditor UEditor = new UEditor();
        if (action == null || upfile == null) {
            UEditor.setState("上传失败");
            return JSON.toJSONString(UEditor);
        }

        if ("uploadvideo".equals(action)) {

            if (upfile != null) {
                Integer company_Seq = getUser().getCompanySeq();
                String commonPath = "ueditor" + File.separator + company_Seq + File.separator + "video" + File.separator;
                /*视频存放地址*/
                String videoDir = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator + commonPath;
                String vfilename = null;
                try {
                    vfilename = FileUtils.upLoadFile(videoDir, null, upfile);
                    /*视频读取地址*/
                    UEditor.setUrl(configUtils.getPictureBaseUrl() + File.separator + commonPath + vfilename);
                    UEditor.setState("SUCCESS");
                    UEditor.setOriginal(vfilename);
                    UEditor.setTitle(vfilename);
                } catch (IOException e) {
                    e.printStackTrace();
                    UEditor.setState("上传失败");
                    return JSON.toJSONString(UEditor);
                }
            } else {
                UEditor.setState("上传文件为null");
            }
        }
        return JSON.toJSONString(UEditor);
    }

    /**
     * 根据当前用户获取所有相关模版记录
     *
     * @param uType 模版类型： 0首页模版；1 未登录模版
     * @return
     */
    @RequestMapping("/content/{uType}")
    public R getEditorRecords(@PathVariable("uType") String uType) {
        Integer company_Seq = getUser().getCompanySeq();
        List result = uEditorService.listByCompanySeqAndUType(company_Seq, uType);
        return R.ok().put("records", result);
    }

    /**
     * 保存上传的编辑内容
     *
     * @param ur
     * @return
     */
    @RequestMapping("/save")
    public R saveRecord(UEditorContentEntity ur) {
        if (ur.getName() != null && ur.getContent() != null && ur.getContent().trim().length() > 0) {
            ur.setCompanySeq(getUser().getCompanySeq());
            uEditorService.saveOrUpdate(ur);
            return R.ok();
        } else {
            return R.error("名称或内容为空");
        }

    }

    /**
     * 根据模板id查询模板记录
     *
     * @param seq
     * @return
     */

    @RequestMapping("/getBySeq")
    public R getBySeq(Integer seq) {
        UEditorContentEntity ur = uEditorService.getById(seq);
        return R.ok().put("content", ur.getContent());
    }

    /**
     * 获取被设定为【主模版】的模版
     *
     * @return
     */
    @RequestMapping("/template/used/{uType}")
    public R getByUsed(@PathVariable String uType) {

        UEditorContentEntity ur = uEditorService.getByCompanySeqAndUsedAndUType(getUser().getCompanySeq(), 1, uType);
        if (ur != null) {
            return R.ok().put("seq", ur.getSeq()).put("result", ur.getContent());
        }
        return R.error("未加载到主模板");
    }

    /**
     * 删除指定id的模版
     *
     * @param seq
     * @return
     */
    @RequestMapping("/delete")
    public R deleteById(Integer seq) {
        uEditorService.delete(seq);
        return R.ok();
    }

    /**
     * 更新选中节点的模版
     *
     * @param ur
     * @return
     */
    @RequestMapping("/update")
    public R updateById(UEditorContentEntity ur) {
        uEditorService.update(ur);
        return R.ok();
    }

    /**
     * 设置主模板(设置或取消)
     *
     * @param ur
     * @return
     */
    @RequestMapping("/setUsed")
    public R setUsed(UEditorContentEntity ur) {
        if (ur.getUsed() == 0) {
            uEditorService.setUsed(ur);
        } else if (ur.getUsed() == 1) {
            uEditorService.cancelUsed(ur);
        }
        return R.ok();
    }

    /**
     * 上传视频缩略图
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadVideoPoster")
    public R uploadVideoPoster(@RequestParam("file") MultipartFile file) {

        if (file != null) {
            Integer company_Seq = getUser().getCompanySeq();
            String commonPath = "ueditor" + File.separator + company_Seq + File.separator + "videoposter" + File.separator;
            /*图片存放地址*/
            String imgDir = FileUtils.getWebAppsPath() + configUtils.getPictureBaseUploadProject() + File.separator + commonPath;
            String filename = null;
            try {
                filename = FileUtils.upLoadFile(imgDir, null, file);
                /*图片读取地址*/
                String imgPath = configUtils.getPictureBaseUrl() + File.separator + commonPath + filename;
                return R.ok().put("imgPath", imgPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return R.error("上传文件为空");
        }

        return R.error("服务器异常");
    }

}
