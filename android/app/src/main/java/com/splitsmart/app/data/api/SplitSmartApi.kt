package com.splitsmart.app.data.api

import com.splitsmart.app.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface SplitSmartApi {
    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getMe(): Response<AuthResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<AuthResponse>

    @PUT("api/auth/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<AuthResponse>

    @POST("api/search-users")
    suspend fun searchUsers(@Body request: SearchUsersRequest): Response<SearchUsersResponse>

    @POST("api/send-friend-request")
    suspend fun sendFriendRequest(@Body request: FriendRequestBody): Response<FriendRequestResponse>

    @POST("api/accept-friend-request")
    suspend fun acceptFriendRequest(@Body request: AcceptRejectRequest): Response<FriendRequestResponse>

    @POST("api/reject-friend-request")
    suspend fun rejectFriendRequest(@Body request: AcceptRejectRequest): Response<FriendRequestResponse>

    @GET("api/get-friends")
    suspend fun getFriends(): Response<FriendsListResponse>

    @GET("api/notifications")
    suspend fun getNotifications(@Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): Response<NotificationsResponse>

    @GET("api/notifications/count")
    suspend fun getNotificationCount(): Response<NotificationCountResponse>

    @POST("api/notifications/read-all")
    suspend fun markAllNotificationsRead(): Response<MarkReadResponse>

    @POST("api/notifications/read-visible")
    suspend fun markVisibleNotificationsRead(@Body request: ReadVisibleRequest): Response<MarkReadResponse>

    @GET("api/groups")
    suspend fun getGroups(): Response<GroupListResponse>

    @POST("api/groups")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<CreateGroupResponse>

    @GET("api/groups/{groupId}")
    suspend fun getGroup(@Path("groupId") groupId: Int): Response<GroupDetailResponse>

    @GET("api/groups/{groupId}/member-suggestions")
    suspend fun getMemberSuggestions(@Path("groupId") groupId: Int): Response<MemberSuggestionsResponse>

    @POST("api/groups/{groupId}/members")
    suspend fun addGroupMember(@Path("groupId") groupId: Int, @Body request: AddMemberRequest): Response<CreateGroupResponse>

    @POST("api/groups/{token}/join")
    suspend fun joinGroupViaInvite(@Path("token") token: String): Response<JoinGroupResponse>

    @GET("api/groups/{groupId}/expenses")
    suspend fun getExpenses(@Path("groupId") groupId: Int): Response<ExpenseListResponse>

    @POST("api/groups/{groupId}/expenses")
    suspend fun createExpense(@Path("groupId") groupId: Int, @Body request: CreateExpenseRequest): Response<CreateExpenseResponse>

    @DELETE("api/groups/{groupId}/expenses/{expenseId}")
    suspend fun deleteExpense(@Path("groupId") groupId: Int, @Path("expenseId") expenseId: Int): Response<DeleteExpenseResponse>

    @GET("api/groups/{groupId}/balances")
    suspend fun getBalances(@Path("groupId") groupId: Int): Response<BalancesResponse>

    @GET("api/groups/{groupId}/settle")
    suspend fun getSettlements(@Path("groupId") groupId: Int): Response<SettlementResponse>

    @POST("api/groups/{groupId}/settlements/request-cash")
    suspend fun requestCashSettlement(@Path("groupId") groupId: Int, @Body request: CashSettlementRequest): Response<SettlementActionResponse>

    @POST("api/groups/{groupId}/settlements/{settlementId}/approve-cash")
    suspend fun approveCashSettlement(@Path("groupId") groupId: Int, @Path("settlementId") settlementId: Int): Response<SettlementActionResponse>

    @POST("api/groups/{groupId}/settlements/initiate-upi")
    suspend fun initiateUpiSettlement(@Path("groupId") groupId: Int, @Body request: UpiSettlementRequest): Response<SettlementActionResponse>

    @POST("api/groups/{groupId}/settlements/{settlementId}/confirm-upi")
    suspend fun confirmUpiSettlement(@Path("groupId") groupId: Int, @Path("settlementId") settlementId: Int): Response<SettlementActionResponse>

    @GET("api/groups/{groupId}/transactions")
    suspend fun getTransactions(@Path("groupId") groupId: Int): Response<TransactionsResponse>
}
