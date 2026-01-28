package com.homecloud.api.transferobject;

public class FileTypeDTO {
    private String name;
    private String type;
    private long size;
    private String description;
    private boolean isFolder;

    public FileTypeDTO(String name, String type, long size, String description, boolean isFolder) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.description = description;
        this.isFolder = isFolder;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFolder() {
        return isFolder;
    }

    @Override
    public String toString() {
        return "FileTypeDTO{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", description='" + description + '\'' +
                ", isFolder=" + isFolder +
                '}';
    }
}
