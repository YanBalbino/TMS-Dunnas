package com.dunnas.tms.feature.comment;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dunnas.tms.feature.comment.dto.CommentDto;
import com.dunnas.tms.feature.comment.dto.CommentRequestDto;
import com.dunnas.tms.feature.user.UserAccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserAccountService userAccountService;

    public CommentController(CommentService commentService, UserAccountService userAccountService) {
        this.commentService = commentService;
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String listByTicket(@RequestParam Long ticketId) {
        return "redirect:/tickets/" + ticketId;
    }

    @GetMapping("/new")
    public String newForm(@RequestParam Long ticketId) {
        return "redirect:/tickets/" + ticketId;
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("commentForm") CommentRequestDto request,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível adicionar o comentário.");
            return "redirect:/tickets/" + request.ticketId();
        }

        Long loggedUserId = userAccountService.findByUsername(authentication.getName()).id();
        CommentDto created = commentService.create(request, loggedUserId);
        redirectAttributes.addFlashAttribute("successMessage", "Comentário adicionado com sucesso.");
        return "redirect:/tickets/" + created.ticketId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id) {
        CommentDto existing = commentService.findById(id);
        return "redirect:/tickets/" + existing.ticketId();
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("commentForm") CommentRequestDto request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível atualizar o comentário.");
            return "redirect:/tickets/" + request.ticketId();
        }

        CommentDto updated = commentService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Comentário atualizado com sucesso.");
        return "redirect:/tickets/" + updated.ticketId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam Long ticketId,
            RedirectAttributes redirectAttributes
    ) {
        commentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Comentário removido com sucesso.");
        return "redirect:/tickets/" + ticketId;
    }
}
