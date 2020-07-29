package io.nuite.modules.order_platform_app.controller;

import io.nuite.common.utils.R;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.entity.UEditorContentEntity;
import io.nuite.modules.system.service.order_platform.UEditorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/7/11 9:30
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("/order/app/editor")
@Api(tags = "jrd-首页定制", description = "获取自定义的页面内容")
public class UEditorAPPController {
    
    @Autowired
    UEditorService UEditorService;
    
    @Login
    @RequestMapping("homePage")
    @ApiOperation(value = "获取App登录后首页定制内容")
    public R getHomepageContent(@ApiIgnore @LoginUser BaseUserEntity loginUser) {
        
        Integer company_Seq = loginUser.getCompanySeq();
        UEditorContentEntity uc = null;
        uc = UEditorService.getHomepageContent(company_Seq);
        
        if (uc == null) {
            return R.error("未获取到登录后首页定制内容！");
        }
        
        String editorContent = uc.getContent();
        String[] res = {editorContent};
        return R.ok().result(res);
    }
    
    @RequestMapping("defaultPage")
    @ApiOperation(value = "获取App未登录首页展示内容")
    public R getDefaultContent() {
        
        List<UEditorContentEntity> contentEntities = UEditorService.listDefaultPageByUsed();
        
        if (contentEntities == null || contentEntities.size() == 0) {
            return R.error("未获取到未登录首页展示内容！");
        }
        
        UEditorContentEntity uEditorContentEntity = contentEntities.get(0);
        String editorContent = uEditorContentEntity.getContent();
        String[] res = {editorContent};
        return R.ok().result(res);
    }
    
}
