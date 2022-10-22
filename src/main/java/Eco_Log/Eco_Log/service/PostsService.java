package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.*;
import Eco_Log.Eco_Log.controller.dto.postDto.PostListResponseDTO;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostUpdateRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostViewResponseDto;
import Eco_Log.Eco_Log.domain.Heart;
import Eco_Log.Eco_Log.domain.post.Behaviors;
import Eco_Log.Eco_Log.domain.post.PRconnect;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final BehaviorRepository behaviorRepository;
    private final PRconnectRepository pRconnectRepository;
    private final HeartService heartService;
    private final HeartRepository heartRepository;
    private final FollowRepository followRepository;

    private List<String> customBehaviorList = new ArrayList<>();

    public List<String> getCustomBehaviorList() {
        return customBehaviorList;
    }

    /**
     * 게시물 저장하기
     * @param saveRequestDto
     * @return
     */
    @Transactional
    public ResponseEntity save(Long userId, PostSaveRequestDto saveRequestDto){


//        Long userId = Long.parseLong(saveRequestDto.getUserId());
        // 0. 엔티티 조회
        Users users = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        // 1. Post저장하고
//        Long result = postsRepository.save(saveRequestDto.toEntity()).getId();
        // 만약 해당일에 이미 있다면 400에러
        Posts postCheck = postsRepository.findByDay(userId,saveRequestDto.getDoingDay());

        if(postCheck == null) {

            Posts posts = Posts.createPost(users, saveRequestDto);
            postsRepository.save(posts);
            for (String behaviorId : saveRequestDto.getBehaviorList()) {
                Long bId = Long.parseLong(behaviorId);
                Behaviors behaviors = behaviorRepository.findById(bId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 행동이 없습니다. id = " + bId));
                // 2. post안의 활동내역을 User활동내역에 반영한다.
                PRconnect pRconnect = PRconnect.createPRconnect(posts, behaviors);

                pRconnectRepository.save(pRconnect);
            }

            if(saveRequestDto.getCustomizedBehaviors().size()>0){
                int count = 0;
                for(String customBehavior : saveRequestDto.getCustomizedBehaviors()){
                    if(customBehaviorList.size()<3){
                        customBehaviorList.add(customBehavior);
                    }else{
                        customBehaviorList.remove(0);
                        customBehaviorList.add(customBehavior);
                    }
                    if(count <3){
                        count++;
                    }else{
                        break;
                    }
                }
            }


            // 3. 뱃지획득 결과를 반환한다


            return ResponseEntity.ok().body(posts.getId());
        }else{
            return ResponseEntity.badRequest().body("해당 날짜에 이미 게시물이 있습니다.");
        }
    }

    /**
     * 수정하기
     */

    @Transactional
    public Long update(Long userId, PostUpdateRequestDto updateRequestDto){
        // 1. User 조회
        Users user =  userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));
        // 2. update 목표 게시물 조회
        Posts targetPost = postsRepository.findById(updateRequestDto.getPostId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. 게시물 id = "+updateRequestDto.getPostId()));

        // 3. 목표게시물 doingList별로 User활동내역에서 -1
        //behaviorMappingService.behaviorCountMinus(user,updateTargetPosts.getDoingList());
        if (targetPost.getUsers().getId()==userId){
            List<PRconnect> targetPRList = pRconnectRepository.findAllByPostId(targetPost.getId());
            // 기존의 PRList를 다 지우고
            for( PRconnect target : targetPRList){
                pRconnectRepository.delete(target);
            }

            for(String behaviorId:updateRequestDto.getBehaviorList()){
                Long bId = Long.parseLong(behaviorId);
                Behaviors behaviors = behaviorRepository.findById(bId)
                        .orElseThrow(()-> new IllegalArgumentException("해당 행동이 없습니다. id = "+bId));
                PRconnect pRconnect = PRconnect.createPRconnect(targetPost,behaviors);

                pRconnectRepository.save(pRconnect);
            }

        }


        // 4. 게시물 update
        Long result = targetPost.update(updateRequestDto);
        //behaviorMappingService.behaviorCountPlus(user,updateRequestDto.getDoingList());

        // 5. 뱃지획득 결과를 반환한다

        return result;

    }


    /**
     * 삭제하기
     * 1. 넘어온 user와 post가 유효한애들인지 확인
     * 2. 삭제할 게시물에 관련된 heart정보 모두 삭제
     * 3. 삭제할 게시물에 관련된 행동연결테이블데이터 모두 삭제
     * 4. user안의 List에서 삭제할 게시물 삭제
     * 5. DB에서 게시물 삭제
     */

    @Transactional
    public Long delete(Long userId,Long postId){

        Users targetUser =  userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        Posts targetPost = postsRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. 게시물 id = "+postId));

        // 만약 지우려는 게시물의 userId와 요청한 userId가 같다면 삭제
        if (targetPost.getUsers().getId()==userId){

            // heartTable에있는 자신의 heart정보를 다 가져옴
            List<Heart> targetHearts = heartService.findAllHeartByPostId(targetPost.getId());
            // 그리고 삭제
            for(Heart heart: targetHearts){
                heartRepository.delete(heart);
            }

            // user의 summary에서 post에있는 행동을 다 내린뒤

            List<PRconnect> targetPRList = pRconnectRepository.findAllByPostId(targetPost.getId());
            for( PRconnect target : targetPRList){
                pRconnectRepository.delete(target);
            }
            targetUser.deletePost(targetPost);
            // 지운다
            postsRepository.delete(targetPost);
        }
        return postId;

    }

    /**
     * 월 단위 게시물 조회하기
     *
     */
    @Transactional(readOnly = true)
    public List<PostListResponseDTO> findPostByMonth(Long userId, String month){
        return postsRepository.findByMonth(userId, month).stream()
                .map(PostListResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 일 단위 게시물 조회하기 => 초기버전 내 게시물만 조회
     * 1. 해당날짜의 user의 게시물들을 다 조회.(나중엔 Following들도)
     *      < 유저별로 >
     *      2. 그 특정된 PostID를가지고있는 PRconnect(행동연결정보)를 다 조회
     *      3. 행동ID를 기반으로 다 돌면서 List에 이름으로 넣음
     * 4. 반환
     */
    @Transactional(readOnly = true)
    public List<PostViewResponseDto> findPostByDay(Long userId, String day){
        List<PostViewResponseDto> postsByDay = new ArrayList<>();

        Users user =  userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        Posts targetPost = postsRepository.findByDay(userId,day);

        List<PRconnect> prList = pRconnectRepository.findAllByPostId(targetPost.getId());

        List<String> behaviorList = new ArrayList<>();
        for(PRconnect data:prList){
            Behaviors behaviors = behaviorRepository.findById(data.getBehaviors().getId())
                    .orElseThrow(()-> new IllegalArgumentException("해당 행동이 없습니다. id = "+data.getBehaviors().getId()));
            behaviorList.add(behaviors.getName());
        }

        UserSimpleInfoInPostDto userInfo = new UserSimpleInfoInPostDto(user.getId(),user.getProfiles().getNickName(),user.getProfiles().getSelfIntroduce());
        PostViewResponseDto postData = new PostViewResponseDto(targetPost.getId(),userInfo,targetPost.getCustomBehaviorList(),behaviorList, targetPost.getComment(),targetPost.getHearts().size());
        postsByDay.add(postData);
        return postsByDay;
    }

// 테스트중

    /**
     * 일 단위 게시물 조회하기 => 팔로윙 유저들꺼까지다
     * 1. 해당날짜의 user의 게시물들을 다 조회.(나중엔 Following들도)
     *      < 유저별로 >
     *      2. 그 특정된 PostID를가지고있는 PRconnect(행동연결정보)를 다 조회
     *      3. 행동ID를 기반으로 다 돌면서 List에 이름으로 넣음
     * 4. 반환
     */
    @Transactional(readOnly = true)
    public List<PostViewResponseDto> findFollowingPostByDay(Long userId, String day){
        List<PostViewResponseDto> postsByDay = new ArrayList<>();

        Users user =  userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));
        // 어떤 post를 가져와야하는 user들 list => 나 + 내following들
        List<Long> targetUserList = new ArrayList<>();
        targetUserList.add(user.getId());
        List<Long> myFollowing = followRepository.findMyFollowingUserIdByFromUserId(user.getId());
        for(Long targetUserId : myFollowing){
            targetUserList.add(targetUserId);
        }

        for(Long targetUserId : targetUserList){
            Posts targetPost = postsRepository.findByDay(targetUserId,day);

            if(targetPost == null){
                continue;
            }
            else{
                Users targetUser = userRepository.findById(targetUserId)
                        .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));
                // 그날에 대상user의 글이있다면
                List<PRconnect> prList = pRconnectRepository.findAllByPostId(targetPost.getId());
                List<String> behaviorList = new ArrayList<>();
                for(PRconnect data:prList){
                    Behaviors behaviors = behaviorRepository.findById(data.getBehaviors().getId())
                            .orElseThrow(()-> new IllegalArgumentException("해당 행동이 없습니다. id = "+data.getBehaviors().getId()));
                    behaviorList.add(behaviors.getName());
                }

                UserSimpleInfoInPostDto userInfo = new UserSimpleInfoInPostDto(targetUser.getId(),targetUser.getProfiles().getNickName(),targetUser.getProfiles().getSelfIntroduce());
                PostViewResponseDto postData = new PostViewResponseDto(targetPost.getId(),userInfo,targetPost.getCustomBehaviorList(),behaviorList, targetPost.getComment(),targetPost.getHearts().size());
                // postdata에 내가 이 게시물을 눌렀는지 확인후 set해줘야함
                boolean heartCheck = heartService.isHeartPushCheck(userId,targetPost.getId());
                postData.setAlreadyHeart(heartCheck);
                postsByDay.add(postData);
            }

        }
        return postsByDay;


    }









}
