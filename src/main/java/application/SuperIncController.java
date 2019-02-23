package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class SuperIncController {

    @Autowired
    MissionRepository missionRepository;

    @GetMapping("/home")
    public String test(@RequestParam(name = "input", required = false, defaultValue = "-") String input, Model model) {
        model.addAttribute("input",input);
        return "home";
    }

    @PostMapping("/createMission")
    public String createMission(@RequestParam(name = "name", required = true) String name, Model model) {
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
