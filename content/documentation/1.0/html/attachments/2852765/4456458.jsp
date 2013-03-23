<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Atom Publishing Protocol (APP) Server using Apache Abdera</title>
<STYLE type="text/css">
body {
	font-family: "Lucida Grande", "Lucida Sans Unicode", sans-serif;
	font-size: small;
	background-color: #A6D2FF;
	text-align: center;
}

h1,h2,h3,h4 {
	font-family: palatino, georgia, serif;
	margin-bottom: 0px;
	text-align: left;
	color: darkblue;
	font-weight: normal;
}

h4 {
	font-family: "Lucida Grande", "Lucida Sans Unicode", sans-serif;
	font-size: 8pt;
	text-transform: uppercase;
	font-weight: bold;
}

p {
	margin-top: 0px;
}

#h3-content {
	font-variant: small-caps;
	font-size: small;
}

#page-container {
	background-color: white;
	border-color: black;
	width: 725px;
	border-color: black;
	position: relative;
	margin: 0pt auto;
}

#main-content {
	padding-left: 25px;
	padding-right: 25px;
	text-align: left;
	float: left;
	position: relative;
	background-color: white;
}

#left-content {
	padding: 0px;
	text-align: left;
	float: left;
	position: relative;
	background-color: white;
}

#links-bar {
	text-align: center;
	font-family: palatino, georgia, serif;
	color: gray;
	background-color: transparent;
	font-variant: small-caps;
	padding: 5px;
}

li {
	padding-bottom: 5px;
	list-style-image: none;
	list-style-position: outside;
	list-style-type: none;
	font-size: 9pt;
}
</STYLE>
</head>
<body>
<%
    /*******************************************************************************
     * Licensed Materials - Property of IBM
     * (c) Copyright IBM Corporation 2006. All Rights Reserved.
     * 
     * Note to U.S. Government Users Restricted Rights:
     * Use, duplication or disclosure restricted by GSA ADP Schedule
     * Contract with IBM Corp. 
     *******************************************************************************/
    String serviceDocument = "blog";
    String feedDocument = "blog/entries";
    String specificEntry = "blog/entries/rail";
    String IBM_document =
        "http://publib.boulder.ibm.com/infocenter/wasinfo/v6r1/index.jsp?topic=/com.ibm.ajax.feed.samples.help/docs/GettingStarted_useage.html";
%>

<DIV id="page-container">
<DIV id="main-content">
<DIV id="left-content">
<h3>ATOM Publishing Protocol support</h3>
<p id="h3-content">Demonstrates the ability to create, retrieve,
update, and delete ATOM content, using the ATOM Publishing Protocol
(APP) support in Apache Abdera.</p>
<h4>Read the service document</h4>
<p id="h4-content">The service document describes the available
content on the APP Server - currently available feeds, and the URL at
which they can be accessed. Click <a href="<%=serviceDocument%>"><%=serviceDocument%></a>
</p>
<h4>Read the available feed</h4>
<p id="h4-content">View the feed, and use this operation to check on
the contents of the feed following a create, update, and delete of
specific entries in the feed. Click <a href="<%=feedDocument%>"><%=feedDocument%></a>
</p>
<h4>Read a specific entry in the feed</h4>
<p id="h4-content">An operation to view the discrete entries that
compose a feed. Click <a href="<%=specificEntry%>"><%=specificEntry%></a></p>
<h4>Create a new entry</h4>
<p id="h4-content">Use the <a href="http://curl.haxx.se/">curl
tool</a> to perform the POST action, and view its result. Refer <a
	href="<%=IBM_document%>">this
documentation</a> to understand how this can be done</p>
<h4>Update an existing entry, using Atom Publishing Protocol</h4>
<p id="h4-content">Use the <a href="http://curl.haxx.se/">curl
tool</a> to perform the PUT action, and view its result. Refer <a
	href="<%=IBM_document%>">this
documentation</a> to understand how this can be done</p>
<h4>Delete an existing entry</h4>
<p id="h4-content">Use the <a href="http://curl.haxx.se/">curl
tool</a> to perform the DELETE action, and view its result. Refer <a
	href="<%=IBM_document%>">this
documentation</a> to understand how this can be done</p>
</DIV>
</DIV>
</DIV>
</BODY>
</HTML>