<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
    var localeCode = "<spring:message code="app.locale"/>";

    const i18n = {}; // https://learn.javascript.ru/object

    i18n["addTitle"] = '<spring:message code="${param.entity}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.entity}.edit"/>';

    <c:forEach var="key" items='${["common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"]}'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>