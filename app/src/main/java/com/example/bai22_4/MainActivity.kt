package com.example.bai22_4

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SimpleAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bai22_4.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    lateinit var db:SQLiteDatabase
    lateinit var rs: Cursor
    lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("Select * from tuhocdb limit 20 ", null)

        if (rs.moveToFirst()) {
            binding.edtUserName.setText(rs.getString(1))
            binding.edtEmail.setText(rs.getString(2))

        }

        binding.btnFirst.setOnClickListener {
            if (rs.moveToFirst()) {
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))

            }
            else {
                Toast.makeText(applicationContext,"No Data",Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnNext.setOnClickListener {
            if (rs.moveToNext()){
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))
            }
            else if (rs.moveToFirst())
            {
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))
            }
            else {
                Toast.makeText(applicationContext,"No Data",Toast.LENGTH_SHORT).show()

            }
        }
        binding.btnPrev.setOnClickListener {
            if (rs.moveToPrevious()){
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))
            }
            else if (rs.moveToLast())
            {
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))
            }
            else {
                Toast.makeText(applicationContext,"No Data",Toast.LENGTH_SHORT).show()

            }

        }

        binding.btnLast.setOnClickListener {
            if (rs.moveToLast()) {
                binding.edtUserName.setText(rs.getString(1))
                binding.edtEmail.setText(rs.getString(2))

            }
            else {
                Toast.makeText(applicationContext,"No Data",Toast.LENGTH_SHORT).show()
            }

        }

        adapter = SimpleCursorAdapter(applicationContext,android.R.layout.simple_expandable_list_item_2,rs,
            arrayOf("user","email"),
            intArrayOf(android.R.id.text1,android.R.id.text2),0
        )
        binding.lvFull.adapter = adapter
        binding.btnViewAll.setOnClickListener {
            binding.srcView.visibility = View.VISIBLE

            binding.lvFull.visibility = View.VISIBLE
//             thong  bao co su thay doi tren list view va update
            adapter.notifyDataSetChanged()
            binding.srcView.queryHint ="Tìm kiếm trong ${rs.count} bản ghi"
        }

        binding.srcView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               rs = db.rawQuery("select * from tuhocdb where user like '%${newText}' or email like '%${newText}'",null)
                adapter.changeCursor(rs)
                return true
            }

        })

        binding.btnInsert.setOnClickListener {
            val ad = ContentValues()
            ad.put("user", binding.edtUserName.text.toString())
            ad.put("email", binding.edtEmail.text.toString())

            db.insert("TUHOCDB",null,ad)
            rs.requery()
            adapter.notifyDataSetChanged()
//             dung de hien thong bao
        var al = AlertDialog.Builder(this)

            al.setTitle("Add record")
            al.setMessage("Successfull")
            al.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

                binding.edtEmail.setText("")
                binding.edtUserName.setText("")
                binding.edtUserName.requestFocus()

            })
            al.show()

        }

        binding.btnUpdate.setOnClickListener {
            val content = ContentValues()
            content.put("user", binding.edtUserName.text.toString())
            content.put("email", binding.edtEmail.text.toString())

            db.update("TUHOCDB",content,"_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            val al = AlertDialog.Builder(this)
            al.setTitle("Update")
            al.setMessage("Successful")
            al.setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which ->

                binding.edtEmail.requestFocus()

            })

            al.show()


        }

        binding.btnClear.setOnClickListener {
            binding.edtUserName.setText("")
            binding.edtEmail.setText("")
            binding.edtUserName.requestFocus()
        }

        binding.btnDelete.setOnClickListener {
            db.delete("TUHOCDB","_id=?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)

            ad.setTitle("Delete")
            ad.setMessage("Successful")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialog, which ->
                if (rs.moveToFirst())
                {
                    binding.edtUserName.setText("")
                    binding.edtEmail.setText("")
                    binding.edtUserName.requestFocus()
                } else {
                    binding.edtUserName.setText("No data found")
                    binding.edtEmail.setText("No data found ")
                }
            })
            ad.show()



        }

//             register for using context menu of listview
        registerForContextMenu(binding.lvFull)


    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(100,11,1,"Delete")
        menu?.setHeaderTitle("Removing Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
       if (item.itemId ==11)
       {
           db.delete("TUHOCDB","_id=?", arrayOf(rs.getString(0)))
           rs.requery()
           adapter.notifyDataSetChanged()
           binding.srcView.queryHint =  "Tìm kiếm trong ${rs.count} bản ghi"
//           Toast.makeText(applicationContext,"hien",Toast.LENGTH_SHORT).show()

       }
        return super.onContextItemSelected(item)
    }
}