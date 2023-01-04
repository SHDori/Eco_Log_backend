package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.BehaviorNameChangeRequestDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BehaviorService {

    private final BehaviorRepository behaviorRepository;

    @Transactional
    public String changeBehaviorName(BehaviorNameChangeRequestDto requestDto){

        Behaviors targetBehavior = behaviorRepository.findById(requestDto.getBehaviorId())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+requestDto.getBehaviorId()));
        String fromName = targetBehavior.getName();
        targetBehavior.update(requestDto.getName());

        return "행동id="+targetBehavior.getId()+"의 이름이 "+fromName+"에서 "+targetBehavior.getName()+"으로 변경되었습니다.";

    }
}
