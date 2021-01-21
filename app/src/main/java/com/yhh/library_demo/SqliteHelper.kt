package com.yhh.library_demo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yhh.library_demo.data.Bbs
import com.yhh.library_demo.data.User

// SQLite 사용하기 위해서는 헬퍼 클래스가 필요하다
class SqliteHelper(context:Context)
    :SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    // 테이블 생성
    override fun onCreate(db: SQLiteDatabase?) {
        val userCreate = "CREATE TABLE USER (USER_NO INTEGER PRIMARY KEY, ID TEXT, PASSWORD TEXT, FILE_PATH TEXT)"
        val bbsCreate = "CREATE TABLE BBS (BBS_NO INTEGER PRIMARY KEY, TITLE TEXT, CONTENT TEXT, AUTHOR TEXT, PASSWORD TEXT, VISIT_COUNT INTEGER, DATE TEXT)"
        db?.execSQL(userCreate)
        db?.execSQL(bbsCreate)
    }

    // 현재 DB 버전이 이전 버전보다 높을 경우 호출
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
    /** USER 테이블 관련 **/
    // 유저 데이터 삽입
    fun insertUser(user: User){
        val values = ContentValues()
        values.put("USER_NO", user.userNo)
        values.put("ID", user.id)
        values.put("PASSWORD", user.password)
        values.put("FILE_PATH", user.filePath)

        val wd = writableDatabase
        wd.insert("USER", null, values)
        wd.close()
    }

    // 유저 데이터 전체 조회
    fun selectUser(): MutableList<User>{
        val list = mutableListOf<User>()

        val select = "SELECT * FROM USER"

        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while(cursor.moveToNext()){
            val userNo = cursor.getLong(cursor.getColumnIndex("USER_NO"))
            val id = cursor.getString(cursor.getColumnIndex("ID"))
            val password = cursor.getString(cursor.getColumnIndex("PASSWORD"))
            val filepath = cursor.getString(cursor.getColumnIndex("FILE_PATH"))

            list.add(User(userNo, id, password, filepath))
        }

        cursor.close()
        rd.close()

        return list
    }

    // 유저 한 건 조회
    fun selectOneUser(userId:String):User{
        val select = "SELECT * FROM USER WHERE ID = '$userId'"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        cursor.moveToFirst()

        val userNo = cursor.getLong(cursor.getColumnIndex("USER_NO"))
        val id = cursor.getString(cursor.getColumnIndex("ID"))
        val password = cursor.getString(cursor.getColumnIndex("PASSWORD"))
        val filepath = cursor.getString(cursor.getColumnIndex("FILE_PATH"))

        return User(userNo, id, password, filepath)
    }

    // 유저 중복 체크
    fun isExistUser(user: User): Boolean{
        val select = "SELECT * FROM USER WHERE ID = '${user.id}'"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select,null)

        cursor.moveToFirst()
        cursor.close()
        rd.close()

        return cursor.count == 0
    }

    // 비밀번호 확인
    fun isCheckPassword(user:User): Boolean{
        val select = "SELECT * FROM USER WHERE ID = '${user.id}' AND PASSWORD = '${user.password}'"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        cursor.moveToFirst()
        cursor.close()
        rd.close()

        return cursor.count == 0
    }

    // 유저 데이터(비번) 수정
    fun updateUser(userNo:Long?, modifyPw: String){
        val values = ContentValues()
        values.put("PASSWORD", modifyPw)

        val wd = writableDatabase
        wd.update("USER", values, "USER_NO = $userNo", null)
        wd.close()
    }

    // 유저 데이터 삭제
    fun deleteUser(user: User){
        val delete = "DELETE FROM USER WHERE USER_NO = ${user.userNo}"
        val wd = writableDatabase
        wd.execSQL(delete)
        wd.close()
    }

    /** BBS 테이블 관련 **/
    fun insertBbs(bbs: Bbs){
        val value = ContentValues()
        value.put("BBS_NO", bbs.bbsNo)
        value.put("TITLE", bbs.title)
        value.put("CONTENT", bbs.content)
        value.put("AUTHOR", bbs.author)
        value.put("PASSWORD", bbs.password)
        value.put("VISIT_COUNT", bbs.visit_count)
        value.put("DATE", bbs.date)

        val wd = writableDatabase
        wd.insert("BBS", null, value)
        wd.close()
    }

    fun selectBbs():MutableList<Bbs>{
        val list = mutableListOf<Bbs>()
        val query = "SELECT * FROM BBS"

        val rd = readableDatabase
        val cursor = rd.rawQuery(query, null)
        while(cursor.moveToNext()){
            val bbsNo = cursor.getLong(cursor.getColumnIndex("BBS_NO"))
            val title = cursor.getString(cursor.getColumnIndex("TITLE"))
            val content = cursor.getString(cursor.getColumnIndex("CONTENT"))
            val author = cursor.getString(cursor.getColumnIndex("AUTHOR"))
            val password = cursor.getString(cursor.getColumnIndex("PASSWORD"))
            val visitCount = cursor.getInt(cursor.getColumnIndex("VISIT_COUNT"))
            val date = cursor.getString(cursor.getColumnIndex("DATE"))

            list.add(Bbs(bbsNo, title, content, author, password, visitCount, date))
        }

        return list
    }

    fun selectOneBbs(bbsNo:Long): Bbs{
        val select = "SELECT * FROM BBS WHERE BBS_NO = ${bbsNo}"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        cursor.moveToFirst()
        val bbsNo = cursor.getLong(cursor.getColumnIndex("BBS_NO"))
        val title = cursor.getString(cursor.getColumnIndex("TITLE"))
        val content = cursor.getString(cursor.getColumnIndex("CONTENT"))
        val author = cursor.getString(cursor.getColumnIndex("AUTHOR"))
        val password = cursor.getString(cursor.getColumnIndex("PASSWORD"))
        val visitCount = cursor.getInt(cursor.getColumnIndex("VISIT_COUNT"))
        val date = cursor.getString(cursor.getColumnIndex("DATE"))

        return Bbs(bbsNo, title, content, author, password, visitCount, date)
    }

    fun selectFindBbs(searchKeyword: String): MutableList<Bbs>{
        var list = mutableListOf<Bbs>()
        val select = "SELECT * FROM BBS WHERE TITLE LIKE '%$searchKeyword%'"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while(cursor.moveToNext()){
            val bbsNo = cursor.getLong(cursor.getColumnIndex("BBS_NO"))
            val title = cursor.getString(cursor.getColumnIndex("TITLE"))
            val content = cursor.getString(cursor.getColumnIndex("CONTENT"))
            val author = cursor.getString(cursor.getColumnIndex("AUTHOR"))
            val password = cursor.getString(cursor.getColumnIndex("PASSWORD"))
            val visitCount = cursor.getInt(cursor.getColumnIndex("VISIT_COUNT"))
            val date = cursor.getString(cursor.getColumnIndex("DATE"))

            list.add(Bbs(bbsNo, title, content, author, password, visitCount, date))
        }

        return list
    }

    fun updateVisitCount(bbsNo:Long, currentVisitCount:Int){
        val values = ContentValues()
        values.put("VISIT_COUNT", currentVisitCount)
        val wd = writableDatabase
        wd.update("BBS", values, "BBS_NO = ${bbsNo}", null)
        wd.close()
    }

    fun deleteBbs(bbsNo:Long): Boolean{
        val wd = writableDatabase
        val result = wd.delete("BBS", "BBS_NO = ${bbsNo}", null)
        wd.close()

        return result == 1
    }

    fun updateBbs(bbs:Bbs){
        val values = ContentValues()
        values.put("title", bbs.title)
        values.put("content", bbs.content)

        val wd = writableDatabase
        wd.update("BBS", values, "BBS_NO = ${bbs.bbsNo}", null)
        wd.close()
    }

    companion object{
        const val DB_VER = 1
        const val DB_NAME = "user.db"
    }
}