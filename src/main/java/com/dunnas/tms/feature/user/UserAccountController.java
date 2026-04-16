package com.dunnas.tms.feature.user;

import java.util.List;
import java.util.Set;

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

import com.dunnas.tms.feature.ticketType.TicketTypeService;
import com.dunnas.tms.feature.unit.UnitService;
import com.dunnas.tms.feature.user.dto.UserAccountDto;
import com.dunnas.tms.feature.user.dto.UserAccountRequestDto;
import com.dunnas.tms.feature.user.service.UserAccountService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UnitService unitService;
    private final TicketTypeService ticketTypeService;

    public UserAccountController(
            UserAccountService userAccountService,
            UnitService unitService,
            TicketTypeService ticketTypeService
    ) {
        this.userAccountService = userAccountService;
        this.unitService = unitService;
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String role, Model model) {
        List<UserAccountDto> users = (role == null || role.isBlank()) 
        ? userAccountService.findAll() 
        : userAccountService.findAllByRole(role);

        model.addAttribute("selectedRole", role);
        model.addAttribute("users", users);
        return "user/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute(
                "userForm",
                new UserAccountRequestDto("", "", "", "RESIDENT", Set.of(), Set.of())
        );
        addFormDependencies(model);
        return "user/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("userForm") UserAccountRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            addFormDependencies(model);
            return "user/form";
        }

        userAccountService.create(request);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserAccountDto existing = userAccountService.findById(id);
        model.addAttribute("userId", existing.id());
        model.addAttribute(
                "userForm",
                new UserAccountRequestDto(
                        existing.name(),
                        existing.username(),
                        "",
                        existing.role(),
                        existing.unitIds(),
                        existing.collaboratorTicketTypeIds()
                )
        );
        addFormDependencies(model);
        return "user/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("userForm") UserAccountRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            addFormDependencies(model);
            return "user/form";
        }

        userAccountService.update(id, request);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userAccountService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Usuário removido com sucesso.");
        return "redirect:/users";
    }

    private void addFormDependencies(Model model) {
        model.addAttribute("availableRoles", List.of("ADMIN", "RESIDENT", "COLLABORATOR"));
        model.addAttribute("units", unitService.findAll());
        model.addAttribute("ticketTypes", ticketTypeService.findAll());
    }
}
