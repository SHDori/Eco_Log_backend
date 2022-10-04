package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;

import java.util.List;

@Getter
public class ProfileViewResponseDto {


    private Long userId;
    private String userNicname;
    private Long userPostTotalCount;
    private List<SummaryInfoDTO> userSummary;

    public ProfileViewResponseDto(Long userId, String userNicname, Long userPostTotalCount, List<SummaryInfoDTO> userSummary) {
        this.userId = userId;
        this.userNicname = userNicname;
        this.userPostTotalCount = userPostTotalCount;
        this.userSummary = userSummary;
    }
}
