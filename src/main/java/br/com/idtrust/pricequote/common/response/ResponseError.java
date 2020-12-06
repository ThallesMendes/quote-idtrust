package br.com.idtrust.pricequote.common.response;

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
public class ResponseError implements Serializable {
  @Serial
  private static final long serialVersionUID = 1961649513796655055L;

  private String message;
}
