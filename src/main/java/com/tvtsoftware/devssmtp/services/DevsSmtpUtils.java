package com.tvtsoftware.devssmtp.services;

import com.tvtsoftware.devssmtp.ContentType;
import com.tvtsoftware.devssmtp.model.Email;
import com.tvtsoftware.devssmtp.model.EmailAttachment;
import com.tvtsoftware.devssmtp.model.EmailContent;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DevsSmtpUtils {
    private static final Pattern CID_PATTERN = Pattern.compile("<img[^>]+src=(?:\"cid:([^\">]+)\"|'cid:([^'>]+)')");

    public String render(EmailContent content) {
        if (content.getContentType() == ContentType.HTML || content.getContentType().equals(ContentType.MULTIPART_MIXED)) {
            return harmonizeHtmlContent(content);
        }
        if (content.getContentType() == ContentType.PLAIN) {
            return convertLineBreaksToParagraphs(content);
        }
        return content.getData();
    }

    private String harmonizeHtmlContent(EmailContent content) {
        return Optional.ofNullable(content.getData())
                .map(c -> replaceCidImageSourcesWithInlineImages(c, content.getEmail()))
                .map(this::harmonizeHtmlDocument)
                .orElse("");
    }

    private String replaceCidImageSourcesWithInlineImages(String html, Email email) {
        if (html.contains("cid:")) {
            var matcher = CID_PATTERN.matcher(html);
            return matcher.replaceAll(mr -> replaceContentIdWithBase64DataWhenAvailable(mr, email));
        }
        return html;
    }

    private String harmonizeHtmlDocument(String html) {
        var doc = Jsoup.parse(html);
        doc = doc.normalise();
        return doc.html();
    }

    private String replaceContentIdWithBase64DataWhenAvailable(MatchResult mr, Email email) {
        return email.getAttachments()
                .stream()
                .filter(emailAttachment -> mr.group(1).equals(emailAttachment.getContentId()))
                .map(i -> mapInlineImage(mr, i))
                .findFirst().orElseGet(mr::group);
    }

    private static String mapInlineImage(MatchResult mr, EmailAttachment i) {
        return mr.group().replace("cid:" + mr.group(1), "data:" + i.getContentType() + ";base64, " + encodeBytes(i.getData()));
    }
    private static String encodeBytes(byte[] data){
        return Base64.getEncoder().withoutPadding().encodeToString(data);
    }

    private String convertLineBreaksToParagraphs(EmailContent content) {
        return Stream.of(content.getData().split("\n")).map(v -> "<p>" + v + "</p>").collect(Collectors.joining());
    }
}
