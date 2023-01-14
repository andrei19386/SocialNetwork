package ru.skillbox.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skillbox.model.*;
import ru.skillbox.response.post.PostDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Mapper
public interface PostMapper {
    public static final int MILLISECONDS_IN_SECONDS = 1000;


    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "time", target = "time", qualifiedByName = "mapTime")
    @Mapping(source = "person", target = "authorId", qualifiedByName = "mapAuthorId")
    @Mapping(source = "postCommentList", target = "commentsCount", qualifiedByName = "mapCommentsCount")
    @Mapping(source = "postFiles", target = "imagePath", qualifiedByName = "mapImagePath")
    @Mapping(source = "postLikes", target = "likeAmount", qualifiedByName = "mapLikeAmount")
    @Mapping(source = "postLikes", target = "myLike", qualifiedByName = "mapMyLike")
    @Mapping(source = "time", target = "publishDate", qualifiedByName = "mapPublishDate")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTags")
    @Mapping(source = "timeChanged", target = "timeChanged", qualifiedByName = "mapPublishDate")
    @Mapping(source = "isDelete", target = "isDelete")
    PostDto postToPostDto(Post post, @Context Long currentUser, @Context boolean isTest);

    @Named("mapTime")
    default String mapTime(Long time, @Context boolean isTest) {

        return mapPublishDate(time, isTest);
    }


    @Named("mapAuthorId")
    default Long mapAuthorId(Person person) {
        return person.getId();
    }

    @Named("mapCommentsCount")
    default Integer mapCommentsCount(List<PostComment> postCommentList) {
        if (postCommentList != null) {
            Map<Long, PostComment> postCommentMap = new HashMap<>();
            for (PostComment postComment : postCommentList) {
                postCommentMap.put(postComment.getId(), postComment);
            }
            return postCommentList.stream()
                    .filter(p -> p.getIsDelete().equals(false))
                    .filter(p -> p.getPerson().getIsEnabled().equals(true))
                    .filter(p -> p.getParentId() == 0L || p.getParentId() != 0L
                            && postCommentMap.get(p.getParentId()).getIsDelete().equals(false))
                    .collect(Collectors.toList())
                    .size();
        }
        return 0;
    }

    @Named("mapImagePath")
    default String mapPhoto(List<PostFile> postFiles) {
        if (postFiles != null && !postFiles.isEmpty()) {
            return postFiles.get(0).getPath();
        }
        return null;
    }

    @Named("mapLikeAmount")
    default Integer mapLikeAmount(List<PostLike> likes) {
        if (likes == null) {
            return 0;
        }

        return likes.stream().filter(p -> p.getIsDelete().equals(false))
                .filter(p -> p.getPerson().getIsEnabled().equals(true))
                .collect(Collectors.toList()).size();
    }

    @Named("mapMyLike")
    default Boolean mapMyLike(List<PostLike> likes, @Context Long currentUser) {
        if (likes == null) {
            return null;
        }
        for (PostLike like : likes) {
            if (like.getPerson().getId().equals(currentUser) &&
                    like.getIsDelete().equals(false)) {
                return true;
            }
        }
        return false;
    }

    static Timestamp correctionTime(Long time) {
        Long timeUTC = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Long timeSystem = LocalDateTime.now().
                toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(LocalDateTime.now()));
        return new Timestamp((time + timeSystem - timeUTC) * MILLISECONDS_IN_SECONDS);
    }


    @Named("mapPublishDate")
    default String mapPublishDate(Long time, @Context boolean isTest) {
        if (isTest) {
            return "0";
        }
        if (time != null) {
            Timestamp timestamp = correctionTime(time);

            LocalDateTime localDateTime = timestamp.toLocalDateTime();


            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        }
        return "";
    }

    @Named("mapTags")
    default String[] mapTags(List<Tag> tags) {
        if (tags != null) {
            List<String> tagList = new ArrayList<>();
            for (Tag tag : tags) {
                tagList.add(tag.getTag());
            }
            String[] tagArray = new String[tagList.size()];
            tagList.toArray(tagArray);
            return tagArray;
        }
        return null;
    }
}