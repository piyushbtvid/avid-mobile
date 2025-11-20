package com.faithForward.media.ui.epg


//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.example.plustv.databinding.EpgParentFragmentBinding
//import com.jakewharton.threetenabp.AndroidThreeTen
//
//class EpgParentFragment: Fragment(), EpgFragment.EpgFragmentListener {
//
//    private val binding:EpgParentFragmentBinding by lazy {
//        EpgParentFragmentBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        AndroidThreeTen.init(requireContext().applicationContext)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        loadEpgFragment()
//    }
//
//
//    private fun loadEpgFragment(){
//        val epgFragment = EpgFragment().apply {
//            this.epgFragmentListener = this@EpgParentFragment
//        }
//        childFragmentManager.beginTransaction()
//            .replace(binding.contentContainer.id, epgFragment)
//            .commit()
//    }
//
//    override fun onSelect(simpleProgram: EpgFragment.SimpleProgram?) {
//        binding.titleText.text = simpleProgram?.title
//        Glide.with(this).load(simpleProgram?.thumbnail).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(binding.contentImage)
//    }
//}