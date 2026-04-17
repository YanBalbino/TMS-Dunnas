<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/nav.jsp" />

<div class="card">
    <c:set var="isEdit" value="${not empty ticketId}" />
    <c:choose>
        <c:when test="${isEdit}">
            <c:set var="formAction" value="${pageContext.request.contextPath}/tickets/${ticketId}" />
        </c:when>
        <c:otherwise>
            <c:set var="formAction" value="${pageContext.request.contextPath}/tickets" />
        </c:otherwise>
    </c:choose>

    <h2 style="margin-top: 0;">
        <c:choose>
            <c:when test="${isEdit}">Editar chamado #${ticketId}</c:when>
            <c:otherwise>Novo chamado</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty errorMessage}">
        <div style="background:#fee2e2;color:#991b1b;border:1px solid #fecaca;padding:10px;border-radius:6px;margin-bottom:12px;">
            ${errorMessage}
        </div>
    </c:if>

    <c:if test="${not empty org.springframework.validation.BindingResult.ticketForm}">
        <div style="background:#fee2e2;color:#991b1b;border:1px solid #fecaca;padding:10px;border-radius:6px;margin-bottom:16px;">
            Verifique os campos obrigatórios e tente novamente.
        </div>
    </c:if>

    <form
        method="post"
        action="${formAction}"
        style="display:flex;flex-direction:column;gap:12px;max-width:760px;"
    >
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div>
            <label for="title" style="display:block;font-weight:600;margin-bottom:6px;">Título</label>
            <input
                id="title"
                name="title"
                type="text"
                maxlength="150"
                required
                value="${ticketForm.title}"
                style="width:100%;box-sizing:border-box;padding:10px;border:1px solid #c9c9c9;border-radius:6px;"
            />
        </div>

        <div>
            <label for="description" style="display:block;font-weight:600;margin-bottom:6px;">Descrição</label>
            <textarea
                id="description"
                name="description"
                rows="5"
                minlength="10"
                maxlength="300"
                required
                style="width:100%;box-sizing:border-box;padding:10px;border:1px solid #c9c9c9;border-radius:6px;resize:vertical;"
            >${ticketForm.description}</textarea>
            <small style="color:#6b7280;">Entre 10 e 300 caracteres.</small>
        </div>

        <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:12px;">
            <div>
                <label for="statusId" style="display:block;font-weight:600;margin-bottom:6px;">Status</label>
                <c:choose>
                    <c:when test="${isEdit}">
                        <select
                            id="statusId"
                            name="statusId"
                            required
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#fff;"
                        >
                            <option value="">Selecione</option>
                            <c:forEach var="status" items="${ticketStatuses}">
                                <option value="${status.id}" <c:if test="${ticketForm.statusId == status.id}">selected</c:if>>
                                    ${status.name}
                                </option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="statusId" value="${defaultStatus.id}" />
                        <input
                            id="statusId"
                            type="text"
                            value="${defaultStatus.name}"
                            disabled
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#f3f4f6;color:#374151;"
                        />
                        <small style="color:#6b7280;">Status definido automaticamente pelo padrão do sistema.</small>
                    </c:otherwise>
                </c:choose>
            </div>

            <div>
                <label for="ticketTypeId" style="display:block;font-weight:600;margin-bottom:6px;">Tipo de chamado</label>
                <c:choose>
                    <c:when test="${isEdit}">
                        <input type="hidden" name="ticketTypeId" value="${ticketForm.ticketTypeId}" />
                        <select
                            id="ticketTypeId"
                            disabled
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#f3f4f6;color:#374151;"
                        >
                            <c:forEach var="type" items="${ticketTypes}">
                                <option value="${type.id}" <c:if test="${ticketForm.ticketTypeId == type.id}">selected</c:if>>
                                    ${type.title}
                                </option>
                            </c:forEach>
                        </select>
                        <small style="color:#6b7280;">Este campo não pode ser alterado após a abertura do chamado.</small>
                    </c:when>
                    <c:otherwise>
                        <select
                            id="ticketTypeId"
                            name="ticketTypeId"
                            required
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#fff;"
                        >
                            <option value="">Selecione</option>
                            <c:forEach var="type" items="${ticketTypes}">
                                <option value="${type.id}" <c:if test="${ticketForm.ticketTypeId == type.id}">selected</c:if>>
                                    ${type.title}
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>

            <div>
                <label for="authorId" style="display:block;font-weight:600;margin-bottom:6px;">Autor</label>
                <c:choose>
                    <c:when test="${isEdit}">
                        <input type="hidden" name="authorId" value="${ticketForm.authorId}" />
                        <select
                            id="authorId"
                            disabled
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#f3f4f6;color:#374151;"
                        >
                            <c:forEach var="user" items="${users}">
                                <option value="${user.id}" <c:if test="${ticketForm.authorId == user.id}">selected</c:if>>
                                    ${user.name} (${user.username})
                                </option>
                            </c:forEach>
                        </select>
                        <small style="color:#6b7280;">Este campo não pode ser alterado após a abertura do chamado.</small>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="authorId" value="${loggedUser.id}" />
                        <input
                            id="authorId"
                            type="text"
                            value="${loggedUser.name} (${loggedUser.username})"
                            disabled
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#f3f4f6;color:#374151;"
                        />
                        <small style="color:#6b7280;">Autor definido automaticamente pelo usuário logado.</small>
                    </c:otherwise>
                </c:choose>
            </div>

            <div>
                <label for="unitId" style="display:block;font-weight:600;margin-bottom:6px;">Unidade</label>
                <c:choose>
                    <c:when test="${isEdit}">
                        <input type="hidden" name="unitId" value="${ticketForm.unitId}" />
                        <select
                            id="unitId"
                            disabled
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#f3f4f6;color:#374151;"
                        >
                            <c:forEach var="unit" items="${units}">
                                <option value="${unit.id}" <c:if test="${ticketForm.unitId == unit.id}">selected</c:if>>
                                    ${unit.blockName} - ${unit.number}
                                </option>
                            </c:forEach>
                        </select>
                        <small style="color:#6b7280;">Este campo não pode ser alterado após a abertura do chamado.</small>
                    </c:when>
                    <c:otherwise>
                        <select
                            id="unitId"
                            name="unitId"
                            required
                            style="width:100%;padding:10px;border:1px solid #c9c9c9;border-radius:6px;background:#fff;"
                        >
                            <option value="">Selecione</option>
                            <c:forEach var="unit" items="${units}">
                                <option value="${unit.id}" <c:if test="${ticketForm.unitId == unit.id}">selected</c:if>>
                                    ${unit.blockName} - ${unit.number}
                                </option>
                            </c:forEach>
                        </select>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div style="display:flex;gap:8px;align-items:center;margin-top:4px;">
            <button
                type="submit"
                style="border:0;border-radius:6px;padding:10px 14px;background:#1d4ed8;color:#fff;font-weight:600;cursor:pointer;"
            >
                <c:choose>
                    <c:when test="${isEdit}">Salvar alterações</c:when>
                    <c:otherwise>Criar chamado</c:otherwise>
                </c:choose>
            </button>

            <a
                href="${pageContext.request.contextPath}/tickets"
                style="display:inline-flex;align-items:center;padding:10px 14px;border:1px solid #d1d5db;border-radius:6px;text-decoration:none;color:#1f2937;"
            >
                Cancelar
            </a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
