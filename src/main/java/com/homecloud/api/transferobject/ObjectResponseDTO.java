package com.homecloud.api.transferobject;

import java.util.Optional;

public class ObjectResponseDTO<T> extends ResponseDTO {

    Optional<T> data;
    Optional<T> meta;

    public ObjectResponseDTO(boolean success, String message, Optional<T> data, Optional<T> meta) {
        super(success, message);
        this.data = data;
        this.meta = meta;
    }

    public Optional<T> getData() {
        return data;
    }

    public void setData(Optional<T> data) {
        this.data = data;
    }

    public Optional<T> getMeta() {
        return meta;
    }

    public void setMeta(Optional<T> meta) {
        this.meta = meta;
    }

}
