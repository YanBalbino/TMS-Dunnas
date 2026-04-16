<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - TMS</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f5f7;
            color: #1f1f1f;
        }
        .container {
            max-width: 420px;
            margin: 64px auto;
            background: #ffffff;
            border: 1px solid #dcdcdc;
            border-radius: 8px;
            padding: 24px;
        }
        h1 {
            margin-top: 0;
            margin-bottom: 20px;
            font-size: 24px;
        }
        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            box-sizing: border-box;
            margin-bottom: 14px;
            padding: 10px;
            border: 1px solid #c9c9c9;
            border-radius: 6px;
            font-size: 14px;
        }
        button {
            width: 100%;
            border: 0;
            border-radius: 6px;
            padding: 10px 12px;
            background: #1d4ed8;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
        }
        .message {
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 14px;
            font-size: 14px;
        }
        .error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }
        .success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Entrar no sistema</h1>

        <% if (request.getParameter("error") != null) { %>
            <div class="message error">Usuário ou senha inválidos.</div>
        <% } %>

        <% if (request.getParameter("logout") != null) { %>
            <div class="message success">Logout realizado com sucesso.</div>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <label for="username">Usuário</label>
            <input type="text" id="username" name="username" required />

            <label for="password">Senha</label>
            <input type="password" id="password" name="password" required />

            <button type="submit">Entrar</button>
        </form>
    </div>
</body>
</html>
