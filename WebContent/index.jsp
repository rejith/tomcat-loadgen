<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%= System.getProperty("title")%></title>
</head>
<body>
	<h1><%= System.getProperty("title")%> <%= System.getProperty("appz.image_version") %>/nano:<%= System.getProperty("nano")%></h1>
	<h2>Application Server: <%= application.getServerInfo().split("/")[0] %><br/> IP Address  : <%@ page import="java.net.*"%>

		<%
		    String ipValue = "error";
           
            try {
                InetAddress inetAddress;
                ipValue = InetAddress.getLocalHost().getHostAddress();
               } catch (UnknownHostException e) {

                e.printStackTrace();
            }
            %><%=ipValue %></h2>		
<pre>System Properties
---------------------------
<%@ page import="java.util.*"%><%
		Properties systemProperties = System.getProperties();
		SortedMap sortedSystemProperties = new TreeMap(systemProperties);
		Set keySet = sortedSystemProperties.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String propertyName = (String) iterator.next();
			String propertyValue = systemProperties.getProperty(propertyName);
			out.println( propertyName + " : "+  propertyValue);
	}		%></pre>
</body>
</html>
