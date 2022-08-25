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

    @PostMapping("/api/post")
    public Long save(@RequestBody PostSaveRequestDto saveDto){
        return postsService.save(saveDto);
    }



}
