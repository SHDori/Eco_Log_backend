package Eco_Log.Eco_Log.controller;

import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
    @PostMapping("/api/post")
    public Long save(@RequestBody PostSaveRequestDto saveDto){
        //일단 하드코딩

        return postsService.save(saveDto);
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


    /**
     * 게시물 삭제
     * 1. 삭제하려는 게시물을 가져온다.
     * 2. 삭제하려는 게시물의 doingBehavior를 가져온다.
     * 3. doingBehavior를 ;단위로 자른뒤
     *   해당하는 index의 user 의 활동 count데이터를 -1 씩해준다.
     * 4. 게시물 삭제.
     */


    /**
     * 게시물 월단위 조회
     * 1. 조회하려는 월을 Parameter로 받는다.
     * 2. 조회하려는 월 + 사용자 정보 + Following정보를 조합해서 List<Post>로 반환.
     */



}
