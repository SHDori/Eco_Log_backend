package Eco_Log.Eco_Log.service;


import Eco_Log.Eco_Log.controller.dto.ProfileDto;
import Eco_Log.Eco_Log.domain.user.Profiles;
import Eco_Log.Eco_Log.domain.user.Users;
import Eco_Log.Eco_Log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServie {

    private final UserRepository userRepository;


    @Transactional
    public Long save(String name,ProfileDto profileDto){

        Profiles profile = Profiles.builder()
                .snsId(profileDto.getSnsId())
                .profileImg(profileDto.getProfileImg())
                .email(profileDto.getEmail())
                .selfIntroduce(profileDto.getSelfIntroduce())
                .build();

        Users user = Users.createUser(name,profile);
        userRepository.save(user);
        return user.getId();
    }
}
