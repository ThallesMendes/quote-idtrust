package br.com.idtrust.pricequote.service.quandl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuandlResponse implements Serializable {
  @Serial
  private static final long serialVersionUID = -6073451133909563553L;

  private Dataset dataset;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Dataset implements Serializable {
    @Serial
    private static final long serialVersionUID = 2841622467400815017L;

    private Long id;
    private List<List<?>> data;
  }

}
