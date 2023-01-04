package Eco_Log.Eco_Log.controller.dto;

import lombok.Getter;

@Getter
public class BehaviorNameChangeRequestDto {
    private Long behaviorId;

    private String name;

    public BehaviorNameChangeRequestDto(Long behaviorId, String name) {
        this.behaviorId = behaviorId;
        this.name = name;
    }
}


