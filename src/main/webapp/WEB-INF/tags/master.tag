<%-- 
    Document   : master
    Created on : Dec 8, 2021, 12:25:27 PM
    Author     : aldairrev
--%>

<%@tag description="Master template html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title"%>

<%-- any content can be specified here e.g.: --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <t:metadata></t:metadata>
    <title>${title} | Contacts Management</title>
</head>
<body>
    <div class="wrapper">
        <jsp:doBody/>
    </div>
    <t:scripts></t:scripts>
</body>
</html>