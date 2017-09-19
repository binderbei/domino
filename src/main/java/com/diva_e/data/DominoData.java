package com.diva_e.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbinder on 19.09.17.
 */
@Data
public class DominoData {
    private List<Domino> dominos = new ArrayList<>();
    private List<String> activeModules = new ArrayList<>();
}
