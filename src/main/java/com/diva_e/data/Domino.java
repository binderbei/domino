package com.diva_e.data;

import lombok.Data;

import java.util.List;

/**
 * Created by jbinder on 28.06.17.
 */
@Data
public class Domino {

    private String dominoId;
    private String moduleId;
    private Integer inValue;
    private Integer outValue;
    private List<String> colorsIn;
    private List<String> colorsOut;
}
