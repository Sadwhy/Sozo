package com.animestudios.animeapp.ui.screen.main

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.animestudios.animeapp.*
import com.animestudios.animeapp.anilist.api.common.Anilist
import com.animestudios.animeapp.databinding.MainScreenBinding
import com.animestudios.animeapp.settings.UISettings
import com.animestudios.animeapp.viewmodel.imp.MainViewModelImp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainScreen : Fragment() {
    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!
    private val model by viewModels<MainViewModelImp>()
    private var uiSettings = UISettings()

    @SuppressLint("ResourceAsColor", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.getUnreadNotificationsCount()
        model.genres.observe(
            this
        ) {
            if (it != null) {
                if (it) {
                    val mainViewPager = binding.viewPager
                    val navbar = binding.navbar
                    binding.navbar.visibility = View.VISIBLE
                    binding.viewPager.isUserInputEnabled = false
                    binding.viewPager.setPageTransformer(ZoomOutPageTransformer(uiSettings))
                    binding.viewPager.adapter = BottomNavigationAdapter(requireActivity())
                    navbar.visible()
                    model.unReadNotificationCountLiveData.observe(viewLifecycleOwner) {
                        if (it == 0)
                            binding.navbar.removeBadge(R.id.notification)
                        else
                            binding.navbar.getOrCreateBadge(R.id.notification).number = it

                    }
                    navbar.setOnItemSelectedListener {
                        when (it.itemId) {
                            R.id.home -> {

                                navbar.getMenu().getItem(0).setChecked(true);
                                navbar.animate().translationZ(12f).setDuration(200).start()
                                mainViewPager.setCurrentItem(0, false)
                            }
                            R.id.brows -> {
                                navbar.getMenu().getItem(1).setChecked(true);
                                navbar.animate().translationZ(12f).setDuration(200).start()
                                mainViewPager.setCurrentItem(1, false)
                            }
                            R.id.list -> {
                                navbar.getMenu().getItem(2).setChecked(true);
                                navbar.animate().translationZ(12f).setDuration(200).start()
                                mainViewPager.setCurrentItem(2, false)
                            }
                            R.id.account -> {
                                navbar.animate().translationZ(12f).setDuration(200).start()
                                navbar.getMenu().getItem(3).setChecked(true);
                                mainViewPager.setCurrentItem(3, false)

                            }
                        }
                        true
                    }
                    model.loadProfile() {

                        val item = bottomBar.menu.getItem(4)
                        item.iconTintList = null
                        item.iconTintMode = PorterDuff.Mode.DST
                        Glide.with(requireView())
                            .load(Anilist.avatar)
                            .circleCrop()
                            .into(object : CustomTarget<Drawable>() {
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    println(resource.current)
                                    item.setIcon(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    item.setIcon(placeholder)
                                }
                            })
                        binding.mainProgressBar.gone()
                        mainViewPager.visible()
                    }

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = com.animestudios.animeapp.databinding.MainScreenBinding.inflate(
            inflater,
            container,
            false
        )
        return _binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navbar = binding.navbar
        val mainViewPager = binding.viewPager
        binding.mainProgressBar.visible()
        bottomBar = navbar
        mainViewPager.gone()
        navbar.gone()
        mainViewPager.isUserInputEnabled = false
        uiSettings = readData("ui_settings") ?: uiSettings
        model.getGenres(requireActivity())

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}