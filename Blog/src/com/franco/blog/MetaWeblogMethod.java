package com.franco.blog;

import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by franco on 13-7-31.
 */
public class MetaWeblogMethod {

	public static final String path = "http://os.blog.163.com/api/xmlrpc/metaweblog/";
	/* XML-PRC 发布文章的 method name */
	public static final String methodNewPost = "metaWeblog.newPost";
	public static final String methodNewEdit = "metaWeblog.editPost";
	public static final String methodGetRecentPosts = "metaWeblog.getRecentPosts";
	public static final String methodGetCategories = "metaWeblog.getCategories";
	public static final String methodDeletePost = "metaWeblog.deletePost";
	public static final String getSupportedMethods = "mt.supportedMethods";

	/* 一组要发送的ＸＭＬ内容 */
	public static String getPostString(String method, String blodId,
			String account, String pwd, String title, String content) {
		String s = "";
		s += "<methodCall>";
		s += "<methodName>" + method + "</methodName>";
		s += "<params>";
		s += "<param><value><string>" + blodId + "</string></value></param>";
		s += "<param><value><string>" + account + "</string></value></param>";
		s += "<param><value><string>" + pwd + "</string></value></param>";
		s += "<param><value><struct>";
		s += "<member><name>title</name>" + "<value><string>" + title
				+ "</string></value></member>";
		s += "<member><name>description</name>" + "<value><string>" + content
				+ "</string></value></member>";
		s += "</struct></value></param>";
		s += "<param><value><boolean>1</boolean></value></param>";
		s += "</params>";
		s += "</methodCall>";
		return s;
	}

	public static String getPostString(String method, String blodId,
			String account, String pwd) {
		String s = "";
		s += "<methodCall>";
		s += "<methodName>" + method + "</methodName>";
		s += "<params>";
		s += "<param><value><string>" + blodId + "</string></value></param>";
		s += "<param><value><string>" + account + "</string></value></param>";
		s += "<param><value><string>" + pwd + "</string></value></param>";
		s += "</params>";
		s += "</methodCall>";
		return s;
	}

	public static String sendPost(String outString) {
		HttpURLConnection conn = null;
		String result = "";
		URL url;
		try {
			url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);// 允许Input，Output
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("Charset", "UTF-8");
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream(), "utf-8");
			out.write(outString);
			out.flush();
			out.close();
			/* 解析返回的XMl内容 */
			result = parseXML(conn.getInputStream());
			conn.disconnect();
		} catch (Exception e) {
			conn.disconnect();
			e.printStackTrace();
			result += e;
		}
		Log.v("franco", "result1 = " + result);
		return result;
	}

	/* 解析Response的XML内容的 */
	public static String parseXML(InputStream is) {
		String result = "";
		Document doc;
		try {
			/* 将XML转换成Document对象 */
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(is);
			doc.getDocumentElement().normalize();
			/* 检查返回值是否有包含fault这个tag，有就代表发布错误 */
			int fault = doc.getElementsByTagName("fault").getLength();
			Log.v("franco", "fault = " + fault);
			if (fault > 0) {
				result += "发表错误！\n";
				/* 取得错误代码 */
				NodeList nList1 = doc.getElementsByTagName("int");
				for (int i = 0; i < nList1.getLength(); i++) {
					String errCode = nList1.item(i).getChildNodes().item(0)
							.getNodeValue();
					result += "错误代码：" + errCode + "\n";
				}
				/* 取得错误信息 */
				NodeList nList2 = doc.getElementsByTagName("string");
				for (int i = 0; i < nList2.getLength(); i++) {
					String errString = nList2.item(i).getChildNodes().item(0)
							.getNodeValue();
					result += "错误信息：" + errString + "\n";
				}
			} else {
				/* 发布成功 */
				NodeList nList = doc.getElementsByTagName("string");
				Log.v("franco", "nList.getLength() = " + nList.getLength());
				for (int i = 0, len = nList.getLength(); i < len; i++) {
					String artId = nList.item(i).getChildNodes().item(0)
							.getNodeValue();
					Log.v("franco", "artId = " + artId);
					result += "Success! \n ID:" + artId;
				}
				if ("".equals(result)) {
					result += "Success!";
				}
			}
		} catch (Exception e) {
			result += e;
		}
		Log.v("franco", "result0 = " + result);
		return result;
	}

}
