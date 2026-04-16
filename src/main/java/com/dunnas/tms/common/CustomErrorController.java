package com.dunnas.tms.common;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        // Extrai os atributos de erro que o Spring inseriu na requisição
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorTitle = "Erro inesperado";
        String errorMessage = "Não foi possível concluir a requisição.";
        String errorPath = uri != null ? uri.toString() : "N/A";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);

            // Mapeamentos específicos para rotas não encontradas ou acessos negados
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle = "Página não encontrada";
                errorMessage = "A rota solicitada não existe no sistema.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle = "Acesso negado";
                errorMessage = "Você não possui permissão para acessar esta página.";
            } else if (message != null && !message.toString().isEmpty()) {
                errorMessage = message.toString();
            }
        }

        // Monta o mesmo ModelAndView que o GlobalExceptionHandler
        ModelAndView modelAndView = new ModelAndView("error/general");
        modelAndView.setStatus(httpStatus);
        modelAndView.addObject("status", httpStatus.value());
        modelAndView.addObject("error", httpStatus.getReasonPhrase());
        modelAndView.addObject("title", errorTitle);
        modelAndView.addObject("message", errorMessage);
        modelAndView.addObject("path", errorPath);

        return modelAndView;
    }
}