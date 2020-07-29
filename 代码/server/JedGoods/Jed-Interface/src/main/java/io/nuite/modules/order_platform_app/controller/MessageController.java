package io.nuite.modules.order_platform_app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.nuite.common.utils.R;
import io.nuite.common.utils.ShiroUtils;
import io.nuite.modules.order_platform_app.annotation.Login;
import io.nuite.modules.order_platform_app.annotation.LoginUser;
import io.nuite.modules.order_platform_app.entity.MessageEntity;
import io.nuite.modules.order_platform_app.service.MessageService;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

/**
 * @description: 消息Controller类
 * @author: jxj
 * @create: 2019-03-30 13:20
 */
@RestController
@RequestMapping("/order/app/message")
@Api(tags = "jed消息")
public class MessageController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageService service;

    @Login
    @ApiOperation("新增消息")
    @PostMapping("/insertMessage")
    public R insertMessage(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,@ApiParam(name = "messageEntity",value = "消息实体类",required = true)@RequestBody MessageEntity messageEntity) {
        try {
            messageEntity.setInputTime(new Date());
            messageEntity.setCreator(baseUserEntity.getSeq());
            messageEntity.setDel(0);
            messageEntity.setIsRead(0);
            if(service.insert(messageEntity)) {
                return R.ok("新增成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("新增失败");
    }

    @Login
    @ApiOperation("修改消息")
    @PostMapping("/updateMessage")
    public R updateMessage(@ApiParam(name = "messageEntity",value = "消息实体类",required = true)@RequestBody MessageEntity messageEntity) {
        try {
            if(service.updateById(messageEntity)) {
                return R.ok("修改成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("修改失败");
    }

    @Login
    @ApiOperation("删除消息")
    @PostMapping("/deleteMessage")
    public R deleteMessage(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                           @ApiParam("消息序号列表")@RequestParam(value = "seqList",required = false) List<Integer> seqList,
                           @ApiParam("消息类型(1,客服消息 2,调价消息 3,系统消息)")@RequestParam("type") Integer type) {
        try {
            Wrapper<MessageEntity> wrapper = new EntityWrapper<>();
            if(seqList != null && seqList.size() > 0) {
                wrapper.in("Seq",seqList);
            }else {
                wrapper.eq("User_Seq",baseUserEntity.getSeq());
            }
            wrapper.eq("Type",type);
            service.delete(wrapper);
            return R.ok("删除成功");
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("删除失败");
    }

    @Login
    @ApiOperation("获取消息")
    @GetMapping("/selectMessage")
    public R selectMessage(@ApiIgnore @LoginUser BaseUserEntity baseUserEntity,
                           @ApiParam("消息类型(1,客服消息 2,调价消息 3,系统消息)")@RequestParam("type") Integer type,
                           @ApiParam("起始条数") @RequestParam(value = "start") Integer start,
                           @ApiParam("总条数") @RequestParam(value = "num") Integer num) {
        try {
            Page<MessageEntity> page = new Page<>(start / num + 1,num);
            return R.ok().result(service.selectMessage(baseUserEntity,type,page));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("获取单条消息")
    @GetMapping("/selectMessageBySeq")
    @ApiImplicitParams({@ApiImplicitParam(name = "seq",value = "消息序号",required = true,paramType = "query")})
    public R selectMessageBySeq(@RequestParam("seq") Integer seq) {
        try {
            return R.ok().result(service.selectMessageBySeq(seq));
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error();
    }

    @Login
    @ApiOperation("设为已读")
    @PostMapping("/updateIsRead")
    @ApiImplicitParams({@ApiImplicitParam(name = "seq",value = "消息序号",required = true,paramType = "query")})
    public R updateIsRead(@RequestParam("seq") Integer seq) {
        try {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setSeq(seq);
            messageEntity.setIsRead(1);
            if(service.updateById(messageEntity)) {
                return R.ok("修改成功");
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
        return R.error("修改失败");
    }
}
