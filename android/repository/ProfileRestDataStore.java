package com.effectivesoft.repository.profile;

import com.effectivesoft.analytics.AnalyticsLogger;
import com.effectivesoft.representation.FullInfoPerson;
import com.effectivesoft.rest.RequestManager;
import com.effectivesoft.rest.api.BaseResponse;
import com.effectivesoft.rest.api.DeletePhotoRequest;
import com.effectivesoft.rest.api.FriendRequest;
import com.effectivesoft.rest.api.IndicateSuggestionRequest;
import com.effectivesoft.rest.api.PhotoEntity;
import com.effectivesoft.rest.api.ReorderPhotoRequest;
import com.effectivesoft.rest.api.ReorderPhotoResponse;
import com.effectivesoft.rest.api.SyncResponse;
import com.effectivesoft.rest.api.UpdateProfileRequest;
import com.effectivesoft.rest.api.UploadPhotoResponse;

import java.io.File;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public class ProfileRestDataStore {

    private interface ApiService {

        @GET("abbreviated_sync/")
        Observable<SyncResponse> sync(@Query("device_token") String device_token);

        @GET("profile/{account_id}/")
        Observable<FullInfoPerson> getUserProfileById(@Path("account_id") int account_id,
                                                      @Query("device_token") String device_token);

        @POST("profile/")
        Observable<BaseResponse> sendProfileToServer(@Body UpdateProfileRequest updateProfileRequest);

        @Headers("Content-Type: application/x-www-form-urlencoded")
        @POST("upload_pic/")
        Observable<UploadPhotoResponse> uploadPhoto(@Query("device_token") String deviceToken, @Body File imageFile,
                                                    @Query("display_order") int displayOrder);

        @POST("profile_photos/delete/")
        Observable<BaseResponse> deletePhoto(@Body DeletePhotoRequest deletePhotoRequest);

        @POST("profile_photos/reorder/")
        Observable<ReorderPhotoResponse> reorderPhotos(@Body ReorderPhotoRequest reorderPhotoRequest);

        @POST("indicate/")
        Observable<BaseResponse> indicateLikeStatus(@Body IndicateSuggestionRequest indicateSuggestionRequest);

        @POST("friend_request/")
        Observable<BaseResponse> sendFriends(@Body FriendRequest indicateSuggestionRequest);
    }

    private ApiService apiService;

    public ProfileRestDataStore() {
        Retrofit retrofit = RequestManager.getAppRetrofit();
        apiService = retrofit.create(ApiService.class);
    }

    public Observable<SyncResponse> sync(String deviceToken) {
        AnalyticsLogger.logEvent(AnalyticsLogger.Event.MODEL.name(), "Sync API calling");
        return apiService.sync(deviceToken);
    }

    public Observable<FullInfoPerson> getUserById(int accountId, String deviceToken) {
        return apiService.getUserProfileById(accountId, deviceToken);
    }

    public Observable<BaseResponse> deletePhoto(String deviceToken, int photoId) {
        DeletePhotoRequest request = new DeletePhotoRequest(deviceToken, photoId);
        return apiService.deletePhoto(request);
    }

    public Observable<UploadPhotoResponse> uploadPhoto(String deviceToken, File imageFile, int displayOrder) {
        return apiService.uploadPhoto(deviceToken, imageFile, displayOrder);
    }

    public Observable<ReorderPhotoResponse> reorderPhoto(String deviceToken, List<PhotoEntity> photoEntityList) {
        ReorderPhotoRequest request = new ReorderPhotoRequest(deviceToken, photoEntityList);
        return apiService.reorderPhotos(request);
    }

    public Observable<BaseResponse> sendProfile(String deviceToken, UpdateProfileRequest profileEntity) {
        profileEntity.setDeviceToken(deviceToken);
        profileEntity.setPolitics(true);
        profileEntity.setFaith(true);
        return apiService.sendProfileToServer(profileEntity);
    }

    public Observable<BaseResponse> indicateLikeStatus(String deviceToken, int matchId, boolean likeStatus) {
        IndicateSuggestionRequest request = new IndicateSuggestionRequest(deviceToken, matchId, likeStatus);
        return apiService.indicateLikeStatus(request);
    }

    public Observable<BaseResponse> sendFacebookFriends(String deviceToken, List<String> friendsFacebookIdList) {
        FriendRequest request = new FriendRequest(deviceToken, friendsFacebookIdList);
        return apiService.sendFriends(request);
    }
}
