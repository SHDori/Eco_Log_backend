package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.BehaviorNameChangeRequestDto;
import Eco_Log.Eco_Log.controller.dto.BehaviorsDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import Eco_Log.Eco_Log.service.BehaviorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BehaviorController {

    private final BehaviorRepository behaviorRepository;
    private final BehaviorService behaviorService;


//    @GetMapping("/behavior")
//    public List<BehaviorsDto> getAllBehaviors(){
//
//
//        return behaviorRepository.getAllBehavior();
//    }

    @GetMapping("/behavior")
    public ResponseEntity<List<BehaviorsDto>> getAllBehaviors(){
        List<BehaviorsDto> behaviorsList = behaviorRepository.getAllBehavior();

        return ResponseEntity.ok().body(behaviorsList);
    }

    @PutMapping("/behavior")
    public String setBehaviorName(HttpServletRequest request, @RequestBody BehaviorNameChangeRequestDto requestDto){


        return behaviorService.changeBehaviorName(requestDto);




    }
}
