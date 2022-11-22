package Eco_Log.Eco_Log.controller.dto;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProfileViewResponseDto {


    private Long userId;
    private String userNickname;
    private Long userPostTotalCount;
    private String selfIntroduce;
    private List<SummaryInfoDTO> userSummary;
    private List<String> recentlyCustomBehaviorList;
    private String createAt;
    private int behaviorCount;

    private boolean isPublic;
    private boolean isMyProfile;
    private boolean isAlreadyFollow;



    public void setMyProfile(boolean myProfile) {
        isMyProfile = myProfile;
    }

    public void setAlreadyFollow(boolean alreadyFollow) {
        isAlreadyFollow = alreadyFollow;
    }

    public void setRecentlyCustomBehaviorList(List<String> recentlyCustomBehaviorList) {
        this.recentlyCustomBehaviorList = recentlyCustomBehaviorList;
    }

    public ProfileViewResponseDto(Long userId, String userNicname, Long userPostTotalCount, List<SummaryInfoDTO> userSummary, String selfIntroduce, boolean isPublic, LocalDateTime createAt,int behaviorCount) {
        this.userId = userId;
        this.userNickname = userNicname;
        this.userPostTotalCount = userPostTotalCount;
        this.userSummary = userSummary;
        this.selfIntroduce = selfIntroduce;
        this.isPublic = isPublic;
        this.createAt = String.valueOf(createAt).substring(0,10);
        this.isMyProfile = false;
        this.isAlreadyFollow= false;
        this.behaviorCount = behaviorCount;
    }
}
