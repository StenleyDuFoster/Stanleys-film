package com.goodbarber.sharjah.eventbus

import com.stenleone.stanleysfilm.eventbus.eventmodels.EventModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

object MessageEventBus {

    val bus: BroadcastChannel<EventModel> = ConflatedBroadcastChannel()

    suspend fun send(o: EventModel) {
            bus.send(o)
    }

    fun asChannel(): ReceiveChannel<EventModel> {
        return bus.openSubscription()
    }
}