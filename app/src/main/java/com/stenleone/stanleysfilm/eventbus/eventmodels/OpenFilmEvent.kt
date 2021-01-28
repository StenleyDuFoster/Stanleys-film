package com.goodbarber.sharjah.eventbus.eventmodels

import com.stenleone.stanleysfilm.eventbus.eventmodels.EventModel
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI

class OpenFilmEvent(var movieUI: MovieUI) : EventModel