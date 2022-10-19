package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.BehaviorsDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BehaviorController {

    private final BehaviorRepository behaviorRepository;


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
}
