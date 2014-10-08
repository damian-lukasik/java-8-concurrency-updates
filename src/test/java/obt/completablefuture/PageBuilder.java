package obt.completablefuture;

public class PageBuilder {
    public static Document parse(String rawWebsiteContent) {
        return new Document(rawWebsiteContent);
    }
}
