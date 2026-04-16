package com.dunnas.tms.feature.comment;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public String listByTicket(@RequestParam Long ticketId, Model model) {
        List<CommentDto> comments = commentService.findAllByTicketId(ticketId);
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("comments", comments);
        return "comment/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam Long ticketId, @RequestParam Long authorId, Model model) {
        model.addAttribute("commentForm", new CommentRequestDto("", ticketId, authorId));
        return "comment/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("commentForm") CommentRequestDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "comment/form";
        }

        CommentDto created = commentService.create(request);
        return "redirect:/comments?ticketId=" + created.ticketId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        CommentDto existing = commentService.findById(id);
        model.addAttribute("commentId", existing.id());
        model.addAttribute(
                "commentForm",
                new CommentRequestDto(
                        existing.description(),
                        existing.ticketId(),
                        existing.authorId()
                )
        );
        return "comment/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("commentForm") CommentRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("commentId", id);
            return "comment/form";
        }

        CommentDto updated = commentService.update(id, request);
        return "redirect:/comments?ticketId=" + updated.ticketId();
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam Long ticketId,
            RedirectAttributes redirectAttributes
    ) {
        commentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Comentário removido com sucesso.");
        return "redirect:/comments?ticketId=" + ticketId;
    }
}
