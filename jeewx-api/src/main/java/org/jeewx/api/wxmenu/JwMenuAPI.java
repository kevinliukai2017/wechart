package org.jeewx.api.wxmenu;

import java.util.ArrayList;
import java.util.List;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;
import org.jeewx.api.core.req.model.menu.MenuConfigureGet;
import org.jeewx.api.core.req.model.menu.MenuCreate;
import org.jeewx.api.core.req.model.menu.MenuDelete;
import org.jeewx.api.core.req.model.menu.MenuGet;
import org.jeewx.api.core.req.model.menu.WeixinButton;
import org.jeewx.api.core.req.model.menu.config.CustomWeixinButtonConfig;
import org.jeewx.api.core.req.model.menu.config.WeixinButtonExtend;
import org.jeewx.api.core.util.WeiXinConstant;
import org.jeewx.api.extend.CustomJsonConfig;
import org.jeewx.api.wxbase.wxtoken.JwTokenAPI;
import org.jeewx.api.wxsendmsg.model.WxArticleConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 微信--menu
 * 
 * @author lizr
 * 
 */
public class JwMenuAPI {

	/**
	 * 创建菜单
	 *  button	是	一级菜单数组，个数应为1~3个
		sub_button	否	二级菜单数组，个数应为1~5个
		type	是	菜单的响应动作类型
		name	是	菜单标题，不超过16个字节，子菜单不超过40个字节
		key	click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节
		url	view类型必须	网页链接，用户点击菜单可打开链接，不超过256字节
	 * @param accessToken
	 * @param button  的json字符串
	 * @throws WexinReqException
	 */
	public static String createMenu(String accessToken,List<WeixinButton> button) throws WexinReqException{
		MenuCreate m = new MenuCreate();
		m.setAccess_token(accessToken);
		m.setButton(button);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(m);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		String msg = "";
		if(error == null){
			msg = result.getString("groupid");
		}else{
			msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
		}
		return msg;
	}
	
	/**
	 * 获取所有的菜单
	 * @param accessToken
	 * @return
	 * @throws WexinReqException
	 */
	public static List<WeixinButton> getAllMenu(String accessToken) throws WexinReqException{
		MenuGet g = new MenuGet();
		g.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(g);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		List<WeixinButton> lstButton = null;
		JSONObject menu = result.getJSONObject("menu");
		JSONArray buttons = menu.getJSONArray("button");
		JSONArray subButtons = null;
		lstButton = new ArrayList<WeixinButton>();
		WeixinButton btn = null;
		WeixinButton subBtn = null;
		List<WeixinButton> lstSubButton = null;
		for (int i = 0; i < buttons.size(); i++) {
			btn = (WeixinButton) JSONObject.toBean(buttons.getJSONObject(i),
					WeixinButton.class);
			subButtons = buttons.getJSONObject(i).getJSONArray("sub_button");
			if (subButtons != null) {
				lstSubButton = new ArrayList<WeixinButton>();
				for (int j = 0; j < subButtons.size(); j++) {
					subBtn = (WeixinButton) JSONObject.toBean(
							subButtons.getJSONObject(j), WeixinButton.class);
					lstSubButton.add(subBtn);
				}
				btn.setSub_button(lstSubButton);
			}
			lstButton.add(btn);
		}
		return lstButton;
	}
	
	/**
	 * 删除所有的菜单
	 * @param accessToken
	 * @return
	 * @throws WexinReqException
	 */
	public static String deleteMenu(String accessToken) throws WexinReqException{
		MenuDelete m = new MenuDelete();
		m.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(m);
		String msg = result.getString(WeiXinConstant.RETURN_ERROR_INFO_MSG);
		return msg;
	}
	
	//update-begin--Author:luobaoli  Date:20150714 for：增加“获取自定义菜单配置接口”功能接口
	//update-begin--Author:luobaoli  Date:20150715 for：优化该方法的处理逻辑
	/**
	 * 获取自定义接口配置
	 * @param accessToken
	 * @return
	 * @throws WexinReqException
	 */
	public static CustomWeixinButtonConfig getAllMenuConfigure(String accessToken) throws WexinReqException{
		MenuConfigureGet cmcg = new MenuConfigureGet();
		cmcg.setAccess_token(accessToken);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(cmcg);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		
		CustomWeixinButtonConfig customWeixinButtonConfig = (CustomWeixinButtonConfig) JSONObject.toBean(result, new CustomJsonConfig(CustomWeixinButtonConfig.class,"selfmenu_info"));
		
		JSONObject selfmenuInfo = result.getJSONObject("selfmenu_info");
		if(selfmenuInfo!=null && !JSONUtils.isNull(selfmenuInfo)){ 
			/**处理父类菜单 */
			JSONArray buttons = selfmenuInfo.getJSONArray("button");
			List<WeixinButtonExtend> listButton = new ArrayList<WeixinButtonExtend>();
			for(int i=0;i<buttons.size();i++){
				WeixinButtonExtend weixinButtonExtend = (WeixinButtonExtend) JSONObject.toBean(buttons.getJSONObject(i),new CustomJsonConfig(WeixinButtonExtend.class,"sub_button"));
				/**处理子类菜单 */
				JSONObject subButtonJsonObj = buttons.getJSONObject(i).getJSONObject("sub_button");
				if(subButtonJsonObj!=null && !JSONUtils.isNull(subButtonJsonObj)){
					JSONArray subButtons = subButtonJsonObj.getJSONArray("list");
					if (subButtons != null) {
						List<WeixinButtonExtend> listSubButton = new ArrayList<WeixinButtonExtend>();
						for (int j = 0; j < subButtons.size(); j++) {
							WeixinButtonExtend subBtn = (WeixinButtonExtend) JSONObject.toBean(subButtons.getJSONObject(j), new CustomJsonConfig(WeixinButtonExtend.class,"news_info"));
							/**处理菜单关联的图文消息 */
							JSONObject newsInfoJsonObj = subButtons.getJSONObject(j).getJSONObject("news_info");
							if(newsInfoJsonObj!=null && !JSONUtils.isNull(newsInfoJsonObj)){
								JSONArray newsInfos = newsInfoJsonObj.getJSONArray("list");
								List<WxArticleConfig> listNewsInfo = new ArrayList<WxArticleConfig>();
								for (int k = 0; k < newsInfos.size(); k++) {
									WxArticleConfig wxArticleConfig = (WxArticleConfig) JSONObject.toBean(newsInfos.getJSONObject(k), WxArticleConfig.class);
									listNewsInfo.add(wxArticleConfig);
								}
								subBtn.setNews_info(listNewsInfo);
							}
							listSubButton.add(subBtn);
						}
						weixinButtonExtend.setSub_button(listSubButton);
					}
				}
				listButton.add(weixinButtonExtend);
			}
			customWeixinButtonConfig.setSelfmenu_info(listButton);
		}
		return customWeixinButtonConfig;
	}
	//update-end--Author:luobaoli  Date:20150715 for：优化该方法的处理逻辑
	//update-end--Author:luobaoli  Date:20150714 for：增加“获取自定义菜单配置接口”功能接口
	
	
	
	
/*	{
	    "button": [
	        {
	            "name": "功能",
	            "sub_button": [
	                {
	                    "type": "click",
	                    "name": "位置信息",
	                    "key": "findLocation"
	                },
	                {
	                    "type": "click",
	                    "name": "实时天气",
	                    "key": "findWeather"
	                },
	                {
	                    "type": "view",
	                    "name": "抽奖",
	                    "key": "lottery",
	                    "url": "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa7e93a3f7bfc8065&redirect_uri=http%3A%2F%2Fherocenter.cn%2Fwx%2Fmarketing%2Flottery&response_type=code&scope=snsapi_base&state=123#wechat_redirect"
	                },
	                {
	                    "type": "scancode_push",
	                    "name": "scancode_push",
	                    "key": "scancode_pushEvent事件"
	                }
	            ]
	        },
	        {
	            "name": "四到六",
	            "sub_button": [
	                {
	                    "type": "scancode_waitmsg",
	                    "name": "scancode_waitmsg",
	                    "key": "scancode_waitmsg事件"
	                },
	                {
	                    "type": "pic_sysphoto",
	                    "name": "pic_sysphoto",
	                    "key": "pic_sysphoto事件"
	                },
	                {
	                    "type": "pic_photo_or_album",
	                    "name": "pic_photo_or_album",
	                    "key": "pic_photo_or_album事件"
	                }
	            ]
	        },
	        {
	            "name": "七到八",
	            "sub_button": [
	                {
	                    "type": "pic_weixin",
	                    "name": "pic_weixin",
	                    "key": "pic_weixin事件"
	                },
	                {
	                    "type": "location_select",
	                    "name": "location_select",
	                    "key": "location_select事件"
	                }
	            ]
	        }
	    ]
	}*/
	
	
	public static void main(String[] args){
		String s="";
		try {
			s = "ew3Mx-ufkNl4ZptEb2qS5nVNA2lCwkeNA1816jMB_TLx_-RDS8uOi-ciIopwHVq-3EsgyLvRmjFeOs-tz6xQHgQzu9Nyu-OhPSEzff0V0ri9fMnMpCoZuNzpz0aPUWYiKSHdACAPXM";
			//s = JwTokenAPI.getAccessToken("wxa7e93a3f7bfc8065","4b91436f04c23ec0b5810e1a5be49d34");
//			s = JwTokenAPI.getAccessToken("wx298c4cc7312063df","fbf8cebf983c931bd7c1bee1498f8605");
			System.out.println(s);
//			WeixinButton button = new WeixinButton();
//			CustomWeixinButtonConfig cb = JwMenuAPI.getAllMenuConfigure(s);
	//		System.out.println("aa");
//			for(WeixinButton bb : b){
//				System.out.println(bb.toString());
//			}
//			List<WeixinButton> sub_button = new ArrayList<WeixinButton>();
//			List<WeixinButton> testsUb = new ArrayList<WeixinButton>();
//			WeixinButton w = new WeixinButton();
//			w.setName("测试菜单");
//			testsUb.add(w);
//			
//			WeixinButton w1 = new WeixinButton();
//			/*
//			   "type": "scancode_waitmsg", 
//               "name": "扫码带提示", 
//               "key": "rselfmenu_0_0",
//            */ 
//			w1.setName("测试sub菜单");
//			w1.setKey("rselfmenu_0_0");
//			w1.setType("scancode_waitmsg");
//			sub_button.add(w1);
//			w.setSub_button(sub_button);
//			
//			
//			//s = getMenuButtonJson("button",b);
//			/*Gson gson = new Gson();
//			System.out.println(json);*/
			
			//抽奖
			//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa7e93a3f7bfc8065&redirect_uri=http%3A%2F%2Fherocenter.cn%2Fwx%2Flottery&response_type=code&scope=snsapi_base&state=123#wechat_redirect
			
			//1、click：点击推事件用户点击click类型按钮后，
			//微信服务器会通过消息接口推送消息类型为event的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
			WeixinButton click = new WeixinButton();
			click.setName("位置信息");
			click.setKey("findLocation");
			click.setType("click");
			
			WeixinButton weather = new WeixinButton();
			weather.setName("实时天气");
			weather.setKey("findWeather");
			weather.setType("click");
			
			//2、view：跳转URL用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
			WeixinButton view = new WeixinButton();
			view.setName("抽奖");
			view.setKey("lottery");
			view.setType("view");
			view.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa7e93a3f7bfc8065&redirect_uri=http%3A%2F%2Fherocenter.cn%2Fwx%2Fmarketing%2Flottery&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			
			
			//3、scancode_push：扫码推事件用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
			WeixinButton scancodePush = new WeixinButton();
			scancodePush.setName("scancode_push");
			scancodePush.setKey("scancode_pushEvent事件");
			scancodePush.setType("scancode_push");
			
			//4、scancode_waitmsg：扫码推事件且弹出“消息接收中”提示框用户点击按钮后，微信客户端将调起扫一扫工具，
			//完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
			WeixinButton scancodeWaitmsg = new WeixinButton();
			scancodeWaitmsg.setName("scancode_waitmsg");
			scancodeWaitmsg.setKey("scancode_waitmsg事件");
			scancodeWaitmsg.setType("scancode_waitmsg");
			
			//5、pic_sysphoto：弹出系统拍照发图用户点击按钮后，微信客户端将调起系统相机，
			//完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
			WeixinButton pciSysphoto = new WeixinButton();
			pciSysphoto.setName("pic_sysphoto");
			pciSysphoto.setKey("pic_sysphoto事件");
			pciSysphoto.setType("pic_sysphoto");
			
			//6、pic_photo_or_album：弹出拍照或者相册发图用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
			WeixinButton picPoA = new WeixinButton();
			picPoA.setName("pic_photo_or_album");
			picPoA.setKey("pic_photo_or_album事件");
			picPoA.setType("pic_photo_or_album");
			
			//7、pic_weixin：弹出微信相册发图器用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，
			//将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
			WeixinButton picWeixin = new WeixinButton();
			picWeixin.setName("pic_weixin");
			picWeixin.setKey("pic_weixin事件");
			picWeixin.setType("pic_weixin");
			
			//8、location_select：弹出地理位置选择器用户点击按钮后，微信客户端将调起地理位置选择工具，
			//完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
			WeixinButton locatioSelect = new WeixinButton();
			locatioSelect.setName("location_select");
			locatioSelect.setKey("location_select事件");
			locatioSelect.setType("location_select");
			
			
			
			
			List<WeixinButton> oneTthree = new ArrayList<WeixinButton>();
			oneTthree.add(click);
			oneTthree.add(weather);
			oneTthree.add(view);
			oneTthree.add(scancodePush);
			
			List<WeixinButton> fourTsix = new ArrayList<WeixinButton>();
			fourTsix.add(scancodeWaitmsg);
			fourTsix.add(pciSysphoto);
			fourTsix.add(picPoA);
			
			List<WeixinButton> servenTeight = new ArrayList<WeixinButton>();
			servenTeight.add(picWeixin);
			servenTeight.add(locatioSelect);
			
			
			WeixinButton oneTthreeButton = new WeixinButton();
			oneTthreeButton.setName("功能");
			oneTthreeButton.setSub_button(oneTthree);
			
			WeixinButton fourTsixButton = new WeixinButton();
			fourTsixButton.setName("四到六");
			fourTsixButton.setSub_button(fourTsix);
			
			WeixinButton sevenTeightButton = new WeixinButton();
			sevenTeightButton.setName("七到八");
			sevenTeightButton.setSub_button(servenTeight);
			
			List<WeixinButton> buttonResutl = new ArrayList<WeixinButton>();
			buttonResutl.add(oneTthreeButton);
			buttonResutl.add(fourTsixButton);
			buttonResutl.add(sevenTeightButton);
			
			s= JwMenuAPI.createMenu(s,buttonResutl);
			System.out.println(s);
		} catch (WexinReqException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
