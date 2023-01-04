package Eco_Log.Eco_Log.controller;


import Eco_Log.Eco_Log.controller.dto.HeartRequestDto;
import Eco_Log.Eco_Log.controller.dto.postDto.PostSaveRequestDto;
import Eco_Log.Eco_Log.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/post/heart")
    public ResponseEntity<List> pushPostHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");
        List<Integer> result = heartService.makeHeartInfo(userId,requestDto.getTargetPostId());
        if(result.size()==0){
            return ResponseEntity.ok().body(result);
        }else if(result.get(0)==-1){

            List<String> msg = new ArrayList<>();
            msg.add("이미 하트가 눌려있습니다");
            return ResponseEntity.badRequest().body(msg);
        }else{
            return ResponseEntity.ok().body(result);
        }

    }

    @DeleteMapping("/post/heart")
    public ResponseEntity<String> cancelHeart(HttpServletRequest request, @RequestBody HeartRequestDto requestDto){
        Long userId = (Long) request.getAttribute("userId");
        String returnMsg =heartService.cancelHeart(userId, requestDto.getTargetPostId());
        return ResponseEntity.ok().body(returnMsg);
    }

}
