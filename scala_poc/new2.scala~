object AssetEventProcess extends Enumeration {
    type AssetEventProcess = Value
    val Process,
    	Discard,
    	DiscardFirst,
      	DiscardSecond,
      	OpenEnd,
      	CloseEnd,
      	Invalidate = Value
  }


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


  case class AssetEventFieldUsed(
		 sectionStartField: Option[AssetEventField.AssetEventField],
		 sectionEndField: Option[AssetEventField.AssetEventField]
  )
 
 object DuractionCalcMatrix {

   private val AssetEventCalcMatrix = 
	 Map(
			 AssetEvent.AssetTunedEvent.toString() -> AssetEventFieldUsed(Some(AssetEventField.currentWatchPoint), None),
			 AssetEvent.AssetTunedEnd.toString() -> AssetEventFieldUsed(None, Some(AssetEventField.currentWatchPoint)),
			 AssetEvent.AssetTunePauseEvent.toString() -> AssetEventFieldUsed(None, Some(AssetEventField.stopPoint)),
			 AssetEvent.AssetTuneResumeEvent.toString() -> AssetEventFieldUsed(Some(AssetEventField.startPoint),None),
			 AssetEvent.AssetTuneForwardEvent.toString() -> AssetEventFieldUsed(Some(AssetEventField.trickEndPoint), Some(AssetEventField.trickStartPoint)),
			 AssetEvent.AssetTuneRewindEvent.toString() -> AssetEventFieldUsed(Some(AssetEventField.trickEndPoint), Some(AssetEventField.trickStartPoint)),
			 AssetEvent.AssetStartOverEvent.toString() -> AssetEventFieldUsed(Some(AssetEventField.programStartTime), Some(AssetEventField.startOverPoint))				
	    )

  private val AssetEventInvalidateException =
    List(	(AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()),
    		(AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()),
    		(AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()),
    		(AssetEvent.AssetTunePauseEvent.toString(),  AssetEvent.AssetStartOverEvent.toString()),
    		(AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTunedEnd.toString())
        )
        
 def findSectionFields(eventType : String):Option[AssetEventFieldUsed]= AssetEventCalcMatrix.get(eventType)
 
 def findSectionStartEnd(startEventType : String, endEventType : String) 
     :Tuple2[Option[AssetEventField.AssetEventField], Option[AssetEventField.AssetEventField]] = {
	   (findSectionFields(startEventType).flatMap(_.sectionStartField), findSectionFields(endEventType).flatMap(_.sectionEndField));
   }
 def isOnExceptionList(firstEvent:String, secondEvent:String):Boolean ={
	   if(AssetEventInvalidateException.indexOf((firstEvent, secondEvent)) < 0) false
	   else true
 }  
   
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


def getStringValue(src:Option[Any]):String = {
    src match {
      case None => ""
      case a:Some[_] => a.get.toString.trim
    }
  }

def pixelEventUtilsGetEventFieldString(event: Map[String, Any], field:String): String = {
    getStringValue(getEventData(event).get(field))
}

 def getEventFieldString(event:Map[String, Any], field:AssetEventField.AssetEventField):String = {
    pixelEventUtilsGetEventFieldString(event, field.toString)
  }


def isFieldPresent2(fieldValue: Any): Boolean = {
    fieldValue != null & !fieldValue.toString.trim.isEmpty
  }


def pixelEventUtilsSsFieldPresent(event: Map[String, Any], field:String):Boolean = {
    getEventData(event).get(field) match {
      case None => false
      case v:Some[_] => isFieldPresent2(v.get)
    }
  }

def isFieldPresent(event: Map[String, Any], field: AssetEventField.AssetEventField): Boolean = {
    pixelEventUtilsSsFieldPresent(event, field.toString)
  }


val  durationCalcMatrix = 
		 Map(
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.DiscardFirst, null, null),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.currentWatchPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardFirst, null, null),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.currentWatchPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.currentWatchPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.currentWatchPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTunedEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Process, AssetEventField.currentWatchPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTunedEvent.toString(), null) -> (AssetEventProcess.OpenEnd, AssetEventField.currentWatchPoint, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.Invalidate, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.Discard, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Discard, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Discard, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Discard, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Discard, null, null),
				 (AssetEvent.AssetTunePauseEvent.toString(), null) -> (AssetEventProcess.CloseEnd, AssetEventField.stopPoint, null),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.Invalidate, null, null),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.startPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.startPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.startPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.startPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneResumeEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Process, AssetEventField.startPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneResumeEvent.toString(), null) -> (AssetEventProcess.OpenEnd,  AssetEventField.startPoint, null),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneForwardEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneForwardEvent.toString(), null) -> (AssetEventProcess.OpenEnd, AssetEventField.trickEndPoint, null),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardSecond, null,  null),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneRewindEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Process, AssetEventField.trickEndPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneRewindEvent.toString(), null) -> (AssetEventProcess.OpenEnd, AssetEventField.trickEndPoint, null),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.programStartTime, AssetEventField.stopPoint),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardSecond, null, null),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.programStartTime, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.programStartTime, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.Process, AssetEventField.programStartTime, AssetEventField.startOverPoint),
				 (AssetEvent.AssetStartOverEvent.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.Process, AssetEventField.programStartTime, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetStartOverEvent.toString(), null) -> (AssetEventProcess.OpenEnd, null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTunedEvent.toString()) -> (AssetEventProcess.DiscardFirst, null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTunePauseEvent.toString()) -> (AssetEventProcess.DiscardFirst, null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTuneResumeEvent.toString()) -> (AssetEventProcess.DiscardFirst,null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTuneForwardEvent.toString()) -> (AssetEventProcess.DiscardFirst,null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTuneRewindEvent.toString()) -> (AssetEventProcess.DiscardFirst,null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetStartOverEvent.toString()) -> (AssetEventProcess.DiscardFirst,null, null),
				 (AssetEvent.AssetTunedEnd.toString(), AssetEvent.AssetTunedEnd.toString()) -> (AssetEventProcess.DiscardFirst, null, null),
				 (AssetEvent.AssetTunedEnd.toString(), null) -> (AssetEventProcess.CloseEnd, AssetEventField.currentWatchPoint, null)
   )

/*
 * Name:  	findSection
 * Inputs: 
 * 			startList: List of starting points of continuous sections  It is a list of tuples.
 *                     startList(*)._1 : device times of the asset event which has that starting point 
 *                     startList(*)._2: the time of that starting point. For example, if AssetTunedEvent, it will be current watch point
 *                     startList(*)._3: live tune type, such as Time-Shift, Catch-up and On-Now
 *                     startList(*)._4: program end time.  It is used to split continuous section if it crosses from time shift to catch up.
 *           endList:  List of ending points of continuous sections.  It is a list of tuples          
 *                     endList(*)._1 : device times of the asset event which has that ending point 
 *                     endList(*)._2: the time of that ending point. For example, if AssetTunedEnd, it will be current watch point
 *                     endList(*)._3: live tune type, such as Time-Shift, Catch-up and On-Now
 *                     endList(*)._4: program end time.  It is used to split continuous section if it crosses from time shift to catch up.
 *         returnList: return list of continuous sections
 *                     returnList(*)._1: starting point of the continuous section
 *                     returnList(*)._2: ending point of the continuous section
 *                     returnList(*)._3: Live tune type, such as Time-Shift, Catch-up and On-Now
 * Output:  returnList
 */

def findSection(startList:List[Tuple4[Long, Long, String, Long]], endList:List[Tuple4[Long, Long, String, Long]], returnList:List[Tuple3[Long, Long, String]]): List[Tuple3[Long, Long, String]] = {
  if(startList.isEmpty) return returnList
  if(endList.isEmpty) return returnList
/*
 * if the start point device time bigger than end point device time, skip.
 * This shouldn't happen since the records were pre-sorted unless some records missing.
 */
  if(startList(0)._1 < endList(0)._1){
/* If the start point device tm < program end tm and end point 
 * device tm > program end tm and it runs under time-shift mode, that 
 * section needs to be splited  
 */
       if((startList(0)._1  < startList(0)._4) && 
          (endList(0)._1 > startList(0)._4)
         ){
	  findSection( 
		startList.tail,
            	endList.tail,
            	returnList :::
/*
 * The left splited section will be on time-shift.  The end point = start point + the time
 * difference between time when program is ended and the device time which triggers the start
 * of the section.  For example, if a tv live, from 8 to 9 and user into time shift mode (press
 * resume @ 8:20 device time).  The time sift duration will be from 8:20 to 9:00.
 * Use device tm is as close as we can get to approx timeshift/catch up split
* 
*/
			List((	startList(0)._2,
              			startList(0)._2  + (startList(0)._4 - startList(0)._1),
				"Time-Shift")
                            ) 
			:::
/* the other part will be catch up
 * 
 */
			List(	(startList(0)._2 + (startList(0)._4 - startList(0)._1),
              			endList(0)._2,
	      			"Catch-up")
		     	     )	 
            		
		      )
         }
    	else{
/*    	  
 * Sometimes, for example, many fast forwards, possible the tricker start of 1 FF is a couple mil sec earlier than
 * previous FF tricker end. Make them equal in this case.  In other case, if a very bad data, it will
 * hide the bad data and not skew the report.  We have other DQ report which will revealthe issue.
 * 
*/
	    if ((endList(0)._2 - startList(0)._2) < 0){
		findSection( startList.tail,
             		 endList.tail,
            		 returnList
                        )
	    }
            else{
    	    findSection( startList.tail,
             		 endList.tail,
            		 returnList ::: 
				List((startList(0)._2,
              		 	      endList(0)._2,
	      		              startList(0)._3
	                             ))   
                        )
            }
         }
    }
     else{
       		findSection(startList, endList.tail, returnList)
	}
}


/*
 * Name: getEventTime
 * Inputs: 
 *      assetType: 	such as VodAsset (recorded video) and LinearAsset (currently on live stream)
 * 		eventType: 	this is used to determine which event field for each event needs to be considered as start or end point of a section
 *                  for example, AssetTunedEvent can only be considered as start point of a section.  AssetTunedEnd can only be considered as end point
 *                  of a section.  For detail logic on how each start and end points defined, see
 *                  https://onbrand.atlassian.net/wiki/display/RA/VideoConsumptionSummarizerJob#VideoConsumptionSummarizerJob-DurationLogic
 *      event:  	event data.
 *      pointType: 	depending on start or end point, events and fields usages are different
 */
def getEventTime(assetType:String, eventType:String, event:Map[String, Any], pointType:String) : Long = eventType match 
{

/* For rewind and forward events, the tricker start is the end of a normal speed section
 * tricker end is the start of the next normal speed section 
 */
  case "AssetTuneRewindEvent" | "AssetTuneForwardEvent" => 
	(if(pointType == "start") 
	     getEventFieldNumeric(event, AssetEventField.trickEndPoint).get.toLong
         else  
           getEventFieldNumeric(event, AssetEventField.trickStartPoint).get.toLong
         )
/*
 * For start over event, if linear asset, the program start time is the start of the next normal speed section.
 * For VOD, it will be 0
 * For LinearAsset, the program start time + the startOverPoint is where the end of the previous normal speed section
 * For VOD, the start over point is the end of the previous normal speed section
*/
  case "AssetStartOverEvent" =>
        (if ((assetType == "LinearAsset") && (pointType == "start"))
          getEventFieldNumeric(event, AssetEventField.programStartTime).get.toLong
         else if
            ((assetType == "LinearAsset") && (pointType == "end")) 
           getEventFieldNumeric(event, AssetEventField.startOverPoint).get.toLong +
           getEventFieldNumeric(event, AssetEventField.programStartTime).get.toLong
	else if
	    ((assetType == "VodAsset") && (pointType == "start")) 
		0
         else
         getEventFieldNumeric(event, AssetEventField.startOverPoint).get.toLong
	)

/*
 * AssetTunedEvent's currentWatchPoint is the start of the normal play speed section. 
 * Has nothing to do with the end of a normal speed section
 */

  case "AssetTunedEvent" =>  
	(if(pointType == "start"){ 
	       if(
	           isFieldPresent(event, AssetEventField.currentWatchPoint)
	         )  
		      getEventFieldNumeric(event, AssetEventField.currentWatchPoint).get.toLong
               else
         getEventFieldNumeric(event, AssetEventField.deviceTimestamp).get.toLong
         }
         else 
	     	-1 
         )                                                                                   
   
/*
 * AssetTunedEnd's currentWatchpoint is the end of a normal play speed section.
 * Has nothing to do with the start of a section
 */
 		
  case "AssetTunedEnd" =>  
	(if(pointType == "end") 
	     getEventFieldNumeric(event, AssetEventField.currentWatchPoint).get.toLong
         else 
	     	-1  
         )    

/*
 * AssetTuneResumeEvent's startPoint is the start point of a normal play speed section
 */

  case "AssetTuneResumeEvent" =>
	(if((pointType == "start"))
		getEventFieldNumeric(event, AssetEventField.startPoint).get.toLong
         else 
	     	-1  
         )
/*
 * AssetTunePauseEvent's stopPoint is the end point of a normal play speed section
 */
         
  case "AssetTunePauseEvent" =>
	(if((pointType == "end"))
	     getEventFieldNumeric(event, AssetEventField.stopPoint).get.toLong
         else 
	     	-1 
         )
					   
  case _ => -1
}

/*
 * Name: isValidAssetTunedEvent
 * Input:  eventList - is sorted event list by device start time stamp.  For now, we just check
 * if the 1st element is AssetTunedEvent.  If yes, return true, else, return false
 */

def isValidAssetTunedEvent(eventList: List[Map[String, Any]]) : Boolean = {
  val fstEvent = eventList(0)
  println("\ndebug xxx1 -> "+fstEvent)
  if((getSchemaProperty(fstEvent, SchemaProperty.name) == AssetEvent.AssetTunedEvent.toString) && (isFieldPresent(fstEvent, AssetEventField.currentWatchPoint))){
       println("\ndebug xxx1 -> stp1")
     if((((getEventFieldString(fstEvent,AssetEventField.assetType) == "LinearAsset") && getEventFieldNumeric(fstEvent, AssetEventField.currentWatchPoint).get.toLong > 0)
         || 
         (getEventFieldString(fstEvent,AssetEventField.assetType) == "VodAsset" && getEventFieldNumeric(fstEvent, AssetEventField.currentWatchPoint).get.toLong >= 0))){
           println("\ndebug xxx1 -> stp2")
        true
     }
     else
	{println("\ndebug xxx1 -> stp3")
       	false
        }
  }
  else {
    println("\ndebug xxx1 -> stp2")
    false
  }    
}


def getStartTime(event:Map[String, Any], firstEventType:String, fieldUsed:AssetEventField.AssetEventField) : Long ={
   if (firstEventType != AssetEvent.AssetStartOverEvent.toString){
		  getEventFieldNumeric(event, fieldUsed).get.toLong
		}
		else if (getEventFieldString(event,AssetEventField.assetType)=="VodAsset"){
		  0
		}
		else{
		  getEventFieldNumeric(event, AssetEventField.programStartTime).get.toLong
		}
}

def getEndTime(event:Map[String, Any], secondEventType:String, fieldUsed:AssetEventField.AssetEventField) : Long ={
   if (secondEventType != AssetEvent.AssetStartOverEvent.toString || getEventFieldString(event,AssetEventField.assetType)=="VodAsset"){
		  getEventFieldNumeric(event, fieldUsed).get.toLong
		}
		else{
		  getEventFieldNumeric(event, AssetEventField.programStartTime).get.toLong + 
		  getEventFieldNumeric(event, fieldUsed).get.toLong
		}
}

def getSection(	firstEvent:Map[String, Any], 
				firstEventType:String, 
				firstFieldUsed:AssetEventField.AssetEventField, 
				secondEvent:Map[String, Any], 
				secondEventType:String, 
				secondFieldUsed:AssetEventField.AssetEventField
				) : List[Tuple5[String, String, Long, Long, String]]={

	val startTime = getStartTime(firstEvent,firstEventType,firstFieldUsed)
	val endTime = getEndTime(secondEvent,secondEventType, secondFieldUsed)
	val startDeviceTime = getEventFieldNumeric(firstEvent, AssetEventField.deviceTimestamp).get.toLong
	val endDeviceTime = getEventFieldNumeric(secondEvent, AssetEventField.deviceTimestamp).get.toLong
	val programEndTime = getEventFieldNumeric(firstEvent, AssetEventField.programEndTime).get.toLong
	val splitType = "SplitPoint"
	if (!(startDeviceTime <= programEndTime &&  programEndTime < endDeviceTime)){
	  List(
			  (firstEventType,
			   secondEventType,   
			   startTime,
			   endTime,
			   getEventFieldString(firstEvent,AssetEventField.liveTuneType)
			   )
	      )
	} else{
		val splitTime = startTime + (programEndTime -startDeviceTime )
		 	List((firstEventType,
			   splitType,   
			   startTime,
			   splitTime,
			   getEventFieldString(firstEvent,AssetEventField.liveTuneType)
			   )
			   ):::
			   List((splitType,
			   secondEventType,   
			   splitTime,
			   endTime,
			   "Catch-up"
			   )
			   )
	}
}

def getDurationFieldValue(event: Map[String, Any], assetField:AssetEventField.AssetEventField) : Long = {
  getEventFieldNumeric(event, assetField) match {
        case Some(x) => x.toLong
        case None => -1L
      }
}

def getAssetFieldValue(event: Map[String, Any], eventType:String, assetField:AssetEventField.AssetEventField, sectionType:String) : Long = {
  if (eventType != "AssetStartOverEvent") 
	  getDurationFieldValue(event, assetField) 
  else{
    if(getEventFieldString(event, AssetEventField.assetType) == "VodAsset"){
      if(sectionType == "end")
        getDurationFieldValue(event, assetField) 
      else 0L
    }
    else{
      val programStart = getDurationFieldValue(event, AssetEventField.programStartTime)
      if (programStart == -1L) -1L
      else
      if(sectionType == "end"){
        val startOverDistance = getDurationFieldValue(event, assetField) 
        if (startOverDistance == -1L) -1L
        else
        	programStart + startOverDistance
      }
      else
        programStart
    }
  }
}

def calcSectionDuration(startEvent: Map[String, Any], 
						endEvent: Map[String, Any]
						): Tuple7[Option[String], Option[String], Boolean, Long, Long, Long, String] = {
  val startEventType = getSchemaProperty(startEvent, SchemaProperty.name)
  val endEventType =  getSchemaProperty(endEvent, SchemaProperty.name)
  val calcFields = DuractionCalcMatrix.findSectionStartEnd(startEventType, endEventType)
  val defaultRecord = 
    	(Option(startEventType), 
		  Option(endEventType), 
		  false, 
		  -1L, 
		  -1L, 
		  -1L, 
		  getEventFieldString(startEvent,AssetEventField.liveTuneType)
		 )
  if ((calcFields._1 == None) || (calcFields._2 == None)){
	  if(DuractionCalcMatrix.isOnExceptionList(startEventType, endEventType))
	    (Option(startEventType), 
		  Option(endEventType), 
		  false, 
		  -1L, 
		  -1L, 
		  0L, 
		  getEventFieldString(startEvent,AssetEventField.liveTuneType)
		 )
		 else
		 defaultRecord
   }
  else {
    val startFieldName : AssetEventField.AssetEventField = calcFields._1 match {
    		case Some(x:AssetEventField.AssetEventField) => x 
    		case None => AssetEventField.deviceTimestamp
    };
    val endFieldName = calcFields._2 match {
    		case Some(x:AssetEventField.AssetEventField) => x 
    		case None => AssetEventField.deviceTimestamp
    };
    val startEventTime = getAssetFieldValue(startEvent, startEventType, startFieldName, "start")
    val endEventTime = getAssetFieldValue(endEvent, endEventType, endFieldName, "end")
    if((startEventTime > endEventTime) || (startEventTime == -1L ) || (endEventTime == -1L))
      defaultRecord
    else {
    	(Option(startEventType), 
		 Option(endEventType), 
		 true, 
		 startEventTime, 
		 endEventTime, 
		 endEventTime-startEventTime, 
		 getEventFieldString(startEvent,AssetEventField.liveTuneType)
		)
    }
  }  
}

def buildSection(events: List[Map[String, Any]], 
				 prevEvent: Map[String, Any], 
				 rsltList:List[Tuple7[Option[String], 
				                      Option[String], 
				                      Boolean, 
				                      Long, 
				                      Long, 
				                      Long, 
				                      String]
				                ]
                   ) 
			    :List[Tuple7[Option[String], Option[String], Boolean, Long, Long, Long, String]] = {
  val curEvent = events.head
  val newSection = calcSectionDuration(prevEvent,curEvent)	
  if((newSection._3 == false && 
      newSection._6 == -1L))
    List(newSection)
   else if (events.tail.isEmpty){
	   if(newSection._3)
		   rsltList::: List(newSection)
	    else
	       rsltList	
   }
   else{
     if(newSection._3)
		   buildSection(events.tail, curEvent,rsltList::: List(newSection))
	    else
	       buildSection(events.tail, curEvent, rsltList)	
   }
}

def getTimeSections(events: List[Map[String, Any]]) : List[Tuple7[Option[String], Option[String], Boolean, Long, Long, Long, String]]  = {
	val firstEventType = getSchemaProperty(events(0), SchemaProperty.name)
	if (events.length == 1 || firstEventType != "AssetTunedEvent" ){
		List((Option(firstEventType), 
			  None, 
			  false, 
			  -1L, 
			  -1L, 
			  -1L, 
			  getEventFieldString(events(0),AssetEventField.liveTuneType)
			)
			)
		}
		else
		{
			val secondEventType = getSchemaProperty(events(0), SchemaProperty.name)
			buildSection(events.tail, events.head, List())
		}
}
def getDurationSections(events: List[Map[String, Any]],  durationSections: List[Tuple5[String, String, Long, Long, String]]) : List[Tuple5[String, String, Long, Long, String]]  = {
	if (events.isEmpty) return durationSections

	val firstEvent = events(0)

	if (events.length > 1) {
	    val nextEvent = events(1)
	    val firstEventType = getSchemaProperty(firstEvent, SchemaProperty.name)
	    val secondEventType = getSchemaProperty(nextEvent, SchemaProperty.name)
		val processInstr = durationCalcMatrix((firstEventType, secondEventType))
		if(processInstr._1 == AssetEventProcess.Process){
			getDurationSections(
			    			events.tail,
			    			durationSections:::
			    			getSection(events(0),firstEventType,processInstr._2,events(1),secondEventType, processInstr._3)
			       )
			  }
			
	    else if (processInstr._1 == AssetEventProcess.Discard){
				getDurationSections(events.tail,
			    durationSections
			   )
		}
		else if (processInstr._1 == AssetEventProcess.DiscardFirst){
				getDurationSections(events.tail,
			    durationSections:::
			     List((firstEventType,
			    	   secondEventType,
			    	   -1L,
			    	   -1L,
			    	   getEventFieldString(events(0),AssetEventField.liveTuneType)
			    		)
			        )
			   )
		}
		else if (processInstr._1 == AssetEventProcess.DiscardSecond)
		{
				getDurationSections(List(events(0)):::events.tail.tail,
			    durationSections:::
			     List((firstEventType,
			    	   secondEventType,
			    	   -1L,
			    	   -1L,
			    	   getEventFieldString(events(0),AssetEventField.liveTuneType)
			    		)
			        )
			   )
		}
		else {
			durationSections:::
			     List((firstEventType,
			    	   secondEventType,
			    	   -2L,
			    	   -2L,
			    	   getEventFieldString(events(0),AssetEventField.liveTuneType)
			    		)
			        )
		}
	}
	else{
		val processInstr = durationCalcMatrix((getSchemaProperty(events(0), SchemaProperty.name),
								                 null))
		val firstEventType = getSchemaProperty(events(0), SchemaProperty.name)
		if(processInstr._1 == AssetEventProcess.OpenEnd){
			durationSections:::
			    				List(
			    					 (firstEventType,
			    					  null,   
			    					  getStartTime(events(0),firstEventType,processInstr._2),
			    					  -1L,
			    					  getEventFieldString(events(0),AssetEventField.liveTuneType)
			    					 )
			                         )
		}else {
		 if(durationSections.length == 0){
		   durationSections:::
			    				List(
			    					 (
			    					  null,
			    					  firstEventType,
			    					  -1L,
			    					  getEndTime(events(0),firstEventType, processInstr._2),
			    					  getEventFieldString(events(0),AssetEventField.liveTuneType)
			    					 )
			                         )
		 }
		 else
			 durationSections 
		}					        
	}
}


/*
 * Name:  	getSortedSection
 * Inputs:
 *     		eventList: a list of asset events for a given asset session id, sorted by device time stamps
 * Output: 	a list of continuous sections (see  sumVideoStreamDistinctDuration for the definition of section)
 * Logic:   Step1: find starting points of continuous sections.
 *          Step2: find ending points of continuous sections.
 *          Step3: Call findSection to construct continuous sections by linking start and end points
 * Assumptions:
 *          1) Input is a list of Asset Events sorted by device times
 *          2) These asset events are within the same asset session id & device id 
 */
def getSortedSection(eventList: List[Map[String, Any]]) : List[Tuple3[Long, Long, String]]  = {   
  val events = 
    /* for now, we only process the following events which will impact durations.
     * We may eventually consider other events, such as AsseStillTune
     */
	  eventList.filter(p => (getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTuneResumeEvent.toString ||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTunePauseEvent.toString ||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTunedEnd.toString||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTunedEvent.toString||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetStartOverEvent.toString ||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTuneRewindEvent.toString||
                             getSchemaProperty(p, SchemaProperty.name) == AssetEvent.AssetTuneForwardEvent.toString
                             )
                      )
  if(isValidAssetTunedEvent(eventList)) 
    findSection(
    (/* 1st step is to figure out all the start points of these sections.  Very important that inputs to this 
      * method is sorted by device date time and assume no 2 events will have same device time stamp for a give asset session id
      * if they are the same, assume the code has some kind of priority logic
     */
     for (event <- events) yield {
        (getEventFieldNumeric(event, AssetEventField.deviceTimestamp).get.toLong,
         getEventTime(getEventFieldString(event,AssetEventField.assetType), event("name").toString, event, "start"),
         getEventFieldString(event,AssetEventField.liveTuneType),
         getEventFieldNumeric(event, AssetEventField.programEndTime).get.toLong
        )
      }
     ).filter(x => x._2 != -1),
	 (
       /*2nd step is to figure out the end point of these sections
        */
       for (event <- events) yield {
         (getEventFieldNumeric(event, AssetEventField.deviceTimestamp).get.toLong,
          getEventTime(getEventFieldString(event,AssetEventField.assetType), event("name").toString, event, "end"),
		  getEventFieldString(event,AssetEventField.liveTuneType),
		  getEventFieldNumeric(event, AssetEventField.programEndTime).get.toLong
          )
       }
      /*filter out records which are no needed.  For example, AssetTunedEnd is not used for providing data for the start point of each section
       */
      ).filter(x => x._2 != -1),
	  List()
   ).sortBy(f => f._1)
  else
    /*if data violate contracts, zero out records
     */
    List((0, 0, getEventFieldString(eventList(0),AssetEventField.liveTuneType)))
}

/*
 * Name: sumVideoStreamDuration
 * Inputs: 	
 * 		sectionList: See the detail description under sumVideoStreamDistinctDuration.  Same definition
 *      cSum:		 Holds the cumulative sum of durations from each sections which includes repeated watching times caused by rewind, start over etc.
 *  Outputs: Total sum of durations of sections which includes repeated watching times caused by rewind, start over etc.
 */
def sumVideoStreamDuration(sectionList:List[Tuple3[Long, Long, String]], cSum:Long) : Long = {
        if(sectionList.isEmpty) return cSum
        else
          sumVideoStreamDuration(sectionList.tail, cSum + (sectionList(0)._2 - sectionList(0)._1))
      }

/*
 * Name: sumVideoStreamDistinctDuration
 * Inputs:
 * 		sectionList: A list of sections sorted by the times of the starting points.  A section is a continuous time when a user watches TV program @ normal speed.
 *                   For example, a user starts watch a program, an event, AssetTunedEvent's current watch point indicates the time within the program where user starts watching
 *                   When user presses fast forward, an event AssetTuneForwardEvent's trick start indicates the time within the program where the user stops watching TV using normal speed.
 *                   The 1st section is constructed using current watch point from AssetTunedEvent (starting point) and trick start from AssetTuneForwardEvent (ending point)
 *                   For detail logic on how each start and end points defined, see
 *                   https://onbrand.atlassian.net/wiki/display/RA/VideoConsumptionSummarizerJob#VideoConsumptionSummarizerJob-DurationLogic
 *                   sectionList_1: starting point of a section
 *                   sectionList_2: ending point of a section
 *                   sectionList_3: live tune type: On-Now, Catch Up or Time shift.
 *      cTime: 		 A variable which tracks of the latest end point time from the sections which have been just processed.  It is a temporary variable which is used to determine if we
 *             		 need to add all the duration, or portion of the duration or nothing from the next section which needs to be processed, depending the relative positions between 
 *                   cTime and the section which function is currently processing.             
 *      cSum:        This variable holds the distinct duration results.             
 * Output: Distinct duration, a total time users watch TV program minus repeated watching caused by Rewind, Start over etc.     
 * Assumption: sectionList has to be sorted by the times of the starting points.                   
 * Logic does left to right sweep and calculate distinct durations and add duration depending positions of start, end points and cTime
*/
def sumVideoStreamDistinctDuration(sectionList:List[Tuple3[Long, Long, String]], cTime:Long, cSum:Long) : Long = {
        if(sectionList.isEmpty) return cSum
        if(cTime <= sectionList(0)._1)
          sumVideoStreamDistinctDuration(sectionList.tail, sectionList(0)._2, cSum + (sectionList(0)._2 - sectionList(0)._1))
        else if(cTime < sectionList(0)._2)
          sumVideoStreamDistinctDuration(sectionList.tail, sectionList(0)._2, cSum + (sectionList(0)._2 - cTime))
        else
          sumVideoStreamDistinctDuration(sectionList.tail, cTime, cSum)
}

  
