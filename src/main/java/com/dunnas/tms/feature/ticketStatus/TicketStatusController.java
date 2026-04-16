package com.dunnas.tms.feature.ticketStatus;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dunnas.tms.feature.ticketStatus.dto.TicketStatusDto;
import com.dunnas.tms.feature.ticketStatus.dto.TicketStatusRequestDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ticket-statuses")
public class TicketStatusController {

    private final TicketStatusService ticketStatusService;

    public TicketStatusController(TicketStatusService ticketStatusService) {
        this.ticketStatusService = ticketStatusService;
    }

    @GetMapping
    public String list(Model model) {
        List<TicketStatusDto> ticketStatuses = ticketStatusService.findAll();
        model.addAttribute("ticketStatuses", ticketStatuses);
        return "ticket-status/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ticketStatusForm", new TicketStatusRequestDto("", null, false, false));
        return "ticket-status/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("ticketStatusForm") TicketStatusRequestDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "ticket-status/form";
        }

        ticketStatusService.create(request);
        return "redirect:/ticket-statuses";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TicketStatusDto existing = ticketStatusService.findById(id);
        model.addAttribute("ticketStatusId", existing.id());
        model.addAttribute(
                "ticketStatusForm",
                new TicketStatusRequestDto(
                        existing.name(),
                        existing.hexColor(),
                        existing.isDefault(),
                        existing.isFinalizer()
                )
        );
        return "ticket-status/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("ticketStatusForm") TicketStatusRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketStatusId", id);
            return "ticket-status/form";
        }

        ticketStatusService.update(id, request);
        return "redirect:/ticket-statuses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ticketStatusService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Status removido com sucesso.");
        return "redirect:/ticket-statuses";
    }
}
