package org.springframe.backend.service.impl;

import jakarta.persistence.criteria.Predicate;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.domain.dto.UserCommentDTO;
import org.springframe.backend.domain.entity.Comment;
import org.springframe.backend.domain.entity.User;
import org.springframe.backend.domain.vo.ArticleCommentVO;
import org.springframe.backend.domain.vo.PageVo;
import org.springframe.backend.repository.CommentRepository;
import org.springframe.backend.repository.UserRepository;
import org.springframe.backend.service.CommentService;
import org.springframe.backend.utils.RedisCache;
import org.springframe.backend.utils.ResponseResult;
import org.springframe.backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisCache redisCache;

    @Override
    public PageVo<List<ArticleCommentVO>> getComment(Integer type, Integer typeId, Integer pageNum, Integer pageSize) {
        Specification<Comment> parentSpec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
            predicates.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            predicates.add(criteriaBuilder.isNull(root.get("parentId")));
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(pageNum-1,pageSize,Sort.by(Sort.Direction.DESC,"createTime"));
        Page<Comment> commentPage = commentRepository.findAll(parentSpec,pageable);
//        List<Comment> comments = commentPage.getContent();
        List<Comment> comments = new ArrayList<>(commentPage.getContent());


        Specification<Comment> childSpec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
            predicates.add(criteriaBuilder.equal(root.get("typeId"), typeId));
            predicates.add(criteriaBuilder.isNotNull(root.get("parentId")));
            query.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Comment> childComments = commentRepository.findAll(childSpec);
        if(!childComments.isEmpty()){
            comments.addAll(childComments);
        }

        List<ArticleCommentVO> commentVOs = comments.stream()
                .map(comment -> comment.asViewObject(ArticleCommentVO.class))
                .toList();

        List<ArticleCommentVO> parentComments = commentVOs.stream()
                .filter(comment -> comment.getParentId()==null)
                .toList();

        List<ArticleCommentVO> collect = parentComments.stream().peek(comment->{
            comment.setChildComments(getChildComment(commentVOs,comment.getId()));
            comment.setChildCommentCount(getChildCommentCount(commentVOs,comment.getId()));
            comment.setParentCommentCount((long) commentRepository.count(
                    (root, query, criteriaBuilder) -> criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("type"),type),
                            criteriaBuilder.equal(root.get("typeId"),typeId),
                            criteriaBuilder.isNull(root.get("parentId"))
                    )
            ));
        }).toList();

        long totalCount = commentRepository.count(
                (root, query, criteriaBuilder) -> criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("type"),type),
                        criteriaBuilder.equal(root.get("typeId"),typeId)
                )
        );

        return new PageVo<>(collect, totalCount);

    }

    private List<ArticleCommentVO> getChildComment(List<ArticleCommentVO> comments,Long parentId) {
        return comments.stream()
                .filter(comment -> {
                    System.out.println("Filtering Comment ID: " + comment.getId() + ", Parent ID: " + comment.getParentId() + ", Given Parent ID: " + parentId);
                    return Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId);
                })
                .peek(comment ->{
                    comment.setChildComments(getChildComment(comments,comment.getId()));
                })
                .toList();
    }
//    private List<ArticleCommentVO> getChildComment(List<ArticleCommentVO> comments, Integer parentId) {
//        return comments.stream()
//                .filter(comment -> {
//                    boolean isValid = Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId);
//                    if (isValid) {
//                        System.out.println("Retained Comment ID: " + comment.getId());
//                    }
//                    return isValid;
//                })
////                .filter(comment -> {
////                    System.out.println("Filtering Comment ID: " + comment.getId() + ", Parent ID: " + comment.getParentId() + ", Given Parent ID: " + parentId);
////                    return  Objects.equals(comment.getParentId(), parentId);
////                })
////                .peek(comment -> {
////                    comment.setChildComments(getChildComment(comments, comment.getId()));
////
////                })
//
//                .toList();
//    }

    private Long getChildCommentCount(List<ArticleCommentVO> comments,Long parentId) {
        return comments.stream()
                .filter(comment -> Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(),parentId))
                .peek(comment -> {
                    Long count = commentRepository.count(getReplyCountSpecification(comment.getId()));
                    comment.setChildCommentCount(count);
                })
                .mapToLong(comment ->{
                    if(!comment.getChildComments().isEmpty()) {
                        return (1 + getChildCommentCount(comment.getChildComments(),comment.getId()));
                    }else{
                        return 1;
                    }
                })
                .sum();
    }

    private Specification<Comment> getReplyCountSpecification(Long commentId) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("replyId"),commentId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public ResponseResult<String> userComment(UserCommentDTO userCommentDTO) {
        Comment comment = userCommentDTO.asViewObject(Comment.class,comment1-> comment1.setCommentUserId(SecurityUtils.getUserId()));
        redisCache.incrementCacheMapValue(RedisConst.ARTICLE_COMMENT_COUNT,userCommentDTO.getTypeId().toString(),1);
        commentRepository.save(comment);
        return ResponseResult.Success();

    }

    @Override
    public ResponseResult<Void> deleteComment(Integer Id) {
        return null;
    }
}
