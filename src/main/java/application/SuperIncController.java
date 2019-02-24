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
    public String createMissionCurl(@RequestParam(name = "name", required = true) String name, Model model) {
        Mission m = new Mission(name);
        missionRepository.save(m);
        model.addAttribute("name", name);
        return "createMission";
    }

    @GetMapping("/missions")
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
