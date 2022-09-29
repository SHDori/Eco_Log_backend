package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.PostListResponseDTO;
import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.controller.dto.PostUpdateRequestDto;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.PostsRepository;
import Eco_Log.Eco_Log.repository.UserRepository;
import Eco_Log.Eco_Log.service.doingListMap.DoingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final BehaviorMappingService behaviorMappingService;


    /**
     * 게시물 저장하기
     * @param saveRequestDto
     * @return
     */
    @Transactional
    public Long save(PostSaveRequestDto saveRequestDto){


        Long userId = Long.parseLong(saveRequestDto.getUserId());
        // 0. 엔티티 조회
        Users users = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        // 1. Post저장하고
//        Long result = postsRepository.save(saveRequestDto.toEntity()).getId();
        Posts posts = Posts.createPost(users,saveRequestDto);
        postsRepository.save(posts);

        // 2. post안의 활동내역을 User활동내역에 반영한다.
        behaviorMappingService.behaviorCountPlus(users,saveRequestDto.getDoingList());

        // 3. 뱃지획득 결과를 반환한다


        return posts.getId();
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
        Posts updateTargetPosts = postsRepository.findById(updateRequestDto.getPostId())
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. 게시물 id = "+updateRequestDto.getPostId()));

        // 3. 목표게시물 doingList별로 User활동내역에서 -1
        behaviorMappingService.behaviorCountMinus(user,updateTargetPosts.getDoingList());

        // 4. 게시물 update
        Long result = updateTargetPosts.update(updateRequestDto);
        behaviorMappingService.behaviorCountPlus(user,updateRequestDto.getDoingList());

        // 5. 뱃지획득 결과를 반환한다

        return result;

    }


    /**
     * 삭제하기
     */

    @Transactional
    public void delete(Long userId,Long postId){

        Users targetUser =  userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        Posts targetPost = postsRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. 게시물 id = "+postId));

        // 만약 지우려는 게시물의 userId와 요청한 userId가 같다면 삭제
        if (targetPost.getUsers().getId()==userId){
            // user의 summary에서 post에있는 행동을 다 내린뒤
            behaviorMappingService.behaviorCountMinus(targetUser,targetPost.getDoingList());
            // 지운다
            postsRepository.delete(targetPost);
        }

    }

    /**
     * 월 단위 게시물 조회하기
     *
     */
    public List<PostListResponseDTO> findPostByMonth(Long userId,String month){
        return postsRepository.findByMonth(userId, month).stream()
                .map(PostListResponseDTO::new)
                .collect(Collectors.toList());
    }


}
