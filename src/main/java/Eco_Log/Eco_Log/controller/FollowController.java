package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.FollowRequestDto;
import Eco_Log.Eco_Log.controller.dto.ProfileUpdateRequestDto;
import Eco_Log.Eco_Log.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

}
