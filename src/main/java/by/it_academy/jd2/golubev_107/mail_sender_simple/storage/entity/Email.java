package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "emails")
public class Email {

    @Id
    private UUID id;

    @Column(name = "recipient_to")
    private String recipientTo;

    @Column(name = "title")
    private String title;

    @Column(name = "body_text")
    private String text;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus emailStatus;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    public Email(UUID id, String recipientTo, String title, String text, EStatus emailStatus,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.recipientTo = recipientTo;
        this.title = title;
        this.text = text;
        this.emailStatus = emailStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Email() {
    }

    public static EmailBuilder builder() {
        return new EmailBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRecipientTo() {
        return recipientTo;
    }

    public void setRecipientTo(String recipientTo) {
        this.recipientTo = recipientTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EStatus getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(EStatus emailStatus) {
        this.emailStatus = emailStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(id, email.id) && Objects.equals(recipientTo, email.recipientTo) && Objects.equals(title, email.title) && Objects.equals(text, email.text) && Objects.equals(emailStatus, email.emailStatus) && Objects.equals(createdAt, email.createdAt) && Objects.equals(updatedAt, email.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipientTo, title, text, emailStatus, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", recipientTo='" + recipientTo + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", emailStatus=" + emailStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public static class EmailBuilder {
        private UUID id;
        private String recipientTo;
        private String title;
        private String text;
        private EStatus emailStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private EmailBuilder() {
        }

        public EmailBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public EmailBuilder setRecipientTo(String recipientTo) {
            this.recipientTo = recipientTo;
            return this;
        }

        public EmailBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EmailBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public EmailBuilder setEmailStatus(EStatus emailStatus) {
            this.emailStatus = emailStatus;
            return this;
        }

        public EmailBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmailBuilder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Email build() {
            return new Email(id, recipientTo, title, text, emailStatus, createdAt, updatedAt);
        }
    }
}
