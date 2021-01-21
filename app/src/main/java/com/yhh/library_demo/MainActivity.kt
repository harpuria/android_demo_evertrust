package com.yhh.library_demo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhh.library_demo.data.Library
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), TextWatcher {
    lateinit var adapter:LibraryRecyclerViewAdapter2
    lateinit var recyclerView:RecyclerView
    lateinit var searchEditText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        // 리사이클러 뷰 설정
        adapter = LibraryRecyclerViewAdapter2()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        // 레트로핏 객체 생성
        val retrofit = Retrofit.Builder()
                .baseUrl(SeoulOpenApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // 레트로핏 통신 진행
        val seoulOpenService = retrofit.create(SeoulOpenApiService::class.java)
        seoulOpenService.getLibraryinfo(SeoulOpenApi.API_KEY).enqueue(object: Callback<Library>{
            override fun onResponse(call: Call<Library>, response: Response<Library>) {
                var data = response.body() as Library
                var dataList = mutableListOf<Map<String, String>>()
                for(d in data.seoulPublicLibraryInfo.row){
                    var dataMap = mutableMapOf<String, String>()
                    dataMap.put("name", d.lBRRYNAME)
                    dataMap.put("addr", d.aDRES)
                    dataMap.put("time", d.oPTIME)
                    dataMap.put("homepage", d.hMPGURL)
                    dataMap.put("tel", d.tELNO)
                    dataMap.put("closeDate", d.fDRMCLOSEDATE)
                    dataMap.put("x", d.xCNTS)
                    dataMap.put("y", d.yDNTS)

                    dataList.add(dataMap)
                }
                adapter.dataList = dataList
                adapter.searchDataList = dataList
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Library>, t: Throwable) {
                Toast.makeText(baseContext, "서버 통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        searchEditText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        adapter.filter.filter(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
    }

    fun initView(){
        recyclerView = findViewById(R.id.libInfoRecyclerView)
        searchEditText = findViewById(R.id.searchEditText)
    }
}

// 리사이클러 어댑터 정의
class LibraryRecyclerViewAdapter2(): RecyclerView.Adapter<LibraryRecyclerViewAdapter2.ViewHolder>(), Filterable {
    var dataList = mutableListOf<Map<String, String>>()
    var searchDataList = mutableListOf<Map<String, String>>()

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun setData(data: Map<String, String>){
            val libNameText  = itemView.findViewById<TextView>(R.id.libNameText)
            val libAddrText  = itemView.findViewById<TextView>(R.id.libAddrText)
            val libTimeText  = itemView.findViewById<TextView>(R.id.libTimeText)
            val homepageImage = itemView.findViewById<ImageView>(R.id.homepageImage)
            val callImage = itemView.findViewById<ImageView>(R.id.callImage)
            val locationMapImage = itemView.findViewById<ImageView>(R.id.locationMapImage)

            libNameText.text = data.get("name")
            libAddrText.text = data.get("addr")
            libTimeText.text = data.get("time")

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, PopupActivity::class.java)
                intent.putExtra("name", data.get("name"))
                intent.putExtra("addr", data.get("addr"))
                intent.putExtra("tel", data.get("tel"))
                intent.putExtra("closeDate", data.get("closeDate"))

                itemView.context.startActivity(intent)
            }

            // 이동 이미지 클릭 시 웹뷰 이동
            homepageImage.setOnClickListener {
                val intent = Intent(itemView.context, WebViewActivity::class.java)
                intent.putExtra("url", data.get("homepage"))
                itemView.context.startActivity(intent)
            }

            // 전화기 이미지 클릭 시 전화 앱 연결
            callImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.get("tel")?.trim()))
                itemView.context.startActivity(intent)
            }

            // 지도 클릭 시 구글 맵
            locationMapImage.setOnClickListener {
                val intent = Intent(itemView.context, GoogleMapActivity::class.java)
                intent.putExtra("name", data.get("name"))
                intent.putExtra("addr", data.get("addr"))
                intent.putExtra("x", data.get("x"))
                intent.putExtra("y", data.get("y"))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_info_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = searchDataList.get(position)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return this.searchDataList.size
    }

    // 검색 필터 설정
    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    searchDataList = dataList
                } else {
                    val filteredList = mutableListOf<Map<String, String>>()
                    //이부분에서 원하는 데이터를 검색할 수 있음
                    for (row in dataList) {
                        if(row.get("name")!!.contains(charString) ||
                                row.get("addr")!!.contains(charString)){
                            filteredList.add(row)
                        }
                    }
                    searchDataList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchDataList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchDataList = results?.values as MutableList<Map<String, String>>
                notifyDataSetChanged()
            }
        }
    }
}