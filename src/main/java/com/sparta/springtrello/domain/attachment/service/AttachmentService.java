package com.sparta.springtrello.domain.attachment.service;

import com.sparta.springtrello.domain.attachment.dto.AttachmentRequest;
import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileTypeNotSupportedException;
import com.sparta.springtrello.domain.attachment.exception.InvalidRequestException;
import com.sparta.springtrello.domain.attachment.repository.AttachmentRepository;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.dto.UploadFileResponse;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.service.S3UploadService;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final S3UploadService s3UploadService;

    // 첨부파일 추가
    @Transactional
    public List<AttachmentEntity> addAttachments(List<MultipartFile> files, Long cardId, Long workspaceId, CustomUserDetails authUser) throws IOException {
        // User 인증
        UserEntity user = UserEntity.fromAuthUser(authUser);

        // 멤버 여부 확인
        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스에 가입된 멤버가 아닙니다."));

        // 읽기 전용 멤버는 첨부파일을 생성할 수 없음
        validatePermission(member);

        // 카드 존재 여부 체크
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new InvalidRequestException("카드를 찾을 수 없습니다."));

        List<AttachmentEntity> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getContentType() == null || !isSupportedFileType(file.getContentType())) {
                throw new FileTypeNotSupportedException("지원되지 않는 파일 형식입니다.");
            }

            // S3에 업로드하고 file URL 얻기
            UploadFileResponse uploadFileResponse = s3UploadService.uploadImageToS3(file);

            // AttachmentEntity 생성
            AttachmentEntity attachment = new AttachmentEntity(
                    uploadFileResponse.getFileName(),
                    uploadFileResponse.getFileExtension(),
                    uploadFileResponse.getFileSize().intValue(),
                    card,
                    uploadFileResponse.getFileUrl()
            );

            attachments.add(attachmentRepository.save(attachment));
        }

        return attachments;
    }

    // 카드에 대한 첨부파일 조회
    public List<AttachmentRequest> getAttachmentsByCardId(Long cardId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity user = UserEntity.fromAuthUser(authUser);

        // 멤버 여부 확인
        Optional<MemberEntity> memberOpt = memberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId);

        // 멤버인 경우
        if (memberOpt.isPresent()) {
            // 모든 멤버는 첨부파일을 조회할 수 있음
        } else {
            // 워크스페이스 소유자 확인
            WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                    .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

            // 워크스페이스 소유자인지 확인
            if (workspace.getCreatedBy() != user.getId()) {
                throw new IllegalArgumentException("해당 워크스페이스를 생성한 사용자가 아닙니다.");
            }
        }


        List<AttachmentEntity> attachments = attachmentRepository.findByCard_Id(cardId);
        if (attachments.isEmpty()) {
            throw new AttachmentNotFoundException("해당 카드 ID에 대한 첨부파일이 없습니다.");
        }
        
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
    @Transactional
    public void deleteAttachment(Long attachmentId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity user = UserEntity.fromAuthUser(authUser);

        // 멤버 여부 확인
        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스에 가입된 멤버가 아닙니다."));

        // 읽기 전용 멤버는 첨부파일을 생성할 수 없음
        validatePermission(member);


        AttachmentEntity attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AttachmentNotFoundException("해당 ID의 첨부파일이 없습니다."));

        // S3에서 파일 삭제
        if (attachment.getFilePath() != null) { // S3 URL이 존재할 때만 삭제
            s3UploadService.deleteImageFromS3(attachment.getFilePath());
        }


        attachmentRepository.deleteById(attachmentId); // ID로 삭제
    }

    // 지원하는 파일 형식 체크
    private boolean isSupportedFileType(String fileType) {
        return "image/jpeg".equals(fileType) || "image/png".equals(fileType) ||
                "application/pdf".equals(fileType) || "text/csv".equals(fileType);
    }

    // 멤버의 권한 확인
    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 첨부파일을 생성, 삭제할 수 없습니다.");
        }
    }
}
