package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.domain.Follow;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.FollowRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class FollowService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
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

    public void deleteFollowRelation(Long fromUserId,Long toUserId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Users toUser = userRepository.findById(toUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ toUserId));

        ///
        Follow targetFollowInfo = followRepository.findSpecificFollowInfo(fromUserId,toUserId)
                .orElseThrow(()-> new IllegalArgumentException(fromUser.getProfiles().getNickName()+"이 "+toUser.getProfiles().getNickName()+"을 Follow하지 않습니다." ));
        followRepository.delete(targetFollowInfo);


    }


    public boolean isFallowCheck(Long fromUserId,Long toUserId){
        Follow followInfo = followRepository.findSpecificFollowInfoNoOptional(fromUserId,toUserId);

        if (followInfo==null){
            return false;
        }else{
            return true;
        }
    }
}
