package com.wechart.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jeewx.api.mp.aes.AesException;
import org.jeewx.api.mp.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wechart.cofig.WXSettings;
import com.wechart.model.Article;
import com.wechart.model.NewsMessage;
import com.wechart.model.TextMessage;

public class MessageUtil {


	private final static Logger logger = LoggerFactory.getLogger(MessageUtil.class);
	
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)and 未关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：已关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SCAN="SCAN";
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    /**
     * 事件类型：VIEW(点击自定义菜单跳转链接时的事件)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";

    /**
     * 事件类型：transfer_customer_service(把消息推送给客服)
     */
    public static final String EVENT_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";


    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @param isEncrypt 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    //屏蔽某些编译时的警告信息(在强制类型转换的时候编译器会给出警告)
    public static Map<String, String> parseXml(HttpServletRequest request, boolean isEncrypt,WXBizMsgCrypt pc) throws Exception {
    	
    	
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        
        //输出xml内容 
        StringBuffer sb = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		String xml = sb.toString();	//次即为接收到微信端发送过来的xml数据
		logger.info("原xml content : " + xml);
		
		if(isEncrypt){
			//解密
			String nonce = request.getParameter("nonce");  
	        String timestamp = request.getParameter("timestamp");  
	        String msgSignature = request.getParameter("msg_signature"); 
	        
			logger.info("----------------------------parm-------------------------------");
			logger.info("msgSignature:" + msgSignature);
			logger.info("timestamp:" + timestamp);
			logger.info("nonce:" + nonce);
			logger.info("----------------------------parm-------------------------------");
			xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);  
			logger.info("解密 xml content : " + xml);
		}
		
		//xml = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[123]]></FromUserName><CreateTime>123456789</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[LOCATION]]></Event><Latitude>23.137466</Latitude><Longitude>113.352425</Longitude><Precision>119.385040</Precision></xml>";
		
        Document document = DocumentHelper.parseText(xml);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     * @throws AesException 
     */
    public static String textMessageToXml(TextMessage textMessage,boolean isEncrypt,WXBizMsgCrypt pc) throws AesException {
        xstream.alias("xml", textMessage.getClass());
        String respMessage = xstream.toXML(textMessage);
        logger.info("发送给用户：" + respMessage);
        if(isEncrypt){
			respMessage = pc.encryptMsg(respMessage, String.valueOf(System.currentTimeMillis()), "hero");
			logger.info("发送给用户加密后：" + respMessage);
        }
        return respMessage;
    }


    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     * @throws AesException 
     */
    public static String newsMessageToXml(NewsMessage newsMessage,boolean isEncrypt,WXBizMsgCrypt pc) throws AesException {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        String respMessage = xstream.toXML(newsMessage);
        logger.info("发送给用户：" + respMessage);
        if(isEncrypt){
			respMessage = pc.encryptMsg(respMessage, String.valueOf(System.currentTimeMillis()), "hero");
			logger.info("发送给用户加密后：" + respMessage);
        }
        return respMessage;
    }

    /**
     * 扩展xstream，使其支持CDATA块
     *
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}
