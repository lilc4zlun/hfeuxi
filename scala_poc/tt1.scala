object AssetEvent extends Enumeration {
    type AssetEvent = Value
    val AssetTunedEvent,
      AssetPlaybackBitrateEvent,
      AssetStillTunedEvent,
      AssetStartOverEvent,
      AssetCommercialBreakStartEvent,
      AssetCommercialBreakEndEvent,
      AssetTuneForwardEvent,
      AssetTuneRewindEvent,
      AssetTunePauseEvent,
      AssetTuneResumeEvent,
      AssetTunedEnd = Value
  }

  object AssetEventField extends Enumeration {
    type AssetEventField = Value
    val assetSessionId,       // most or all events
      deviceTimestamp,
      serviceTimestamp,
      sessionId,
      programStartTime,
      programEndTime,
      accountId,
      deviceId,
      progId,
      assetId,
      cid,                    // channel id
      profileId,
      assetType,              // linear or vod
      contentType,            // e.g. TV
      deviceType,             // model no?
      liveTuneType,           // on-now, timeshifted, or catch-up
      onNowPoint,             // current live point (should be roughly equivalent to deviceTimestamp for on-now or timeshifted)
      currentWatchPoint,      // current play point
      videoBitrate,           // playback bitrate
      audioBitrate,
      bandwidthEstimate,
      cdnUrl,
      startOverPoint,         // start over
      currentLiveBookmark,    // tuned
      trickStartPoint,        // forward and rewind
      trickEndPoint,
      startPoint,             // forward, rewind, resume
      stopPoint,              // pause
      playTime = Value        // tuned end
  }

  object LiveTuneType extends Enumeration {
    type LiveTuneType = Value
    val onNow = Value("On-now")
    val timeShifted = Value("On-now_TimeShifted")
    val catchUp = Value("Catch-up")
  }

val EventData = "eventData"

def getEventData(event: Map[String, Any]): Map[String, Any] = {
    if(event.contains(EventData)) event.get(EventData).get.asInstanceOf[Map[String, Any]]
    else event
}

object SchemaProperty extends Enumeration {
    type SchemaProperty = Value
    val namespace, name = Value
    val eventVersion = Value("event.version")
  }

def getSchemaProperty(event: Map[String, Any], sProp: SchemaProperty.SchemaProperty): String = {
    event.get(sProp.toString) match {
      case Some(a:Any) => a.toString
      case None => null
    }
  }

def getNumericValue(src:Option[Any]):Option[Double] = {
    try {
      src match {
        case Some(d:Double) => Some(d)
        case Some(l:Long) => Some(l.toDouble)
        case Some(i:Int) => Some(i.toDouble)
        case Some(s:String) => Some(s.toDouble)
        case _ => None
      }
    } catch {
      case e:NumberFormatException => None
    }
}

def pixelEventUtilsGetEventFieldNumeric(event: Map[String, Any], numericField:String): Option[Double] = {
    getNumericValue(getEventData(event).get(numericField))
 }

def getEventFieldNumeric(event: Map[String, Any], numericField: AssetEventField.AssetEventField): Option[Double] = {
    pixelEventUtilsGetEventFieldNumeric(getEventData(event), numericField.toString)
}

