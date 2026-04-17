<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>TMS - Condomínio</title>
    <style>
        /* Base Styles - Mantendo a identidade do login e general */
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f5f7;
            color: #1f1f1f;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        /* Topbar */
        .topbar {
            background: #4a397f;
            color: #ffffff;
            padding: 16px 24px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .topbar h1 { margin: 0; font-size: 20px; font-weight: bold; }
        .user-panel { display: flex; align-items: center; gap: 16px; font-size: 14px; }
        .btn-logout {
            background: transparent;
            border: 1px solid #ffffff;
            color: #ffffff;
            padding: 6px 12px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s;
        }
        .btn-logout:hover { background: rgba(255, 255, 255, 0.1); }

        /* Layout Container */
        .app-container {
            display: flex;
            flex: 1;
        }

        /* Sidebar Nav */
        .sidebar {
            width: 250px;
            background: #ffffff;
            border-right: 1px solid #dcdcdc;
            padding: 24px 0;
        }
        .sidebar a {
            display: block;
            padding: 12px 24px;
            color: #1f1f1f;
            text-decoration: none;
            font-weight: 500;
            border-left: 3px solid transparent;
        }
        .sidebar a:hover {
            background: #f0f4ff;
            border-left-color: #1d4ed8;
            color: #1d4ed8;
        }
        .nav-section {
            padding: 0 24px;
            font-size: 12px;
            color: #666;
            font-weight: bold;
            margin-top: 24px;
            margin-bottom: 8px;
            text-transform: uppercase;
        }

        /* Main Content */
        .main-content {
            flex: 1;
            padding: 32px;
            overflow-y: auto;
        }
        .card {
            background: #ffffff;
            border: 1px solid #dcdcdc;
            border-radius: 8px;
            padding: 24px;
        }
    </style>
</head>
<body>
    <header class="topbar">
        <h1>TMS - Condomínio</h1>
        <div class="user-panel">
            <span>Olá, <strong><sec:authentication property="name" /></strong></span>
            
            <form method="post" action="${pageContext.request.contextPath}/logout" style="margin: 0;">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn-logout">Sair</button>
            </form>
        </div>
    </header>
    
    <div class="app-container">