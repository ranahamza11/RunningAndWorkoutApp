package com.drovisfrovis.runningandworkout.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.drovisfrovis.runningandworkout.db.RunningDatabase
import com.drovisfrovis.runningandworkout.db.WorkoutDatabase
import com.drovisfrovis.runningandworkout.misc.Constants
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_COUNTDOWN_TIME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_FIRST_TIME_TOGGLE
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_NAME
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_TRAINING_REST
import com.drovisfrovis.runningandworkout.misc.Constants.KEY_WEIGHT
import com.drovisfrovis.runningandworkout.misc.Constants.RUNNING_DATABASE_NAME
import com.drovisfrovis.runningandworkout.misc.Constants.SHARED_PREFERENCES_NAME
import com.drovisfrovis.runningandworkout.misc.Constants.WORKOUT_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("run")
    fun provideRunningDatabase(
        @ApplicationContext appContext: Context
    ): RunningDatabase = Room.databaseBuilder(appContext,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME).build()

    @Singleton
    @Provides
    @Named("runDao")
    fun provideRunDao( @Named("run") db: RunningDatabase) = db.getRunDao()

    @Singleton
    @Provides
    @Named("workout")
    fun provideWorkoutDatabase(
        @ApplicationContext appContext: Context
    ): WorkoutDatabase = Room.databaseBuilder(appContext,
        WorkoutDatabase::class.java,
        Constants.WORKOUT_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    @Named("workoutDao")
    fun provideWorkoutDao(@Named("workout") db: WorkoutDatabase) = db.getWorkoutDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName (sharedPreferences: SharedPreferences) = sharedPreferences.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight (sharedPreferences: SharedPreferences) = sharedPreferences.getFloat(KEY_WEIGHT, 80f)

    @Provides
    fun provideFirstTimeToggle (sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_FIRST_TIME_TOGGLE, true
    )


}