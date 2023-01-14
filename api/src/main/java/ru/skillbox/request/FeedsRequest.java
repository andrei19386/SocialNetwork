package ru.skillbox.request;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
@Getter
public class FeedsRequest {
    private List<String> words;
    private List<String> tags;
    private Long dateFrom;
    private Long dateTo;
    private final Boolean isDelete;
    private String author;
    private final Pageable pageable;

    private Boolean withFriends;

    private Long accountId;

    private HttpServletRequest httpServletRequest;


    public FeedsRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
        this.pageable = generatePageableObjectByServlet(httpServletRequest);
        this.words = getWords(httpServletRequest);
        this.dateFrom = getDate(httpServletRequest, "dateFrom");
        this.dateTo = getDate(httpServletRequest, "dateTo");
        this.isDelete = getIsDelete(httpServletRequest);
        this.author = getAuthor(httpServletRequest);
        this.tags = getTags(httpServletRequest);
        this.withFriends = getWithFriends(httpServletRequest);
        this.accountId = getAccountId(httpServletRequest);
    }

    private Long getAccountId(HttpServletRequest httpServletRequest) {
        String accountIdString = httpServletRequest.getParameter("accountIds");
        if (accountIdString != null) {
            return Long.parseLong(accountIdString);
        }
        return null;
    }

    private Boolean getWithFriends(HttpServletRequest httpServletRequest) {
        String withFriendsString = httpServletRequest.getParameter("withFriends");
        if (withFriendsString != null) {
            return Boolean.parseBoolean(withFriendsString);
        }
        return null;
    }

    public FeedsRequest(Pageable pageable) {
        this.pageable = pageable;
        this.isDelete = false;
    }

    private List<String> getTags(HttpServletRequest httpServletRequest) {
        String tagsString = httpServletRequest.getParameter("tags");
        if (tagsString != null) {
            String[] tags = tagsString.toLowerCase(Locale.ROOT).split(",");
            Arrays.stream(tags).collect(Collectors.toList()).forEach(t -> log.debug("tag={}", t));
            return Arrays.stream(tags).collect(Collectors.toList());
        }
        return null;
    }

    private String getAuthor(HttpServletRequest httpServletRequest) {
        String authorString = httpServletRequest.getParameter("author");
        if (authorString != null) {
            log.debug("author={}", authorString.trim().replaceAll("\\s{2,}", "\\s"));
            return authorString.trim().toLowerCase(Locale.ROOT).replaceAll("\\s{2,}", "\\s");
        }
        return null;
    }

    private Long getDate(HttpServletRequest httpServletRequest, String name) {
        String dateString = httpServletRequest.getParameter(name);
        if (dateString != null) {
            log.debug(name + " " + LocalDateTime.ofEpochSecond(Long.parseLong(dateString),
                    0, ZoneOffset.of("+03:00")));
            log.debug("Long " + name + " " + dateString);
            return Long.parseLong(dateString);
        }
        return null;
    }

    private boolean getIsDelete(HttpServletRequest httpServletRequest) {
        String isDeleteString = httpServletRequest.getParameter("isDelete");
        if (isDeleteString != null) {
            return Boolean.parseBoolean(isDeleteString);
        }
        return false;
    }


    private List<String> getWords(HttpServletRequest httpServletRequest) {
        String text = httpServletRequest.getParameter("text");
        if (text != null) {
            String textOnlyBooksAndDigits = text
                    .toLowerCase(Locale.ROOT).replaceAll("[^0-9А-яA-z\\s]+", "");
            String textWithoutDoubleSpaces = textOnlyBooksAndDigits.replaceAll("[\\s]{2,}", "\\s");
            log.debug("Only Books in String: " + textOnlyBooksAndDigits);
            String[] textWords = textWithoutDoubleSpaces.trim().split("\\s");
            log.debug("Words number: {}", textWords.length);
            return Arrays.stream(textWords).collect(Collectors.toList());
        }
        return null;
    }

    private Pageable generatePageableObjectByServlet(HttpServletRequest httpServletRequest) {
        String pageString = httpServletRequest.getParameter("page");
        int page = pageString != null ? Integer.parseInt(pageString) : 0;
        log.debug("page= " + page);
        String sizeString = httpServletRequest.getParameter("size");
        int size = sizeString != null ? Integer.parseInt(sizeString) : 1;
        log.debug("size= " + size);
        String sort = httpServletRequest.getParameter("sort");
        if (page < 0) {
            page = 0;
        }
        Pageable pageable;
        if (sort != null) {
            String[] words = sort.split(",");
            if (words.length >= 2 && words[1].equals("desc")) {
                pageable = PageRequest.of(page, size, Sort.by(
                        words[0]).descending().and(Sort.by("id").descending()
                ));
            } else {
                pageable = PageRequest.of(page, size, Sort.by(
                        words[0]).ascending().and(Sort.by("id").ascending()
                ));
            }
        } else {
            pageable = PageRequest.of(page, size, Sort.by(
                    "time").descending().and(Sort.by("id").descending()
            ));
        }
        return pageable;
    }


}
