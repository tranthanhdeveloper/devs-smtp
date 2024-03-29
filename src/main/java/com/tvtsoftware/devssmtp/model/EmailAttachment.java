package com.tvtsoftware.devssmtp.model;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "email_attachment")
public class EmailAttachment {
    @Id
    @SequenceGenerator(name = "email_attachment_generator", sequenceName = "email_attachment_sequence", allocationSize = 1)
    @GeneratedValue(generator = "email_attachment_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email")
    private Email email;

    @Column(name = "filename")
    private String filename;

    @Column(name = "content_id", nullable = false)
    private String contentId;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Lob
    @Column(name = "data", nullable = false)
    @Basic(optional = false)
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContenttype(String contenttype) {
        this.contentType = contenttype;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAttachment that = (EmailAttachment) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(filename, that.filename) && Objects.equals(contentId, that.contentId) && Objects.equals(contentType, that.contentType) && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, filename, contentId, contentType);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
