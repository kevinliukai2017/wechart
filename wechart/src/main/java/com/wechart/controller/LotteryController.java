package com.wechart.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechart.model.lottery.LotteryModel;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.model.weuser.WeUserModel;
import com.wechart.service.lottery.LotteryService;
import com.wechart.service.webauth.WebAuthService;
import com.wechart.service.weuser.WeUserService;
import com.wechart.util.LotteryUtil;

@Controller
public class LotteryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private WebAuthService webAuthService;

    @Autowired
    private WeUserService weUserService;

    /**
     * 抽奖页面
     * @param model
     * @param request
     * @return
     * @throws WexinReqException
     */
    @RequestMapping("/wx/marketing/lottery")
    public String showLotteryPage(Model model, HttpServletRequest request) throws WexinReqException {
        String code = request.getParameter("code");
        WebAccessTokenModel webAccessToken = new WebAccessTokenModel();
        WeUserModel weUserModel = new WeUserModel();
        if(StringUtils.isNotBlank(code)){
            webAccessToken = webAuthService.getWebAccessToken(code);
            weUserModel = weUserService.getWeUserByOpenid(webAccessToken.getOpenid());
        }
        model.addAttribute("webAccessToekn", webAccessToken);
        model.addAttribute("lotteryCount", null == weUserModel ? 0 : weUserModel.getLotterycount());
        
        //jssdk config
        Map<String, String> ret = webAuthService.generateJSWXConfig();
        for (Map.Entry entry : ret.entrySet()) {
            logger.info(entry.getKey() + "=" + entry.getValue());
            model.addAttribute(entry.getKey().toString(), entry.getValue());
        }
        //WebAccessTokenModel webAccessToken = new WebAccessTokenModel();
        //webAccessToken.setOpenid("aaaa");
        //model.addAttribute("webAccessToekn", webAccessToken);
        
        return "marketing/lottery";
    }

    /**
     * 抽奖
     * @param openid
     * @return
     */
    // text/plain;charset=UTF-8
    // application/json;charset=UTF-8
    @RequestMapping(value = "/wx/marketing/lotteryResult", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String lotteryResult(String openid) {
        Object result[] = LotteryUtil.award(LotteryUtil.prizeArr);

        logger.info("转动角度:" + result[0] + "\t奖项ID:" + result[1] + "\t提示信息:" + result[2]);
        // 抽奖记录添加到数据库
        LotteryModel record = new LotteryModel();
        record.setOpenid(openid);
        record.setAwards(result[1].toString());
        record.setDescription(result[2].toString());
        lotteryService.addLotteryRecord(record);
        // 查询剩余的抽奖次数
        WeUserModel weUserModel = weUserService.getWeUserByOpenid(openid);
        // 有剩余次数
        if (weUserModel.getLotterycount() > 0) {
            weUserModel.setLotterycount(weUserModel.getLotterycount() - 1);
            weUserModel.setModifytime(new Timestamp(System.currentTimeMillis()));
            weUserService.updateWeUser(weUserModel);
            logger.info("修改用户：" + weUserModel.getOpenid() + "剩余次数成功，剩余：" + weUserModel.getLotterycount());
        }
        return "{\"angle\":\"" + result[0] + "\",\"msg\":\"" + result[2] + "\",\"lotteryCount\":\"" + weUserModel.getLotterycount() + "\"}";
    }

    
    @RequestMapping(value = "/wx/marketing/addChanceForShare", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String addChanceForShare(String openid){
        
        WeUserModel weUserModel = weUserService.getWeUserByOpenid(openid);
        //库中未查询到用户信息，未关注
        if(null == weUserModel){
            return "needsubscribe";
        }
        if(null != weUserModel.getLotterysharetime()){
            //最新的关注时间
            String lotterysharetime = DateUtils.timestamptoStr(weUserModel.getLotterysharetime());
            String today = DateUtils.timestamptoStr(new Timestamp(System.currentTimeMillis()));
            logger.info("分享时间 " + lotterysharetime);
            logger.info("当前时间 " + today);
            if(lotterysharetime.equals(today)){
                return "hasshared";
            }
        }
      
        logger.info("用户：" + openid + "进行分享抽奖活动，增加10次抽奖机会~");
        WeUserModel record = new WeUserModel();
        record.setModifytime(new Timestamp(System.currentTimeMillis()));
        record.setOpenid(openid);
        record.setLotterycount(10 + weUserModel.getLotterycount());
        record.setLotterysharetime(new Timestamp(System.currentTimeMillis()));
        weUserService.updateWeUser(record);
        return "sharesuccess";
    }
    
    @RequestMapping("/marketing/getaward")
    public String getAward() throws WexinReqException {
        return "marketing/awards";
    }
}
