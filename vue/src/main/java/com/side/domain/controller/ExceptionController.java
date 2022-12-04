package com.side.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.global.exception.UnAuthorizedException;
import com.side.global.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

 @GetMapping(value = "/entrypoint")
 public UserNotFoundException entrypointException() {
     throw new UnAuthorizedException();
 }
}