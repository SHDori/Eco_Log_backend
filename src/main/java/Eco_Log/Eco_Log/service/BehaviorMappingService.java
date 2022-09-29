package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.service.doingListMap.DoingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BehaviorMappingService {


    // 일단테스트중
    private DoingMapper doingMapper  = new DoingMapper();

    @Transactional
    public void behaviorCountPlus(Users user, List<String> doingList){


        for(String behaviorName : doingList){
            int behaviorIndex = doingMapper.mappingList.get(behaviorName);
            if (behaviorIndex==0){
                user.getSummary().plusCountOfConsumption();
            }
            else if (behaviorIndex==1){
                user.getSummary().plusCountOfOutSide();
            }
            else if (behaviorIndex==2){
                user.getSummary().plusCountOfLiving();
            }
            else if (behaviorIndex==3){
                user.getSummary().plusCountOfEat();
            }
            else if (behaviorIndex==4){
                user.getSummary().plusCountOfOthers();
            }
        }
    }


    @Transactional
    public void behaviorCountMinus(Users user, List<String> doingList){


        for(String behaviorName : doingList){
            int behaviorIndex = doingMapper.mappingList.get(behaviorName);
            if (behaviorIndex==0){
                user.getSummary().minusCountOfConsumption();
            }
            else if (behaviorIndex==1){
                user.getSummary().minusCountOfOutSide();
            }
            else if (behaviorIndex==2){
                user.getSummary().minusCountOfLiving();
            }
            else if (behaviorIndex==3){
                user.getSummary().minusCountOfEat();
            }
            else if (behaviorIndex==4){
                user.getSummary().minusCountOfOthers();
            }
        }
    }
}
