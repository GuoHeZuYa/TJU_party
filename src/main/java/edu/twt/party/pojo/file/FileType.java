package edu.twt.party.pojo.file;


public enum FileType {
    DOCUMENT(),
    IMAGE(),
    ARCHIVE(),
    MEDIA(),
    UNKNOWN();


    public static FileType getFileType(String mimeType){
        if(mimeType.equals("application/zip")){

        }
        if(mimeType.equals("application/zip") || mimeType.startsWith("application/x-rar-compressed") || mimeType.equals("application/x-7z-compressed"))return ARCHIVE;
        if(mimeType.equals("application/pdf") || mimeType.equals("application/x-tika-ooxml") ||mimeType.equals("application/x-tika-msoffice"))return DOCUMENT;
        if(mimeType.startsWith("image/"))return IMAGE;
        if(mimeType.startsWith("audio") || mimeType.startsWith("video"))return MEDIA;
        return UNKNOWN;
    }
}
