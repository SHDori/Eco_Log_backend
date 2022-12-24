package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.HeartRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/post/heart")
    public ResponseEntity<List> pushPostHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");
        List<Integer> badgeNotifyList = heartService.makeHeartInfo(userId,requestDto.getTargetPostId());
        return ResponseEntity.ok().body(badgeNotifyList);
    }

    @DeleteMapping("/post/heart")
    public ResponseEntity<String> cancelHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");
        String returnMsg =heartService.cancelHeart(userId, requestDto.getTargetPostId());
        return ResponseEntity.ok().body(returnMsg);
    }

}
