package com.dunnas.tms.feature.ticket;

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

import com.dunnas.tms.feature.ticket.dto.TicketDto;
import com.dunnas.tms.feature.ticket.dto.TicketRequestDto;
import com.dunnas.tms.feature.ticketStatus.TicketStatusService;
import com.dunnas.tms.feature.ticketType.TicketTypeService;
import com.dunnas.tms.feature.unit.UnitService;
import com.dunnas.tms.feature.user.service.UserAccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketStatusService ticketStatusService;
    private final TicketTypeService ticketTypeService;
    private final UnitService unitService;
    private final UserAccountService userAccountService;

    public TicketController(
            TicketService ticketService,
            TicketStatusService ticketStatusService,
            TicketTypeService ticketTypeService,
            UnitService unitService,
            UserAccountService userAccountService
    ) {
        this.ticketService = ticketService;
        this.ticketStatusService = ticketStatusService;
        this.ticketTypeService = ticketTypeService;
        this.unitService = unitService;
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Long unitId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long ticketTypeId,
            @RequestParam(required = false) Long collaboratorId,
            Model model
    ) {
        List<TicketDto> tickets;

        //TODO: filtros dinâmicos com Specifications ou QueryDSL
        if (collaboratorId != null) {
            tickets = ticketService.findAllForCollaborator(collaboratorId);
        } else if (unitId != null) {
            tickets = ticketService.findAllByUnitId(unitId);
        } else if (authorId != null) {
            tickets = ticketService.findAllByAuthorId(authorId);
        } else if (statusId != null) {
            tickets = ticketService.findAllByStatusId(statusId);
        } else if (ticketTypeId != null) {
            tickets = ticketService.findAllByTicketTypeId(ticketTypeId);
        } else {
            tickets = ticketService.findAll();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("selectedUnitId", unitId);
        model.addAttribute("selectedAuthorId", authorId);
        model.addAttribute("selectedStatusId", statusId);
        model.addAttribute("selectedTicketTypeId", ticketTypeId);
        model.addAttribute("selectedCollaboratorId", collaboratorId);
        return "ticket/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ticketForm", new TicketRequestDto("", "", null, null, null, null));
        addFormDependencies(model);
        return "ticket/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("ticketForm") TicketRequestDto request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addFormDependencies(model);
            return "ticket/form";
        }

        ticketService.create(request);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TicketDto existing = ticketService.findById(id);
        model.addAttribute("ticketId", existing.id());
        model.addAttribute(
                "ticketForm",
                new TicketRequestDto(
                        existing.title(),
                        existing.description(),
                        existing.statusId(),
                        existing.authorId(),
                        existing.unitId(),
                        existing.ticketTypeId()
                )
        );
        addFormDependencies(model);
        return "ticket/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("ticketForm") TicketRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketId", id);
            addFormDependencies(model);
            return "ticket/form";
        }

        ticketService.update(id, request);
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ticketService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Chamado removido com sucesso.");
        return "redirect:/tickets";
    }

    private void addFormDependencies(Model model) {
        model.addAttribute("ticketStatuses", ticketStatusService.findAll());
        model.addAttribute("ticketTypes", ticketTypeService.findAll());
        model.addAttribute("units", unitService.findAll());
        model.addAttribute("users", userAccountService.findAll());
    }
}
