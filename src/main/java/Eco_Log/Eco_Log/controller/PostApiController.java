package Eco_Log.Eco_Log.controller;

//import Eco_Log.Eco_Log.controller.dto.MessageDto;
import Eco_Log.Eco_Log.controller.dto.MessageDto;
import Eco_Log.Eco_Log.controller.dto.postDto.*;
import Eco_Log.Eco_Log.domain.post.Posts;
import Eco_Log.Eco_Log.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostsService postsService;

    // 나중에 어떤걸 요청받더라도 User를 특정할수있는 값을 받아와야함

    /**
     * 게시물 저장
     * 1. 저장할 게시물 data를 받아온다.
     * 2. 게시물 data 중에 활동데이터를 Key값 + ; 의 조합으로 바꾼다.
     *    (Hash Map을 통해서 String을 해당하는 숫자로 변환)
     * 3. 해당 key값에 대응하는 user의 활통 count데이터를 +1 해줌
     * 4. 게시물 저장.
     * 5. 뱃지 조건에 맞는지 체크.
     */
//    @PostMapping("/api/post")
//    public Long save(HttpServletRequest request, @RequestBody PostSaveRequestDto saveDto){
//        Long userId = (Long) request.getAttribute("userId");
//
//
//        return postsService.save(userId,saveDto);
//    }

    @PostMapping("/api/post")
    public ResponseEntity save(HttpServletRequest request, @RequestBody PostSaveRequestDto saveDto){
        Long userId = (Long) request.getAttribute("userId");
        Long resultCode = postsService.save(userId,saveDto);

        if(resultCode==-1l){
            return ResponseEntity.badRequest().body(new MessageDto("해당 날짜에 이미 게시물이 있습니다."));
            //return ResponseEntity.badRequest().body("해당 날짜에 이미 게시물이 있습니다.");
        }else{
            return ResponseEntity.ok().body(resultCode);
        }
        //return postsService.save(userId,saveDto);
    }

    /**
     * 게시물 수정
     * 1. 수정할 게시물 data를 받아온다.
     * 2. 수정하려는 게시물의 doingBehavior를 가져온다.
     * 3. doingBehavior를 ;단위로 자른뒤
     *    해당하는 index의 user 의 활동 count데이터를 -1 씩해준다.
     * 4. 수정할 data 중에 활동데이터를 Key값 + ; 의 조합으로 바꾼다.
     *    (Hash Map을 통해서 String을 해당하는 숫자로 변환)
     * 5. 해당 key값에 대응하는 user의 활통 count데이터를 +1 해줌
     * 6. 게시물 수정.
     * 7. 뱃지 조건에 맞는지 체크.
     */
    @PutMapping("/api/post/change")
    public ResponseEntity postUpdate(HttpServletRequest request, @RequestBody PostUpdateRequestDto updateRequestDto){
        Long userId = (Long) request.getAttribute("userId");
        //일단 하드코딩
        Long targetPostId = postsService.update(userId,updateRequestDto);
        return ResponseEntity.ok().body(targetPostId+"번 게시물이 변경 되었습니다");
    }


    /**
     * 게시물 삭제
     * 1. 삭제하려는 게시물을 가져온다.
     * 2. 삭제하려는 게시물의 doingBehavior를 가져온다.
     * 3. doingBehavior를 ;단위로 자른뒤
     *   해당하는 index의 user 의 활동 count데이터를 -1 씩해준다.
     * 4. 게시물 삭제.
     */
    @DeleteMapping("/api/post")
    public ResponseEntity postDelete(HttpServletRequest request, @RequestBody PostDeleteRequestDto deleteRequestDto){
        Long userId = (Long) request.getAttribute("userId");
        //일단 하드코딩
        Long targetPostId = deleteRequestDto.getPostId();
        Long resultId = postsService.delete(userId,targetPostId);
        return ResponseEntity.ok().body(resultId+"번 게시물이 삭제 되었습니다");
    }

    /**
     * 게시물 월단위 조회 => for 캘린더 마킹을 위해
     * 1. 조회하려는 월을 Parameter로 받는다.
     * 2. 조회하려는 월 + 사용자 정보 + Following정보를 조합해서 List<String>로 반환.
     */
    @GetMapping("/api/post/Monthly")
    public ResponseEntity<List<String>> findMonthlyPostingDayForMarking(HttpServletRequest request,@RequestParam("month") String targetMonth){
        Long userId = (Long) request.getAttribute("userId");
        List<PostListResponseDTO> targetMonthPostingData =postsService.findPostByMonth(userId,targetMonth);
        List<String> postingDayList = new ArrayList<>();

        for (PostListResponseDTO data:targetMonthPostingData){
            postingDayList.add(data.getDoingDay());
        }

        return ResponseEntity.ok().body(postingDayList);

    }

    /**
     * 게시물 일단위 조회 => for 캘린더 마킹을 위해
     * 1. 조회하려는 일을 Parameter로 받는다.
     * 2. 조회하려는 일 + 사용자 정보 + Following정보를 조합해서 List<Post>로 반환.
     */
//    @GetMapping("/api/post/daily")
//    public List<PostViewResponseDto> findAllpostingByDay(HttpServletRequest request, @RequestParam("day") String targetDay){
//        Long userId = (Long) request.getAttribute("userId");
//
//
//        return postsService.findPostByDay(userId,targetDay);
//
//    }

    /**
     * 게시물 일단위 조회 => for 캘린더 마킹을 위해
     * 1. 조회하려는 일을 Parameter로 받는다.
     * 2. 조회하려는 일 + 사용자 정보 + Following정보를 조합해서 List<Post>로 반환.
     */
    @GetMapping("/api/post/daily")
    public ResponseEntity<List<PostViewResponseDto>> findAllpostingByDayPlusFollowing(HttpServletRequest request, @RequestParam("day") String targetDay){
        Long userId = (Long) request.getAttribute("userId");
        List<PostViewResponseDto> responseData = postsService.findFollowingPostByDay(userId,targetDay);

        return ResponseEntity.ok().body(responseData);

    }

}
