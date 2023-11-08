package com.cozybinarybase.accountstopthestore.model.accountbook.service;

import com.cozybinarybase.accountstopthestore.model.accountbook.domain.AccountBook;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookImageResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookSaveResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookUpdateRequestDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.AccountBookUpdateResponseDto;
import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.TransactionType;
import com.cozybinarybase.accountstopthestore.model.accountbook.exception.AccountBookNotValidException;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.entity.AccountBookEntity;
import com.cozybinarybase.accountstopthestore.model.accountbook.persist.repository.AccountBookRepository;
import com.cozybinarybase.accountstopthestore.model.asset.exception.AssetNotValidException;
import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import com.cozybinarybase.accountstopthestore.model.asset.persist.repository.AssetRepository;
import com.cozybinarybase.accountstopthestore.model.category.exception.CategoryNotValidException;
import com.cozybinarybase.accountstopthestore.model.category.persist.entity.CategoryEntity;
import com.cozybinarybase.accountstopthestore.model.category.persist.repository.CategoryRepository;
import com.cozybinarybase.accountstopthestore.model.images.persist.entity.ImageEntity;
import com.cozybinarybase.accountstopthestore.model.member.domain.Member;
import com.cozybinarybase.accountstopthestore.model.member.persist.repository.MemberRepository;
import com.cozybinarybase.accountstopthestore.model.member.service.MemberService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccountBookService {

  private final AccountBookRepository accountBookRepository;
  private final CategoryRepository categoryRepository;
  private final AssetRepository assetRepository;
  private final MemberService memberService;
  private final AccountBook accountBook;
  private final MemberRepository memberRepository;

  @Transactional
  public AccountBookSaveResponseDto saveAccountBook(
      AccountBookSaveRequestDto requestDto, Member member) {
    memberService.validateAndGetMember(member);

    CategoryEntity categoryEntity = categoryRepository.findByNameAndMember_Id(
            requestDto.getCategoryName(), member.getId())
        .orElseThrow(CategoryNotValidException::new);

    AssetEntity assetEntity = assetRepository.findByNameAndMember_Id(requestDto.getAssetType(),
        member.getId()).orElseThrow(
        AssetNotValidException::new);

    AccountBookEntity accountBookEntity = accountBookRepository.save(
        accountBook.createAccountBook(requestDto, categoryEntity.getId(), assetEntity.getId(),
            member.getId(), categoryEntity.getName(), assetEntity.getName()).toEntity());

    return AccountBookSaveResponseDto.fromEntity(accountBookEntity);
  }

  @Transactional
  public AccountBookUpdateResponseDto updateAccountBook(Long accountId,
      AccountBookUpdateRequestDto requestDto, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember_Id(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    categoryRepository.findByNameAndMember_Id(
            requestDto.getCategoryName(), member.getId())
        .orElseThrow(CategoryNotValidException::new);

    assetRepository.findByNameAndMember_Id(requestDto.getAssetType(),
        member.getId()).orElseThrow(
        AssetNotValidException::new);

    AccountBook accountBookDomain = AccountBook.fromEntity(accountBookEntity);
    accountBookDomain.updateAccountBook(requestDto);

    AccountBookEntity updateAccountBookEntity = accountBookDomain.toEntity();
    accountBookRepository.save(updateAccountBookEntity);

    return AccountBookUpdateResponseDto.fromEntity(updateAccountBookEntity);
  }

  @Transactional
  public void deleteAccountBook(Long accountId, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember_Id(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    accountBookRepository.delete(accountBookEntity);
  }

  @Transactional(readOnly = true)
  public List<AccountBookResponseDto> getAccountBooks(LocalDate startDate, LocalDate endDate,
      TransactionType transactionType, int page, int limit, Member member) {
    memberService.validateAndGetMember(member);

    if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
      throw new AccountBookNotValidException("날짜 설정을 다시 해주시길 바랍니다.");
    }

    Pageable pageable = PageRequest.of(page, limit);

    List<AccountBookEntity> accountBookEntityList =
        accountBookRepository.findByCreatedAtBetweenAndTransactionTypeAndMember_Id(
            startDate.atStartOfDay(),
            endDate.atStartOfDay(),
            transactionType,
            member.getId(),
            pageable).getContent();

    return accountBookEntityList.stream()
        .map(AccountBookResponseDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<AccountBookImageResponseDto> getAccountBookImages(Long accountId, Member member) {
    memberService.validateAndGetMember(member);

    AccountBookEntity accountBookEntity = accountBookRepository.findByIdAndMember_Id(accountId,
            member.getId())
        .orElseThrow(AccountBookNotValidException::new);

    List<ImageEntity> images = accountBookEntity.getImages();

    return images.stream()
        .map(AccountBookImageResponseDto::of)
        .collect(Collectors.toList());
  }
}
