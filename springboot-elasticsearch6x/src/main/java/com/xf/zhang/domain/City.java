package com.xf.zhang.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class City implements Serializable {

    private String id;

    private String name;

    private String description;

    private int score;

}
