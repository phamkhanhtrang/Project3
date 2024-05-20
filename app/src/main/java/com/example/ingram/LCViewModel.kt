package com.example.ingram

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.ingram.data.CHATS
import com.example.ingram.data.COMMENT
import com.example.ingram.data.ChatData
import com.example.ingram.data.ChatUser
import com.example.ingram.data.Comment
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
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.ingram.data.STORY
import com.example.ingram.data.StoryData
import java.io.File


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

    val inProcessPost = mutableStateOf(false)
    val Post = mutableStateOf<List<PostData>>(listOf())
    val inProcessComment  = mutableStateOf(false)
    var currentCommentListen : ListenerRegistration?=null
    val comment  = mutableStateOf<List<Comment>>(listOf())

    var inProcessChats = mutableStateOf(false)
    val chats = mutableStateOf<List<ChatData>>(listOf())

    val chatMessage = mutableStateOf<List<Message>>(listOf())
    val inProcessChatMessage = mutableStateOf(false)
    var currentChatMessageListen: ListenerRegistration? = null

    val storys= mutableStateOf<List<StoryData>>(listOf())
    val inProgressStory = mutableStateOf(false)

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

    fun onSendComment(postID:String, comment: String ){
        val time = Calendar.getInstance().time.toString()
        val msg = Comment(userData.value?.name,userData.value?.imageUrl, comment, time)
        db.collection(POST).document(postID).collection(COMMENT).document().set(msg)
    }

    fun onSendReply(chatID: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatID).collection(MESSAGE).document().set(msg)

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
    fun logout(){
        auth.signOut()
        signIn.value=false
        userData.value=null
        eventMutableState.value=Event("Logged Out")
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
                    "Đăng ảnh thành cônng",
                    Toast.LENGTH_SHORT
                ).show()
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
                val user = value.toObject<UseData>()
                userData.value = user
                inProcess.value = false
                populateChats()
                populatePost()
                populateStory()
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
    fun populatePost(){
        inProcessPost.value=true
        db.collection(POST).addSnapshotListener{
                value, error ->
            if(error!=null){
                handleException(error)
            }
            if (value!=null){
                Post.value= value.documents.mapNotNull {
                    it.toObject<PostData>()
                }
                inProcessChats.value= false
            }
        }
    }

    fun createPost(
        imageUrl: List<String>? = null,
        content: String? = null,
    ) {
        val id = db.collection(POST).document().id
        val newPost = PostData(
            postID = id,
            user = ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number
            ),
            imageUrl = imageUrl,
            content = content
        )
        db.collection(POST).document(id).set(newPost)
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error adding document", exception)
            }

        db.collection(POST).addSnapshotListener { snapshots, exception ->
            if (exception != null) {
                Log.e(TAG, "Listen failed: ", exception)
                return@addSnapshotListener
            }
            val postList = snapshots?.map { it.toObject(PostData::class.java) } ?: emptyList()
            Post.value = postList
        }
    }


    fun populateComment(postId: String){
        inProcessComment.value=true
        currentCommentListen = db.collection(POST).document(postId).collection(COMMENT).addSnapshotListener{ value, error->
            if(error!=null){
                handleException(error)
            }
            if (value!=null){
                comment.value=value.documents.mapNotNull {
                    it.toObject<Comment>()
                }.sortedBy {
                    it.timestamp
                }
                inProcessComment.value=false
            }
        }
    }

    fun uploadStory(uri: Uri,context: Context) {
        upLoadImage(uri, context = context, type = "image"){
        createStory(it.toString())
        }
    }

    fun createStory(imageUrl: String) {
        val userId = userData.value?.userId
        if (userId != null) {
            val user = ChatUser(
                userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.number,
            )
            val newStory = StoryData(
                user,
                imageUrl,
                System.currentTimeMillis()
            )
            val storyRef = db.collection(STORY).document()
            db.runTransaction { transaction ->
                transaction.set(storyRef, newStory)
            }.addOnSuccessListener {
                Log.d(TAG, "Story created successfully")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error creating story", e)
            }
        } else {
            Log.e(TAG, "User data is null")
        }
    }

    fun populateStory(){
        val timeDelta = 24L *60 *60 *1000
        val cutOff = System.currentTimeMillis()- timeDelta
        inProgressStory.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId),
            )
        ).addSnapshotListener{
                value, error->
            if(error!=null)
                handleException(error)
            if (value!=null){
                val currentConnections = arrayListOf(userData.value?.userId)
                val chats = value.toObjects<ChatData>()
                chats.forEach{chat ->
                    if(chat.user1.userId == userData.value?.userId){
                        currentConnections.add(chat.user2.userId)
                    }else
                        currentConnections.add(chat.user1.userId)
                }
                db.collection(STORY).whereGreaterThan("timestamp", cutOff).whereIn("user.userId",currentConnections)
                    .addSnapshotListener{  value, error ->
                    if(error!=null){
                        handleException(error)
                    }
                    if (value!=null){
                        storys.value= value.toObjects ()
                        inProgressStory.value= false
                    }
                }
            }
        }
    }
    fun getUriFromUrl(context: Context, url: String): Uri? {
        return try {
            val file = Glide.with(context)
                .asFile()
                .load(url)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun shareOrder(context: Context,subject: String? = null, imageUrls: List<String>? = null, text: String? = null) {
        val uris = imageUrls?.mapNotNull { getUriFromUrl(context, it) }
          val intent = Intent(Intent.ACTION_SEND).apply {
              type = "image/*"
              putExtra(Intent.EXTRA_SUBJECT, subject)
              putExtra(Intent.EXTRA_TEXT, text)
              putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))

          }
          context.startActivity(
              Intent.createChooser(
                  intent,
                  context.getString(R.string.instagram)
              )
          )
    }
}