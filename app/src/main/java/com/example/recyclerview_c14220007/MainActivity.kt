    package com.example.recyclerview_c14220007

    import android.content.DialogInterface
    import android.content.Intent
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.util.Log
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken
    import java.lang.reflect.Array

    class MainActivity : AppCompatActivity() {
        private var _nama : MutableList<String> = emptyList<String>().toMutableList()
        private var _karakter : MutableList<String> = emptyList<String>().toMutableList()
        private var _deskripsi : MutableList<String> = emptyList<String>().toMutableList()
        private var _gambar : MutableList<String> = emptyList<String>().toMutableList()

        private var arWayang = ArrayList<wayang>()

        private lateinit var _rvWayang : RecyclerView

        lateinit var sp : SharedPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            sp = getSharedPreferences("dataSP", MODE_PRIVATE)

            val gson = Gson()
            val isiSP = sp.getString("spWayang", null)
            val type = object: TypeToken<ArrayList<wayang>>(){}.type
            if (isiSP != null){
                arWayang = gson.fromJson(isiSP, type)
            }

            _rvWayang = findViewById<RecyclerView>(R.id.rvWayang)
            if (arWayang.size == 0){
                siapkanData()
            } else {
                arWayang.forEach{
                    _nama.add(it.nama)
                    _karakter.add(it.karakter)
                    _deskripsi.add(it.deskripsi)
                    _gambar.add(it.foto)
                }
                arWayang.clear()
            }
            tambahData()
            tampilkanData()
        }

        fun siapkanData(){
            _nama = resources.getStringArray(R.array.namaWayang).toMutableList()
            _karakter = resources.getStringArray(R.array.karakterUtamaWayang).toMutableList()
            _deskripsi = resources.getStringArray(R.array.deskripsiWayang).toMutableList()
            _gambar = resources.getStringArray(R.array.gambarWayang).toMutableList()
        }

        fun tambahData(){
            val gson = Gson()
            val editor = sp.edit()

            arWayang.clear()
            for (position in _nama.indices){
                val data = wayang(
                    _gambar[position],
                    _nama[position],
                    _karakter[position],
                    _deskripsi[position]
                )
                arWayang.add(data)
            }

            val json = gson.toJson(arWayang)
            editor.putString("spWayang", json)
            editor.apply()
        }

        fun tampilkanData(){
            _rvWayang.layoutManager = GridLayoutManager(this, 2)
            val adapterWayang = adapterRevView(arWayang)
            _rvWayang.adapter = adapterWayang

            adapterWayang.setOnItemClickCallback(object: adapterRevView.onItemClickCallback{
                override fun onItemClicked(data: wayang) {
//                Toast.makeText(this@MainActivity, data.nama, Toast.LENGTH_LONG).show()
                    val intent = Intent(this@MainActivity, detWayang::class.java)
                    intent.putExtra("kirimData", data)
                    startActivity(intent)
                }

                override fun delData(pos: Int) {
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("HAPUS DATA")
                        .setMessage("Apakah Benar Data "+_nama[pos]+" Akan Dihapus?")
                        .setPositiveButton(
                            "HAPUS",
                            DialogInterface.OnClickListener { dialog, which ->
                                _gambar.removeAt(pos)
                                _nama.removeAt(pos)
                                _karakter.removeAt(pos)
                                _deskripsi.removeAt(pos)
                                tambahData()
                                tampilkanData()
                            }
                        )
                        .setNegativeButton("BATAL",
                            DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(this@MainActivity, "Data Batal Dihapus", Toast.LENGTH_LONG).show()
                            }
                        ).show()
                }
            })
        }
    }