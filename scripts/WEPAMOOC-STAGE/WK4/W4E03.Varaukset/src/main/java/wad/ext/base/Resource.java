package wad.ext.base;

import org.springframework.hateoas.ResourceSupport;

public class Resource<T> extends ResourceSupport {

    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
