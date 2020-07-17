package com.example.myapplication.domain;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Filter {

    @Retention(SOURCE)
    @StringDef({
            GENDER,
            NAME,
            STATUS,
            SPECIE
    })

    public @interface Type {
    }

    public static final String STATUS = "status";
    public static final String GENDER = "gender";
    public static final String NAME = "name";
    public static final String SPECIE = "species";


}
