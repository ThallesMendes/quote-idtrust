package br.com.idtrust.pricequote.v1.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import br.com.idtrust.pricequote.business.QuoteBusiness;
import br.com.idtrust.pricequote.common.exception.EntityNotFoundException;
import br.com.idtrust.pricequote.common.exception.ServiceException;
import br.com.idtrust.pricequote.common.response.Response;
import br.com.idtrust.pricequote.common.response.ResponseError;
import br.com.idtrust.pricequote.config.RestControllerAdvice;
import br.com.idtrust.pricequote.seed.QuotationSeeder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

  @Mock
  private QuoteBusiness quoteBusiness;

  private ObjectMapper mapper;

  private QuoteController controller;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {

    this.controller = new QuoteController(this.quoteBusiness);
    this.mapper = new ObjectMapper().findAndRegisterModules();

    final var controllerAdvice = new RestControllerAdvice();

    this.mockMvc = standaloneSetup(controller)
        .setControllerAdvice(controllerAdvice)
        .build();
  }

  @Test
  @DisplayName("should be quote success")
  void shouldBeQuoteSuccess() throws Exception {

    when(this.quoteBusiness.quotePrice(anyString(), any(LocalDate.class)))
        .thenReturn(QuotationSeeder.simple());

    final var response = this.mockMvc.perform(get("/v1/quote/CULTURE")
        .queryParam("date", "2020-12-04"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    final var responseObj = this.mapper.readValue(response, Response.class);

    assertNotNull(responseObj);
    assertFalse(responseObj.getData().isEmpty());

    final var quotation = responseObj.getData().get(0);

    assertNotNull(quotation);
  }

  @Test
  @DisplayName("should be quote error service exception")
  void shouldBeQuoteErrorServiceException() throws Exception {

    final var ex = new ServiceException("client_default", "{}");

    when(this.quoteBusiness.quotePrice(anyString(), any(LocalDate.class)))
        .thenAnswer(invocationOnMock -> {
          throw ex;
        });

    final var response = this.mockMvc.perform(get("/v1/quote/CULTURE")
        .queryParam("date", "2020-12-04"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message", equalTo(ex.getMessage())))
        .andReturn()
        .getResponse()
        .getContentAsString();

    final var responseObj = this.mapper.readValue(response, ResponseError.class);

    assertNotNull(responseObj);
  }

  @Test
  @DisplayName("should be quote error entity not found exception")
  void shouldBeQuoteErrorEntityNotFoundException() throws Exception {

    final var ex = new EntityNotFoundException("Quotation");

    when(this.quoteBusiness.quotePrice(anyString(), any(LocalDate.class)))
        .thenAnswer(invocationOnMock -> {
          throw ex;
        });

    final var response = this.mockMvc.perform(get("/v1/quote/CULTURE")
        .queryParam("date", "2020-12-04"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", equalTo(ex.getMessage())))
        .andReturn()
        .getResponse()
        .getContentAsString();

    final var responseObj = this.mapper.readValue(response, ResponseError.class);

    assertNotNull(responseObj);
  }

  @Test
  @DisplayName("should be quote error generic exception")
  void shouldBeQuoteErrorGenericException() throws Exception {

    final var ex = new Exception("Generic Exception");

    when(this.quoteBusiness.quotePrice(anyString(), any(LocalDate.class)))
        .thenAnswer(invocationOnMock -> {
          throw ex;
        });

    final var response = this.mockMvc.perform(get("/v1/quote/CULTURE")
        .queryParam("date", "2020-12-04"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message", equalTo("Unknown error ! Class: Exception Message: Generic Exception")))
        .andReturn()
        .getResponse()
        .getContentAsString();

    final var responseObj = this.mapper.readValue(response, ResponseError.class);

    assertNotNull(responseObj);
  }
}