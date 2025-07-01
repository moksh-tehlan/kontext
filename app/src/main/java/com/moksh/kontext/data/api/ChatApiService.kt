package com.moksh.kontext.data.api

import com.moksh.kontext.data.model.ApiResponse
import com.moksh.kontext.data.model.chat.Chat
import com.moksh.kontext.data.model.chat.ChatMessage
import com.moksh.kontext.data.model.chat.CreateChatRequest
import com.moksh.kontext.data.model.chat.SendMessageRequest
import com.moksh.kontext.data.model.chat.UpdateChatRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChatApiService {

    @POST("api/v1/chats")
    suspend fun createChat(@Body request: CreateChatRequest): Response<ApiResponse<Chat>>

    @POST("api/v1/chats/{chatId}/chat")
    suspend fun sendMessage(
        @Path("chatId") chatId: String,
        @Body request: SendMessageRequest
    ): Response<ApiResponse<String>>

    @GET("api/v1/chats/{chatId}/history")
    suspend fun getChatHistory(@Path("chatId") chatId: String): Response<ApiResponse<List<ChatMessage>>>

    @GET("api/v1/chats/project/{projectId}")
    suspend fun getProjectChats(@Path("projectId") projectId: String): Response<ApiResponse<List<Chat>>>

    @PUT("api/v1/chats/{chatId}")
    suspend fun updateChat(
        @Path("chatId") chatId: String,
        @Body request: UpdateChatRequest
    ): Response<ApiResponse<Chat>>

    @DELETE("api/v1/chats/{chatId}")
    suspend fun deleteChat(@Path("chatId") chatId: String): Response<ApiResponse<Unit>>
}