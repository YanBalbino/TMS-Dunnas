package com.dunnas.tms.feature.attachment;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dunnas.tms.feature.attachment.dto.AttachmentDto;
import com.dunnas.tms.feature.attachment.dto.AttachmentRequestDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
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
            @Valid @ModelAttribute("attachmentForm") AttachmentRequestDto request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível adicionar o anexo.");
            return "redirect:/tickets/" + request.ticketId();
        }

        AttachmentDto created = attachmentService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Anexo adicionado com sucesso.");
        return "redirect:/tickets/" + created.ticketId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id) {
        AttachmentDto existing = attachmentService.findById(id);
        return "redirect:/tickets/" + existing.ticketId();
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("attachmentForm") AttachmentRequestDto request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não foi possível atualizar o anexo.");
            return "redirect:/tickets/" + request.ticketId();
        }

        AttachmentDto updated = attachmentService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Anexo atualizado com sucesso.");
        return "redirect:/tickets/" + updated.ticketId();
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam Long ticketId,
            RedirectAttributes redirectAttributes
    ) {
        attachmentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Anexo removido com sucesso.");
        return "redirect:/tickets/" + ticketId;
    }
}
