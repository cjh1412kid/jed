package io.nuite.modules.system.controller.order_platform;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.controller.BaseController;
import io.nuite.modules.order_platform_app.entity.NoticeEntity;
import io.nuite.modules.order_platform_app.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2019/3/14 14:08
 * @Version: 1.0
 */

@RestController
@RequestMapping("/system/notice")
@Api(tags = "后台 - 公告管理")
public class NoticeController extends BaseController {
    
    @Autowired
    NoticeService noticeService;
    
    @GetMapping("/list")
    @ApiOperation(value = "获取公告列表", notes = "根据公司seq获取公告列表，按创建时间降序排列")
    public R list() {
        Integer companySeq = getUser().getCompanySeq();
        
        List<NoticeEntity> noticeEntities = noticeService.selectList(new EntityWrapper<NoticeEntity>()
            .eq("CompanySeq", companySeq).orderBy("InputTime", false));
        
        String imgBasePath = configUtils.getPictureBaseUrl() + "/notice/" + companySeq + "/";
        for (NoticeEntity noticeEntity : noticeEntities) {
            //设置公告图地址
            noticeEntity.setImgSrc(imgBasePath + noticeEntity.getImageName());
        }
        
        return R.ok().result(noticeEntities);
    }
    
    @PostMapping("/list2")
    @ApiOperation(value = "获取公告列表", notes = "根据公司seq获取公告列表，按创建时间降序排列；表格方式分页查询")
    public R list2(@ApiParam("当前页") Integer page, @ApiParam("每页数量") Integer limit) {
        Integer companySeq = getUser().getCompanySeq();
        
        Page<NoticeEntity> noticeEntityPage = new Page<>(page, limit);
        
        Page<NoticeEntity> noticePage = noticeService.selectPage(noticeEntityPage, new EntityWrapper<NoticeEntity>()
            .eq("CompanySeq", companySeq).orderBy("InputTime", false));
        
        String imgBasePath = configUtils.getPictureBaseUrl() + "/notice/" + companySeq + "/";
        for (NoticeEntity noticeEntity : noticePage.getRecords()) {
            //设置公告图地址
            noticeEntity.setImgSrc(imgBasePath + noticeEntity.getImageName());
        }
        
        return R.ok().put("page", new PageUtils(noticePage));
    }
    
    @PostMapping("/save")
    @ApiOperation(value = "新建公告", notes = "公告图不能为空")
    public R add(MultipartFile file, NoticeEntity notice) {
        
        Integer companySeq = getUser().getCompanySeq();
        //保存文件
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = System.currentTimeMillis() + ext;
        String imagePath = "notice" + File.separator + companySeq;
        String absolutePath = localUploadDir() + imagePath;
        
        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, newFileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("文件保存失败");
        }
        
        notice.setCompanySeq(companySeq);
        notice.setImageName(newFileName);
        notice.setInputTime(new Date());
        Integer seq = noticeService.save(notice);
        
        return R.ok().put("seq", seq);
    }
    
    @PostMapping("/update")
    @ApiOperation(value = "更新公告内容", notes = "更新内容包括公告图，公告详情页，创建时间")
    public R update(@RequestParam(required = false) MultipartFile file, NoticeEntity notice) {
        if (file != null) {
            Integer companySeq = getUser().getCompanySeq();
            //保存文件
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = System.currentTimeMillis() + ext;
            String imagePath = "notice" + File.separator + companySeq;
            String absolutePath = localUploadDir() + imagePath;
            
            File dir = new File(absolutePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(dir, newFileName);
            try {
                file.transferTo(dest);
                //更改公告图名称
                notice.setImageName(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return R.error("文件保存失败");
            }
        }
        
        //notice.setInputTime(new Date());
        noticeService.updateById(notice);
        return R.ok();
    }
    
    @PostMapping("/title/update")
    @ApiOperation(value = "更新标题", notes = "根据公告seq更新标题,会更新创建时间")
    public R updateTitle(@ApiParam("更新的公告名称") String name, @ApiParam("公告seq") Integer seq) {
        noticeService.updateNameBySeq(name, seq);
        return R.ok();
    }
    
    /**
     * 设置公告是否展示
     *
     * @return
     */
    @RequestMapping("/setUsed")
    public R setUsed(@RequestParam("seq") Integer seq,
                     @RequestParam("isUsed") Integer isUsed) {
        noticeService.setIsUsed(seq, isUsed);
        return R.ok();
    }
    
    @GetMapping("/del/{seq}")
    @ApiOperation(value = "删除公告", notes = "根据公告序号删除公告，逻辑删除")
    public R delete(@PathVariable Integer seq) {
        noticeService.deleteById(seq);
        return R.ok();
    }
}
