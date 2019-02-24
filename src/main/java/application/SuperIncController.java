package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class SuperIncController {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private SuperHeroRepository heroRepository;

    @GetMapping("/")
    public String homepage() {
        return "home";
    }

    @GetMapping("/createMission")
    public String createMissionForm(Model model) {
        model.addAttribute("mission", new Mission());
        return "createMissionForm";
    }

    @PostMapping("/createMission")
    public String createMission(@ModelAttribute Mission mission) {
        missionRepository.save(mission);
        return "createMission";
    }

    @PostMapping("/createMissionCurl")
    public String createMissionCurl(@RequestParam String name, Model model) {
        Mission mission = new Mission(name);
        missionRepository.save(mission);
        model.addAttribute("mission", mission);
        return "createMission";
    }

    @GetMapping("/createHero")
    public String createHeroForm(Model model) {
        model.addAttribute("superHero", new SuperHero());
        return "createHeroForm";
    }

    @PostMapping("/createHero")
    public String createHero(@ModelAttribute SuperHero superHero) {
        heroRepository.save(superHero);
        return "createHero";
    }

    @PostMapping("/createHeroCurl")
    public String createHeroCurl(@RequestParam String firstName, @RequestParam String lastName,
                                 @RequestParam String heroName, Model model) {
        SuperHero superHero = new SuperHero(firstName, lastName, heroName);
        heroRepository.save(superHero);
        model.addAttribute("superHero", superHero);
        return "createHero";
    }

    @GetMapping("/showMissions")
    public String showMissions(Model model){

        List<Mission> missions = new ArrayList<Mission>();
        Iterator<Mission> iterator = missionRepository.findAll().iterator();

        while (iterator.hasNext()) {
            missions.add(iterator.next());
        }
        model.addAttribute("missions", missions);

        return "showMissions";
    }

}
