package application;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "Super hero company management controller", description = "Operations pertaining to hero and mission management")
public class SuperIncController {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private SuperHeroRepository heroRepository;

    @ApiOperation(value = "View the homepage")
    @GetMapping(value = "/", produces = "text/html")
    public String homepage() {
        return "home";
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

    @ApiOperation(value = "Create a new mission")
    @PostMapping("/createMissionCurl")
    public ResponseEntity<String> createMissionCurl(@RequestParam String name, Model model) {
        Mission mission = new Mission(name);
        missionRepository.save(mission);
        model.addAttribute("mission", mission);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @ApiOperation(value = "Create a new superhero")
    @PostMapping("/createHeroCurl")
    public ResponseEntity<String> createHeroCurl(@ApiParam(value = "Hero's first name") @RequestParam String firstName,
                                                 @ApiParam(value = "Hero's last name") @RequestParam String lastName,
                                                 @ApiParam(value = "Hero's superhero identity") @RequestParam String heroName,
                                                 Model model) {
        SuperHero superHero = new SuperHero(firstName, lastName, heroName);
        heroRepository.save(superHero);
        model.addAttribute("superHero", superHero);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @ApiOperation(value = "Send a superhero on a mission")
    @PostMapping("/addMission")
    public ResponseEntity<String> addMission(@ApiParam(value = "Hero's ID number") @RequestParam Long heroId,
                                             @ApiParam(value = "Mission's ID number") @RequestParam Long missionId) {

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

    @ApiOperation(value = "Remove a superhero from a non-completed mission")
    @PostMapping("/removeMission")
    public ResponseEntity<String> removeMission(@ApiParam(value = "Hero's ID number") @RequestParam Long heroId,
            @ApiParam(value = "Mission's ID number, must not be already completed") @RequestParam Long missionId) {

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

    @ApiOperation(value = "Delete a superhero")
    @DeleteMapping("/deleteHero/{id}")
    public ResponseEntity<String> deleteHero(@ApiParam(value = "The ID number of the hero to be deleted") @PathVariable Long id) {

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

    @ApiOperation(value = "Soft delete a mission")
    @DeleteMapping("/deleteMission/{id}")
    public ResponseEntity<String> deleteMission(@ApiParam(value = "ID number of the mission to be deleted") @PathVariable Long id) {

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
