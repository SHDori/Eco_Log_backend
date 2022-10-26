package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.FollowRequestDto;
import Eco_Log.Eco_Log.controller.dto.FollowViewResponseDto;
import Eco_Log.Eco_Log.controller.dto.ProfileUpdateRequestDto;
import Eco_Log.Eco_Log.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/user/follow")
    public ResponseEntity plusFollower(HttpServletRequest request, @RequestBody FollowRequestDto followRequestDto){
        Long userId = (Long) request.getAttribute("userId");
        String resultMsg = followService.makeFollowRelation(userId,followRequestDto.getTargetId());

        return ResponseEntity.ok().body(resultMsg);
    }
    /**
     * 팔로잉 취소
     */
    @DeleteMapping("/user/follow")
    public ResponseEntity cancelFollow(HttpServletRequest request, @RequestBody FollowRequestDto followRequestDto){
        Long userId = (Long) request.getAttribute("userId");
        String resultMsg =followService.deleteFollowRelation(userId,followRequestDto.getTargetId());

        return ResponseEntity.ok().body(resultMsg);
    }


    /**
     * 팔로잉 조회(내가 from유저)
     */
    @GetMapping("/user/following")
    public ResponseEntity getFollowing(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        List<FollowViewResponseDto> followingList=followService.getMyFollowing(userId);
        return ResponseEntity.ok().body(followingList);
    }

    /**
     * 팔로워 조회(내가 to 유저)
     */
    @GetMapping("/user/follower")
    public ResponseEntity getFollower(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        List<FollowViewResponseDto> followingList=followService.getMyFollower(userId);
        return ResponseEntity.ok().body(followingList);
    }
}
