package id.rezajuliandri.github.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.rezajuliandri.github.favorites.databinding.FragmentFavoriteBinding
import id.rezajuliandri.github.favorites.di.favoriteModule
import id.rezajuliandri.github.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private val viewModel: FavoriteViewModel by viewModel()
    private val activityContext: MainActivity by lazy {
        (activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activityContext.supportActionBar?.apply {
            title = getString(R.string.favorite)
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        loadKoinModules(favoriteModule)
        val favAdapter = FavoriteAdapter { selectedUser ->
            val direction =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(selectedUser.username)
            findNavController().navigate(direction)
        }
        viewModel.getFavorites()
        binding?.apply {
            rvFavorites.apply {
                layoutManager = LinearLayoutManager(activityContext.applicationContext)
                setHasFixedSize(true)
                adapter = favAdapter
            }
            viewModel.favoritesResponse.observe(viewLifecycleOwner) {
                favAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.rvFavorites?.adapter = null
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.rvFavorites?.adapter = null
    }

    override fun onPause() {
        super.onPause()
        binding?.rvFavorites?.adapter = null
        _binding = null
    }
}