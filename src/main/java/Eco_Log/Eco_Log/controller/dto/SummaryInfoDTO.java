package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SummaryInfoDTO {

    private Long behaviorId;
    private String name;
    private Long count;

    public SummaryInfoDTO(Long behaviorId, String name, Long count) {
        this.behaviorId = behaviorId;
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "SummaryInfoDTO{" +
                "behaviorId=" + behaviorId +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
