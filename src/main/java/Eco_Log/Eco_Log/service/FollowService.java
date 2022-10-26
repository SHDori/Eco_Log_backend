package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.FollowViewResponseDto;
import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.FollowRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String makeFollowRelation(Long fromUserId,Long toUserId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Users toUser = userRepository.findById(toUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ toUserId));

        Follow follow = followRepository.save(new Follow(fromUserId,toUserId));

        return follow.toString();
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
}
