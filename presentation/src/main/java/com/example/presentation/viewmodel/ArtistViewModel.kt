package com.example.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.FavoritesDao
import com.example.domain.repository.SearchRepository
import com.example.presentation.model.ArtistDisplay
import com.example.presentation.model.toDisplay
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository: SearchRepository,
    private val favoritesDao: FavoritesDao
    ) : ViewModel() {

    private val _artist = MutableLiveData<ArtistDisplay>()
    val artist: LiveData<ArtistDisplay> = _artist

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Unit>()
    val loading: LiveData<Unit> = _loading

    private val _favoritesState = MutableLiveData<Boolean>()
    val favoritesState: LiveData<Boolean> = _favoritesState

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _error.postValue(exception.message)
    }

    private val _id = MutableLiveData<Int>()
    val id: LiveData<Int> = _id

    fun loadArtist(id: Int) =
        viewModelScope.launch(exceptionHandler) {
            _loading.postValue(Unit)
            val curArtist = repository.getArtist(id)
            _artist.postValue(curArtist.toDisplay())
            val isInFavorites = favoritesDao.isInFavorites(curArtist)
            _favoritesState.value = isInFavorites
            Log.d("load", "Favorites:" + _favoritesState.value)
            _id.value = id
        }
    fun onFavoritesClicked() {
        viewModelScope.launch(exceptionHandler) {
            if (_favoritesState.value!!) {
                favoritesDao.delete(repository.getArtist(_id.value!!))
                _favoritesState.value = false
                Log.d("FavClick", "Deleted" + id.value)
            } else {
                favoritesDao.add(repository.getArtist(_id.value!!))
                _favoritesState.value = true
                Log.d("FavClick", "Added" + id.value)
            }
        }
    }
}