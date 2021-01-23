package com.goodbarber.sharjah.eventbus.eventmodels

import com.stenleone.stanleysfilm.eventbus.eventmodels.EventModel
import com.stenleone.stanleysfilm.network.entity.movie.Movie

class OpenFilmEvent(var movie: Movie) : EventModel