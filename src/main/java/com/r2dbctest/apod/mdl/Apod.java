package com.r2dbctest.apod.mdl;

import java.time.LocalDateTime;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.r2dbctest.exception.ApodException;
import com.r2dbctest.exception.ErrorCode;

import static java.util.Objects.requireNonNull;
import lombok.Getter;
import lombok.ToString;

@Table("apod")
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Apod {
    @Id
    private Long id;
    private String copyright, explanation, mediaType, hdurl, url;

    private @AccessType(Type.PROPERTY) String title;
    @Transient
    private String date;
    @Column(value = "created_time")
    private LocalDateTime createdTime;

    public Apod(){}

    public Apod (String title, String copyright, String date, String explanation, String hdurl, String mediaType, String url, LocalDateTime createdTime) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.createdTime = createdTime;
        this.hdurl = hdurl;
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = requireNonNull(title, "title");

        if(title.contains("%")) {
            throw new ApodException(ErrorCode.TITLE_NOT_PROPER.getMessage(), ErrorCode.TITLE_NOT_PROPER.getStatusCode());
        }
    }
}
