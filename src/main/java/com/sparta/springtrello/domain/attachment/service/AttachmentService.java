package com.sparta.springtrello.domain.attachment.service;

import com.sparta.springtrello.domain.attachment.dto.AttachmentRequest;
import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileTypeNotSupportedException;
import com.sparta.springtrello.domain.attachment.exception.InvalidRequestException;
import com.sparta.springtrello.domain.attachment.repository.AttachmentRepository;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    // 첨부파일 추가
    @Transactional
    public List<AttachmentEntity> addAttachments(List<MultipartFile> files, Long cardId) {
        // 현재 인증된 사용자 확인 (필요한 경우 주석 해제)
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null && authentication.getAuthorities().stream()
        //         .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_READ_ONLY"))) {
        //     throw new ReadOnlyUserException("읽기 전용 사용자로서 첨부파일을 추가할 수 없습니다.");
        // }

        // 카드 존재 여부 체크
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new InvalidRequestException("카드를 찾을 수 없습니다."));

        List<AttachmentEntity> attachments = new ArrayList<>();

        for (MultipartFile file : files) {
            // 파일 형식 체크
            if (file.getContentType() == null || !isSupportedFileType(file.getContentType())) {
                throw new FileTypeNotSupportedException("지원되지 않는 파일 형식입니다.");
            }

            // 첨부파일 엔티티 생성
            AttachmentEntity attachment = new AttachmentEntity(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    (int) file.getSize(),
                    card
            );

            // 첨부파일 저장
            attachments.add(attachmentRepository.save(attachment));
        }

        return attachments;
    }

    // 카드에 대한 첨부파일 조회
    public List<AttachmentRequest> getAttachmentsByCardId(Long cardId) {
        List<AttachmentEntity> attachments = attachmentRepository.findByCard_Id(cardId);
        if (attachments.isEmpty()) {
            throw new AttachmentNotFoundException("해당 카드 ID에 대한 첨부파일이 없습니다.");
        }

        // 임의의 사용자 찾기 (예: 첫 번째 사용자)
        UserEntity user = userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("사용자를 찾을 수 없습니다."));
        
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new InvalidRequestException("카드를 찾을 수 없습니다."));

        return attachments.stream()
                .map(attachment -> new AttachmentRequest(
                        attachment.getId(),
                        attachment.getFileName(),
                        attachment.getFileType(),
                        attachment.getFileSize(),
                        attachment.getCreatedAt(),
                        attachment.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    // 첨부파일 삭제
    public void deleteAttachment(Long attachmentId) {
//        // 현재 인증된 사용자 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_READ_ONLY"))) {
//            throw new ReadOnlyUserException("읽기 전용 사용자로서 첨부파일을 삭제할 수 없습니다.");
//        }

        if (!attachmentRepository.existsById(attachmentId)) {
            throw new AttachmentNotFoundException("해당 ID의 첨부파일이 없습니다.");
        }
        attachmentRepository.deleteById(attachmentId); // ID로 삭제
    }

    // 지원하는 파일 형식 체크
    private boolean isSupportedFileType(String fileType) {
        return "image/jpeg".equals(fileType) || "image/png".equals(fileType) ||
                "application/pdf".equals(fileType) || "text/csv".equals(fileType);
    }
}
