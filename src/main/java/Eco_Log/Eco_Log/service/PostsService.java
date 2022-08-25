package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.PostSaveRequestDto;
import Eco_Log.Eco_Log.domain.post.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostSaveRequestDto saveRequestDto){
        return postsRepository.save(saveRequestDto.toEntity()).getId();
    }
}
