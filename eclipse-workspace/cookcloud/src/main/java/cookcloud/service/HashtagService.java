package cookcloud.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Hashtag;
import cookcloud.repository.HashtagRepository;

@Service
public class HashtagService {

	@Autowired
	private HashtagRepository hashtagRepository;

	public List<Hashtag> saveOrUpdateHashtags(List<String> hashtags) {
		return hashtags.stream().map(this::findOrCreateHashtag).collect(Collectors.toList());
	}

	public List<Hashtag> getTopHashtags() {
	    return hashtagRepository.findTopByUsageCount();
	}
	
	@Transactional
	private Hashtag findOrCreateHashtag(String tagName) {
		return hashtagRepository.findByHashName(tagName).orElseGet(() -> {
			Hashtag newTag = new Hashtag(tagName, 62L, 0L);
			return hashtagRepository.save(newTag);
		});
	}

}
