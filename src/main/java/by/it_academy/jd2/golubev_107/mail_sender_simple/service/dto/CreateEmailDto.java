package by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto;

public class CreateEmailDto {

    private String recipientTo;
    private String title;
    private String text;

    private CreateEmailDto(String recipientTo, String title, String text) {
        this.recipientTo = recipientTo;
        this.title = title;
        this.text = text;
    }

    public static CreateEmailDtoBuilder builder() {
        return new CreateEmailDtoBuilder();
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


    public static class CreateEmailDtoBuilder {
        private String recipientTo;
        private String title;
        private String text;

        private CreateEmailDtoBuilder() {
        }

        public CreateEmailDtoBuilder setRecipientTo(String recipientTo) {
            this.recipientTo = recipientTo;
            return this;
        }

        public CreateEmailDtoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public CreateEmailDtoBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public CreateEmailDto build() {
            return new CreateEmailDto(recipientTo, title, text);
        }
    }
}
