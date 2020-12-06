package br.com.idtrust.pricequote.common.response;

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
public class Response implements Serializable {
  @Serial
  private static final long serialVersionUID = 8149890161252664214L;

  private String server;
  private String version;
  private List<? extends Serializable> data;

}
