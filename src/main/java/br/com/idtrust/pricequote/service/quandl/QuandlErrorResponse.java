package br.com.idtrust.pricequote.service.quandl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlErrorResponse implements Serializable {
  @Serial
  private static final long serialVersionUID = -6710888956608467680L;

  @JsonProperty("quandl_error")
  private QuandlError quandlError;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class QuandlError implements Serializable {
    @Serial
    private static final long serialVersionUID = 5565032986683146382L;

    private String code;
    private String message;
  }
}
