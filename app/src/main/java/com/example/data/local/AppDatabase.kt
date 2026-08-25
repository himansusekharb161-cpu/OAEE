package com.example.data.local

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {
    @Query("SELECT * FROM test_results ORDER BY timestamp DESC")
    fun getAllTestResults(): Flow<List<TestResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestResult(result: TestResultEntity)

    @Query("DELETE FROM test_results WHERE id = :id")
    suspend fun deleteTestResultById(id: String)

    @Query("DELETE FROM test_results")
    suspend fun clearAll()
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}

@Dao
interface SafetyLogDao {
    @Query("SELECT * FROM safety_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<SafetyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SafetyLogEntity)

    @Query("DELETE FROM safety_logs")
    suspend fun clearLogs()
}

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity)

    @Query("SELECT SUM(durationMinutes) FROM study_sessions")
    fun getTotalStudyMinutes(): Flow<Int?>
}

@Database(
    entities = [TestResultEntity::class, ChatMessageEntity::class, SafetyLogEntity::class, StudySessionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testResultDao(): TestResultDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun safetyLogDao(): SafetyLogDao
    abstract fun studySessionDao(): StudySessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aoee_punyansu_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

