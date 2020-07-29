package io.nuite.modules.system.controller;

import io.nuite.common.exception.RRException;
import io.nuite.common.utils.Constant;
import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.SysMenuEntity;
import io.nuite.modules.sr_base.service.SysMenuService;
import io.nuite.modules.system.service.ShiroService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 */
@RestController
@RequestMapping("/system/menu")
public class SystemMenuController extends AbstractController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     */
    @GetMapping("/nav")
    public R nav() {
        //当前系统暂时只对用户属于不同的平台进行区分，不区分具体的菜单权限
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserSeq());
        Set<String> permissions = shiroService.getUserPermissions(getUserSeq());

        return R.ok().put("menuList", menuList).put("permissions", permissions);
    }

    /**
     * 工厂管理员选择可分配菜单列表
     */
    @GetMapping("/mylist")
    public List<SysMenuEntity> myList() {
        return sysMenuService.getAuthorList(getAdminUserSeq());
    }

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public List<SysMenuEntity> list() {
        List<SysMenuEntity> menuList = sysMenuService.selectList(null);
        for (SysMenuEntity sysMenuEntity : menuList) {
            SysMenuEntity parentMenuEntity = sysMenuService.selectById(sysMenuEntity.getParentSeq());
            if (parentMenuEntity != null) {
                sysMenuEntity.setParentName(parentMenuEntity.getName());
            }
        }

        return menuList;
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public R select() {
        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setSeq(0L);
        root.setName("一级菜单");
        root.setParentSeq(-1L);
        root.setOpen(true);
        menuList.add(root);

        return R.ok().put("menuList", menuList);
    }


    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId}")
    @RequiresPermissions("sys:menu:info")
    public R info(@PathVariable("menuId") Long menuId) {
        SysMenuEntity menu = sysMenuService.selectById(menuId);
        return R.ok().put("menu", menu);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public R save(@RequestBody SysMenuEntity menu) {
        //数据校验
        verifyForm(menu);

        sysMenuService.insert(menu);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R update(@RequestBody SysMenuEntity menu) {
        //数据校验
        verifyForm(menu);

        sysMenuService.updateById(menu);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{menuId}")
    @RequiresPermissions("sys:menu:delete")
    public R delete(@PathVariable("menuId") long menuId) {
        /*if (menuId <= 31) {
            return R.error("系统菜单，不能删除");
        }*/

        //判断是否有子菜单或按钮
        List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return R.error("请先删除子菜单或按钮");
        }

        sysMenuService.delete(menuId);

        return R.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentSeq() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentSeq() != 0) {
            SysMenuEntity parentMenu = sysMenuService.selectById(menu.getParentSeq());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
