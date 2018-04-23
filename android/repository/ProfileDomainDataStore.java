package com.effectivesoft.repository.profile;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.effectivesoft.database.DatabaseScheduler;
import com.effectivesoft.domain.FriendModel;
import com.effectivesoft.domain.FriendModel_Table;
import com.effectivesoft.domain.MySuggestionModel;
import com.effectivesoft.domain.MySuggestionModel_Table;
import com.effectivesoft.domain.PersonFriendshipModel;
import com.effectivesoft.domain.PersonFriendshipModel_Table;
import com.effectivesoft.domain.PersonModel;
import com.effectivesoft.domain.PersonModel_Table;
import com.effectivesoft.domain.PhotoModel;
import com.effectivesoft.domain.PhotoModel_Table;
import com.effectivesoft.domain.SuggestionToMeInfoModel;
import com.effectivesoft.domain.SuggestionToMeInfoModel_Table;
import com.effectivesoft.domain.SuggestionToMeModel;
import com.effectivesoft.domain.SyncResponseModelsHolder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.effectivesoft.database.AppDatabase.transaction;

public class ProfileDomainDataStore {

    public void updateSyncResponseModels(SyncResponseModelsHolder modelsHolder) {
        transaction(() -> {
            deletePersonModels();
            deleteFriendModels();
            deletePersonFriendshipModels();
            deletePhotoModels();
            deleteSuggestionToMeInfoModels();
            deleteSuggestionToMeModels();
            deleteMySuggestionModels();

            savePersonModels(modelsHolder.getPersonModels());
            saveFriendModels(modelsHolder.getFriendModels());
            savePersonFriendshipModels(modelsHolder.getPersonFriendshipModels());
            savePhotoModels(modelsHolder.getPhotoModels());
            saveSuggestionToMeInfoModels(modelsHolder.getSuggestionToMeInfoModels());
            saveSuggestionToMeModels(modelsHolder.getSuggestionToMeModels());
            saveMySuggestionModels(modelsHolder.getMySuggestionModels());
        });
    }

    public void savePersonModel(PersonModel personModel) {
        transaction(personModel::save);
    }

    private void savePersonModels(List<PersonModel> personModels) {
        transaction(() -> {
            for (PersonModel person : personModels) {
                person.save();
            }
        });
    }

    private void saveFriendModels(List<FriendModel> friendModels) {
        transaction(() -> {
            for (FriendModel friendModel : friendModels) {
                friendModel.save();
            }
        });
    }

    private void savePersonFriendshipModels(List<PersonFriendshipModel> friendshipModels) {
        transaction(() -> {
            for (PersonFriendshipModel friendshipModel : friendshipModels) {
                friendshipModel.save();
            }
        });
    }

    public void savePhotoModels(List<PhotoModel> photoModels) {
        transaction(() -> {
            for (PhotoModel photoModel : photoModels) {
                photoModel.save();
            }
        });
    }

    public void savePhotoModel(PhotoModel photoModel) {
        transaction(photoModel::save);
    }

    private void saveSuggestionToMeInfoModels(List<SuggestionToMeInfoModel> suggestionToMeInfoModels) {
        transaction(() -> {
            for (SuggestionToMeInfoModel suggestionToMeInfoModel : suggestionToMeInfoModels) {
                suggestionToMeInfoModel.save();
            }
        });
    }


    private void saveMySuggestionModels(List<MySuggestionModel> mySuggestionModels) {
        transaction(() -> {
            for (MySuggestionModel mySuggestionModel : mySuggestionModels) {
                mySuggestionModel.save();
            }
        });
    }

    private void saveSuggestionToMeModels(List<SuggestionToMeModel> suggestionToMeModels) {
        transaction(() -> {
            for (SuggestionToMeModel suggestionToMeModel : suggestionToMeModels) {
                suggestionToMeModel.save();
            }
        });
    }

    public Observable<PersonModel> getPersonByAccountId(int accountId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(PersonModel.class)
                        .where(PersonModel_Table.accountId.eq(accountId)).querySingle())
                .filter(personModel -> personModel != null)
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<PersonModel>> getPersonNotBlockedFriendsByName(int personAccountId, String name) {
        return getPersonFriendshipModels(personAccountId)
                .concatMap(Observable::from)
                .map(friendshipModel -> SQLite.select().from(PersonModel.class)
                        .where(PersonModel_Table.accountId.eq(friendshipModel.getSecondAccountId()))
                        .and(PersonModel_Table.name.like("%" + name + "%"))
                        .and(PersonModel_Table.blocked.eq(Boolean.FALSE)).querySingle()
                )
                .filter(personModel -> personModel != null)
                .toList()
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<Integer>> getPersonFriendsIds(int personAccountId) {
        return getPersonFriendshipModels(personAccountId)
                .concatMap(Observable::from)
                .map(PersonFriendshipModel::getSecondAccountId)
                .toList();
    }

    private Observable<List<PersonFriendshipModel>> getPersonFriendshipModels(int personAccountId) {
        return Observable.fromCallable(() ->
                SQLite.select().from(PersonFriendshipModel.class)
                        .where(PersonFriendshipModel_Table.firstAccountId.eq(personAccountId))
                        .queryList())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<PersonModel>> getPersonAllFriends(int personAccountId, boolean includeBlocked) {
        return getPersonFriendshipModels(personAccountId)
                .concatMap(Observable::from)
                .map(friendshipModel -> {
                    SQLCondition personModelIsFriend = PersonModel_Table.accountId.eq(friendshipModel.getSecondAccountId());
                    SQLCondition personModelIsNotBlocked = PersonModel_Table.blocked.eq(Boolean.FALSE);
                    return SQLite.select().from(PersonModel.class)
                            .where(includeBlocked ? personModelIsFriend
                                    : ConditionGroup.clause().andAll(personModelIsFriend, personModelIsNotBlocked))
                            .querySingle();
                })
                .filter(personModel -> personModel != null)
                .toList()
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<PersonModel>> getPersonFriendsInCommon(int currentPersonAccountId, int anotherPersonAccountId) {
        return getPersonFriendshipModels(anotherPersonAccountId)
                .concatMap(Observable::from)
                .filter(friendshipModel -> SQLite.select().from(PersonFriendshipModel.class)
                        .where(PersonFriendshipModel_Table.secondAccountId.eq(friendshipModel.getSecondAccountId()))
                        .and(PersonFriendshipModel_Table.firstAccountId.eq(currentPersonAccountId)).querySingle() != null)
                .map(friendshipModel -> SQLite.select().from(PersonModel.class)
                        .where(PersonModel_Table.accountId.eq(friendshipModel.getSecondAccountId())).querySingle())
                .toList()
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    /**
     * Return list of accountIds for that persons whom current user recommend for person with accountId
     */
    public Observable<List<Integer>> getMySuggestionsToPersonAccountId(int personAccountId) {
        return getMySuggestionsAssociatedWithPersonAccountId(personAccountId)
                .map(mySuggestionModels -> {
                    List<Integer> personAccountIds = new ArrayList<>(mySuggestionModels.size());
                    for (MySuggestionModel mySuggestionModel : mySuggestionModels) {
                        int firstAccountId = mySuggestionModel.getDaterOneAccountId();
                        int secondAccoutdId = mySuggestionModel.getDaterTwoAccountId();
                        personAccountIds.add(firstAccountId != personAccountId ? firstAccountId : secondAccoutdId);
                    }
                    return personAccountIds;
                })
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    private Observable<List<MySuggestionModel>> getMySuggestionsAssociatedWithPersonAccountId(int personAccountId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(MySuggestionModel.class)
                        .where(MySuggestionModel_Table.daterOneAccountId.eq(personAccountId))
                        .or(MySuggestionModel_Table.daterTwoAccountId.eq(personAccountId))
                        .queryList())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<FriendModel>> getFriendList() {
        return Observable
                .fromCallable(() -> SQLite.select().from(FriendModel.class).queryList())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<FriendModel> getFriend(String facebookId) {
        return Observable
                .fromCallable(() ->
                        SQLite.select()
                                .from(FriendModel.class)
                                .where(FriendModel_Table.facebookId.eq(facebookId))
                                .querySingle())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<PhotoModel>> getPersonPhotos(int accountId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(PhotoModel.class)
                        .where(PhotoModel_Table.personId.eq(accountId)).queryList())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<SuggestionToMeInfoModel> getFirstSuggestionToMeInfoModel() {
        return Observable
                .fromCallable(() -> SQLite.select().from(SuggestionToMeInfoModel.class).queryList())
                .concatMap(Observable::from)
                .filter(suggestionToMeInfoModel -> suggestionToMeInfoModel.getMyApproval() == null)
                .first()
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<SuggestionToMeInfoModel> getSuggestionToMeInfoModel(int matchId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(SuggestionToMeInfoModel.class)
                        .where(SuggestionToMeInfoModel_Table.matchId.is(matchId)).querySingle())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<Integer> getSuggestionToMeInfoModelCount() {
        return Observable
                .fromCallable(() -> SQLite.select().from(SuggestionToMeInfoModel.class).queryList())
                .concatMap(Observable::from)
                .filter(suggestionToMeInfoModel -> suggestionToMeInfoModel.getMyApproval() == null)
                .toList()
                .map(List::size)
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<SuggestionToMeInfoModel> indicateSuggestionToMeLikeState(int matchId, boolean isLiked) {
        return Observable
                .fromCallable(() -> SQLite.select().from(SuggestionToMeInfoModel.class)
                        .where(SuggestionToMeInfoModel_Table.matchId.is(matchId)).querySingle())
                .doOnNext(suggestionToMeInfoModel -> suggestionToMeInfoModel.setMyApproval(isLiked))
                .doOnNext(BaseModel::save)
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<List<SuggestionToMeInfoModel>> getConfirmedSuggestionToMeInfoModels() {
        return Observable
                .fromCallable(() -> SQLite.select().from(SuggestionToMeInfoModel.class).queryList())
                .concatMap(Observable::from)
                .filter(suggestionToMeInfoModel -> Boolean.TRUE.equals(suggestionToMeInfoModel.getDaterApproval())
                        && Boolean.TRUE.equals(suggestionToMeInfoModel.getMyApproval()))
                .toList()
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<PhotoModel> getPersonMainPhoto(int accountId) {
        return getPersonMainPhotoFromCurrentThread(accountId)
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler());
    }

    public Observable<PhotoModel> getPersonMainPhotoFromCurrentThread(int accountId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(PhotoModel.class)
                        .where(PhotoModel_Table.personId.eq(accountId))
                        .orderBy(PhotoModel_Table.displayOrder, true)
                        .querySingle());
    }

    public Observable<Void> deletePhoto(int photoId) {
        return Observable.fromCallable(() -> SQLite.delete().from(PhotoModel.class)
                .where(PhotoModel_Table.id.eq(photoId)).query())
                .subscribeOn(DatabaseScheduler.INSTANCE.getScheduler())
                .map(cursor -> null);
    }

    private void deletePersonModels() {
        SQLite.delete(PersonModel.class).query();
    }

    private void deleteFriendModels() {
        SQLite.delete(FriendModel.class).query();
    }

    private void deletePersonFriendshipModels() {
        SQLite.delete(PersonFriendshipModel.class).query();
    }

    private void deletePhotoModels() {
        SQLite.delete(PhotoModel.class).query();
    }

    public void deletePersonPhotoModels(int personAccountId) {
        SQLite.delete(PhotoModel.class).where(PhotoModel_Table.personId.eq(personAccountId)).query();
    }

    private void deleteMySuggestionModels() {
        SQLite.delete(MySuggestionModel.class).query();
    }

    private void deleteSuggestionToMeInfoModels() {
        SQLite.delete(SuggestionToMeInfoModel.class).query();
    }

    private void deleteSuggestionToMeModels() {
        SQLite.delete(SuggestionToMeModel.class).query();
    }

    public Observable<PersonModel> getPersonInMainThread(int accountId) {
        return Observable
                .fromCallable(() -> SQLite.select().from(PersonModel.class)
                        .where(PersonModel_Table.accountId.eq(accountId)).querySingle())
                .filter(personModel -> personModel != null)
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
