package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.domain.Heart;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.HeartRepository;
import Eco_Log.Eco_Log.repository.PostsRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class HeartService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final HeartRepository heartRepository;
    /**
     * 하트 누르기
     */
    public List<Integer> makeHeartInfo(Long fromUserId,Long toPostId){

        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Posts targetPost = postsRepository.findById(toPostId)
                .orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다. id = "+ toPostId));

        Heart heartCheck = heartRepository.findSpecificHeartInfo(fromUserId,toPostId);


        if(heartCheck==null) {
            Heart heart = Heart.createHeart(targetPost, fromUserId);
            heartRepository.save(heart);

            List<Integer> badgeNotifyList = new ArrayList<>();

            String badgeState = fromUser.getBadgeState();
            List<Character> badgeStateList = new ArrayList<>();

            for (int i = 0; i < badgeState.length(); i++) {
                badgeStateList.add(badgeState.charAt(i));
            }

            if (badgeStateList.get(8) == '0') {
                // 123456
                if (heartRepository.findOtherPostHeartList(fromUserId).size() >= 3) {
                    badgeNotifyList.add(8);
                    StringBuilder badgeList = new StringBuilder(badgeState);
                    badgeList.setCharAt(8, '1');
                    fromUser.setBadgeState(badgeList.toString());
                }
            } else if (badgeStateList.get(9) == '0') {
                if (heartRepository.findOtherPostHeartList(fromUserId).size() >= 30) {
                    badgeNotifyList.add(9);
                    StringBuilder badgeList = new StringBuilder(badgeState);
                    badgeList.setCharAt(9, '1');
                    fromUser.setBadgeState(badgeList.toString());
                }
            }

            return badgeNotifyList;
            //return heart.toString();
        }else{
            List<Integer> result = new ArrayList<>();
            result.add(-1);
            result.add(-1);
            return result;
        }
    }


    /**
     * 하트 취소하기
     */
    public String cancelHeart(Long fromUserId,Long toPostId){
        Users fromUser = userRepository.findById(fromUserId)
                .orElseThrow(()-> new IllegalArgumentException("해당 User가 없습니다. id = "+ fromUserId));
        Posts targetPost = postsRepository.findById(toPostId)
                .orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다. id = "+ toPostId));
        Heart targetHeart = heartRepository.findSpecificHeartInfo(fromUserId,targetPost.getId());
        if(targetHeart == null){
            return "userId=>"+fromUserId+"의 user는 게시물id=>"+targetPost.getId()+"의 게시물에이미 좋아요가 취소되어있습니다";
        }else{
            //먼저 게시물에 heart정보를 지우고
            targetPost.deleteHeart(targetHeart);
            // DB에서도 heart를지운다.
            heartRepository.delete(targetHeart);
            return "성공적으로 좋아요가 취소되었습니다.";
        }
    }

    /**
     * 하트 눌렀는지 확인
     * 이미 하트를 누른유저면 true
     * 아니면 false반환
     */
    public boolean isHeartPushCheck(Long userId,Long postId){
        Heart heartInfo = heartRepository.findSpecificHeartInfo(userId,postId);

        if(heartInfo == null){
            return false;
        }else{
            return true;
        }
    }
    @Transactional(readOnly = true)
    public List<Heart> findAllHeartByPostId(Long postId){
        return heartRepository.findAllByPostsId(postId);
    }
}
