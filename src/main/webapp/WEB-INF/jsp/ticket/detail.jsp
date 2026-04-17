<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/nav.jsp" />

<div class="card" style="margin-bottom:16px;">
    <h2 style="margin-top:0;">Chamado #${ticket.id}</h2>
    <p style="margin:4px 0;"><strong>Título:</strong> ${ticket.title}</p>
    <p style="margin:4px 0;"><strong>Status:</strong> ${ticket.statusName}</p>
    <p style="margin:4px 0;"><strong>Tipo:</strong> ${ticket.ticketTypeTitle}</p>
    <p style="margin:4px 0;"><strong>Autor:</strong> ${ticket.authorName}</p>
    <p style="margin:4px 0;"><strong>Unidade:</strong> ${ticket.unitNumber}</p>
    <p style="margin:4px 0;"><strong>Descrição:</strong> ${ticket.description}</p>

    <div style="margin-top:12px;">
        <a href="${pageContext.request.contextPath}/tickets"
           style="display:inline-flex;align-items:center;padding:8px 12px;border:1px solid #d1d5db;border-radius:6px;text-decoration:none;color:#1f2937;">
            Voltar para chamados
        </a>
    </div>
</div>

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

<div class="card" style="margin-bottom:16px;">
    <h3 style="margin-top:0;">Comentários</h3>

    <form method="post" action="${pageContext.request.contextPath}/comments" style="display:flex;flex-direction:column;gap:8px;margin-bottom:12px;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="ticketId" value="${ticket.id}" />
        <div>
            <label for="description" style="display:block;font-weight:600;margin-bottom:6px;">Novo comentário</label>
            <textarea id="description" name="description" rows="3" maxlength="2000" required
                      style="width:100%;box-sizing:border-box;padding:10px;border:1px solid #c9c9c9;border-radius:6px;resize:vertical;"></textarea>
        </div>
        <button type="submit"
                style="width:max-content;border:0;border-radius:6px;padding:8px 12px;background:#4a397f;color:#fff;font-weight:600;cursor:pointer;">
            Adicionar comentário
        </button>
    </form>

    <c:choose>
        <c:when test="${empty comments}">
            <p style="color:#6b7280;">Ainda não há comentários neste chamado.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="comment" items="${comments}">
                <div style="border:1px solid #e5e7eb;border-radius:6px;padding:10px;margin-bottom:8px;">
                    <p style="margin:0 0 6px 0;"><strong>${comment.authorName}</strong></p>
                    <p style="margin:0 0 10px 0;">${comment.description}</p>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <form method="post" action="${pageContext.request.contextPath}/comments/${comment.id}/delete" style="margin:0;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input type="hidden" name="ticketId" value="${ticket.id}" />
                            <button type="submit"
                                    style="border:1px solid #fca5a5;background:#fff;color:#b91c1c;border-radius:6px;padding:6px 10px;cursor:pointer;">
                                Remover comentário
                            </button>
                        </form>
                    </sec:authorize>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
