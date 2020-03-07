package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.CityEmailDto;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Profile("web")
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;

    @GetMapping
    public String sendEmailView(Model model) {
        model.addAttribute("emailMapping", new CityEmailDto());
        return "reports";
    }

    @PostMapping
    public String sentReport(@Valid CityEmailDto emailMapping) {
        reportService.createReportAndNotifySingleEmail(
                emailMapping.getCity(), emailMapping.getEmail());
        return "redirect:/";
    }
}
