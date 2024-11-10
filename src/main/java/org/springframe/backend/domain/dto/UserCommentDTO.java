package org.springframe.backend.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframe.backend.domain.BaseData;

@Data
public class UserCommentDTO implements BaseData {
    @NotNull
    private Integer type;
    private Integer typeId;
    private Long parentId;
    private Long replyId;
    private String commentContent;
    private Integer replyUserId;
}
