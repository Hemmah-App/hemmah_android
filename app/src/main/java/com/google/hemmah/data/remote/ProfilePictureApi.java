package com.google.hemmah.data.remote;

import static com.google.hemmah.Utils.Constants.getProfilePicturePath;
import static com.google.hemmah.Utils.Constants.updateProfilePicturePath;

import com.google.hemmah.data.remote.dto.ApiResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ProfilePictureApi {
    @GET(getProfilePicturePath)
    Observable<Response<ApiResponse>> getProfilePicture(@Header("Authorization") String token);

    @Multipart
    @POST(updateProfilePicturePath)
    Observable<Response<ApiResponse>> updateProfilePicture(
            @Header("Authorization") String token,
            @Part MultipartBody.Part image);

}
