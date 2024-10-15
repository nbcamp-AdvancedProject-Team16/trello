package com.sparta.springtrello.domain.attachment.entity;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "attachments")
public class AttachmentEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card; // Card 엔티티와의 관계


    public AttachmentEntity(String fileName, String fileType, int size, CardEntity card) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = size;
        this.card = card;
    }
}