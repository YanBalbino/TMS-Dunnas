package com.dunnas.tms.feature.ticket;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.dunnas.tms.feature.attachment.AttachmentService;
import com.dunnas.tms.feature.comment.CommentService;
import com.dunnas.tms.feature.ticketStatus.TicketStatusService;
import com.dunnas.tms.feature.ticketType.TicketTypeService;
import com.dunnas.tms.feature.unit.dto.UnitDto;
import com.dunnas.tms.feature.unit.UnitService;
import com.dunnas.tms.feature.user.dto.UserAccountDto;
import com.dunnas.tms.feature.user.UserAccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketStatusService ticketStatusService;
    private final TicketTypeService ticketTypeService;
    private final UnitService unitService;
    private final UserAccountService userAccountService;
    private final CommentService commentService;
    private final AttachmentService attachmentService;

    public TicketController(
            TicketService ticketService,
            TicketStatusService ticketStatusService,
            TicketTypeService ticketTypeService,
            UnitService unitService,
            UserAccountService userAccountService,
            CommentService commentService,
            AttachmentService attachmentService
    ) {
        this.ticketService = ticketService;
        this.ticketStatusService = ticketStatusService;
        this.ticketTypeService = ticketTypeService;
        this.unitService = unitService;
        this.userAccountService = userAccountService;
        this.commentService = commentService;
        this.attachmentService = attachmentService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Integer unitNumber,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String statusName,
            @RequestParam(required = false) String ticketTypeTitle,
            @RequestParam(required = false) String collaboratorName,
            Model model
    ) {
        List<TicketDto> tickets;

        if (hasText(collaboratorName)) {
            Long collaboratorId = userAccountService.findByName(collaboratorName.trim()).id();
            tickets = ticketService.findAllForCollaborator(collaboratorId);
        } else if (unitNumber != null) {
            tickets = ticketService.findAllByUnitNumber(unitNumber);
        } else if (hasText(authorName)) {
            tickets = ticketService.findAllByAuthorName(authorName.trim());
        } else if (hasText(statusName)) {
            tickets = ticketService.findAllByStatusName(statusName.trim());
        } else if (hasText(ticketTypeTitle)) {
            tickets = ticketService.findAllByTicketTypeTitle(ticketTypeTitle.trim());
        } else {
            tickets = ticketService.findAll();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("selectedUnitNumber", unitNumber);
        model.addAttribute("selectedAuthorName", authorName);
        model.addAttribute("selectedStatusName", statusName);
        model.addAttribute("selectedTicketTypeTitle", ticketTypeTitle);
        model.addAttribute("selectedCollaboratorName", collaboratorName);
        return "ticket/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        TicketDto ticket = ticketService.findById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("comments", commentService.findAllByTicketId(id));
        model.addAttribute("attachments", attachmentService.findAllByTicketId(id));
        return "ticket/detail";
    }

    @GetMapping("/new")
    public String newForm(Authentication authentication, Model model) {
        model.addAttribute("ticketForm", new TicketRequestDto("", "", null, null, null, null));
        addCreateFormDependencies(authentication, model);
        return "ticket/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("ticketForm") TicketRequestDto request,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            addCreateFormDependencies(authentication, model);
            return "ticket/form";
        }

        Long loggedUserId = userAccountService.findByUsername(authentication.getName()).id();
        ticketService.create(request, loggedUserId);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
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
    @PreAuthorize("hasRole('ADMIN')")
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

    private void addCreateFormDependencies(Authentication authentication, Model model) {
        UserAccountDto loggedUser = userAccountService.findByUsername(authentication.getName());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("defaultStatus", ticketStatusService.findDefault());
        model.addAttribute("ticketTypes", ticketTypeService.findAll());

        if (isResidentRole(loggedUser.role())) {
            List<UnitDto> residentUnits = unitService.findAllByResidentId(loggedUser.id());
            model.addAttribute("units", residentUnits);
        } else {
            model.addAttribute("units", unitService.findAll());
        }
    }

    private boolean isResidentRole(String role) {
        if (role == null) {
            return false;
        }

        return "ROLE_RESIDENT".equalsIgnoreCase(role) || "RESIDENT".equalsIgnoreCase(role);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
