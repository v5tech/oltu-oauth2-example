<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../top.jsp"/>
</head>
<body>

<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 应用</h3>
    </div>

    <form:form method="post" commandName="client" cssClass="form-inline">
        <form:hidden path="id"/>
        <form:hidden path="clientId"/>
        <form:hidden path="clientSecret"/>

    <div class="form-group">
        <form:label path="clientName">应用名：</form:label>
        <form:input path="clientName"/>
    </div>

        <form:button>${op}</form:button>

    </form:form>

<jsp:include page="../footer.jsp"/>