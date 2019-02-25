package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<String> createMissionCurl(@RequestParam String name, Model model) {
        Mission mission = new Mission(name);
        missionRepository.save(mission);
        model.addAttribute("mission", mission);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<String> createHeroCurl(@RequestParam String firstName, @RequestParam String lastName,
                                                 @RequestParam String heroName, Model model) {
        SuperHero superHero = new SuperHero(firstName, lastName, heroName);
        heroRepository.save(superHero);
        model.addAttribute("superHero", superHero);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/showMissions")
    public String showMissions(Model model){

        List<Mission> missions = new ArrayList<>();
        Iterator<Mission> iterator = missionRepository.findAll().iterator();

        while (iterator.hasNext()) {
            missions.add(iterator.next());
        }
        model.addAttribute("missions", missions);

        return "showMissions";
    }

    @PostMapping("/addMission")
    public ResponseEntity<String> addMission(@RequestParam Long heroId, @RequestParam Long missionId) {

        Optional<Mission> optM = missionRepository.findById(missionId);
        Optional<SuperHero> optH = heroRepository.findById(heroId);
        Mission mission;
        SuperHero superHero;

        if (optM.isPresent() && optH.isPresent()) {
            mission = optM.get();
            superHero = optH.get();

            // if either already has a link to the other, something is wrong so do not continue the transaction
            if (!superHero.hasMission(mission) && !mission.hasHero(superHero)) {
                superHero.addMission(mission);
                mission.addHero(superHero);
                missionRepository.save(mission);
                heroRepository.save(superHero);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mission-Hero link incorrect");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IDs entered.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/removeMission")
    public ResponseEntity<String> removeMission(@RequestParam Long heroId, @RequestParam Long missionId) {

        Optional<Mission> optM = missionRepository.findById(missionId);
        Optional<SuperHero> optH = heroRepository.findById(heroId);
        Mission mission;
        SuperHero superHero;

        if (optM.isPresent() && optH.isPresent()) {
            mission = optM.get();

            if (!mission.getCompleted()) {
                superHero = optH.get();

                // if either does not have a link to the other, something is wrong so do not continue the transaction
                if (superHero.hasMission(mission) && mission.hasHero(superHero)) {
                    superHero.removeMission(mission);
                    mission.removeHero(superHero);
                    missionRepository.save(mission);
                    heroRepository.save(superHero);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mission-Hero link incorrect");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Completed mission cannot be removed");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid IDs entered.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteHero/{id}")
    public ResponseEntity<String> deleteHero(@PathVariable Long id) {

        Optional<SuperHero> optH = heroRepository.findById(id);
        SuperHero hero;

        if (optH.isPresent()) {
            hero = optH.get();

            if (hero.missionSize() == 0) {
                heroRepository.delete(hero);
            } else {
                // delete references to and from the hero, avoiding concurrent modification exception
                for (Iterator<Mission> iterator = hero.getMissions().iterator(); iterator.hasNext();) {
                    Mission m = iterator.next();
                    m.removeHero(hero);
                    iterator.remove();
                }
                heroRepository.delete(hero);
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hero ID");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteMission/{id}")
    public ResponseEntity<String> deleteMission(@PathVariable Long id) {

        Optional<Mission> optM = missionRepository.findById(id);
        Mission mission;

        if (optM.isPresent()) {
            mission = optM.get();
            mission.setDeleted(true);
            missionRepository.save(mission);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid mission ID");
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
