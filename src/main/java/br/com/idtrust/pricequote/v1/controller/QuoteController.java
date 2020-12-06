package br.com.idtrust.pricequote.v1.controller;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.common.controller.BaseController;
import br.com.idtrust.pricequote.common.response.Response;
import br.com.idtrust.pricequote.common.validator.ValidDate;
import br.com.idtrust.pricequote.v1.mapper.QuotationMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/quote")
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Autowired))
@Validated
public class QuoteController extends BaseController {

  private final QuoteBusiness quoteBusiness;

  @GetMapping("/{culture}")
  public ResponseEntity<Response> quote(
      @PathVariable("culture") @NotBlank @Size(min = 2) final String culture,
      @RequestParam(value = "date") @ValidDate final String date) {

    final var dateQuote = LocalDate.parse(date);
    final var quotation = this.quoteBusiness.quotePrice(culture, dateQuote);

    return this.buildResponse(HttpStatus.OK, QuotationMapper.toResponseDto(quotation));
  }

}
