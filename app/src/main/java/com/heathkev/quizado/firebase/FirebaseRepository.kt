package com.heathkev.quizado.firebase

import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirebaseRepository"

@Singleton
class FirebaseRepository @Inject constructor(){

    private val firebaseFireStore = FirebaseFirestore.getInstance()

    suspend fun registerUser(userId: String, userMap: HashMap<String, Any?>): Void? {
        return firebaseFireStore.collection("Users").document(userId).set(userMap).await()
    }

    suspend fun getQuizListAsync(): QuerySnapshot? {
        return firebaseFireStore.collection("QuizList").get().await()
    }

    suspend fun getQuizListAsync(category: String?): QuerySnapshot? {
        return firebaseFireStore.collection("QuizList").whereEqualTo("category", category).get().await()
    }

    suspend fun getSingleQuiz(): QuerySnapshot? {
        return firebaseFireStore.collection("QuizList").limit(1).get().await()
    }

    suspend fun getQuestion(quizId: String): QuerySnapshot? {
        return firebaseFireStore.collection("QuizList").document(quizId).collection("Questions").get().await()
    }

    suspend fun submitQuizResult(quizId: String, userId: String, result: HashMap<String, Any?>): Void? {
        return firebaseFireStore.collection("QuizList").document(quizId).collection("Results").document(userId).set(result).await()
    }

    suspend fun getResultsByQuizIdAsync(quizId: String, userId: String): DocumentSnapshot? {
        return firebaseFireStore.collection("QuizList").document(quizId).collection("Results").document(userId).get().await()
    }

    suspend fun getAllResultsAsync(): QuerySnapshot? {
        return firebaseFireStore.collectionGroup("Results").get().await()
    }

    suspend fun getResultsByUserIdAsync(userId: String): QuerySnapshot? {
        return firebaseFireStore.collectionGroup("Results").whereEqualTo("player_id", userId)
            .get()
            .await()
    }

    suspend fun sendQuestion(questionMap : HashMap<String, Any?>): Void? {
        return firebaseFireStore.collection("QuestionRequest").document().set(questionMap).await()
    }
}