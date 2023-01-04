package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.BehaviorNameChangeRequestDto;
import Eco_Log.Eco_Log.controller.dto.BehaviorViewResponseDto;
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
    public ResponseEntity<BehaviorViewResponseDto> getAllBehaviors(){


        return ResponseEntity.ok().body(behaviorService.getAllBehaviors());
    }

    @PutMapping("/behavior")
    public String setBehaviorName(HttpServletRequest request, @RequestBody BehaviorNameChangeRequestDto requestDto){


        return behaviorService.changeBehaviorName(requestDto);




    }
}
