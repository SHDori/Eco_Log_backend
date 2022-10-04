package Eco_Log.Eco_Log.service;

import Eco_Log.Eco_Log.controller.dto.ProfileUpdateRequestDto;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    /**
     * 프로필 수정
     */
    @Transactional
    public Long update(Long userId, ProfileUpdateRequestDto updateRequestDto){

        Users users = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다. id = "+userId));

        Long result =users.getProfiles().update(updateRequestDto);

        return result;

    }
}
