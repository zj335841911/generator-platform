package com.qdkj.firstpro.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tool {

	/**
	 * 获取访问的域名
	 * @return
	 */
	public static String getDomain(){
		return (((HttpServletRequest)getRequest_Response_Session()[0]).getServerName()+":"+((HttpServletRequest)getRequest_Response_Session()[0]).getServerPort());
	}
	/**
	 * 获取带http或https等开头的完整访问的域名
	 * @return
	 */
	public static String getHttpDomain(){
		StringBuffer url=((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURL();
		return url.delete(url.length() - ((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURI().length(), url.length()).append("/").toString();
	}
	/**
	 * 辅助方法:判断字符串是否为空字符串或空
	 * @param string
	 * @return
	 */
	public static boolean isNull(Object string){
		return string==null||"".equals(string.toString().trim())||"null".equals(string.toString().trim());
	}
	/**
	 * 判断map里面是否有这个key,并且这key是否不为null,两者有一个否,就返回false
	 * @param m
	 * @param k
	 * @return
	 */
	public static boolean mapGetKeyNotEmpty(Map<String, Object> m,String k){
		return m.containsKey(k)&&m.get(k)!=null&&(m.get(k) instanceof String?m.get(k).toString().trim()!=""||m.get(k).toString().trim()!="null":true);
	}
	/**
	 * 辅助方法:判断集合是否为空
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> boolean listIsNull(List<T>list){return(list==null||list.isEmpty()||list.size()==0||(list.size()==1&&list.get(0)==null));}
	/**
	 * HttpServletRequest从上下文中获取,HttpServletResponse和HttpSession从HttpServletRequest获取
	 * @return Object{HttpServletRequest,HttpServletResponse,HttpSession}
	 */
	public static final Object[] getRequest_Response_Session(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		List<Object>req_res_session=new ArrayList<>();
		if(sra!=null){
			req_res_session.add(sra.getRequest());
			ServletWebRequest servletWebRequest = new ServletWebRequest((HttpServletRequest)req_res_session.get(0));
			req_res_session.add(servletWebRequest.getResponse());
			req_res_session.add(((HttpServletRequest)req_res_session.get(0)).getSession());
			return req_res_session.toArray();
		}else{
			return null;
		}
	}

	/**
	 * 根据需要排序的字段数字,按顺序降序排列
	 * @param list
	 * @param keys
	 * @return
	 */
	public static final List<Map<String, Object>> ListMapOrderByMapKeyDesc(List<Map<String, Object>> list,final String [] keys){
	    Collections.sort(list,new Comparator<Map>() {
	          public int compare(Map o1, Map o2) {
	               return recursion(o1, o2, 0);
	          }
	          private int recursion(Map o1, Map o2, int i) {
	               if (o1.containsKey(keys[i]) && o2.containsKey(keys[i])) {
	                     Object value1 = o1.get(keys[i]);
	                     Object value2 = o2.get(keys[i]);
	                     if (value1 == null && value2 == null) {
	                          if ((i+1) < keys.length) {
	                                int recursion = recursion(o1, o2, i+1);
	                                return recursion;
	                          }else{
	                                return 0;
	                          }
	                     }else if(value1 == null && value2 != null){
	                          return 1;
	                     }else if(value1 != null && value2 == null){
	                          return -1;
	                     }else{
	                          if (value1.equals(value2)) {
	                                if ((i+1) < keys.length) {
	                                     return recursion(o1, o2, i+1);
	                                }else{
	                                     return 0;
	                                }
	                          }else{
	                                if (value1 instanceof String && value2 instanceof String) {
	                                     return value2.toString().compareTo(value1.toString());
	                                }else if(value1 instanceof Timestamp && value2 instanceof Timestamp){
	                                	return ((Timestamp)(value2)).compareTo(new Date(((Timestamp)(value1)).getTime()));
	                                }else{
	                                     return new BigDecimal(value2.toString()).compareTo(new BigDecimal(value1.toString()));
	                                }
	                          }
	                     }
	               }else{
	                     System.out.println(" ** The current map do not containskey : " + keys[i] + ",or The value of key is null **");
	                     return 0;
	               }
	          }
	    });
	    return list;
	}

	/**
	 * 根据keys数组来移除map里对应的key和value
	 * @param map
	 * @param keys
	 */
	public static  void removeMapParmeByKey(Map map,String[]keys){
		for (String key : keys) {
			if(map.containsKey(key))map.remove(key);
		}
	}
	/**
	 * 随机生成指定位数验证码
	 * @return
	 */
	public static String getRandomNum(int count){
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for(int i=0;i<count;i++){
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num)+""), "");
		}
		return sb.toString();
	}
	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now;
		now = new Date();
		Date date=new Date();
        try {
            date=df.parse(StrDate);
        } catch (ParseException e) {
            return "未知";
        }
        long times = now.getTime()-date.getTime();
		long day  =  times/(24*60*60*1000);
		long hour = (times/(60*60*1000)-day*24);
		long min  = ((times/(60*1000))-day*24*60-hour*60);
		long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

		StringBuffer sb = new StringBuffer();
		//sb.append("发表于：");
		if(day>0 ){
			sb.append(day+"天前");
		}else if(hour>0 ){
			sb.append(hour+"小时前");
		} else if(min>0){
			sb.append(min+"分钟前");
		} else{
			sb.append(sec+"秒前");
		}
		resultTimes = sb.toString();
		return resultTimes;
	}

	/**
	 * 将数字转换成字符串,并且处理10000开始后面四位数替换成字符W
	 * @param i
	 * @param substring_point_after_length 小数点后保留位数,最多支持4位,超过无效
	 * @return
	 */
	public static String IntegerToString(Integer i,int substring_point_after_length){
		if(isNull(i)){
			return "0";
		}else if(i>9999){
			return String.valueOf(i).substring(0,String.valueOf(i).length()-4)+(substring_point_after_length>0&&substring_point_after_length<5?("."+String.valueOf(i).substring(String.valueOf(i).length()-4,4+substring_point_after_length)):"")+"W";
		}else{
			return String.valueOf(i);
		}
	}
	/**
	 * XML格式字符串转换为Map
	 *
	 * @param strXML XML字符串
	 * @return XML数据转换后的Map
	 * @throws Exception
	 */
	public static Map<String, Object> xmlToMap(String strXML) throws Exception {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(),  element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception ex) {
			getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
			throw ex;
		}

	}
	/**
	 * 日志
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}

	/**
	 * @param after_day 多少天之后
	 * @return
	 */
	public static Date getFutrueTime(int after_day){
		Date now = new Date();
		Long time =after_day * 24 * 60 * 60 * 1000L;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
		Long end = now.getTime() + time;
		Date date = new Date(end);
		return date;
	}

	public static Map<String,Object>getHaveSignatureMap(String jsapi_ticket,String url){
		if(isNull(jsapi_ticket)||isNull(url))return null;
		Map<String,Object>result=new TreeMap<>();
		String noncestr=UuidUtil.get32UUID().substring(0,16),timestamp=String.valueOf(System.currentTimeMillis()).substring(0,10);
		result.put("jsapi_ticket",jsapi_ticket);
		result.put("noncestr",noncestr);
		result.put("timestamp",timestamp);
		result.put("url",url);
		String parame=("jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url);
		result.put("signature",new SHA1().getDigestOfString(parame.getBytes()).toLowerCase());
		return result;
	}

	public static Map<String,Object>getUrlRequestParame(){
		Map<String,Object>result=new HashMap<>();
		Map<String,Object>parameMap=new HashMap<>();
		HttpServletRequest request=((HttpServletRequest)getRequest_Response_Session()[0]);
		StringBuffer buffer=new StringBuffer("?");
		List<String>parame=new ArrayList<>();
		Enumeration enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName=(String)enu.nextElement();
			parameMap.put(paraName,request.getParameter(paraName));
			parame.add((paraName+"="+request.getParameter(paraName)));
		}
		result.put("parameMap",parameMap);
		result.put("parameString",("?"+ StringUtils.join(parame,"&")));
		return result;
	}

//	public static void putWX_config(Model model, SettingService settingService) {
//		Setting setting=settingService.getById(1);
//		Map<String,Object> wx_config=getHaveSignatureMap(setting.getWechatTicket(),(((HttpServletRequest)getRequest_Response_Session()[0]).getRequestURL()+getUrlRequestParame().get("parameString").toString()));
//		wx_config.put("appid",setting.getWechatAppId());
//		model.addAttribute("wx_config",wx_config);
//	}


}
