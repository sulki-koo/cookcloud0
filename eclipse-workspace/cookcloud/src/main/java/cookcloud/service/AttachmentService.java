package cookcloud.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cookcloud.entity.Attachment;
import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.repository.AttachmentRepository;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	public List<Attachment> getAttachmentsByRecipe(Long recipeId) {
		return attachmentRepository.findByRecipeIdAndAttachIsDeleted(recipeId);
	}
	
	public Attachment saveAttachment(Long recipeId, MultipartFile file, Map<CodeId, Code> attachTypes)
			throws IOException {
		
		String uploadDir = "C:/Users/Administrator/git/cookcloud/cookcloud/src/main/resources/static/img/recipe/";
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toUpperCase();
		String serverFilename = UUID.randomUUID().toString() + "." + extension.toLowerCase();
		
		File uploadFile = new File(uploadDir + serverFilename);
		file.transferTo(uploadFile);

		Long attachTypeCode = attachTypes.keySet().stream()
				.filter(key -> attachTypes.get(key).getCodeName().equalsIgnoreCase(extension)).map(CodeId::getChildCode)
				.findFirst().orElse(null);
		
		if (attachTypeCode == null) {
			throw new IllegalArgumentException("Unsupported file type: " + extension);
		}
		
		String StrAttachTypeCode = "8" + (attachTypeCode != null ? attachTypeCode : "");

		Attachment attachment = new Attachment();
		attachment.setRecipeId(recipeId);
		attachment.setAttachUploadName(originalFilename);
		attachment.setAttachServerName(serverFilename);
		attachment.setAttachSize(file.getSize());
		attachment.setAttachInsertAt(LocalDateTime.now());
		attachment.setAttachIsDeleted("N");
		attachment.setAttachTypeCode(Long.parseLong(StrAttachTypeCode));

		return attachmentRepository.save(attachment);
	}

	public void deleteAttachment(Long attachId) {
		attachmentRepository.findById(attachId).ifPresent(att -> {
			att.setAttachIsDeleted("Y");
			att.setAttachDeleteAt(LocalDateTime.now());
			attachmentRepository.save(att);
		});
	}

}
