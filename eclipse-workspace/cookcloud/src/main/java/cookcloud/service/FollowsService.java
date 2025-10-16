package cookcloud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Follows;
import cookcloud.entity.Member;
import cookcloud.repository.FollowsRepository;
import cookcloud.repository.MemberRepository;

@Service
public class FollowsService {

	@Autowired
	private FollowsRepository followsRepository;

	@Autowired
	private MemberRepository memberRepository;

	public Optional<Follows> isFollowing(String followingId, String followerId) {
		return followsRepository.findByFollowingIdAndFollowerId(followingId, followerId);
	}

	// 팔로잉한 사용자의 목록을 가져오기
	public List<Member> getMyFollowings(String memId) {
		// 내가 팔로잉한 사람들 목록을 찾음
		List<Follows> followsList = followsRepository.findByFollowerId(memId);

		List<Member> followingMembers = new ArrayList<>();

		// 팔로잉한 사람의 memId로 해당 사용자의 정보를 조회
		for (Follows follow : followsList) {
			Optional<Member> member = memberRepository.findById(follow.getFollowingId());
			// 해당하는 사용자가 있으면, 리스트에 추가
			member.ifPresent(followingMembers::add);
		}
		return followingMembers;
	}

	// 팔로워 목록 가져오기
	public List<Member> getMyFollowers(String memId) {
		// 내가 팔로워한 사용자 목록을 찾음
		List<Follows> followersList = followsRepository.findByFollowingId(memId);

		List<Member> followerMembers = new ArrayList<>();

		for (Follows follow : followersList) {
			Optional<Member> member = memberRepository.findById(follow.getFollowerId());
			member.ifPresent(followerMembers::add);
		}

		return followerMembers;
	}

	@Transactional
	public boolean toggleFollow(String followingId, String followerId) {
		if (followingId == followerId) {
			return false;
		}
		Optional<Follows> Follows = followsRepository.findByFollowingIdAndFollowerId(followingId, followerId);
		if (Follows.isPresent()) {
			Follows existingFollow = Follows.get();
			if ("Y".equals(existingFollow.getFollowIsFollowing())) {
				existingFollow.setFollowIsFollowing("N"); // 좋아요 취소
				followsRepository.save(existingFollow);
				return false;
			} else {
				existingFollow.setFollowIsFollowing("Y"); // 다시 좋아요
				followsRepository.save(existingFollow);
				return true;
			}
		} else {
			Follows follow = new Follows();
			follow.setFollowingId(followingId);
			follow.setFollowerId(followerId);
			follow.setFollowIsFollowing("Y");
			followsRepository.save(follow);
			return true;
		}
	}

}
