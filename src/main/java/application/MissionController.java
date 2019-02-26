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

import javax.swing.text.html.Option;
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

    @ApiOperation(value = "View missions")
    @GetMapping(value = "/viewMissions", produces = "application/json")
    public ResponseEntity<Iterable<Mission>> viewMissions() {
        return new ResponseEntity<>(missionRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "View a mission")
    @GetMapping(value = "/viewMission/{id}", produces = "application/json")
    public ResponseEntity<Mission> viewMission(@ApiParam(value = "Mission's ID number") @PathVariable Long id) {

        Optional<Mission> optM = missionRepository.findById(id);
        Mission mission;

        if (optM.isPresent()) {
            mission = optM.get();
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid mission ID");
        }
        return new ResponseEntity<>(mission, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a mission, but may not soft delete here")
    @PatchMapping("/updateMission/{id})")
    public ResponseEntity<String> updateMission(@ApiParam(value = "New mission name") @RequestParam(required = false) String missionName,
                                                @ApiParam(value = "New mission completion state") @RequestParam(required = false) Boolean isCompleted,
                                                @ApiParam(value = "Mission's ID number") @PathVariable Long id) {

        Optional<Mission> optM = missionRepository.findById(id);
        Mission mission;

        if (optM.isPresent()) {
            mission = optM.get();
            if (missionName != null) {
                mission.setMissionName(missionName);
            }
            if (isCompleted != null) {
                mission.setCompleted(isCompleted);
            }
            missionRepository.save(mission);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid mission ID");
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
