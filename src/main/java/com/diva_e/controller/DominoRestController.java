package com.diva_e.controller;

import com.diva_e.data.Domino;
import com.diva_e.services.DominoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jbinder on 28.06.17.
 */
@RestController
public class DominoRestController {

    @Autowired
    private DominoService dominoService;

    @RequestMapping("/dominos/{moduleId}")
    public List<Domino> getModuleDominos(@PathVariable("moduleId") Integer moduleId) {
        return dominoService.getDominosForId(moduleId);
    }
}
