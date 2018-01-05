package com.journal.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.journal.entities.Note;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Oct 23, 2014 10:45:30 AM
 */
public class NoteController {
    public static Dao<Note, String> getDao() throws SQLException {
        return DBController.getInstance().getDB().getDao(Note.class);
    }

    public static int addOrUpdate(Note note) {
        try {
            return getDao().createOrUpdate(note).getNumLinesChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int delete(Note note) {
        try {
            return getDao().delete(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static ArrayList<Note> queryAllByTimeAsc() {
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            QueryBuilder<Note, String> queryBuilder = getDao().queryBuilder();
            queryBuilder.orderBy(Note.DATE_MODIFIED, true);
            notes = (ArrayList<Note>) getDao().query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }


    public static ArrayList<Note> queryByKeyOnContent(String key) {
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            QueryBuilder<Note, String> queryBuilder = getDao().queryBuilder();
            Where<Note, String> where = queryBuilder.where();
            where.like(Note.CONTENT, "%" + key + "%").query();
            //倒序 从大到小
            queryBuilder.orderBy(Note.DATE_CREATED, true);
            notes = (ArrayList<Note>) getDao().query(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }


}
