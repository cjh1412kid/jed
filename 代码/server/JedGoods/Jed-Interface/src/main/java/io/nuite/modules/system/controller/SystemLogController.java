package io.nuite.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.PageUtils;
import io.nuite.common.utils.R;
import io.nuite.modules.system.entity.order_platform.SystemLogEntity;
import io.nuite.modules.system.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author: yangchuang
 * @Date: 2018/8/29 14:06
 * @Version: 1.0
 * @Description:
 */

@RestController
@RequestMapping("log")
public class SystemLogController {

    @Autowired
    SystemLogService logService;

    @RequestMapping("list")
    public R getLogList( @RequestParam("page") Integer pageNum,
                         @RequestParam("limit") Integer pageSize){

        Page<SystemLogEntity> log = logService.selectPage(new Page<SystemLogEntity>(pageNum, pageSize),
                new EntityWrapper<SystemLogEntity>().orderBy("InputTime",false));

        PageUtils pageUtils = new PageUtils(log);
        return R.ok().put("page", pageUtils);
    }

    @RequestMapping("one")
    public R getOneLog(Integer seq){
        SystemLogEntity systemLogEntity = logService.selectById(seq);
        return R.ok(systemLogEntity);
    }

    @RequestMapping("add")
    public R addLog(SystemLogEntity logEntity){
        logService.insert(logEntity);
        return R.ok("日志添加成功");
    }

    @RequestMapping("update")
    public R updateLog(SystemLogEntity logEntity){
        logEntity.setUpdateTime(new Date());
        logService.updateById(logEntity);
        return R.ok("日志修改成功");
    }

    @RequestMapping("delete")
    public R deleteLog(Integer seq){
        logService.deleteById(seq);
        return R.ok("日志删除成功");
    }
    @RequestMapping("deleteS")
    public R deleteMultiLog(String seqs){
        List<Integer> seqs2 = JSON.parseArray(seqs, Integer.class);
        logService.deleteBatchIds(seqs2);
        return R.ok("日志删除成功");
    }
}
