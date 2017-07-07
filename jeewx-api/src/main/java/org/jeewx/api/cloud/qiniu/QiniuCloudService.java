package org.jeewx.api.cloud.qiniu;

import org.apache.log4j.Logger;
import org.jeewx.api.cloud.qiniu.model.QiniuCloud;


public class QiniuCloudService {
	
	private static Logger logger = Logger.getLogger(QiniuCloudService.class);
	
	//七牛云 默认域名  根据 bucket 和默认域名改变决定
	private static String url = "http://7u2th8.com1.z0.glb.clouddn.com/";
	
	public static void main(String[] args) throws Exception  {
		String key = "test111.jpg";
		String localFile = "E:\\logo.jpg";
		String ACCESS_KEY = "UENgD6Fb9NepoNnkAe35h2SRFGlWQPF_q_iMRhno";
		String SECRET_KEY = "ujSBYMyPPyMagtxS4JUpZwQPlUTVrl2aeY7O_jqx";
		QiniuCloud qc = new QiniuCloud();
		qc.setKey(key);
		qc.setLocalFile(localFile);
		qc.setAk(ACCESS_KEY);
		qc.setSk(SECRET_KEY);
		//uploadFile(qc);
		
		System.out.println(getImageUrl(qc));
	} 
	
	/**
	 * 向七牛云服务器 上传资料
	 */
/*	public static PutRet uploadFile(QiniuCloud qc) throws Exception {
		Mac mac = new Mac(qc.getAk(), qc.getSk());
		// 请确保该bucket已经存在
		String bucketName = "jeewximage";
		PutPolicy putPolicy = new PutPolicy(qc.getBucket());
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		PutRet ret = IoApi.putFile(uptoken, qc.getKey(), qc.getLocalFile(), extra);
		return ret;
	}*/
	
	/**
	 * 获得上传的资源的外链接
	 * @return 
	 */
	public static String getImageUrl(QiniuCloud qc) throws Exception {
		return url+qc.getKey();
	}
}