package com.dunnas.tms.feature.attachment;

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
    public String listByTicket(@RequestParam Long ticketId, Model model) {
        List<AttachmentDto> attachments = attachmentService.findAllByTicketId(ticketId);
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("attachments", attachments);
        return "attachment/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam Long ticketId, Model model) {
        model.addAttribute("attachmentForm", new AttachmentRequestDto("", null, "", ticketId));
        return "attachment/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("attachmentForm") AttachmentRequestDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "attachment/form";
        }

        AttachmentDto created = attachmentService.create(request);
        return "redirect:/attachments?ticketId=" + created.ticketId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AttachmentDto existing = attachmentService.findById(id);
        model.addAttribute("attachmentId", existing.id());
        model.addAttribute(
                "attachmentForm",
                new AttachmentRequestDto(
                        existing.fileName(),
                        existing.mimeType(),
                        existing.storagePath(),
                        existing.ticketId()
                )
        );
        return "attachment/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("attachmentForm") AttachmentRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("attachmentId", id);
            return "attachment/form";
        }

        AttachmentDto updated = attachmentService.update(id, request);
        return "redirect:/attachments?ticketId=" + updated.ticketId();
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam Long ticketId,
            RedirectAttributes redirectAttributes
    ) {
        attachmentService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Anexo removido com sucesso.");
        return "redirect:/attachments?ticketId=" + ticketId;
    }
}
