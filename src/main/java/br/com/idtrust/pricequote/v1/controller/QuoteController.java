package br.com.idtrust.pricequote.v1.controller;

import java.time.LocalDate;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.common.controller.BaseController;
import br.com.idtrust.pricequote.common.response.Response;
import br.com.idtrust.pricequote.v1.mapper.QuotationMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/quote")
@AllArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@Autowired))
public class QuoteController extends BaseController {

  private final QuoteBusiness quoteBusiness;

  @GetMapping("/{culture}")
  public ResponseEntity<Response> quote(
      @PathVariable("culture") final String culture,
      @RequestParam(value = "date")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {

    final var quotation = this.quoteBusiness.quotePrice(culture, date);
    return this.buildResponse(HttpStatus.OK, QuotationMapper.toResponseDto(quotation));
  }

}
