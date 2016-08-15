package cn.tomoya.android.md.model.api;

import java.util.List;

import cn.tomoya.android.md.model.entity.Author;
import cn.tomoya.android.md.model.entity.Notification;
import cn.tomoya.android.md.model.entity.Page;
import cn.tomoya.android.md.model.entity.Reply;
import cn.tomoya.android.md.model.entity.Result;
import cn.tomoya.android.md.model.entity.TabType;
import cn.tomoya.android.md.model.entity.Topic;
import cn.tomoya.android.md.model.entity.TopicWithReply;
import cn.tomoya.android.md.model.entity.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //=====
    // 话题
    //=====

    @GET("topics")
    Call<Result<Page<Topic>>> getTopicList(
            @Query("tab") TabType tab,
            @Query("p") Integer page
    );

    @GET("topic/{topicId}")
    Call<Result<TopicWithReply>> getTopic(
            @Path("topicId") String topicId,
            @Query("token") String accessToken,
            @Query("mdrender") Boolean mdrender
    );

    @POST("topic/create")
    @FormUrlEncoded
    Call<Result<Topic>> createTopic(
            @Field("token") String accessToken,
            @Field("tab") TabType tab,
            @Field("title") String title,
            @Field("content") String content
    );

    //=========
    // 话题收藏
    //=========

    @POST("topic/collect")
    @FormUrlEncoded
    Call<Result> collectTopic(
            @Field("token") String accessToken,
            @Field("tid") String topicId
    );

    @POST("topic/del_collect")
    @FormUrlEncoded
    Call<Result> decollectTopic(
            @Field("token") String accessToken,
            @Field("tid") String topicId
    );

    @GET("collects/{loginName}")
    Call<Result<List<Topic>>> getCollectTopicList(
            @Path("loginName") String loginName
    );

    //=====
    // 回复
    //=====

    @POST("reply/create")
    @FormUrlEncoded
    Call<Result<Reply>> createReply(
            @Field("tid") String topicId,
            @Field("token") String accessToken,
            @Field("content") String content
    );

    //=====
    // 用户
    //=====

    @GET("user/{nickname}")
    Call<Result<User>> getUser(
            @Path("nickname") String loginName,
            @Query("mdrender") Boolean mdrender
    );

    @POST("accesstoken")
    @FormUrlEncoded
    Call<Result<Author>> accessToken(
            @Field("token") String accessToken
    );

    //=========
    // 消息通知
    //=========

    @GET("notification/count")
    Call<Result<Integer>> getMessageCount(
            @Query("token") String accessToken
    );

    @GET("notifications")
    Call<Result<Page<Notification>>> getMessages(
            @Query("token") String accessToken,
            @Query("mdrender") Boolean mdrender
    );

}
