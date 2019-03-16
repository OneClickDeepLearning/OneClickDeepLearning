package com.ocdl.proxy.dto;

import java.io.Serializable;

public class ProjectDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public ProjectDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
