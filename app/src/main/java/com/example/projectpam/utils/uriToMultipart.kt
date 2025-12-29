package com.example.projectpam.utils

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun uriToMultipart(
    uri: Uri,
    contentResolver: ContentResolver,
    partName: String
): MultipartBody.Part? {
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val bytes = inputStream.readBytes()
    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
    val fileName = "upload_${System.currentTimeMillis()}.jpg"
    return MultipartBody.Part.createFormData(partName, fileName, requestBody)
}
