package com.wechart.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.jeewx.api.mp.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wechart.cofig.ThirdAPISettings;
import com.wechart.cofig.WXSettings;
import com.wechart.model.Article;
import com.wechart.model.LatLng;
import com.wechart.model.NewsMessage;
import com.wechart.model.TextMessage;
import com.wechart.model.location.LocationModel;
import com.wechart.model.weuser.WeUserModel;
import com.wechart.req.gaode.GaoDeAPI;
import com.wechart.service.location.LocationService;
import com.wechart.service.weuser.WeUserService;
import com.wechart.util.CoordinateUtil;
import com.wechart.util.MessageUtil;
import com.wechart.util.SignUtil;

@Service
public class WXService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WXSettings wxSettings;
	@Autowired
	private ThirdAPISettings thirdAPIsettings;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private WeUserService weUserService;

	/**
	 * 服务器之间的认证使用
	 * 
	 * @return
	 */
	public String signGet() {

		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String signature = request.getParameter("signature");
		String echostr = request.getParameter("echostr");

		logger.info("url： " + request.getRequestURL());
		logger.info("parm： " + request.getQueryString());
		logger.info("nonce： " + nonce);
		logger.info("timestamp： " + timestamp);
		logger.info("signature： " + signature);
		logger.info("echostr： " + echostr);

		if (SignUtil.checkSignature(wxSettings.getToken(), signature, timestamp, nonce)) {
			logger.info("auth success");
		} else {
			logger.error("auth fail!");
			echostr = "error";
		}
		return echostr;
	}

	/**
	 * 处理微信发来的请求（包括事件的推送）
	 *
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String processRequest() {

		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			
			
			//获取url参数 判断是否是加密模式
	    	boolean isEncrypt = false;
	    	String encryptType = request.getParameter("encrypt_type");
	    	if("aes".equals(encryptType)){
	    		logger.info("采用消息加密-安全模式");
	    		isEncrypt = true;
	    	}
			
			// xml请求解析
			WXBizMsgCrypt pc = new WXBizMsgCrypt(wxSettings.getToken(), wxSettings.getEncodingAESKey(), wxSettings.getAppId());
			Map<String, String> requestMap = MessageUtil.parseXml(request,isEncrypt,pc);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);

			List<Article> articleList = new ArrayList<Article>();
			// 接收文本消息内容
			String content = requestMap.get("Content");
			// 自动回复文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				// 如果用户发送表情，则回复同样表情。
				if (isQqFace(content)) {
					respContent = content;
					textMessage.setContent(respContent);
					// 将文本消息对象转换成xml字符串
					respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
				} else {
					// 回复固定消息
					switch (content) {

					case "1": {
						StringBuffer buffer = new StringBuffer();
						buffer.append("您好，我是小8，请回复数字选择服务：").append("\n\n");
						buffer.append("11 可查看测试单图文").append("\n");
						buffer.append("12  可测试多图文发送").append("\n");
						buffer.append("13  可测试网址").append("\n");

						buffer.append("或者您可以尝试发送表情").append("\n\n");
						buffer.append("回复“1”显示此帮助菜单").append("\n");
						respContent = String.valueOf(buffer);
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						break;
					}
					case "11": {
						// 测试单图文回复
						Article article = new Article();
						article.setTitle("微信公众帐号开发教程Java版");
						// 图文消息中可以使用QQ表情、符号表情
						article.setDescription("这是测试有没有换行\n\n如果有空行就代表换行成功\n\n点击图文可以跳转到百度首页");
						// 将图片置为空
						article.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
						article.setUrl("http://www.baidu.com");
						articleList.add(article);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage,isEncrypt,pc);
						break;
					}
					case "12": {
						// 多图文发送
						Article article1 = new Article();
						article1.setTitle("紧急通知，不要捡这种钱！泰兴都已经传疯了！\n");
						article1.setDescription("");
						article1.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
						article1.setUrl(
								"http://mp.weixin.qq.com/s?__biz=MjM5Njc2OTI4NQ==&mid=2650924309&idx=1&sn=8bb6ae54d6396c1faa9182a96f30b225&chksm=bd117e7f8a66f769dc886d38ca2d4e4e675c55e6a5e01e768b383f5859e09384e485da7bed98&scene=4#wechat_redirect");

						Article article2 = new Article();
						article2.setTitle("湛江谁有这种女儿，请给我来一打！");
						article2.setDescription("");
						article2.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
						article2.setUrl(
								"http://mp.weixin.qq.com/s?__biz=MjM5Njc2OTI4NQ==&mid=2650924309&idx=2&sn=d7ffc840c7e6d91b0a1c886b16797ee9&chksm=bd117e7f8a66f7698d094c2771a1114853b97dab9c172897c3f9f982eacb6619fba5e6675ea3&scene=4#wechat_redirect");

						Article article3 = new Article();
						article3.setTitle("以上图片我就随意放了");
						article3.setDescription("");
						article3.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
						article3.setUrl(
								"http://mp.weixin.qq.com/s?__biz=MjM5Njc2OTI4NQ==&mid=2650924309&idx=3&sn=63e13fe558ff0d564c0da313b7bdfce0&chksm=bd117e7f8a66f7693a26853dc65c3e9ef9495235ef6ed6c7796f1b63abf1df599aaf9b33aafa&scene=4#wechat_redirect");

						articleList.add(article1);
						articleList.add(article2);
						articleList.add(article3);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage,isEncrypt,pc);
						break;
					}

					case "13": {
						// 测试网址回复
						respContent = "<a href=\"http://www.baidu.com\">百度主页</a>";
						textMessage.setContent(respContent);
						// 将文本消息对象转换成xml字符串
						respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						break;
					}

					default: {
						respContent = "（这是里面的）很抱歉，现在小8暂时无法提供此功能给您使用。\n\n回复“1”显示帮助信息";
						textMessage.setContent(respContent);
						// 将文本消息对象转换成xml字符串
						respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
					}
					}
				}
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
				textMessage.setContent(respContent);
				// 将文本消息对象转换成xml字符串
				respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
				textMessage.setContent(respContent);
				// 将文本消息对象转换成xml字符串
				respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
				textMessage.setContent(respContent);
				// 将文本消息对象转换成xml字符串
				respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
				textMessage.setContent(respContent);
				// 将文本消息对象转换成xml字符串
				respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
			}
			// 事件推送
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
				String event = requestMap.get("Event");
				//订阅
				if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(event)){
					logger.info("用户:" + fromUserName + "订阅公众号");
					respContent = "欢迎订阅本公众号,更多功能开发中...如果你有好的想法或者诉求\n请联系邮箱:herocenter@163.com";
					textMessage.setContent(respContent);
					// 将文本消息对象转换成xml字符串
					respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
					//将用户添加到数据库，赠送两次抽奖机会
					if(null == weUserService.getWeUserByOpenid(fromUserName) ){
					    WeUserModel record = new WeUserModel();
	                    record.setOpenid(fromUserName);
	                    record.setLotterycount(5);
	                    record.setCreatetime(new Timestamp(System.currentTimeMillis()));
	                    record.setModifytime(new Timestamp(System.currentTimeMillis()));
	                    weUserService.addWeUser(record);
	                    logger.info("用户:" + fromUserName + "插入数据库成功!");
					}
					
					
				}
				//取消订阅
				if(MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equalsIgnoreCase(event)){
					logger.info("用户:" + fromUserName + "取消订阅公众号!");
				}
				//地理位置
				if(MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equalsIgnoreCase(event)){
					String latitude = requestMap.get("Latitude");
					String longitude = requestMap.get("Longitude");
					String precision = requestMap.get("Precision");
					logger.info("获取到GPS经纬度信息，进行转换火星坐标");
					LatLng latLng = new LatLng();
					latLng.setLatitude(Double.valueOf(latitude));
					latLng.setLongitude(Double.valueOf(longitude));
					latLng = CoordinateUtil.transformFromWGSToGCJ(latLng);
					logger.info("火星坐标转换完毕");
					logger.info("调用高德API查询地理位置和aocode");
					Map<String,String> res = GaoDeAPI.getInfoByGDLatLng(thirdAPIsettings.getGaodeKey(),thirdAPIsettings.getGaodeoutPutType(),
							latLng.getLongitude()+","+latLng.getLatitude(),thirdAPIsettings.getGaodeExtensions());
					//存入到数据库
					LocationModel locationModel = locationService.findLocationByWxId(fromUserName);
					LocationModel record = new LocationModel();
					record.setWxId(fromUserName);
					record.setLat(latitude);
					record.setLng(longitude);
					record.setPrecision(precision);
					record.setAddress(res.get("formatted_address"));
					record.setAdcode(res.get("adcode"));
					if(null == locationModel){
						locationService.addWXLocation(record);
					}else{
						locationService.modifyWXLocation(record);
					}
					logger.info("获取到经纬度信息，存库完毕");
				}
				//单击事件 
				if(MessageUtil.EVENT_TYPE_CLICK.equalsIgnoreCase(event)){
					String eventKey = requestMap.get("EventKey");
					//获取地理位置
					if("findLocation".equals(eventKey)){
						LocationModel locationModel = locationService.findLocationByWxId(fromUserName);
						if(null == locationModel){
							//未找到定位消息
							logger.info(fromUserName + ":未找到地理位置信息，提示用户允许定位");
							respContent = "远方的你,请允许获取地理位置,不然查询不到地址哦~";
							textMessage.setContent(respContent);
							// 将文本消息对象转换成xml字符串
							respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						}else{
							logger.info("经纬度转换为地址信息");
							respContent = "位置：" + locationModel.getAddress();
							textMessage.setContent(respContent);
							// 将文本消息对象转换成xml字符串
							respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						
						}
					}
					
					//获取天气信息
					if("findWeather".equals(eventKey)){
						LocationModel locationModel = locationService.findLocationByWxId(fromUserName);
						if(null == locationModel){
							//未找到定位消息
							logger.info(fromUserName + ":未找到地理位置信息，提示用户允许定位");
							respContent = "远方的你,请允许获取地理位置,不然查询不到所在地天气哦~";
							textMessage.setContent(respContent);
							// 将文本消息对象转换成xml字符串
							respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						}else{
							logger.info("开始查询天气信息");
							Map<String,String> res = GaoDeAPI.getWeatherByAdcode(thirdAPIsettings.getGaodeKey(), thirdAPIsettings.getGaodeoutPutType(),
									locationModel.getAdcode(), thirdAPIsettings.getGaodeExtensions());
							
							respContent = "天气:" +  res.get("weather") + "\n" +
										  "气温:" +  res.get("temperature") + "℃" + "\n" +
										  "风向:" +  res.get("winddirection") + "风" + "\n" +
										  "级别:" +  res.get("windpower") + "级" + "\n" +
										  "更新时间:" +  res.get("reporttime");
							textMessage.setContent(respContent);
							// 将文本消息对象转换成xml字符串
							respMessage = MessageUtil.textMessageToXml(textMessage,isEncrypt,pc);
						
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return respMessage;
	}

	/**
	 * 判断是否是QQ表情
	 *
	 * @param content
	 * @return
	 */
	public static boolean isQqFace(String content) {
		boolean result = false;

		// 判断QQ表情的正则表达式
		String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher m = p.matcher(content);
		if (m.matches()) {
			result = true;
		}
		return result;
	}
}
