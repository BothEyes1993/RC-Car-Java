package com.zbkj.crmeb.pub.controller;

import com.zbkj.crmeb.wechatMessage.service.WeChatMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 微信缓存表 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/public/wechat/message")
@Api(tags = "微信开放平台 -- 消息")
public class WeChatMessageController {

    @Autowired
    private WeChatMessageService weChatMessageService;

    /**
     * 微信关键字回复
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    @ApiOperation(value = "接受微信推送过来的消息")
    @RequestMapping(value = "/webHook", method = RequestMethod.POST)
    public String webHook(HttpServletRequest request){
       return weChatMessageService.init(request);
    }
//微信推送地址验证
//    @RequestMapping(value = { "/webHook" }, method = RequestMethod.GET)
//    private void webHook(
//            @RequestParam(value = "signature", required = false) String signature,
//            @RequestParam(value = "timestamp", required = false) String timestamp,
//            @RequestParam(value = "nonce", required = false) String nonce,
//            @RequestParam(value = "echostr") String echostr,
//            HttpServletResponse response) throws IOException {
//        PrintWriter writer = response.getWriter();
//        writer.print(echostr);
//        writer.flush();
//        writer.close();
//    }
}



