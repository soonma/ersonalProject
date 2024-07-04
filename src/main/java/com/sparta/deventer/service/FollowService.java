package com.sparta.deventer.service;

import com.sparta.deventer.dto.FollowUserInfoResponseDto;
import com.sparta.deventer.dto.FollowWithPostResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.repository.FollowRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.UserRepository;
import com.sparta.deventer.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    public boolean toggleFollow(Long following, User user) {
        User followingUser = getFindUser(following);

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

    public Page<FollowWithPostResponseDto> followPostList(UserDetailsImpl userDetails,
            Pageable pageable) {
        List<FollowWithPostResponseDto> followWithPostResponseDto = new ArrayList<>();

        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );

        List<Follow> followList = followRepository.findByFollower(userDetails.getUser());
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Follow follow : followList) {

            List<Post> posts = postRepository.findByUserId(follow.getFollowing().getId());
            for (Post post : posts) {
                postResponseDtos.add(new PostResponseDto(post));
            }
            followWithPostResponseDto.add(
                    new FollowWithPostResponseDto(
                            new FollowUserInfoResponseDto(follow.getFollowing()),
                            postResponseDtos));
        }

        return new PageImpl<>(followWithPostResponseDto
                , sortedByCreatedAtDesc, followWithPostResponseDto.size());
    }

    public User getFindUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저가 존재 하지 않습니다.")
        );
    }
}
