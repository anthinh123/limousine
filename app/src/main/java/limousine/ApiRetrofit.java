package limousine;

import java.util.List;

import limousine.Model.BringLocation;
import limousine.Model.Office;
import limousine.Model.Route;
import limousine.Model.Seat;
import limousine.Model.TripByDate;
import limousine.Model.Update;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by An Van Thinh on 9/1/2017.
 */

public interface ApiRetrofit {
    @GET("office")
    Call<List<Office>> getAllOffice(@Query("type") String type);

    @GET("routes")
    Call<List<Route>> getRoute(@Query("id_office") int id_office);

    @GET("tripbydate")
    Call<List<TripByDate>> getTripByDate(@Query("idRoute") int id_route,
                                         @Query("date") String date);

    @GET("bringlocation")
    Call<List<BringLocation>> getLocation(@Query("idRoute") int idRoute);

    @GET("ticketbyseat")
    Call<List<Seat>> getAllSeat(@Query("idTrip") int idTrip);

    @PUT("ticketbyseat/update")
    Call<Update> updateSeatStatus(@Query("idSeat") int idSeat,
                                  @Query("status") int status);


    @GET("ticketbyseat")
    Call<List<Seat>> getSeatById(@Query("idSeat") int idSeat);

}
