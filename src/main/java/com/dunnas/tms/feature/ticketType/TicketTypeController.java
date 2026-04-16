package com.dunnas.tms.feature.ticketType;

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

import com.dunnas.tms.feature.ticketType.dto.TicketTypeDto;
import com.dunnas.tms.feature.ticketType.dto.TicketTypeRequestDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ticket-types")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping
    public String list(Model model) {
        List<TicketTypeDto> ticketTypes = ticketTypeService.findAll();
        model.addAttribute("ticketTypes", ticketTypes);
        return "ticket-type/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ticketTypeForm", new TicketTypeRequestDto("", 1));
        return "ticket-type/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("ticketTypeForm") TicketTypeRequestDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "ticket-type/form";
        }

        ticketTypeService.create(request);
        return "redirect:/ticket-types";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TicketTypeDto existing = ticketTypeService.findById(id);
        model.addAttribute("ticketTypeId", existing.id());
        model.addAttribute("ticketTypeForm", new TicketTypeRequestDto(existing.title(), existing.deadlineDays()));
        return "ticket-type/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("ticketTypeForm") TicketTypeRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketTypeId", id);
            return "ticket-type/form";
        }

        ticketTypeService.update(id, request);
        return "redirect:/ticket-types";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ticketTypeService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Tipo de chamado removido com sucesso.");
        return "redirect:/ticket-types";
    }
}
