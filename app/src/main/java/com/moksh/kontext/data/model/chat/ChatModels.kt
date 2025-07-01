package com.moksh.kontext.data.model.chat

import com.google.gson.annotations.SerializedName

// Request models
data class CreateChatRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("projectId")
    val projectId: String
)

data class SendMessageRequest(
    @SerializedName("query")
    val query: String
)

data class UpdateChatRequest(
    @SerializedName("name")
    val name: String
)

// Response models
data class Chat(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("updatedBy")
    val updatedBy: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("projectId")
    val projectId: String
)

data class ChatMessage(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("updatedBy")
    val updatedBy: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("chatId")
    val chatId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("type")
    val type: String
)