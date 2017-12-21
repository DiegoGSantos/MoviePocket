package com.androidprojectbase.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View.GONE
import com.androidprojectbase.adapter.MoviesAdapter
import com.androidprojectbase.restclient.response.MovieListResponse
import com.moviepocket.R
import com.moviepocket.restclient.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_page.*
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.androidprojectbase.interfaces.MoviesCLickListener
import com.bumptech.glide.Glide
import com.moviepocket.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*


/**
 * Created by diegosantos on 12/17/17.
 */
class PageFragment : Fragment(), MoviesCLickListener {

    lateinit var builder: Dialog

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_page, container, false)
    }

    override fun onResume() {
        super.onResume()

        setListeners()
        listMovies()
    }

    private fun setListeners() {
        moviesList.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_UP)
                    hideQuickView()
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, event: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun listMovies() {
        val compositeDisposable = CompositeDisposable()
        val service = Service.Factory.create()

        compositeDisposable.add(
                service.listMovies()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe ({
                            result ->
                            updateUi(result)
                        }, { error ->
                            error.printStackTrace()
                        })
        )
    }

    private fun updateUi(movieListResponse: MovieListResponse) {
        moviesList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = MoviesAdapter(this.context, movieListResponse.results, this@PageFragment)

            progress.visibility = GONE 
        }
    }

    companion object {
        val ARG_PAGE = "ARG_PAGE"

        fun newInstance(page: Int): PageFragment {
            val args = Bundle()
            args.putInt(ARG_PAGE, page)
            val fragment = PageFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onMovieClick(movie: Movie) {
        Toast.makeText(context, "Movie click", Toast.LENGTH_LONG).show();
    }

    override fun onMovieLongClick(movie: Movie) {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_movie_preview, null)

        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .fallback(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .into(view.mMovieImage)

        builder = Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        builder.setContentView(view);
        builder.show();
    }

    fun hideQuickView() {
        builder.let { it.dismiss() }
    }
}