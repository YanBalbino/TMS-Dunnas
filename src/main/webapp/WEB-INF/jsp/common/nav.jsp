<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="sidebar">
    <div class="nav-section">Principal</div>
    
    <a href="${pageContext.request.contextPath}/tickets">Chamados</a>

    <sec:authorize access="hasAnyRole('ROLE_RESIDENT', 'ROLE_COLLABORATOR', 'ROLE_ADMIN')">
        <a href="${pageContext.request.contextPath}/tickets/new">Abrir Chamado</a>
    </sec:authorize>

    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <div class="nav-section">Administração</div>
        <a href="${pageContext.request.contextPath}/admin/users">Usuários</a>
        <a href="${pageContext.request.contextPath}/admin/blocks">Blocos e Unidades</a>
        <a href="${pageContext.request.contextPath}/admin/types">Tipos de Chamado</a>
        <a href="${pageContext.request.contextPath}/admin/statuses">Status de Chamado</a>
    </sec:authorize>
</nav>

<main class="main-content">