package com.dunnas.tms.common;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNotFound(NoSuchElementException exception, HttpServletRequest request) {
        return errorView(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException exception, HttpServletRequest request) {
        return errorView(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleBusinessConflict(IllegalStateException exception, HttpServletRequest request) {
        return errorView(
                HttpStatus.CONFLICT,
                "Operação não permitida",
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbidden(AccessDeniedException exception, HttpServletRequest request) {
        return errorView(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não possui permissão para executar esta ação.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnexpected(Exception exception, HttpServletRequest request) {
        LOGGER.error("Unexpected error while processing request [{}]", request.getRequestURI(), exception);
        return errorView(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente.",
                request.getRequestURI()
        );
    }

    private ModelAndView errorView(HttpStatus status, String title, String message, String path) {
        ModelAndView modelAndView = new ModelAndView("error/general");
        modelAndView.setStatus(status);
        modelAndView.addObject("status", status.value());
        modelAndView.addObject("error", status.getReasonPhrase());
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", message);
        modelAndView.addObject("path", path);
        return modelAndView;
    }
}
