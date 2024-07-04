package com.sparta.deventer.service;

import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.repository.FollowRepository;
import com.sparta.deventer.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowService {

    private UserRepository userRepository;
    private FollowRepository followRepository;

    public boolean toggleFollow(Long following, User user) {
        User followingUser = userRepository.findById(following).orElseThrow(
                () -> new IllegalArgumentException("팔로우할 유저가 존재 하지 않습니다.")
        );
        if (followingUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("동일한 유저를 팔로우 할수 없습니다.");
        }
        Optional<Follow> followOptional = followRepository.findByFollowingAndFollower(followingUser,
                user);

        if (followOptional.isEmpty()) {
            Follow followSave = new Follow(followingUser, user);
            followRepository.save(followSave);
            return true;
        } else {
            followRepository.delete(followOptional.get());
            return false;
        }

    }

    public long followCount(User user) {
        List<Follow> followList = followRepository.findByFollower(user);
        return followList.size();
    }
}
