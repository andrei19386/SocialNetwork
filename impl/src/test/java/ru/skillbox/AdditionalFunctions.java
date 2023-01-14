package ru.skillbox;

public class AdditionalFunctions {

    public static String generateExpectedCommentString(long personId, long postId, long postCommentId) {
        return "{" +
                "  \"totalElements\": 1," +
                "  \"totalPages\": 1," +
                "  \"number\": 0," +
                "  \"size\": 5," +
                "  \"content\": [" +
                "    {" +
                "      \"id\": " + postCommentId + "," +
                "      \"commentType\": \"POST\"," +
                "      \"time\": \"0\"," +
                "      \"timeChanged\": null," +
                "      \"authorId\": " + personId + "," +
                "      \"parentId\": 0," +
                "      \"commentText\": \"Text of comment\"," +
                "      \"postId\": " + postId + "," +
                "      \"isBlocked\": false," +
                "      \"isDelete\": false," +
                "      \"likeAmount\": 0," +
                "      \"myLike\": false," +
                "      \"commentsCount\": 1," +
                "      \"imagePath\": null" +
                "    }" +
                "  ]," +
                "  \"sort\": {" +
                "    \"empty\": false," +
                "    \"unsorted\": false," +
                "    \"sorted\": true" +
                "  }," +
                "  \"first\": true," +
                "  \"last\": true," +
                "  \"numberOfElements\": 1," +
                "  \"pageable\": {" +
                "    \"offset\": 0," +
                "    \"sort\": {" +
                "      \"empty\": false," +
                "      \"unsorted\": false," +
                "      \"sorted\": true" +
                "    }," +
                "    \"pageSize\": 5," +
                "    \"pageNumber\": 0," +
                "    \"paged\": true," +
                "    \"unpaged\": false" +
                "  }," +
                "  \"empty\": false" +
                "}";
    }

    public static String generateExpectedResponseString(long personId, long postId) {
        return "{" +
                "\"totalElements\": 1, " +
                "\"totalPages\": 1, " +
                "\"number\": 0, " +
                "\"size\": 5, " +
                "\"content\": [" +
                "{" +
                "\"id\": " + postId + "," +
                "\"time\": \"0\"," +
                "\"timeChanged\": \"0\"," +
                "\"authorId\": " + personId + "," +
                "\"title\": \"SomeTitle\"," +
                "\"type\": \"POSTED\"," +
                "\"postText\": \"SomeText\"," +
                "\"isBlocked\": true," +
                "\"isDelete\": false," +
                "\"commentsCount\": 2," +
                "\"tags\": []," +
                "\"likeAmount\": 0," +
                "\"myLike\": false," +
                "\"imagePath\": null," +
                "\"publishDate\": \"0\"" +
                "}" +
                "]," +
                "  \"sort\": {" +
                "    \"empty\": false," +
                "    \"unsorted\": false," +
                "    \"sorted\": true" +
                "  }," +
                "  \"first\": true," +
                "  \"last\": true," +
                "  \"numberOfElements\": 1," +
                "  \"pageable\": {" +
                "    \"offset\": 0," +
                "    \"sort\": {" +
                "      \"empty\": false," +
                "      \"unsorted\": false," +
                "      \"sorted\": true" +
                "    }," +
                "    \"pageSize\": 5," +
                "    \"pageNumber\": 0," +
                "    \"paged\": true," +
                "    \"unpaged\": false" +
                "  }," +
                "  \"empty\": false" +
                "}";
    }


    public static String generateExpectedSubCommentString(Long subCommentId,
                                                          Long personId,
                                                          Long postCommentId,
                                                          Long postId) {
        return "{" +
                "  \"totalElements\": 1," +
                "  \"totalPages\": 1," +
                "  \"number\": 0," +
                "  \"size\": 2," +
                "  \"content\": [" +
                "    {" +
                "      \"id\": " + subCommentId + "," +
                "      \"commentType\": \"POST\"," +
                "      \"time\": \"0\"," +
                "      \"timeChanged\": null," +
                "      \"authorId\": " + personId + "," +
                "      \"parentId\": " + postCommentId + "," +
                "      \"commentText\": \"Text of subcomment\"," +
                "      \"postId\": " + postId + "," +
                "      \"isBlocked\": false," +
                "      \"isDelete\": false," +
                "      \"likeAmount\": 0," +
                "      \"myLike\": false," +
                "      \"commentsCount\": 0," +
                "      \"imagePath\": null" +
                "    }" +
                "  ]," +
                "  \"sort\": {" +
                "    \"empty\": false," +
                "    \"unsorted\": false," +
                "    \"sorted\": true" +
                "  }," +
                "  \"first\": true," +
                "  \"last\": true," +
                "  \"numberOfElements\": 1," +
                "  \"pageable\": {" +
                "    \"offset\": 0," +
                "    \"sort\": {" +
                "      \"empty\": false," +
                "      \"unsorted\": false," +
                "      \"sorted\": true" +
                "    }," +
                "    \"pageSize\": 2," +
                "    \"pageNumber\": 0," +
                "    \"paged\": true," +
                "    \"unpaged\": false" +
                "  }," +
                "  \"empty\": false" +
                "}";
    }
}
