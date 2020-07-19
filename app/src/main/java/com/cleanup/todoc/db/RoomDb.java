package com.cleanup.todoc.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Random;

@Database(entities = {Task.class, Project.class}, version=1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static RoomDb instance;
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    public static RoomDb getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDb.class, "TasksDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(addProjectsOnDbCreation())
                    .build();
        }
        return instance;
    }

    private static Callback addProjectsOnDbCreation() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Project[] projects = Project.getAllProjects();
                for (Project project : projects) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", project.getId());
                    contentValues.put("name", project.getName());
                    contentValues.put("color", project.getColor());
                    db.insert("project", OnConflictStrategy.IGNORE, contentValues);
                }
            }
        };
    }
}
