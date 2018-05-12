<%@ page import="java.io.*,java.util.*"%><%
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
    out.println(CT);
%>