package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;

@Getter
public class BehaviorsDto {

    private Long behaviorId;

    private String name;


    public BehaviorsDto(Long behaviorId, String name) {
        this.behaviorId = behaviorId;
        this.name = name;
    }
}
