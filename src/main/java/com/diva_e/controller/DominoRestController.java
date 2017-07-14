package com.diva_e.controller;

import com.diva_e.data.Domino;
import com.diva_e.services.DominoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jbinder on 28.06.17.
 */
@RestController
@RequestMapping("rest-api")
public class DominoRestController {

    @Autowired
    private DominoService dominoService;

    @RequestMapping(value = "/dominos/{moduleId}", method = RequestMethod.GET)
    public List<Domino> getModuleDominos(
            @PathVariable("moduleId") Integer moduleId,
            @RequestParam(value="selected", required = false, defaultValue = "") List<String> selected) {
        return dominoService.getDominosForId(moduleId);
    }
}
