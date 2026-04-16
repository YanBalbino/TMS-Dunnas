<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${title != null ? title : 'Erro'}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background: #f7f7f9;
            color: #222;
        }
        .container {
            max-width: 720px;
            margin: 48px auto;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 24px;
        }
        h1 { margin-top: 0; }
        .meta { color: #666; font-size: 14px; }
        .block { margin-top: 16px; }
        a { color: #0b57d0; text-decoration: none; }
    </style>
</head>
<body>
    <div class="container">
        <h1>${title != null ? title : 'Ocorreu um erro'}</h1>
        <p class="meta">HTTP ${status != null ? status : 500} - ${error != null ? error : 'Internal Server Error'}</p>

        <div class="block">
            <strong>Mensagem:</strong>
            <p>${message != null ? message : 'Não foi possível concluir a operação.'}</p>
        </div>

        <div class="block">
            <strong>Rota:</strong>
            <p>${path != null ? path : 'N/A'}</p>
        </div>

        <div class="block">
            <a href="/">Voltar para a página inicial</a>
        </div>
    </div>
</body>
</html>
