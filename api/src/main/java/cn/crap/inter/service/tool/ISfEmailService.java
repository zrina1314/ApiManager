package cn.crap.inter.service.tool;

public interface ISfEmailService {

	void sendAddArticleEmail(String title, String author, String url, String content) ;

	void sendUpdateArticleEmail(String title, String author, String url, String content) ;

	void sendJenkinsEmail(String jobName, String buildStatus,String buildUrl, String uatQrImg, String uatApk,String productQrImg,String productApk,String changesSinceLastSuccess) ;
}