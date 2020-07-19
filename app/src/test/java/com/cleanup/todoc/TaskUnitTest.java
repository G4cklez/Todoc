package com.cleanup.todoc;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.cleanup.todoc.db.ProjectDao;
import com.cleanup.todoc.db.RoomDb;
import com.cleanup.todoc.db.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */

@RunWith(AndroidJUnit4.class)
public class TaskUnitTest {


    private RoomDb mDataBase;
    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDataBase = Room.inMemoryDatabaseBuilder(context, RoomDb.class)
                .allowMainThreadQueries()
                .addCallback(prepopulateDatabase())
                .build();

        mTaskDao = mDataBase.taskDao();
        mProjectDao = mDataBase.projectDao();
    }

    @After
    public void closeDb() {
        mDataBase.close();
    }


    private static RoomDatabase.Callback prepopulateDatabase() {
        return new RoomDatabase.Callback() {

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


    @Test
    public void test_projects() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", mProjectDao.getProjectById(task1.getProjectId()).getName());
        assertEquals("Projet Lucidia", mProjectDao.getProjectById(task2.getProjectId()).getName());
        assertEquals("Projet Circus", mProjectDao.getProjectById(task3.getProjectId()).getName());
        assertNull(mProjectDao.getProjectById(task4.getProjectId()));
        assertNull(task4.getProject());
    }

    @Test
    public void test_insert_task() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task1);
        assertEquals(task1.getName(), mTaskDao.getTask(task1.getId()).getName());
    }

    @Test
    public void test_delete_task() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        mTaskDao.insertTask(task1);
        assertEquals(task1.getName(), mTaskDao.getTask(task1.getId()).getName());
        mTaskDao.deleteTask(task1);
        assertNull(mTaskDao.getTask(task1.getId()));

    }


    @Test
    public void get_task_list_test() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());
        mTaskDao.insertTask(task1);
        mTaskDao.insertTask(task2);
        mTaskDao.insertTask(task3);
        mTaskDao.insertTask(task4);


        assertEquals(4, mTaskDao.getTasksList().size());
    }



    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}