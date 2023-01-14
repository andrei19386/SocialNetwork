package ru.skillbox.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skillbox.model.CommentLike;
import ru.skillbox.model.Person;
import ru.skillbox.model.Post;
import ru.skillbox.model.PostComment;
import ru.skillbox.repository.PostCommentRepository;
import ru.skillbox.response.post.PostCommentDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostCommentMapper {
    PostCommentMapper INSTANCE = Mappers.getMapper(PostCommentMapper.class);


    @Mapping(target = "time", qualifiedByName = "mapTime")
    @Mapping(source = "person", target = "authorId", qualifiedByName = "mapAuthorId")
    @Mapping(source = "id", target = "commentsCount", qualifiedByName = "mapCommentsCount")
    @Mapping(source = "commentLikes", target = "likeAmount", qualifiedByName = "mapLikeAmount")
    @Mapping(source = "commentLikes", target = "myLike", qualifiedByName = "mapMyLike")
    @Mapping(source = "post", target = "postId", qualifiedByName = "mapPostId")
    @Mapping(source = "isDelete", target = "isDelete")
    PostCommentDto postCommentToPostCommentDto(PostComment comment, @Context long currentUserId,
                                               @Context PostCommentRepository postCommentRepository,
                                               @Context boolean isTest);


    @Named("mapTime")
    default String mapTime(Long time, @Context boolean isTest) {
        if (isTest) {
            return "0";
        }
        if (time != null) {
            Timestamp timestamp = PostMapper.correctionTime(time);

            LocalDateTime localDateTime = timestamp.toLocalDateTime();

            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        }
        return "";
    }


    @Named("mapAuthorId")
    default Long mapAuthorId(Person person) {
        return person.getId();
    }

    @Named("mapCommentsCount")
    default Integer mapCommentsCount(Long id, @Context PostCommentRepository postCommentRepository) {
        PostComment postComment = postCommentRepository.findById(id).get();
        List<PostComment> postCommentList = postComment.getPost().getPostCommentList();

        List<PostComment> filteredPostCommentList = postCommentList.stream().filter(p ->
                p.getParentId() != null && p.getParentId().equals(postComment.getId())
                        && p.getIsDelete().equals(false)
        ).collect(Collectors.toList());
        return filteredPostCommentList.size();

    }

    @Named("mapLikeAmount")
    default Integer mapLikeAmount(List<CommentLike> likes) {
        if (likes == null || likes.isEmpty()) {
            return 0;
        }
        return (int) likes.stream().filter(p -> p.getIsDelete().equals(false)).count();
    }

    @Named("mapMyLike")
    default Boolean mapMyLike(List<CommentLike> likes, @Context Long currentUser) {
        if (likes == null) {
            return null;
        }
        for (CommentLike like : likes) {
            if (like.getPerson().getId().equals(currentUser) &&
                    like.getIsDelete().equals(false)) {
                return true;
            }
        }
        return false;
    }

    @Named("mapPostId")
    default Long mapPostId(Post post) {
        return post.getId();
    }


}


