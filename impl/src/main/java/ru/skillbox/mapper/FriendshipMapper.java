package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendshipMapper {

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "photo", source = "photo")
//    //@Mapping(target = "statusCode", source = "statusCode")
//    @Mapping(target = "firstName", source = "firstName")
//    @Mapping(target = "lastName", source = "lastName")
//    @Mapping(target = "city", source = "city")
//    @Mapping(target = "country", source = "country")
//    @Mapping(target = "isOnline", source = "isOnline")
//    @Mapping(target = "birthDate", source = "birthDate")
//
//
//    PersonDto personToFriendship(Person person, StatusCode code);
}
