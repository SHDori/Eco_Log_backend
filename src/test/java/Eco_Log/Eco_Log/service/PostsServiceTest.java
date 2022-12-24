//package Eco_Log.Eco_Log.service;
//
//import Eco_Log.Eco_Log.controller.dto.*;
//import Eco_Log.Eco_Log.controller.dto.postDto.PostListResponseDTO;
//import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
//import Eco_Log.Eco_Log.controller.dto.postDto.PostUpdateRequestDto;
//import Eco_Log.Eco_Log.controller.dto.postDto.PostViewResponseDto;
//import Eco_Log.Eco_Log.domain.post.Behaviors;
//import Eco_Log.Eco_Log.domain.post.Posts;
//import Eco_Log.Eco_Log.domain.user.Users;
//import Eco_Log.Eco_Log.repository.BehaviorRepository;
//import Eco_Log.Eco_Log.repository.PRconnectRepository;
//import Eco_Log.Eco_Log.repository.PostsRepository;
//import Eco_Log.Eco_Log.repository.UserRepository;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class PostsServiceTest {
//
//    @Autowired
//    PostsRepository postsRepository;
//
//    @Autowired
//    UserServie userServie;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PostsService postsService;
//
//    @Autowired
//    BehaviorRepository behaviorRepository;
//    @Autowired
//    PRconnectRepository pRconnectRepository;
//    @Autowired
//    FollowService followService;
//    @Autowired
//    HeartService heartService;
//    @Before
//    public void makeBehaviors(){
//        Behaviors behaviors = new Behaviors("친환경 상점 이용");
//        behaviorRepository.save(behaviors);
//
//        Behaviors behaviors1 = new Behaviors("중고거래");
//        behaviorRepository.save(behaviors1);
//        Behaviors behaviors2 = new Behaviors("기부와 나눔");
//        behaviorRepository.save(behaviors2);
//
//    }
//
//    @After
//    public void cleanup(){
//        postsRepository.deleteAll();
//        userRepository.deleteAll();
//
//    }
//
//    @Test
//    public void 행동갯수_상승여부(){
//        Users users = createUser();
//        String doingDay = "2022-8-18";
//
//        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//
//        //when
//        Long postsId = postsService.save(users.getId(),saveRequestDto).getPostId();
//
//        //then
//        Assert.assertEquals("행동count 가 5 이어야한다",5,users.getProfiles().getBehaviorCount()+users.getProfiles().getCustomBehaviorCount());
//        Assert.assertEquals("행동count 가 5 이어야한다",3,users.getProfiles().getBehaviorCount());
//        Assert.assertEquals("행동count 가 5 이어야한다",2,users.getProfiles().getCustomBehaviorCount());
//    }
//
//    @Test
//    public void 게시글_저장(){
//        //given
//        Users users = createUser();
//        Users users1 = createUser_1();
//
//        String doingDay = "2022-8-18";
//
//        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto_1("2022-8-19");
//        PostSaveRequestDto saveRequestDto_2 = getSaveRequestDto_1(doingDay);
//        // when
//        Long postsId = postsService.save(users.getId(),saveRequestDto).getPostId();
//        Long postsId_1 = postsService.save(users.getId(),saveRequestDto_1).getPostId();
//
//        Long postsId_2 = postsService.save(users1.getId(),saveRequestDto_2).getPostId();
//
//
//        // then
//        Users getUsers = userRepository.findById(users.getId())
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
//        Posts getPosts = postsRepository.findById(postsId)
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+postsId));
//        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
//        List<SummaryInfoDTO> otherUserSummaryInfoList = pRconnectRepository.summaryFindByUserID(users1.getId());
//
//        System.out.println("기존 user"+users);
//        System.out.println("user summary정보=>");
//        System.out.println(userSummaryInfoList);
//
//        System.out.println("다른 user"+users1);
//        System.out.println("anOther user summary정보=>");
//        System.out.println(otherUserSummaryInfoList);
//
//        System.out.println(getUsers.toString());
//        System.out.println(getPosts);
//
//        Assert.assertEquals("User의 정보가 같아야한다.", users.getName(), getUsers.getName());
//        Assert.assertEquals("게시글이 잘 저장되어야한다.",saveRequestDto.getComment(),getPosts.getComment());
//        Assert.assertEquals("게시글에 유저정보가 잘 들어가있어야한다.",getPosts.getUsers().getId(), getUsers.getId());
//
//        Assert.assertEquals("User의 Summary정보의 길이는 3이어야한다.", 3, userSummaryInfoList.size());
//        Assert.assertEquals("행동count 가 6 이어야한다",6,users.getProfiles().getBehaviorCount());
//    }
//
//
//
//    @Test
//    public void 게시글_수정(){
//        //given
//        Users users = createUser();
//
//
//        String doingDay = "2022-8-18";
//        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//        Long targetPostId = postsService.save(users.getId(),saveRequestDto).getPostId();
//
//        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
//        System.out.println("user summary정보=>");
//        System.out.println(userSummaryInfoList);
//
//        String chagedComment ="이건 수정된 말이야!";
//
//        List<String> changedBehaviorsList = new ArrayList<>();
//        changedBehaviorsList.add("2");
//        changedBehaviorsList.add("3");
//        List<String> changeCustomBehaviorList = new ArrayList<>();
//        changeCustomBehaviorList.add("나무심기");
//        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
//                .postId(targetPostId)
//                .comment(chagedComment)
//                .behaviorList(changedBehaviorsList)
//                .customizedBehaviors(changeCustomBehaviorList)
//                .build();
//        // when
//        postsService.update(users.getId(), updateRequestDto);
//
//
//        // then
//        Posts getPosts = postsRepository.findById(targetPostId)
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+targetPostId));
//
//        userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
//        System.out.println("user summary정보=>");
//        System.out.println(userSummaryInfoList);
//
//        Assert.assertEquals("User의 Summary정보의 길이는 2이어야한다.", 2, userSummaryInfoList.size());
//        Assert.assertEquals("Comment가 잘 바뀌어 있어야한다.", chagedComment, getPosts.getComment());
//        Assert.assertEquals("행동count 가 3 이어야한다",3,users.getProfiles().getBehaviorCount());
//    }
//
//    @Test
//    public void 게시글_삭제_요약정보까지(){
//        //given
//        Users users = createUser();
//        String doingDay = "2022-8-18";
//        String doingDay1 = "2022-8-19";
//
//        PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto_1(doingDay1);
//        // when
//        Long postsId = postsService.save(users.getId(),saveRequestDto).getPostId();
//        Long postsId_1 = postsService.save(users.getId(),saveRequestDto_1).getPostId();
//
//        System.out.println("user의 삭제전 게시물 갯수=>"+users.getPosts().size());
//        postsService.delete(users.getId(),postsId);
//        System.out.println("user의 게시물 갯수=>"+users.getPosts().size());
//        // then
//        Users getUsers = userRepository.findById(users.getId())
//                .orElseThrow(()-> new IllegalArgumentException("해당 게시물이 없습니다. id = "+ users.getId()));
//
//        List<SummaryInfoDTO> userSummaryInfoList = pRconnectRepository.summaryFindByUserID(users.getId());
//
//        System.out.println("user summary정보=>");
//        System.out.println(userSummaryInfoList);
//
//
//        Assert.assertEquals("User의 Post수가 잘 줄어들어야한다",1,users.getPosts().size());
//        Assert.assertEquals("Summary가 잘 줄어들어야한다",1,userSummaryInfoList.size());
//        Assert.assertEquals("행동count 가 1 이어야한다",1,users.getProfiles().getBehaviorCount());
//    }
//
//
//    @Test
//    public void 월단위_게시물_조회(){
//
//        //given
//        Users users = createUser();
//        userRepository.save(users);
//
//        for (int i=1;i<=10;i++){
//            String doingDay = "2022-8-"+i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(),saveRequestDto);
//        }
//        for (int i=1;i<=5;i++){
//            String doingDay = "2022-7-"+i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(),saveRequestDto);
//        }
//
//        // when
//        String targetMonth = "2022-8";
//        List<PostListResponseDTO> findPostList = postsService.findPostByMonth(users.getId(), targetMonth);
//        // then
//
//        System.out.println(findPostList.size());
//        Assert.assertEquals("8월 게시물 10개만 조회되어야 한다.", 10, findPostList.size());
//    }
//
//
//    @Test
//    public void 특정일짜_following_게시글까지_조회(){
//
//        // given
//        Users users1 = createCustomUser(0L,"김승환");
//        Users users2 = createCustomUser(1L,"김강민");
//        Users users3 = createCustomUser(2L,"최지훈");
//        //1 의 following => 2,3  // 2의 following => 없음// 3의 following => 2
//        followService.makeFollowRelation(users1.getId(),users2.getId());
//        followService.makeFollowRelation(users1.getId(),users3.getId());
//        followService.makeFollowRelation(users3.getId(),users2.getId());
//
//        String doingDay = "2022-8-18";
//        // user 1꺼
//        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto(doingDay);
//        // user 2꺼
//        PostSaveRequestDto saveRequestDto_2 = getSaveRequestDto_1(doingDay);
//        // user 3꺼
//        PostSaveRequestDto saveRequestDto_3 = getSaveRequestDto_1(doingDay);
//
//        postsService.save(users1.getId(), saveRequestDto_1);
//        postsService.save(users2.getId(), saveRequestDto_2);
//        postsService.save(users3.getId(), saveRequestDto_3);
//
//        // when
//
//
//
//        List<PostViewResponseDto> user1_followingPostByDay =
//                postsService.findFollowingPostByDay(users1.getId(), doingDay);
//        List<PostViewResponseDto> user2_followingPostByDay =
//                postsService.findFollowingPostByDay(users2.getId(), doingDay);
//        List<PostViewResponseDto> user3_followingPostByDay =
//                postsService.findFollowingPostByDay(users3.getId(), doingDay);
//
//        // then
//        Assert.assertEquals("User1의 특정날짜 follower게시물의 수가 3이여야한다",3, user1_followingPostByDay.size());
//        Assert.assertEquals("User2의 특정날짜 follower게시물의 수가 1이여야한다",1, user2_followingPostByDay.size());
//        Assert.assertEquals("User3의 특정날짜 follower게시물의 수가 2이여야한다",2, user3_followingPostByDay.size());
//    }
//
//
//    @Test
//    public void 특정일짜_following_게시글조회시_하트여부검사() {
//
//        // given
//        Users users1 = createCustomUser(0L, "김승환");
//        Users users2 = createCustomUser(1L, "김강민");
//        Users users3 = createCustomUser(2L, "최지훈");
//        //1 의 following => 2,3  // 2의 following => 없음// 3의 following => 2
//        followService.makeFollowRelation(users1.getId(), users2.getId());
//        followService.makeFollowRelation(users1.getId(), users3.getId());
//        followService.makeFollowRelation(users3.getId(), users2.getId());
//        String doingDay = "2022-8-18";
//        // user 1꺼
//        PostSaveRequestDto saveRequestDto_1 = getSaveRequestDto(doingDay);
//        // user 2꺼
//        PostSaveRequestDto saveRequestDto_2 = getSaveRequestDto_1(doingDay);
//        // user 3꺼
//        PostSaveRequestDto saveRequestDto_3 = getSaveRequestDto_1(doingDay);
//
//        //Long user1PostId= postsService.save(users1.getId(), saveRequestDto_1);
//        Long user2PostId=postsService.save(users2.getId(), saveRequestDto_2).getPostId();
//        //Long user3PostId=postsService.save(users3.getId(), saveRequestDto_3);
//
//        // when
//        heartService.makeHeartInfo(users3.getId(),user2PostId);
//
//        //then
//        List<PostViewResponseDto> user3_followingPostByDay =
//                postsService.findFollowingPostByDay(users3.getId(), doingDay);
//        Assert.assertEquals("User3이 하트누른 user2의 게시물에는 하트가 이미눌려있어야한다.",true, user3_followingPostByDay.get(0).isAlreadyHeart());
//
//    }
//
//
//
//    @Test
//    public void 게시물15개뱃지_획득_Test() {
//
//        //given
//        Users users = createUser();
//        userRepository.save(users);
//
//        for (int i = 1; i <= 15; i++) {
//            String doingDay = "2022-8-" + i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(), saveRequestDto);
//        }
//        for (int i = 1; i <= 5; i++) {
//            String doingDay = "2022-7-" + i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(), saveRequestDto);
//        }
//
//        // when
//
//
//        //then
//        System.out.println("벳지상태는 => "+ users.getBadgeState());
//        Assert.assertEquals("뱃지가 획득되어있어야한다","0010010000", users.getBadgeState());
//    }
//
//
//    @Test
//    public void 게시물40개뱃지_획득_Test() {
//
//        //given
//        Users users = createUser();
//        userRepository.save(users);
//
//        for (int i = 1; i <= 20; i++) {
//            String doingDay = "2022-8-" + i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(), saveRequestDto);
//        }
//        for (int i = 1; i <= 20; i++) {
//            String doingDay = "2022-7-" + i;
//            PostSaveRequestDto saveRequestDto = getSaveRequestDto(doingDay);
//            postsService.save(users.getId(), saveRequestDto);
//        }
//
//        // when
//
//        //then
//        System.out.println("벳지상태는 => "+ users.getBadgeState());
//        Assert.assertEquals("뱃지가 획득되어있어야한다","0011010000", users.getBadgeState());
//    }
//
//
//
//
//
//
//
//
//    // 편의 메서드들
//
//
//
//    private PostSaveRequestDto getSaveRequestDto(String doingDay) {
//        String comment ="오늘도 수고많았다";
//
//
//
//        List<String> behaviorsList = new ArrayList<>();
//        behaviorsList.add("1");
//        behaviorsList.add("2");
//        behaviorsList.add("3");
//        List<String> customBehaviorsList = new ArrayList<>();
//        customBehaviorsList.add("스뎅 빨대사용");
//        customBehaviorsList.add("업사이클링");
//        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
//                .builder()
//                .doingDay(doingDay)
//                .behaviorList(behaviorsList)
//                .comment(comment)
//                .customizedBehaviors(customBehaviorsList)
//                .build();
//        return saveRequestDto;
//    }
//    private PostSaveRequestDto getSaveRequestDto_1(String doingDay) {
//        String comment ="오늘도 지구를 위하여!";
//
//
//
//        List<String> behaviorsList = new ArrayList<>();
//
//        behaviorsList.add("2");
//
//        List<String> customBehaviorsList = new ArrayList<>();
//        PostSaveRequestDto saveRequestDto = PostSaveRequestDto
//                .builder()
//                .behaviorList(behaviorsList)
//                .doingDay(doingDay)
//                .customizedBehaviors(customBehaviorsList)
//                .comment(comment)
//                .build();
//        return saveRequestDto;
//    }
//
//
//
//    private Users createUser() {
//        //given
//        Long snsID = 1L;
//        String profileImg = "임의의 이미지주소";
//
//        String email = "narasarang@naver.com";
//        ProfileDto profileDto = ProfileDto.builder()
//                .snsId(snsID)
//                .profileImg(profileImg)
//                .email(email)
//
//                .build();
//        //when
//        Users savedUser = userServie.save("김승환",profileDto);
//        return savedUser;
//    }
//
//    private Users createUser_1() {
//        //given
//        Long snsID = 2L;
//        String profileImg = "임의의 이미지주소";
//
//        String email = "zzang@naver.com";
//        ProfileDto profileDto = ProfileDto.builder()
//                .snsId(snsID)
//                .profileImg(profileImg)
//                .email(email)
//
//                .build();
//        //when
//        Users savedUser = userServie.save("승도리",profileDto);
//        return savedUser;
//    }
//
//
//    private Users createCustomUser(Long snsId,String name) {
//        //given
//        Long snsID = snsId;
//        String profileImg = "임의의 이미지주소";
//
//        String email = "narasarang@naver.com";
//        ProfileDto profileDto = ProfileDto.builder()
//                .snsId(snsID)
//                .profileImg(profileImg)
//                .email(email)
//
//                .build();
//        //when
//        Users savedUser = userServie.save(name,profileDto);
//        return savedUser;
//    }
//
//}