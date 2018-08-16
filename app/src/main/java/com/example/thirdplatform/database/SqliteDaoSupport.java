package com.example.thirdplatform.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteDaoSupport<T> implements IDaoSupport<T> {
    // ContentValues putString, putInt
    private static Map<String, Method> mPutMethodMap = new HashMap<>();
    // Cursor getInt getString
    private static Map<String, Method> mGetMethodMap = new HashMap<>();

    private SQLiteDatabase mDatabase;
    private Class<?> mEntityType;
    private String mTableName;
    private Field mIdField;

    public SqliteDaoSupport() {
    }

    @Override
    public void init(SQLiteDatabase database, Class<?> clazz) {
        this.mDatabase = database;
        this.mEntityType = clazz;
        this.mTableName = clazz.getSimpleName();
        Field[] fields = mEntityType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                mIdField = field;
                break;
            }
        }

        if (mIdField == null) {
            throw new IllegalArgumentException("No id defined");
        }
    }

    public static void createTable(SQLiteDatabase database, Class<?> entityClass, boolean ifNotExists) {
        String constraint = ifNotExists? "if not exists ": "";
        Field[] fields = entityClass.getDeclaredFields();
        String sql = "create table " + constraint + entityClass.getSimpleName() + "(";
        for (int i = 0; i < fields.length; i++) {
            String typeName = getTypeName(fields[i].getType());
            if (fields[i].getAnnotation(Id.class) != null) {
                sql += fields[i].getName() + " " + typeName + " primary key, ";
            } else if (i == fields.length - 1) {
                sql += fields[i].getName() + " " + typeName + ")";
            } else {
                sql += fields[i].getName() + " " + typeName + ",";
            }
        }

        database.execSQL(sql);
    }

    private static String getTypeName(Class<?> type) {
        if (type == String.class) {
            return "text";
        } else if (type == Integer.class || type == int.class ||
                type == Long.class || type == long.class) {
            return "integer";
        }
        return "";
    }

    public static void dropTable(SQLiteDatabase database, Class<?> entityClass, boolean ifExist) {
        String ifStr = ifExist ? "if exists" : "";
        String sql = "drop table " + ifStr + " " + entityClass.getSimpleName();
        database.execSQL(sql);
    }

    @Override
    public void insert(T t) {
        try {
            ContentValues contentValues = obj2Values(t);
            mDatabase.insert(getTableName(), null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void insertAll(List<T> list) {
        mDatabase.beginTransaction();
        try {
            for (T obj : list) {
                ContentValues contentValues = obj2Values(obj);
                mDatabase.insert(getTableName(), null, contentValues);
            }
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            mDatabase.endTransaction();
        }
    }

    public ContentValues obj2Values(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        ContentValues values = new ContentValues();
        for (Field field : fields) {
            Method method = mPutMethodMap.get(field.getType().getName());
            if (method == null) {
                method = ContentValues.class.getDeclaredMethod("put", String.class, field.getType());
                mPutMethodMap.put(field.getType().getName(), method);
            }

            method.setAccessible(true);
            field.setAccessible(true);
            method.invoke(values, field.getName(), field.get(object));
        }

        return values;
    }

    @Override
    public List<T> query(String where, String[] args) {
        List<T> list = new ArrayList<>();
        String[] columns = getFieldNames(getEntityClass());
        Cursor cursor = mDatabase.query(getTableName(), columns,
                where, args, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Object obj = getEntityClass().newInstance();
                    Field[] fields = getEntityClass().getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        int index = cursor.getColumnIndex(fields[i].getName());
                        Method method = getCursorRetrieveMethod(fields[i].getType());
                        method.setAccessible(true);
                        Object val = method.invoke(cursor, index);
                        fields[i].setAccessible(true);
                        fields[i].set(obj, val);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private Method getCursorRetrieveMethod(Class<?> type) throws NoSuchMethodException {
        switch (type.getSimpleName()) {
            case "int":
            case "Integer": {
                Method method = mGetMethodMap.get("getInt");
                if (method == null) {
                    method = Cursor.class.getDeclaredMethod("getInt", int.class);
                    mGetMethodMap.put("getInt", method);
                }
                return method;
            }
            case "double":
            case "Double": {
                Method method = mGetMethodMap.get("getDouble");
                if (method == null) {
                    method = Cursor.class.getDeclaredMethod("getDouble", int.class);
                    mGetMethodMap.put("getDouble", method);
                }
                return method;
            }
            case "short":
            case "Short": {
                Method method = mGetMethodMap.get("getShort");
                if (method == null) {
                    method = Cursor.class.getDeclaredMethod("getShort", int.class);
                    mGetMethodMap.put("getShort", method);
                }
                return method;
            }
            case "String": {
                Method method = mGetMethodMap.get("getString");
                if (method == null) {
                    method = Cursor.class.getDeclaredMethod("getString", int.class);
                    mGetMethodMap.put("getString", method);
                }
                return method;
            }
            case "float":
            case "Float": {
                Method method = mGetMethodMap.get("getFloat");
                if (method == null) {
                    method = Cursor.class.getDeclaredMethod("getFloat", int.class);
                    mGetMethodMap.put("getFloat", method);
                }
                return method;
            }
        }

        return null;
    }

    private String[] getFieldNames(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        String[] columns = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columns[i] = fields[i].getName();
        }

        return columns;
    }

    @Override
    public void update(T t) {
        try {
            ContentValues contentValues = obj2Values(t);
            Object id = mIdField.get(t);
            mDatabase.update(getTableName(), contentValues, mIdField.getName() + "=?", new String[] { String.valueOf(id) });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String where, String[] whereArgs) {
        mDatabase.delete(getTableName(), where, whereArgs);
    }

    public String getTableName() {
        return mTableName;
    }

    public Class<?> getEntityClass() {
        return mEntityType;
    }
}
