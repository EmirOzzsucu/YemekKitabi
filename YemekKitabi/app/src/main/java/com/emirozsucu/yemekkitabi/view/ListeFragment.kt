package com.emirozsucu.yemekkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.emirozsucu.yemekkitabi.adapter.TarifAdapter
import com.emirozsucu.yemekkitabi.databinding.FragmentListeBinding
import com.emirozsucu.yemekkitabi.model.Tarif
import com.emirozsucu.yemekkitabi.roomdb.TarifDAO
import com.emirozsucu.yemekkitabi.roomdb.TarifDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListeFragment : Fragment() {

    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TarifDatabase
    private lateinit var tarifDao: TarifDAO
    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(), TarifDatabase::class.java, "Tarifler").build()
        tarifDao = db.tarifDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { yeniEkle(it) }
        binding.tarifRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        verileriAl()


    }

    private fun verileriAl() {
        mDisposable.add(
            tarifDao.getALL()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(tarifler: List<Tarif>) {
       val adapter = TarifAdapter(tarifler)
        binding.tarifRecyclerView.adapter = adapter

    }

    fun yeniEkle(view: View) {
        val action =
            ListeFragmentDirections.actionListeFragmentToTarifFragment(bilgi = "yeni", id = 0)
        Navigation.findNavController(view).navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}