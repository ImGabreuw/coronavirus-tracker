package me.gabreuw.coronavirustracker.controller;

import me.gabreuw.coronavirustracker.model.entities.LocationStats;
import me.gabreuw.coronavirustracker.model.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService service;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = service.getAllStats();

        int totalReportedCases = allStats
                .stream()
                .mapToInt(LocationStats::getLatestTotalCases)
                .sum();
        int totalNewCases = allStats
                .stream()
                .mapToInt(LocationStats::getDiffFromPrevDay)
                .sum();

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "home";
    }

}
