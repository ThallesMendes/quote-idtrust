package br.com.idtrust.pricequote.service.quandl;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Quandl {

  @GET("/api/v3/datasets/CEPEA/{culture}")
  Call<QuandlResponse> getQuote(@Path("culture") final String culture,
                                @Query("start_date") final LocalDate startDate,
                                @Query("end_date") final LocalDate endDate,
                                @Query("api_key") final String apiKey);

}
