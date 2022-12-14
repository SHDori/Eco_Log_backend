package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.followDto.FollowMakeResponseDto;
import Eco_Log.Eco_Log.controller.dto.followDto.FollowViewResponseDto;
import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.FollowRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {


    private final UserRepository userRepository;

    private final FollowRepository followRepository;

    /**
     * 팔로우 하기
     */
    public FollowMakeResponseDto makeFollowRelation(Long fromUserId,Long toUserId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Users toUser = userRepository.findById(toUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ toUserId));

        Follow follow = followRepository.save(new Follow(fromUserId,toUserId));

        String badgeState = fromUser.getBadgeState();
        List<Character> badgeStateList = new ArrayList<>();

        List<Integer> badgeNotifyList = new ArrayList<>();

        for(int i=0;i<badgeState.length();i++){
            badgeStateList.add(badgeState.charAt(i));
        }

        if(badgeStateList.get(7)=='0'){
            badgeNotifyList.add(7);
            StringBuilder badgeList = new StringBuilder(badgeState);
            badgeList.setCharAt(7, '1');
            fromUser.setBadgeState(badgeList.toString());
        }


        //return follow.toString();
        return new FollowMakeResponseDto(follow.toString(),badgeNotifyList);
    }



    /**
     * 팔로우 취소
     */

    public String deleteFollowRelation(Long fromUserId,Long toUserId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Users toUser = userRepository.findById(toUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ toUserId));

        ///
        Follow targetFollowInfo = followRepository.findSpecificFollowInfo(fromUserId,toUserId)
                .orElseThrow(()-> new IllegalArgumentException(fromUser.getProfiles().getNickName()+"이 "+toUser.getProfiles().getNickName()+"을 Follow하지 않습니다." ));
        followRepository.delete(targetFollowInfo);

        return fromUser.getId()+"유저가 "+toUser.getId()+"유저의 follow를 취소했습니다.";
    }

    /**
     * 팔로워 취소
     */
    public String deleteFollowerRelation(Long fromUserId,Long toUserId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Users toUser = userRepository.findById(toUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ toUserId));

        ///
        Follow targetFollowInfo = followRepository.findSpecificFollowInfo(toUserId,fromUserId)
                .orElseThrow(()-> new IllegalArgumentException(fromUser.getProfiles().getNickName()+"이 "+toUser.getProfiles().getNickName()+"을 Follow하지 않습니다." ));
        followRepository.delete(targetFollowInfo);

        return fromUser.getId()+"유저의 팔로워"+toUser.getId()+"를 follow 취소했습니다.";
    }

    @Transactional(readOnly = true)
    public boolean isFallowCheck(Long fromUserId,Long toUserId){
        Follow followInfo = followRepository.findSpecificFollowInfoNoOptional(fromUserId,toUserId);

        if (followInfo==null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 내 팔로워 조회
     */

    public List<FollowViewResponseDto> getMyFollower(Long userId){

        List<FollowViewResponseDto> responseDtos = new ArrayList<>();

        List<Long> followerIdList = followRepository.findMyFollowerUserIdByToUserId(userId);

        for(Long followerId : followerIdList){

            Users follower = userRepository.findById(followerId)
                    .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ followerId));

            responseDtos.add(new FollowViewResponseDto(follower.getId(),follower.getProfiles().getNickName(),follower.getProfiles().getSelfIntroduce()));

        }

        return  responseDtos;

    }




    /**
     * 내 팔로잉 조회
     */

    public List<FollowViewResponseDto> getMyFollowing(Long userId){

        List<FollowViewResponseDto> responseDtos = new ArrayList<>();

        List<Long> followingIdList = followRepository.findMyFollowingUserIdByFromUserId(userId);

        for(Long followerId : followingIdList){

            Users follower = userRepository.findById(followerId)
                    .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ followerId));

            responseDtos.add(new FollowViewResponseDto(follower.getId(),follower.getProfiles().getNickName(),follower.getProfiles().getSelfIntroduce()));

        }

        return responseDtos;

    }

    /**
     * 유저 삭제시 모든 팔로워 팔로잉 관계 정리
     */

    public String deleteAllFollowWhenDeleteUser(Long targetUserId){

        List<Follow> myFollowers = followRepository.findMyFollowerByUserId(targetUserId);
        List<Follow> myFollowings = followRepository.findMyFollowingByUserId(targetUserId);
        int followerCount =0;
        // 모든 팔로워 삭제
        for(Follow target : myFollowers){
            followRepository.delete(target);
            followerCount++;
        }

        int followingCount = 0;
        // 모든 팔로잉 삭제
        for(Follow target : myFollowings){
            followRepository.delete(target);
            followingCount++;
        }

        return "총 "+followerCount+"명의 팔로워와 "+followingCount+"명의 팔로잉 데이터를 삭제했습니다.";






    }
}
