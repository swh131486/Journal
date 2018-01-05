package com.journal.utils;

public enum AttachmentType {
    PNG("image/png", "png"), TIFF("image/tiff", "tiff"), GIF("image/gif", "gif"), JPG("image/jpg", "jpg"), JPEG("image/jpeg", "jpg"), TXT(
            "text/plain", "txt"), RTF("text/rtf", "rtf"), HTML("text/html", "html"), HTM("text/html", "htm"), XML("application/xml", "xml"), PDF(
            "application/pdf", "pdf"), DOC("application/msword", "doc"), XLS("application/vnd.ms-excel", "xls"), PPT(
            "application/vnd.ms-powerpoint", "ppt"), PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"), XLSX(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"), DOCX(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"), MP4("video/mp4", "mp4"), AAC("audio/aac", "aac"), MSG(
            "application/vnd.ms-outlook", "msg"), ZIP("application/zip", "zip"), TIF("image/tiff", "tif"), DWG("application/acad", "dwg"), DWF(
            "drawing/x-dwf", "dwf"), DXF("image/vnd.dwg", "dxf"), MOV("video/quicktime", "mov"), M4V("video/x-m4v", "m4v"), THREEGP("video/3gpp",
            "3gp"), BMP("image/bmp", "bmp"), ICO("image/x-icon", "ico"), CUR("image/vnd.microsoft.icon", "cur"), XBM("image/x-xbitmap", "xbm"), MP3(
            "audio/mpeg3", "mp3");

    private String mimeType;
    private String extension;

    public String fetchMimeType() {
        return mimeType;
    }

    public String fetchExtension() {
        return extension;
    }

    AttachmentType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
}