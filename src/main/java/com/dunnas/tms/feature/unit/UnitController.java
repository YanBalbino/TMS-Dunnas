package com.dunnas.tms.feature.unit;

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

import com.dunnas.tms.feature.unit.dto.UnitDto;
import com.dunnas.tms.feature.unit.dto.UnitRequestDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long blockId, Model model) {
        List<UnitDto> units = blockId == null ? unitService.findAll() : unitService.findAllByBlockId(blockId);

        model.addAttribute("selectedBlockId", blockId);
        model.addAttribute("units", units);
        return "unit/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam Long blockId, Model model) {
        model.addAttribute("unitForm", new UnitRequestDto(null, null, blockId, Set.of()));
        return "unit/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("unitForm") UnitRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "unit/form";
        }

        UnitDto created = unitService.create(request);
        return "redirect:/units?blockId=" + created.blockId();
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UnitDto existing = unitService.findById(id);
        model.addAttribute("unitId", existing.id());
        model.addAttribute(
                "unitForm",
                new UnitRequestDto(
                        existing.number(),
                        existing.floorNumber(),
                        existing.blockId(),
                        existing.residentIds()
                )
        );
        return "unit/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("unitForm") UnitRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("unitId", id);
            return "unit/form";
        }

        UnitDto updated = unitService.update(id, request);
        return "redirect:/units?blockId=" + updated.blockId();
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable Long id,
            @RequestParam(required = false) Long blockId,
            RedirectAttributes redirectAttributes
    ) {
        unitService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Unidade removida com sucesso.");
        if (blockId != null) {
            return "redirect:/units?blockId=" + blockId;
        }
        return "redirect:/units";
    }
}
