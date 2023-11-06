package com.cozybinarybase.accountstopthestore.model.asset.dto;

import com.cozybinarybase.accountstopthestore.model.asset.dto.constants.AssetType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssetSaveRequestDto {

  private AssetType assetType;

  @NotBlank(message = "자산명을 입력해주시길 바랍니다.")
  private String assetName;

  @Positive(message = "양수의 값만 입력할 수 있습니다.")
  private Long amount;

  private String memo;
}