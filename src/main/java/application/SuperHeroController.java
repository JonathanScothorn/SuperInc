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

import java.util.Iterator;
import java.util.Optional;

@Controller
@Api(value = "Super hero management controller", description = "Operations pertaining to heroes")
public class SuperHeroController {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private SuperHeroRepository heroRepository;

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

    @ApiOperation(value = "View superheroes")
    @GetMapping(value = "/viewHeroes", produces = "application/json")
    public ResponseEntity<Iterable<SuperHero>> viewHeroes() {
        return new ResponseEntity<>(heroRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "View a superhero's information")
    @GetMapping(value = "/viewHero/{id}", produces = "application/json")
    public ResponseEntity<SuperHero> viewHero(@ApiParam(value = "Hero's ID number") @PathVariable Long id) {

        Optional<SuperHero> optH = heroRepository.findById(id);
        SuperHero hero;

        if (optH.isPresent()) {
            hero = optH.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hero ID");
        }
        return new ResponseEntity<>(hero, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a superhero's information")
    @PatchMapping("/updateHero/{id}")
    public ResponseEntity<String> updateHero(@ApiParam(value = "New first name") @RequestParam(required = false) String firstName,
                                             @ApiParam(value = "New last name") @RequestParam(required = false) String lastName,
                                             @ApiParam(value = "New superhero name") @RequestParam(required = false) String heroName,
                                             @ApiParam(value = "Hero's ID number") @PathVariable Long id) {

        Optional<SuperHero> optH = heroRepository.findById(id);
        SuperHero hero; 

        if (optH.isPresent()) {
            hero = optH.get();
            if (firstName != null) {
                hero.setFirstname(firstName);
            }
            if (lastName != null) {
                hero.setLastname(lastName);
            }
            if (heroName != null) {
                hero.setSuperheroname(heroName);
            }
            heroRepository.save(hero);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid hero ID");
        }
        return new ResponseEntity<>(HttpStatus.OK);
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

    @ApiOperation(value = "Delete a superhero safely")
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

}
