package com.zbkj.crmeb.wechat.service.impl;

import com.exception.CrmebException;
import com.utils.CrmebUtil;
import com.zbkj.crmeb.article.model.Article;
import com.zbkj.crmeb.article.service.ArticleService;
import com.zbkj.crmeb.user.model.UserToken;
import com.zbkj.crmeb.user.service.UserTokenService;
import com.zbkj.crmeb.wechat.service.WeChatService;
import com.zbkj.crmeb.wechat.service.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 微信用户表 服务实现类
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-11
 */
@Service
public class WechatUserServiceImpl implements WechatUserService {
    @Autowired
    private ArticleService articleService;

    @Lazy
    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserTokenService userTokenService;


    /**
     * 消息推送
     * @param userId 用户id
     * @param newsId 图文消息id
     * @author Mr.Zhang
     * @since 2020-04-11
     * @return Boolean
     */
    @Override
    public void push(String userId, Integer newsId) {
        //检查文章是否存在
        Article article = articleService.getById(newsId);
        if(article == null){
            throw new CrmebException("你选择的文章不存在！");
        }



//        {
//            "touser":"od9iXwsAl3c0e3POY39awOq0nnJ4",
//            "msgtype":"news",
//            "news":{
//                "articles": [
//                    {
//                        "title":"Happy Day",
//                        "description":"Is Really A Happy Day",
//                        "url":"http://front.java.crmeb.net:20002/articleManager",
//                        "picurl":"https://wuht-1300909283.cos.ap-chengdu.myqcloud.com/image/wechat/2020/06/16/003b595d6cc544dd981d3468d5caafa38p24bq7sa7.jpg"
//                    }
//                ]
//            }
//        }
        List<Integer> userIdList = CrmebUtil.stringToArray(userId);
        List<UserToken> userList = userTokenService.getList(userIdList);
        if(null == userList){
            throw new CrmebException("没有用户关注微信号");
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("msgtype", "news");

        HashMap<String, Object> articleInfo = new HashMap<>();
        ArrayList<Object> articleList = new ArrayList<>();

        HashMap<String, String> articleInfoItem = new HashMap<>();

        for (UserToken userToken : userList) {
            map.put("touser", userToken.getToken());

            articleInfoItem.put("title", article.getTitle());
            articleInfoItem.put("description", article.getSynopsis());
            articleInfoItem.put("url", article.getUrl()); //前端地址或者三方地址
            articleInfoItem.put("picurl", article.getImageInput());
            articleList.add(articleInfoItem);
            articleInfo.put("articles", articleList);
            map.put("news", articleInfo);
            weChatService.pushKfMessage(map);
        }
    }

}
