package ru.skillbox.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendshipResponse extends AbstractResponse {

    private int id;
    @JsonProperty("status_id")
    private Integer statusId;
    @JsonProperty("src_person_id")
    private Integer srcPersonId;
    @JsonProperty("dst_person_id")
    private Integer dstPersonId;

}
