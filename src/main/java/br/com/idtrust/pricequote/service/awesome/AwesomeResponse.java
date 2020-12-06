package br.com.idtrust.pricequote.service.awesome;

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
public class AwesomeResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = -4547036189309112050L;

  private String code;

  @JsonProperty("codein")
  private String codeIn;

  private Double ask;
}
