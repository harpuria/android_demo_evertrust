package com.yhh.library_demo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.yhh.library_demo.data.Bbs


class SettingFragment : Fragment() {

    lateinit var settingProfileImage:ImageView
    lateinit var settingIdLabel:TextView
    lateinit var settingPasswordEdit:EditText
    lateinit var settingPasswordModifyBtn:Button
    lateinit var settingsDeleteUserBtn:Button
    lateinit var logoutBtn:Button
    lateinit var mContext: Context
    lateinit var helper:SqliteHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        helper = SqliteHelper(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        initView(view)

        settingProfileImage.setImageURI(Uri.parse(Singleton.curUser.filePath))
        settingIdLabel.text = "ID : ${Singleton.curUser.id}"
        settingPasswordEdit.setText(Singleton.curUser.password)

        settingPasswordModifyBtn.setOnClickListener {
            if(settingPasswordEdit.text.isEmpty()){
                Toast.makeText(mContext, "변경할 비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            helper.updateUser(Singleton.curUser.userNo, settingPasswordEdit.text.toString())
            Toast.makeText(mContext, "비밀번호가 변경되었습니다", Toast.LENGTH_LONG).show()
        }

        settingsDeleteUserBtn.setOnClickListener {
            showDialog()
        }

        logoutBtn.setOnClickListener {
            Singleton.curUser.userNo = 0
            Singleton.curUser.id = ""
            Singleton.curUser.password = ""
            val intent = Intent(mContext, LoginActivity::class.java)
            mContext.startActivity(intent)
        }

        return view
    }

    fun initView(view:View){
        settingProfileImage = view.findViewById(R.id.settingProfileImage)
        settingIdLabel = view.findViewById(R.id.settingIdLabel)
        settingPasswordEdit = view.findViewById(R.id.settingPasswordEdit)
        settingPasswordModifyBtn = view.findViewById(R.id.settingPasswordModifyBtn)
        settingsDeleteUserBtn = view.findViewById(R.id.settingsDeleteUserBtn)
        logoutBtn = view.findViewById(R.id.logoutBtn)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    fun showDialog(){
        val msgBuilder = AlertDialog.Builder(mContext)
                .setTitle("계정 삭제")
                .setMessage("계정을 삭제하시겠습니까?")
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    helper.deleteUser(Singleton.curUser)
                    Toast.makeText(mContext, "계정 삭제가 완료되었습니다", Toast.LENGTH_LONG).show()
                    val intent = Intent(mContext, LoginActivity::class.java)
                    mContext.startActivity(intent)
                })
                .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->

                })
        val msgDlg = msgBuilder.create()
        msgDlg.show()
    }
}