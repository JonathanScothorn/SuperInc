package application;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class HtmlPagesController {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private SuperHeroRepository heroRepository;

    @ApiOperation(value = "View the homepage")
    @GetMapping(value = "/", produces = "text/html")
    public String homepage() {
        return "home";
    }

    @ApiOperation(value = "View create hero form")
    @GetMapping(value = "/createHero", produces = "text/html")
    public String createHeroForm(Model model) {
        model.addAttribute("superHero", new SuperHero());
        return "createHeroForm";
    }

    @ApiOperation(value = "Submit create hero form")
    @PostMapping(value = "/createHero", produces = "text/html")
    public String createHero(@ModelAttribute SuperHero superHero) {
        heroRepository.save(superHero);
        return "createHero";
    }

    @ApiOperation(value = "View create mission form")
    @GetMapping(value = "/createMission", produces = "text/html")
    public String createMissionForm(Model model) {
        model.addAttribute("mission", new Mission());
        return "createMissionForm";
    }

    @ApiOperation(value = "Submit create mission form")
    @PostMapping(value = "/createMission", produces = "text/html")
    public String createMission(@ModelAttribute Mission mission) {
        missionRepository.save(mission);
        return "createMission";
    }

    @ApiOperation(value = "View mission page")
    @GetMapping(value = "/showMissions", produces = "text/html")
    public String showMissions(Model model){

        List<Mission> missions = new ArrayList<>();
        Iterator<Mission> iterator = missionRepository.findAll().iterator();

        while (iterator.hasNext()) {
            missions.add(iterator.next());
        }
        model.addAttribute("missions", missions);

        return "showMissions";
    }

}
