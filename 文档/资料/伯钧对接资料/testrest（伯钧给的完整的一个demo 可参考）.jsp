<%@ page language="java" import="nds.rest.*,org.json.*,java.net.*,java.io.*,java.security.MessageDigest" pageEncoding="utf-8"%>
<%@ include file="/html/nds/common/init.jsp" %>
<%@page errorPage="/html/nds/error.jsp"%>
<%!
public String MD5(String s){
	String r="";
	try{
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(s.getBytes());
		byte b[]=md.digest();	
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) { 
			i = b[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
		}
		r=buf.toString();
	}catch(Exception e){	
	}
	return r;
}
%>
<html><body>
<h3>测试REST数据封装格式</h3>
在本页面进行JSON数据格式的测试，您可以查看<a target="_blank" href="rest.demo.txt">各个命令的示例JSON对象格式</a>，可复制到params里运行
<%
	String command=request.getParameter("command");
	String cmdparam=  request.getParameter("params");
	ValueHolder vh= null;
	JSONArray ja=null;
if(	command !=null){
	SimpleDateFormat a=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	a.setLenient(false);
	
	String sipKey="nea@burgeon.com.cn";
	String tt= a.format(new Date());
	//String tt="2012-02-13 11:05:42.000";
	String m=MD5("buyaogaosubieren");
	
	HashMap<String, String> params =new HashMap<String, String>();
	
	params.put("sip_appkey",sipKey);
	params.put("sip_timestamp", tt);

	
	params.put("sip_sign",MD5(sipKey+tt+m));
/*
tranaction - 单个Transaction的内容，不能与transactions 同时存在
transactions -[transaction,...] //多个Transaction, 一个transaction里的多个操作将全部成功，或全部失败，每个Transaction对象的定义见下
transaction:{
	id: <transaction-id> // 通过ID使得客户端能获取transaction的执行情况
	command:"ObjectCreate"|"ObjectModify"|"ObjectDelete"|"ObjectSubmit"|"WebAction"|"ProcessOrder"|"Query"|"Import",//Transaction的操作命令
	params:{ //操作命令的参数
		<command-param>:<command-value>,
		...
	}*/
	JSONObject tra=new JSONObject();
	
	tra.put("id", 112);
	tra.put("command",command);
	tra.put("nds.control.ejb.UserTransaction","Y");
	tra.put("params",  new JSONObject(cmdparam));
	
	ja=new JSONArray();
	ja.put(tra);
	
	params.put("transactions", ja.toString());
	//out.print(buf.toString());
	//out.print(tt);
	vh=RestUtils.sendRequest("http://192.168.1.236:600/servlets/binserv/Rest", params,"POST");
}	
%>
<form name="p" method="post" action="testrest.jsp">
	<table border=0><tr><td>
	command:</td><td><select id="command" name="command"><option value="ObjectCreate">ObjectCreate </option>
		<option value="ObjectModify">ObjectModify </option> 
		<option value="ObjectDelete">ObjectDelete </option> 
		<option value="ObjectSubmit">ObjectSubmit </option> 
		<option value="ProcessOrder">ProcessOrder </option>
		<option value="ExecuteWebAction">ExecuteWebAction </option> 
		<option value="Import">Import </option> 
		<option value="Query">Query </option> 
		<option value="GetObject">GetObject </option> 
	</select> </td></tr>
<td>params<br>(json format):</td><td><textarea name="params" rows="12" cols="80"><%=(cmdparam==null?"":cmdparam)%></textarea></td></tr>
<tr><td>&nbsp;</td><td>
<input type="submit" value="Submit"/></td></tr>
</table>	
</form>	
<script>
function sel(ele, val) {
  var found  = false;
  var i;
  var j;
  for (i = 0; i < ele.options.length; i++) {
    ele.options[i].selected = false;
    if (ele.options[i].value == val) {
        ele.options[i].selected = true;
    }
  }
}  

sel(document.getElementById("command"),"<%=(command==null?"ProcessOrder":command)%>");
</script>
<p>
<%if(vh!=null){%>
<h3>运行结果</h3>
vh:<%=vh%>
return code:<%=vh.get("code")%> <p>
return message:<%=vh.get("message")%><p>
transactions:<%=ja.toString() %><p>
query:<%=vh.get("queryString")%><p>
<%}%> 
</body></html>