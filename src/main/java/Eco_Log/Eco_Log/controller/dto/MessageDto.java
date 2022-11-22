package Eco_Log.Eco_Log.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageDto {

    private String message;

    @Builder
    public MessageDto(String message) {
        this.message = message;
    }
}
