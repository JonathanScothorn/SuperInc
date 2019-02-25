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
@Api(value = "Mission management controller", description = "Operations pertaining exclusively to missions")
public class MissionController {

    @Autowired
    private MissionRepository missionRepository;

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
