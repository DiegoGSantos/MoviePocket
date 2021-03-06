package com.moviepocket.features.moviesList.view.adapter
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.moviepocket.features.moviesList.model.domain.Movie
import com.moviepocket.interfaces.MoviesCLickListener
import com.moviepocket.util.adapter.AdapterConstants
import com.moviepocket.util.adapter.ViewType
import com.moviepocket.util.adapter.ViewTypeDelegateAdapter

/**
 * Created by diegosantos on 12/17/17.
 */
class MoviesAdapter(listener: MoviesCLickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var movies: ArrayList<ViewType>
    private var delegateAdapters =
        SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.MOVIE, MoviesDelegateAdapter(listener))
        movies = ArrayList()
        movies.add(loadingItem)
    }

    fun addMovies(newMovies: List<Movie>, isThereMoreItemsToLoad: Boolean) {
        // first remove loading and notify
        val initPosition = movies.size - 1
        movies.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert movies and the loading at the end of the list
        movies.addAll(newMovies)

        var loadingView = 0
        if (isThereMoreItemsToLoad) {
            movies.add(loadingItem)
            loadingView = 1
        }

        notifyItemRangeChanged(initPosition, movies.size + loadingView /* plus loading item */)
    }
    
    override fun getItemCount(): Int = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            delegateAdapters.get(viewType)!!.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))?.onBindViewHolder(holder, movies[position])
    }

    override fun getItemViewType(position: Int) = movies[position].getViewType()
}