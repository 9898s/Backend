package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.persist.entity.AssetEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AssetSaveResponseDto {

  private Long assetId;
  private Long memberId;
  private String assetType;
  private String assetName;
  private Long amount;
  private String memo;

  public static AssetSaveResponseDto fromEntity(AssetEntity assetEntity) {
    return AssetSaveResponseDto.builder()
        .assetId(assetEntity.getId())
        .memberId(assetEntity.getMember().getId())
        .assetType(assetEntity.getType().name())
        .assetName(assetEntity.getName())
        .amount(assetEntity.getAmount())
        .memo(assetEntity.getMemo())
        .build();
  }
}