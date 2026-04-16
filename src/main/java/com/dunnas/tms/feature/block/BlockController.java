package com.dunnas.tms.feature.block;

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

import com.dunnas.tms.feature.block.dto.BlockDto;
import com.dunnas.tms.feature.block.dto.BlockRequestDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/blocks")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping
    public String list(Model model) {
        List<BlockDto> blocks = blockService.findAll();
        model.addAttribute("blocks", blocks);
        return "block/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("blockForm", new BlockRequestDto("", null, null, 1, 1));
        return "block/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("blockForm") BlockRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "block/form";
        }

        blockService.create(request);
        return "redirect:/blocks";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BlockDto existing = blockService.findById(id);
        model.addAttribute("blockId", existing.id());
        model.addAttribute(
                "blockForm",
                new BlockRequestDto(
                        existing.name(),
                        existing.street(),
                        existing.number(),
                        existing.floorCount(),
                        existing.unitsPerFloor()
                )
        );
        return "block/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("blockForm") BlockRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("blockId", id);
            return "block/form";
        }

        blockService.update(id, request);
        return "redirect:/blocks";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        blockService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Bloco removido com sucesso.");
        return "redirect:/blocks";
    }
}
