package com.yhh.library_demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhh.library_demo.data.Bbs
import java.text.SimpleDateFormat
import java.util.*

class BbsFragment : Fragment() {
    lateinit var mContext: Context
    lateinit var recyclerView: RecyclerView
    lateinit var bbsSearchView: SearchView
    lateinit var searchBtn: Button
    lateinit var writeBtn: Button

    lateinit var helper: SqliteHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bbs, container, false)

        initView(view)

        helper = SqliteHelper(mContext)

        val adapter = BbsRecyclerViewAdapter()
        adapter.tempList = helper.selectBbs()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        val swipeHelperCallback = SwipeHelperCallback()
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // 서치 뷰에서 엔터키 입력시에도 검색이 되게 처리
        bbsSearchView.setOnKeyListener { v, keyCode, event ->
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER){
                adapter.tempList = helper.selectFindBbs(bbsSearchView.query.toString())
                recyclerView.adapter = adapter
            }
            true
        }

        searchBtn.setOnClickListener {
            adapter.tempList = helper.selectFindBbs(bbsSearchView.query.toString())
            recyclerView.adapter = adapter
        }

        writeBtn.setOnClickListener {
            val intent = Intent(mContext, BbsFormActivity::class.java)
            mContext.startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val adapter = BbsRecyclerViewAdapter()
        adapter.tempList = helper.selectBbs()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    fun initView(view: View){
        recyclerView = view.findViewById(R.id.bbsRecyclerView)
        bbsSearchView = view.findViewById(R.id.bbsSearchView)
        searchBtn = view.findViewById(R.id.searchBtn)
        writeBtn = view.findViewById(R.id.writeBtn)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }
}

class BbsRecyclerViewAdapter(): RecyclerView.Adapter<BbsRecyclerViewAdapter.ViewHolder>(){

    var tempList = mutableListOf<Bbs>()

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun setData(data: Bbs){
            val bbsTitleText = itemView.findViewById<TextView>(R.id.bbsTitleText)
            val bbsAuthorText = itemView.findViewById<TextView>(R.id.bbsAuthorText)
            val bbsDateText = itemView.findViewById<TextView>(R.id.bbsDateText)
            val bbsVisitCountText = itemView.findViewById<TextView>(R.id.bbsVisitCountText)

            bbsTitleText.text = "제목 : ${data.title}"
            bbsAuthorText.text = "작성자 : ${data.author}"
            bbsDateText.text = "작성일 : ${data.date}"
            bbsVisitCountText.text = "조회수 : ${data.visit_count.toString()}"

            itemView.setOnClickListener{
                var intent = Intent(itemView.context, BbsDetailActivity::class.java)

                intent.putExtra("bbs_no", data.bbsNo)
                intent.putExtra("title", data.title)
                intent.putExtra("content", data.content)
                intent.putExtra("visit_count", data.visit_count)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bbs_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = tempList.get(position)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return this.tempList.size
    }
}

// 리사이클러 뷰 아이템에서 스와이프할 경우 이벤트 처리
class SwipeHelperCallback : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    // Drag 되었을 때 호출
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // Swipe 되었을 때 호출
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        println("끼요오오오오옷!!!!")
    }
}