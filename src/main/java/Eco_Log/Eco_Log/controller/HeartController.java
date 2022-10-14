package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.HeartRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/post/heart")
    public String pushPostHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");

        return heartService.makeHeartInfo(userId,requestDto.getTargetPostId());
    }

    @DeleteMapping("/post/heart")
    public String cancelHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");
        return heartService.cancelHeart(userId, requestDto.getTargetPostId());
    }

}
