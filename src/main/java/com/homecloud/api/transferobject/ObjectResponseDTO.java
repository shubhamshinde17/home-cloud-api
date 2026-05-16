package com.homecloud.api.transferobject;

import java.util.Optional;

public class ObjectResponseDTO<T, MT> {

    Optional<T> data;
    Optional<MT> meta;

    public ObjectResponseDTO(boolean success, String message, Optional<T> data, Optional<MT> meta) {
        this.data = data;
        this.meta = meta;
    }

    public Optional<T> getData() {
        return data;
    }

    public void setData(Optional<T> data) {
        this.data = data;
    }

    public Optional<MT> getMeta() {
        return meta;
    }

    public void setMeta(Optional<MT> meta) {
        this.meta = meta;
    }

}
