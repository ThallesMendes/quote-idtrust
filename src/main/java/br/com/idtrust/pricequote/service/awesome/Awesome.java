package br.com.idtrust.pricequote.service.awesome;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Awesome {

  @GET("/json/daily/{currencyCode}-{currencyCodeIn}")
  Call<List<AwesomeResponse>> getCurrencyQuote(@Path("currencyCode") final String currencyCode,
                                               @Path("currencyCodeIn") final String currencyCodeIn,
                                               @Query("start_date") final String startDate,
                                               @Query("end_date") final String endDate);

}
