<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/nav.jsp" />

<div class="card">
    <h2 style="margin-top: 0;">Chamados</h2>

    <c:if test="${not empty successMessage}">
        <div style="background:#dcfce7;color:#166534;border:1px solid #bbf7d0;padding:10px;border-radius:6px;margin-bottom:12px;">
            ${successMessage}
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div style="background:#fee2e2;color:#991b1b;border:1px solid #fecaca;padding:10px;border-radius:6px;margin-bottom:12px;">
            ${errorMessage}
        </div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/tickets" style="display:flex;gap:8px;flex-wrap:wrap;margin-bottom:16px;">
        <input type="number" name="unitNumber" value="${selectedUnitNumber}" placeholder="Filtrar por número da unidade"
               style="padding:8px;border:1px solid #c9c9c9;border-radius:6px;" />
        <input type="text" name="authorName" value="${selectedAuthorName}" placeholder="Filtrar por  autor"
               style="padding:8px;border:1px solid #c9c9c9;border-radius:6px;" />
        <input type="text" name="statusName" value="${selectedStatusName}" placeholder="Filtrar por status"
               style="padding:8px;border:1px solid #c9c9c9;border-radius:6px;" />
        <input type="text" name="ticketTypeTitle" value="${selectedTicketTypeTitle}" placeholder="Filtrar por tipo"
               style="padding:8px;border:1px solid #c9c9c9;border-radius:6px;" />
        <input type="text" name="collaboratorName" value="${selectedCollaboratorName}" placeholder="Filtrar por colaborador"
               style="padding:8px;border:1px solid #c9c9c9;border-radius:6px;" />

        <button type="submit"
                style="border:0;border-radius:6px;padding:8px 12px;background:#4a397f;color:#fff;font-weight:600;cursor:pointer;">
            Aplicar
        </button>

        <a href="${pageContext.request.contextPath}/tickets"
           style="display:inline-flex;align-items:center;padding:8px 12px;border:1px solid #d1d5db;border-radius:6px;text-decoration:none;color:#1f2937;">
            Limpar
        </a>
    </form>

    <div style="margin-bottom: 16px;">
        <a href="${pageContext.request.contextPath}/tickets/new"
           style="display:inline-flex;align-items:center;padding:8px 12px;border-radius:6px;text-decoration:none;background:#16a34a;color:#fff;font-weight:600;">
            + Novo chamado
        </a>
    </div>

    <div style="overflow-x:auto;">
        <table style="width:100%;border-collapse:collapse;font-size:14px;">
            <thead>
                <tr style="background:#f9fafb;">
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Título</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Status</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Tipo</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Autor</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Unidade</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Prazo</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Concluído em</th>
                    <th style="text-align:left;border:1px solid #e5e7eb;padding:8px;">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty tickets}">
                        <tr>
                            <td colspan="9" style="border:1px solid #e5e7eb;padding:12px;color:#6b7280;">
                                Nenhum chamado encontrado.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="ticket" items="${tickets}">
                            <tr>
                                <td style="border:1px solid #e5e7eb;padding:8px;">${ticket.title}</td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">${ticket.statusName}</td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">${ticket.ticketTypeTitle}</td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">${ticket.authorName}</td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">${ticket.unitNumber}</td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">
                                    <c:out value="${ticket.dueDateFormatted}" />
                                </td>
                                <td style="border:1px solid #e5e7eb;padding:8px;">
                                    <c:out value="${ticket.completedAtFormatted}" />
                                </td>
                                <td style="border:1px solid #e5e7eb;padding:8px;white-space:nowrap;">
                                    <a href="${pageContext.request.contextPath}/tickets/${ticket.id}"
                                       style="text-decoration:none;padding:6px 10px;border:1px solid #86efac;border-radius:6px;color:#166534;">
                                        Detalhes
                                    </a>
                                    <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_COLLABORATOR')">
                                        <a href="${pageContext.request.contextPath}/tickets/${ticket.id}/edit"
                                           style="text-decoration:none;padding:6px 10px;border:1px solid #93c5fd;border-radius:6px;color:#1d4ed8;margin-left:6px;">
                                            Editar
                                        </a>
                                    </sec:authorize>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        <form method="post" action="${pageContext.request.contextPath}/tickets/${ticket.id}/delete"
                                              style="display:inline-block;margin-left:6px;">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                            <button type="submit"
                                                    style="border:1px solid #fca5a5;background:#fff;color:#b91c1c;border-radius:6px;padding:6px 10px;cursor:pointer;">
                                                Excluir
                                            </button>
                                        </form>
                                    </sec:authorize>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
