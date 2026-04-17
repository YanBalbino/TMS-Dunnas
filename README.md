# TMS — Sistema de Gerenciamento de Chamados para Condomínio

Projeto desenvolvido como solução para o desafio técnico da Dunnas Tecnologia, utilizando **Java, Spring Boot (MVC), JSP e PostgreSQL**.

## 1) Objetivo do projeto

A aplicação tem como objetivo gerenciar chamados de um condomínio com estrutura hierárquica composta por:

- blocos;
- andares;
- unidades;
- usuários com perfis distintos (administrador, colaborador e morador).

Fluxo funcional principal:

1. o morador registra um chamado para uma de suas unidades;
2. colaborador e/ou administrador atualizam o andamento do chamado;
3. os envolvidos interagem por meio de comentários e anexos;
4. o histórico operacional é preservado por metadados de auditoria.

## 2) Processo de desenvolvimento e principais decisões técnicas

### 2.1 Etapas adotadas

1. construção de um mini projeto de validação de stack (Spring Boot, Security, Flyway e JSP);
2. modelagem conceitual inicial em papel;
3. refinamento da modelagem conceitual em ambiente digital;
4. modelagem física com definição de chaves estrangeiras e restrições;
5. implementação incremental da solução principal.

### 2.2 Ordem de implementação

1. entidades (Entities);
2. repositórios (Repositories);
3. DTOs;
4. mapeadores (Mappers);
5. serviços (Services);
6. controladores (Controllers);
7. segurança (Security);
8. telas JSP;
9. Docker;
10. documentação.

### 2.3 Decisões técnicas relevantes

- **Stack escolhida:** Spring Boot MVC com JSP e PostgreSQL;
- **Versionamento de banco:** Flyway (`V1` para schema e `V2` para carga inicial);
- **Estrutura de código:** organização por funcionalidade (`packaging by feature`);
- **Reuso de interface:** componentes comuns para header, navegação lateral e footer;
- **Auditoria de dados:** uso de `created_at`, `updated_at`, `created_by` e `updated_by`;
- **Segurança:** autenticação com Spring Security (form login);
- **Controle de acesso:** combinação de restrições na interface e validação no backend (`@PreAuthorize`).

## 3) Estrutura do sistema

### 3.1 Organização de pacotes

- `feature/ticket`
- `feature/comment`
- `feature/attachment`
- `feature/block`
- `feature/unit`
- `feature/user`
- `feature/ticketStatus`
- `feature/ticketType`
- `common` (tratamento global e configurações compartilhadas)
- `security` (autenticação e autorização)
- `resources`
- `webapp/WEB-INF/jsp` (telas JSP)

### 3.2 Camadas por funcionalidade

- Entity
- Repository
- DTO / RequestDTO
- Mapper
- Service
- Controller (MVC)

## 4) Funcionalidades implementadas (núcleo)

### 4.1 Chamados

- listagem com filtros;
- criação de chamado;
- edição com restrições de integridade;
- exclusão (administrador);
- visualização de detalhes com comentários e anexos.

### 4.2 Regras de criação e edição de chamados

- na criação, o status é sempre o status padrão configurado no sistema;
- na criação, o autor é sempre o usuário autenticado;
- usuários com `ROLE_RESIDENT` visualizam apenas unidades às quais estão vinculados;
- usuários `ROLE_ADMIN` e `ROLE_COLLABORATOR` têm visão completa de unidades na criação;
- em edição, os campos **autor**, **unidade** e **tipo de chamado** são imutáveis.

### 4.3 Comentários

- criação no detalhe do chamado;
- autoria definida automaticamente pelo usuário autenticado;
- remoção disponível apenas para administrador.

## 5) Controle de acesso e permissões

Perfis implementados:

- `ROLE_ADMIN`
- `ROLE_COLLABORATOR`
- `ROLE_RESIDENT`

Regras de permissão relevantes:

- ação **Editar** chamado: administrador e colaborador;
- ação **Excluir** chamado: apenas administrador;
- ação **Remover comentário**: apenas administrador;
- morador atua conforme seu escopo de unidades vinculadas.

## 6) Banco de dados e relacionamentos

Migrations aplicadas:

- `V1__initial_schema.sql`: criação da estrutura do banco;
- `V2__seed_initial_data.sql`: carga inicial de dados para uso e demonstração.

### 6.1 Diagrama lógico-físico

<p align="center">
  <img src="images/Modelo Logico-Fisico - DunnasSGC.png" alt="Diagrama de modelagem física" width="900" />
</p>

### 6.2 Tabelas principais

- `ticket`
- `ticket_status`
- `ticket_type`
- `user_account`
- `block`
- `unit`
- `comment`
- `attachment`
- `resident_unit` (relação N:N)
- `collaborator_ticket_type` (relação N:N)

> Outros diagramas complementares estão disponíveis no diretório `images`.

## 7) Instruções de execução

### 7.1 Pré-requisitos

- Docker;
- Docker Compose.

### 7.2 Subida da aplicação com docker

```bash
docker compose up --build
```

Após a subida:

- aplicação: `http://localhost:8080`;
- banco PostgreSQL:
  - host: `localhost`
  - porta: `5434`
  - database: `tms-project`
  - usuário: `postgres`
  - senha: `password`

As migrations do Flyway são executadas automaticamente no processo de inicialização da aplicação.

## 8) Credenciais iniciais (seed)

- **Administrador**
  - usuário: `admin`
  - senha: `admin`
- **Colaborador**
  - usuário: `collab`
  - senha: `collab`
- **Morador 1**
  - usuário: `resident1`
  - senha: `resident1`
- **Morador 2**
  - usuário: `resident2`
  - senha: `resident2`

## 9) Limitações e observações finais

- a entrega prioriza o núcleo funcional, com foco nas regras de negócio essenciais do desafio;
- por restrição de tempo, itens de deploy público e suíte de testes automatizados não foram concluídos;
- a auditoria foi implementada por metadados de domínio (`created_at`, `updated_at`, `created_by`, `updated_by`), mantendo a simplicidade e oferecendo base para evolução futura.
