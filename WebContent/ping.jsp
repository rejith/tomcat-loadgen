<%@ page import="java.io.*,java.util.*,java.net.*"%>
<%
    // Set refresh, autoload time as 1 seconds
    response.setIntHeader("Refresh", 1);

    // Get current time
    Calendar calendar = new GregorianCalendar();
    String am_pm;

    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    if (calendar.get(Calendar.AM_PM) == 0)
		am_pm = "AM";
    else
		am_pm = "PM";
    String CT = (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "")
		    + second + " " + am_pm;

    String ipValue = "error";

    try
    {
		InetAddress inetAddress;
		ipValue = InetAddress.getLocalHost().getHostAddress();
    }
    catch (UnknownHostException e)
    {

		e.printStackTrace();
    }
    
    out.println("---------------------------------------------------");
    out.println(CT);
    out.println(System.getProperty("title"));
    out.println(System.getProperty("appz.image_version"));
    out.println("nano: " + System.getProperty("nano"));
    out.println("IP: " + ipValue);
    out.println("---------------------------------------------------");
%>