JEEWX-API  ΢�ż���SDK
===============

���°汾�� 1.1���������ڣ�20170301��

ժҪ��
-----------------------------------
 JEEWX-API �ǵ�һ��΢�Ź���ƽ̨Java�漫��SDK������ jeewx-api ������������ӵ�м����õ�API���ÿ��������������磬��ʡ����ʱ��


JEEWX-API�ӿ�WIKI�ĵ�
-----------------------------------
* [�ӿ�WIKI](http://wiki.jeecg.org/pages/viewpage.action?pageId=7110659)




Jeewx-api ���ɷ���
-----------------------------------
###  [1].maven ��ʽ
        ��pom.xml ���jeewx-api 1.1-SNAPSHOT����
    <dependency>  
		<groupId>org.jeewx</groupId>  
		<artifactId>jeewx-api</artifactId>  
		<version>1.1-SNAPSHOT</version>  
	</dependency>

###  [2].��maven��ʽ
         ֱ�ӿ���jeewx-api-1.1-SNAPSHOT.jar����ĿLib��


����ʾ��
-----------------------------------
    public static void main(String[] args) {
		try {
			String accesstoken = "yALYWcUbB1hdURQvJ-Qn1jbyq5E0qNraZixnxhC1wtN5sKrAfHifyFHHpRWiUnZ1xnhjN_dcnYqFAgpJqeJJybx2NOVoEDZd7SFLjwFIvM8AJv3a8EGarbY0jo--4vuqUNNhADAQJJ";
			String user_openid = "o8QKAuAyDxxfyuBZ9ugSMR4SR5XQ";
			JwUserAPI.getWxuser(accesstoken, user_openid);
		} catch (WexinReqException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    

��������
-----------------------------------
* 	QQ����Ⱥ�� ��176031980����289782002