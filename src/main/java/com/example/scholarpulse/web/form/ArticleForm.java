package com.example.scholarpulse.web.form;

import jakarta.validation.constraints.Size;

public class ArticleForm {

    @Size(min = 1, max = 500, message = "Title must be between 1 and 500 characters")
    private String title;

    private String authors;
    private String abstractText;
    private Integer publicationYear;
    private String doi;
    private String url;
    private String sourceName;
    private Integer citationCount;

    public ArticleForm() {
    }

    public ArticleForm(
            String title,
            String authors,
            String abstractText,
            Integer publicationYear,
            String doi,
            String url,
            String sourceName,
            Integer citationCount
    ) {
        this.title = title;
        this.authors = authors;
        this.abstractText = abstractText;
        this.publicationYear = publicationYear;
        this.doi = doi;
        this.url = url;
        this.sourceName = sourceName;
        this.citationCount = citationCount;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public String getDoi() {
        return doi;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Integer getCitationCount() {
        return citationCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setCitationCount(Integer citationCount) {
        this.citationCount = citationCount;
    }
}
