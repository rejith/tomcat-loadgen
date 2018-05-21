<html>
    <head>
        <title><%=System.getProperty("title")%></title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta charset="utf-8">
        <style>
          * {
              box-sizing: border-box;
              font-family: verdana;
          }

          label {
              padding: 1px 1px 1px 0;
              display: inline-block;
          }

          .column {
              float: left;
              width: 25%;
              padding: 10px;
              height: 50px; /* Should be removed. Only for demonstration */
          }

          .column-mid {
              background-color:#3377ff;
              float: left;
              width: 25%;
              padding: 10px;
              height: 50px; /* Should be removed. Only for demonstration */
          }

          .column-mid-white {
              background-color:#3377ff;
              color: white;
              float: left;
              width: 25%;
              padding: 10px;
              height: 50px; /* Should be removed. Only for demonstration */
          }

          .column-50 {
              background-color:#f1f1f1;
              float: left;
              width: 50%;
              padding: 10px;
              height: 50px; /* Should be removed. Only for demonstration */
          }

          .column-50-blue {
              background-color:#3377e0;
              float: left;
              width: 50%;
              padding: 10px;
              height: 50px; /* Should be removed. Only for demonstration */
          }

          .text-bk {
              background-color:#3377ff;
          }

          /* Clear floats after the columns */
          .row:after {
              content: "";
              display: table;
              clear: both;
          }

          .responsive {
              width: 100%;
              max-width: 250px;
              height: auto;
          }

        </style>

    </head>
    <body>
            <div class="row">
                <div>
                    <h1 style="color:#DAA520;"><center>APP-Z DEMO</center></h1>
                    <p><center>Fully Automated Hybrid Cloud Platform</center></p>
                </div>
            </div>

            <div class="row">
                <div class="column">
                </div>

                <div class="column-mid-white">
                        <label for="app"><%=System.getProperty("title")%></label>
                </div>

                <div class="column-mid-white" style="text-align:right">
                        <label for="ver">VERSION: <%= System.getProperty("appz.image_version")%></label>
                </div>

                <div class="column">
                </div>
            </div>

            <div class="row">
                <div class="column" style="height:100px">
                </div>

                <div class="column-50" style="text-align:center; height:100px">
                    <%@ page import="java.io.*,java.util.*"%>
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
                        String CT = (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute + ":" +  (second < 10 ? "0" : "") + second + " " + am_pm;
                     %>
                        <label for="time"><h1><%=CT %></h1></label>
                </div>

                <div class="column" style="height:100px">
                </div>
            </div>

            <div class="row">
                <div class="column">
                </div>

                <div class="column-mid-white">
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
                        <label for="ip">IP: <%=ipValue %></label>
                </div>

                <div class="column-mid-white" style="text-align:right">
                        <label for="nano">NANO: <%= System.getProperty("nano")%></label>
                </div>

                <div class="column">
                </div>
            </div>

            <div class="row">
                <div class="column">
                </div>
                <div class="column-50" style="text-align:center; height:125px">
                    <h3><center>Application Server</center></h3>
                    <h2 style="color:#3377ff;"><center><%= application.getServerInfo().indexOf('$') > 0 ? application.getServerInfo().split("/")[0] : application.getServerInfo() %></center></h2>
                </div>
                <div class="column">
                </div>
            </div>

            <div class="row">

                <div>
                    <center><img src="images/logo.png" alt="CLOUDBOURNE" class="responsive" width="90px" style="margin:10px"></center>
                </div>
            </div>

    </body>
</html>
