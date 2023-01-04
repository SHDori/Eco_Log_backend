package Eco_Log.Eco_Log.controller.dto;

import Eco_Log.Eco_Log.domain.post.Behaviors;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BehaviorViewResponseDto {

    private List<BehaviorsDto> spend;
    private List<BehaviorsDto> outdoor;
    private List<BehaviorsDto> living;
    private List<BehaviorsDto> eat;
    private List<BehaviorsDto> etc;

    public BehaviorViewResponseDto() {
        spend=new ArrayList<>();
        outdoor=new ArrayList<>();
        living=new ArrayList<>();
        eat=new ArrayList<>();
        etc=new ArrayList<>();
    }
}
