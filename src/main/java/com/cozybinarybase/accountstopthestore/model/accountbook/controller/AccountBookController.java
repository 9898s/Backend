package com.cozybinarybase.accountstopthestore.model.accountbook.controller;

import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.service.AccountBookService;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountBookController {

  private final AccountBookService accountBookService;

  @PostMapping
  public ResponseEntity<?> saveAccountBook(
      @RequestBody AccountBookSaveRequestDto requestDto,
      @AuthenticationPrincipal Member member
  ) {
    AccountBookSaveResponseDto responseDto =
        accountBookService.saveAccountBook(requestDto, member);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }
}
