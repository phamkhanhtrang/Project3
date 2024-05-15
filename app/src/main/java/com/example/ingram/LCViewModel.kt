package com.example.ingram

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.ingram.data.CHATS
import com.example.ingram.data.ChatData
import com.example.ingram.data.ChatUser
import com.example.ingram.data.Event
import com.example.ingram.data.MESSAGE
import com.example.ingram.data.Message
import com.example.ingram.data.POST
import com.example.ingram.data.PostData
import com.example.ingram.data.USER_NODE
import com.example.ingram.data.UseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {
    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UseData?>(null)

    val Post = mutableStateOf<List<PostData>>(listOf())

    var inProcessChats = mutableStateOf(false)
    val chats = mutableStateOf<List<ChatData>>(listOf())

    val chatMessage = mutableStateOf<List<Message>>(listOf())
    val inProcessChatMessage = mutableStateOf(false)
    var currentChatMessageListen: ListenerRegistration? = null

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }

    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("App ", "Live chat execption:", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNotEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = " Please Fill")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number,)
                        Log.d("TAG", "signup: User Logged In")
                    } else {
                        handleException(it.exception, customMessage = "sign up failed")
                    }
                }
            } else {
                handleException(customMessage = "number already exit")
                inProcess.value = false
            }
        }
    }

    fun loginIn(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill")
            return

        } else {
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(exception = it.exception, customMessage = " login failed")
                    }
                }
        }
    }


    fun upLoadImage(uri: Uri, context: Context, type: String, onSuccess: (String) -> Unit) {

        val storageRef = storage.reference
        val unique_image_name = UUID.randomUUID().toString()
        val spaceRef = storageRef.child("images/$unique_image_name.jpg")
        inProcess.value=true

        val byteArray: ByteArray? = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }
        //ham them du lieu
        byteArray?.let {
            val uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Upload failed",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener { _ ->
                Toast.makeText(
                    context,
                    "Upload succeeded",
                    Toast.LENGTH_SHORT
                ).show()
                inProcess.value=true
                spaceRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Gọi hàm gọi lại onSuccess với đường dẫn của ảnh
                    onSuccess(imageUrl.toString())
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Failed to get download URL",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        note: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val useData = UseData(
                userId = uid,
                name = name ?: userData.value?.name,
                number = number ?: userData.value?.number,
                note = note ?: userData.value?.note,
                imageUrl = imageUrl ?: userData.value?.imageUrl
            )

            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Update user data
                    db.collection(USER_NODE).document(uid).set(useData, SetOptions.merge())
                        .addOnSuccessListener {
                            inProcess.value = false
                            // You can add success handling here
                        }
                        .addOnFailureListener { exception ->
                            inProcess.value = false
                            handleException(exception, "Cannot update user")
                        }
                } else {
                    // Create new user data
                    db.collection(USER_NODE).document(uid).set(useData)
                        .addOnSuccessListener {
                            inProcess.value = false
                            getUserData(uid)
                        }
                        .addOnFailureListener { exception ->
                            inProcess.value = false
                            handleException(exception, "Cannot create user")
                        }
                }
            }.addOnFailureListener { exception ->
                inProcess.value = false
                handleException(exception, "Cannot retrieve user")
            }
        }
    }


    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, " Con not retreive User")
            }
            if (value != null) {
                var user = value.toObject<UseData>()
                userData.value = user
                inProcess.value = false
                populateChats()
//                populateStatuses()
            }
        }
    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number must be cintain digitas")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.equalTo("user2.number", number)
                    ),
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(customMessage = "number not found")
                            } else {
                                val chatPartner = it.toObjects<UseData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatID = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name, chatPartner.imageUrl, chatPartner.number
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {

                            handleException(it)

                        }
                } else {
                    handleException(customMessage = "chat already exists")
                }
            }
        }
    }

    fun onSendReply(chatID: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)

    }

    fun populateMessages(chatId: String) {
        inProcessChatMessage.value = true
        currentChatMessageListen = db.collection(CHATS).document(chatId).collection(MESSAGE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)

                }
                if (value != null) {
                    chatMessage.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy {
                        it.timestamp
                    }
                    inProcessChatMessage.value = false
                }
            }
    }

    fun depopulateMessage() {
        chatMessage.value = listOf()
        currentChatMessageListen = null

    }
    fun populateChats(){
        inProcessChats.value=true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener{
                value, error ->
            if(error!=null){
                handleException(error)
            }
            if (value!=null){
                chats.value= value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats.value= false
            }
        }
    }
    fun createPost(
        imageUrl: List<String>? = null,
        content: String? = null,
        context: Context,

    ) {
        val id = db.collection(CHATS).document().id
        val newPost = PostData(
            postID = id,
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number
            ),
            imageUrl = imageUrl,
            content = content
        )
        db.collection(POST).document().set(newPost)
            .addOnSuccessListener {
                // Khi thêm bài viết thành công, bạn cần truy vấn dữ liệu mới từ Firestore và gán cho Post.value
                db.collection(POST).get()
                    .addOnSuccessListener { documents ->
                        val postList = mutableListOf<PostData>()
                        for (document in documents) {
                            val postData = document.toObject(PostData::class.java)
                            postList.add(postData)
                        }
                        // Gán giá trị mới cho Post.value
                        Post.value = documents.toObjects()
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }

                Toast.makeText(
                    context, "Đăng bài viết thành công ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context, "Thất bại  ",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}


