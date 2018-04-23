package com.effectivesoft.repository.profile;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.effectivesoft.R;
import com.effectivesoft.AppApp;
import com.effectivesoft.analytics.AnalyticsLogger;
import com.effectivesoft.converters.ProfileConverter;
import com.effectivesoft.database.DatabaseScheduler;
import com.effectivesoft.domain.PersonModel;
import com.effectivesoft.domain.PhotoModel;
import com.effectivesoft.domain.SuggestionToMeInfoModel;
import com.effectivesoft.repository.game.GameRepository;
import com.effectivesoft.representation.Friend;
import com.effectivesoft.representation.FullInfoPerson;
import com.effectivesoft.representation.Person;
import com.effectivesoft.representation.Photo;
import com.effectivesoft.representation.SuggestionToMe;
import com.effectivesoft.rest.api.ProfileEntity;
import com.effectivesoft.rest.api.SyncResponse;
import com.effectivesoft.rest.api.UpdateProfileRequest;
import com.effectivesoft.tools.facebookapi.FacebookApiManager;
import com.effectivesoft.tools.facebookapi.FacebookUserProfile;
import com.effectivesoft.tools.facebookapi.UploadPhotoScheduler;
import com.effectivesoft.utils.FileUtils;
import com.effectivesoft.utils.PreferencesUtils;
import com.effectivesoft.utils.ProfileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

public class ProfileRepository {

    private ProfileDomainDataStore profileDomainDataStore;
    private ProfileRestDataStore profileRestDataStore;

    private ConnectableObservable<SyncResponse> syncConnectableObservable;
    private volatile boolean isSyncInProgress;

    private static final ProfileRepository INSTANCE = new ProfileRepository();

    private ProfileRepository() {
        profileDomainDataStore = new ProfileDomainDataStore();
        profileRestDataStore = new ProfileRestDataStore();
    }

    public static ProfileRepository getInstance() {
        return INSTANCE;
    }

    public Observable<Void> updateCurrentPerson(Person updatedPerson) {
        return profileRestDataStore
                .sendProfile(PreferencesUtils.getDeviceToken(), ProfileConverter.fillProfileEntityFromPerson(new UpdateProfileRequest(), updatedPerson))
                .doOnCompleted(() -> profileDomainDataStore.savePersonModel(ProfileConverter.personToPersonModel(updatedPerson)))
                .map(response -> null);
    }

    public Observable<Person> updateCurrentPersonInDb(Person updatedPerson) {
        return Observable.fromCallable(() -> {
            profileDomainDataStore.savePersonModel(ProfileConverter.personToPersonModel(updatedPerson));
            return updatedPerson;
        });
    }

    public Observable<Person> getCurrentPerson() {
        return getPerson(PreferencesUtils.getAccountId());
    }

    public Observable<Person> getPerson(int accountId) {
        return profileDomainDataStore
                .getPersonByAccountId(accountId)
                .map(ProfileConverter::personModelToPerson)
                .flatMap(person -> getPersonPhotos(accountId)
                        .doOnNext(person::setPhotos)
                        .map(photos -> person));
    }

    public Observable<List<Person>> getCurrentPersonAllFriends() {
        return profileDomainDataStore
                .getPersonAllFriends(PreferencesUtils.getAccountId(), false)
                .map(ProfileConverter::personModelsToPersons)
                .doOnNext(Collections::sort);
    }

    //TODO delete when implement showing user common friends from server
    public Observable<List<Person>> getPersonFriendsInCommon(int personAccountId) {
        return profileDomainDataStore
                .getPersonFriendsInCommon(PreferencesUtils.getAccountId(), personAccountId)
                .map(ProfileConverter::personModelsToPersons);
    }

    public Observable<List<Integer>> getCurrentPersonFriendsIds() {
        return profileDomainDataStore.getPersonFriendsIds(PreferencesUtils.getAccountId());
    }

    public Observable<List<Person>> getCurrentPersonNotBlockedFriendsByName(String name) {
        return profileDomainDataStore
                .getPersonNotBlockedFriendsByName(PreferencesUtils.getAccountId(), name)
                .map(ProfileConverter::personModelsToPersons);
    }

    public Observable<List<Photo>> getCurrentPersonPhotos() {
        return getPersonPhotos(PreferencesUtils.getAccountId());
    }

    public Observable<List<Photo>> getPersonPhotos(int personAccountId) {
        return profileDomainDataStore
                .getPersonPhotos(personAccountId)
                .map(ProfileConverter::photoModelsToPhotos);
    }

    public Observable<SuggestionToMe> getFirstSuggestionToMe() {
        return profileDomainDataStore.getFirstSuggestionToMeInfoModel()
                .flatMap(this::getSuggestionToMe);
    }

    public Observable<SuggestionToMe> getSuggestionToMe(int matchId) {
        return profileDomainDataStore.getSuggestionToMeInfoModel(matchId)
                .flatMap(this::getSuggestionToMe);
    }

    private Observable<SuggestionToMe> getSuggestionToMe(SuggestionToMeInfoModel suggestionToMeInfoModel) {
        Observable<SuggestionToMeInfoModel> suggestionToMeInfoModelObservable = Observable.just(suggestionToMeInfoModel);
        Observable<PersonModel> suggestedPersonModelObservable = profileDomainDataStore
                .getPersonByAccountId(suggestionToMeInfoModel.getDaterId());
        Observable<List<PhotoModel>> suggestedPersonPhotoModelsObservable = profileDomainDataStore
                .getPersonPhotos(suggestionToMeInfoModel.getDaterId());
        return Observable.zip(suggestionToMeInfoModelObservable, suggestedPersonModelObservable,
                suggestedPersonPhotoModelsObservable,
                ProfileConverter::suggestionToMeInfoModelToSuggestionToMe);
    }

    public Observable<Integer> getSuggestionsToMeCount() {
        return profileDomainDataStore.getSuggestionToMeInfoModelCount();
    }

    public Observable<List<SuggestionToMe>> getMatchesList() {
        //TODO: get all suggestion where both user approved (or divide into my matches and matches for me)
        return Observable.just(new ArrayList<>());
    }

    public Observable<Photo> getPersonMainPhoto(int personAccountId) {
        return profileDomainDataStore
                .getPersonMainPhoto(personAccountId)
                .map(ProfileConverter::photoModelToPhoto)
                .onErrorReturn(throwable -> null);
    }

    public Observable<Photo> getCurrentPersonMainPhoto() {
        return getPersonMainPhoto(PreferencesUtils.getAccountId());
    }

    public Observable<List<Photo>> uploadPhotoAsMainPhoto(File imageFile) {
        return uploadPhoto(imageFile, 0);
    }

    private Observable<List<Photo>> uploadPhoto(File imageFile, int displayOrder) {
        AnalyticsLogger.logEvent(AnalyticsLogger.Event.MODEL.name(), "Uploading photo");
        return profileRestDataStore
                .uploadPhoto(PreferencesUtils.getDeviceToken(), imageFile, displayOrder)
                .flatMap(response -> {
                    AnalyticsLogger.logEvent(AnalyticsLogger.Event.MODEL.name(),
                            "Uploading photo response", "success", String.valueOf(response.isSuccess()));
                    if (response.isSuccess()) {
                        profileDomainDataStore.savePhotoModel(ProfileConverter.uploadPhotoResponseToPhotoModel(response, PreferencesUtils.getAccountId()));
                        return profileDomainDataStore.getPersonPhotos(PreferencesUtils.getAccountId())
                                .map(ProfileConverter::photoModelsToPhotos);
                    } else {
                        return null;
                    }
                });
    }

    public Observable<Void> uploadFacebookPhotosIfUserHasNoPhoto() {
        return getCurrentPersonMainPhoto()
                .flatMap(photo -> photo == null ? uploadFacebookPhotosToServer() : Observable.just(null));
    }

    private Observable<Void> uploadFacebookPhotosToServer() {
        return FacebookApiManager.getInstance()
                .getUserPhotoUrls()
                .flatMap(photosForUploading -> uploadAllPhotosOneByOne(photosForUploading, 0));
    }

    /**
     * Recursive function to upload photos one by one to the server.
     * Take first photo from {@code photosForUploading} list and upload it to the server with {@code displayOrder}
     * <p>
     * There is no more clean solution for this task because is no way to tell
     * retrofit to use only one thread for those calls. Source: http://stackoverflow.com/a/32317522/6709091
     *
     * @param photosForUploading list of photos that need to be uploaded. Degrease by one on each call of this method.
     * @param displayOrder       display order that will be used for uploaded photo.
     * @return Observable with next calling of this method or Observable.just(null) when all photos was uploaded
     */
    private Observable<Void> uploadAllPhotosOneByOne(List<String> photosForUploading, int displayOrder) {
        if (photosForUploading.isEmpty()) {
            return Observable.just(null);
        }
        return Observable.just(photosForUploading.get(0))
                .map(FileUtils::urlToFile)
                .flatMap(imageFile -> uploadPhoto(imageFile, displayOrder))
                .subscribeOn(UploadPhotoScheduler.INSTANCE.getScheduler())
                .flatMap(uploadedPhotos -> {
                    photosForUploading.remove(0);
                    return uploadAllPhotosOneByOne(photosForUploading, uploadedPhotos.size());
                });
    }

    public Observable<Void> deletePhoto(int photoId) {
        return profileRestDataStore
                .deletePhoto(PreferencesUtils.getDeviceToken(), photoId)
                .flatMap(baseResponse -> profileDomainDataStore.deletePhoto(photoId));
    }

    public Observable<List<Friend>> getFriendList() {
        return profileDomainDataStore.getFriendList()
                .map(ProfileConverter::friendModelsToFriends);
    }

    public Observable<Friend> getFriend(String facebookId) {
        return profileDomainDataStore.getFriend(facebookId)
                .map(ProfileConverter::friendModelToFriend);
    }

    public Observable<Void> reorderPhotos(List<Photo> photos) {
        return profileRestDataStore
                .reorderPhoto(PreferencesUtils.getDeviceToken(), ProfileConverter.photosToPhotoEntities(photos))
                .doOnNext(reorderPhotoResponse -> profileDomainDataStore.deletePersonPhotoModels(
                        PreferencesUtils.getAccountId()))
                .doOnNext(reorderPhotoResponse -> profileDomainDataStore.savePhotoModels(
                        ProfileConverter.photoEntitiesToPhotoModels(reorderPhotoResponse.getPhotoEntities(), PreferencesUtils.getAccountId())))
                .map(response -> null);
    }

    public Observable<Boolean> indicateLikeState(int matchId, boolean likeStatus) {
        return profileRestDataStore
                .indicateLikeStatus(PreferencesUtils.getDeviceToken(), matchId, likeStatus)
                .flatMap(baseResponse -> {
                    if (baseResponse.isSuccess()) {
                        return profileDomainDataStore
                                .indicateSuggestionToMeLikeState(matchId, likeStatus)
                                .map(suggestionToMeInfoModel -> Boolean.TRUE);
                    } else {
                        return Observable.just(Boolean.FALSE);
                    }
                });
    }

    public Observable<List<Integer>> getMySuggestionsToPersonAccountId(int personAccountId) {
        return profileDomainDataStore.getMySuggestionsToPersonAccountId(personAccountId);
    }

    public Observable<Void> syncUserData() {
        return FacebookApiManager.getInstance().getAppFacebookFriends()
                .flatMap(this::getNewFacebookFriendsIds)
                .subscribeOn(Schedulers.newThread())
                .flatMap(newFacebookFriendsIds -> newFacebookFriendsIds.isEmpty()
                        ? Observable.just(null)
                        : profileRestDataStore.sendFacebookFriends(PreferencesUtils.getDeviceToken(), newFacebookFriendsIds))
                .flatMap(response -> sync())
                .flatMap(syncResponse -> GameRepository.getInstance().updateDailyFiveUsers());
    }

    private Observable<List<String>> getNewFacebookFriendsIds(List<FacebookUserProfile> facebookFriends) {
        return getCurrentPersonAllFriends()
                .map(serverFriends -> ProfileUtils.extractNewFacebookFriendsIds(facebookFriends, serverFriends));
    }

    public Observable<Void> setPersonBlocked(int accountId, boolean blocked) {
        return getPerson(accountId)
                .doOnNext(person -> person.setBlocked(blocked))
                .map(ProfileConverter::personToPersonModel)
                .doOnNext(personModel -> profileDomainDataStore.savePersonModel(personModel))
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler())
                .map(personModel -> null);
    }

    public Observable<Integer> getCupidCash() {
        return profileDomainDataStore
                .getPersonByAccountId(PreferencesUtils.getAccountId())
                .map(PersonModel::getCupidCash);
    }

    public Observable<Integer> updateCupidCash(int cash) {
        return profileDomainDataStore
                .getPersonByAccountId(PreferencesUtils.getAccountId())
                .doOnNext(personModel -> personModel.setCupidCash(cash))
                .doOnNext(BaseModel::update)
                .map(personModel -> cash);
    }

    public Observable<Integer> getRedeemableCupidCash() {
        return profileDomainDataStore
                .getPersonByAccountId(PreferencesUtils.getAccountId())
                .map(PersonModel::getRedeemableCupidCash);
    }

    public Observable<FullInfoPerson> getUserById(int accountId) {
        return profileRestDataStore.getUserById(accountId, PreferencesUtils.getDeviceToken());
    }

    private Observable<SyncResponse> sync() {
        if (!isSyncInProgress) {
            isSyncInProgress = true;
            syncConnectableObservable = profileRestDataStore
                    .sync(PreferencesUtils.getDeviceToken())
                    .doOnNext(response -> {
                        AnalyticsLogger.logEvent(AnalyticsLogger.Event.MODEL.name(), "Sync API response");
                        profileDomainDataStore.updateSyncResponseModels(
                                ProfileConverter.syncResponseEntityToModels(response));
                        ProfileEntity profile = response.getProfileEntity();
                        PreferencesUtils.saveAccountInfo(
                                profile.getAccountId(),
                                profile.getName(),
                                response.getCupidCashWithdrawMin(),
                                response.getCupidCashPerMatchValue(),
                                response.getCupidCashDirectSuggest(),
                                response.getCupidCashExtraDailyPick());
                        if (response.getMessages() != null) {
                            PreferencesUtils.saveMessages(response.getMessages().getInviteFriendsReward());
                        }
                    })
                    .doOnTerminate(() -> isSyncInProgress = false)
                    .publish();
            syncConnectableObservable.connect();
        }
        return syncConnectableObservable;
    }

    public Observable<Person> getCurrentPersonInMainThread() {
        return profileDomainDataStore
                .getPersonInMainThread(PreferencesUtils.getAccountId())
                .map(ProfileConverter::personModelToPerson);
    }
}
