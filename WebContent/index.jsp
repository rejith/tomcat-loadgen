<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tocat-loadgen Application</title>
</head>
<body>
	<h1>Welcome to Tomcat LoadGen Application</h1>
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
	<h3>System Properties</h3>
	<table border=0>
	<tr>
		<td><pre>java.version</pre></td><td><pre>:</pre></td><td><pre><%= System.getProperty("java.version")%></pre></td>
	</tr>
	<tr>
		<td><pre>java.home</pre></td><td><pre>:</pre></td><td><pre><%= System.getProperty("java.home")%></pre></td>
	</tr>
	</table>
</body>
</html>
