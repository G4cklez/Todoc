package com.cleanup.todoc.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insertTask(Task task);

    @Delete
    int deleteTask(Task task);

    @Query("SELECT * FROM Task WHERE id = :taskId")
    Task getTask(long taskId);

    @Query("select * from task")
    List<Task> getTasksList();

    @Query("delete  from task")
    int deleteAllTasks();
}
