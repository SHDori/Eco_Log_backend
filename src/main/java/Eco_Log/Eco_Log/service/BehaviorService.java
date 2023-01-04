package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.BehaviorNameChangeRequestDto;
import Eco_Log.Eco_Log.controller.dto.BehaviorViewResponseDto;
import Eco_Log.Eco_Log.controller.dto.BehaviorsDto;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.repository.BehaviorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BehaviorService {

    private final BehaviorRepository behaviorRepository;

    public BehaviorViewResponseDto getAllBehaviors(){
        List<BehaviorsDto> behaviorsList = behaviorRepository.getAllBehavior();
        BehaviorViewResponseDto responseDto = new BehaviorViewResponseDto();
        for (BehaviorsDto target : behaviorsList){
            String category = target.getName().substring(0,target.getName().lastIndexOf("@"));
            target.setName(target.getName().substring(target.getName().lastIndexOf("@")+1));
            if(category.equals("소비")){
                responseDto.getSpend().add(target);
            }else if(category.equals("외출")){
                responseDto.getOutdoor().add(target);
            }else if(category.equals("생활")){
                responseDto.getLiving().add(target);
            }else if(category.equals("식사")){
                responseDto.getEat().add(target);
            }else{
                responseDto.getEtc().add(target);
            }


        }
        return responseDto;
    }



    @Transactional
    public String changeBehaviorName(BehaviorNameChangeRequestDto requestDto){

        Behaviors targetBehavior = behaviorRepository.findById(requestDto.getBehaviorId())
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+requestDto.getBehaviorId()));
        String fromName = targetBehavior.getName();
        targetBehavior.update(requestDto.getName());

        return "행동id="+targetBehavior.getId()+"의 이름이 "+fromName+"에서 "+targetBehavior.getName()+"으로 변경되었습니다.";

    }
}
