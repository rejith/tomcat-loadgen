<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tomcat-loadgen Application</title>
</head>
<body>
	<h1>Welcome to <%= System.getProperty("title")%>   <%= System.getProperty("nano")%></h1>
	<h2>
		IP Address  : 

		<%@ page import="java.net.*"%>

		<%
		    String ipValue = "error";
           
            try {
                InetAddress inetAddress;
                ipValue = InetAddress.getLocalHost().getHostAddress();
               } catch (UnknownHostException e) {

                e.printStackTrace();
            }
            %>
		<%=ipValue %>
	</h2>
	
	<h3>
		System Properties
		</br>
		<pre>
		 <%@ page import="java.util.*"%>
	<%
		Properties systemProperties = System.getProperties();
		out.println();
		SortedMap sortedSystemProperties = new TreeMap(systemProperties);
		Set keySet = sortedSystemProperties.keySet();
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String propertyName = (String) iterator.next();
			String propertyValue = systemProperties.getProperty(propertyName);
			out.println( propertyName + " : "+  propertyValue);
	}
		%>
	</pre>

	</h3>

</body>
</html>
