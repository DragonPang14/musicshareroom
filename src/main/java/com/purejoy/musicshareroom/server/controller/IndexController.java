package com.purejoy.musicshareroom.server.controller;

import com.purejoy.musicshareroom.common.dto.ResultDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @PostMapping("/login")
    public ResultDto login(){
        return ResultDto.successOf();
    }

}
