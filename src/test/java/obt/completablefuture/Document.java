package obt.completablefuture;

public class Document {
    private String rawWebsiteContent;

    public Document(String rawWebsiteContent) {

        this.rawWebsiteContent = rawWebsiteContent;
    }

    @Override
    public String toString() {
        return "Document{rawWebsiteContent='" + rawWebsiteContent + '\'' + '}';
    }
}
