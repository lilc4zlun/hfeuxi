//The following function finds video view sections with normal speed
//In Time-Shift mode, it finds if any section which intersects with program end date time.  If it does, that
//section will be split into Time-Shift section and Catch-up section.


def findSection(startList:List[Tuple4[Long, Long, String, Long]], endList:List[Tuple4[Long, Long, String, Long]], runMode:String, returnList:List[Tuple3[Long, Long, String]]): List[Tuple3[Long, Long, String]] = {
  if(startList.isEmpty) return returnList
  if(endList.isEmpty) return returnList
//if the start point device time bigger than end point device time, skip.
//This shouldn't happen since the records were pre-sorted unless some records missing.
  if(startList(0)._1 < endList(0)._1){
//If the start point device tm < program end tm and end point 
//device tm > program end tm and it runs under time-shift mode, that 
//section needs to be splited 
       if((startList(0)._1  < startList(0)._4) && 
          (endList(0)._1 > startList(0)._4) &&
           runMode == "Time-Shift"
         ){
	  findSection( 
		startList.drop(1),
            	endList.drop(1),
                runMode,
            	returnList :::
//The left splitted section will be on time-shift.  The end point = start point + the time
//difference between time when program is ended and the device time which triggers the start
//of the section.  For example, if a tv live, from 8 to 9 and user into time shift mode (press
//resume @ 8:20 device time).  The time sift duration will be from 8:20 to 9:00.
//Use device tm is as close as we can get to approx timeshift/catch up split
			List((	startList(0)._2,
              			startList(0)._2  + (startList(0)._4 - startList(0)._1),
				"Time-Shift")
                            ) 
			:::
//the other part will be catch up
			List(	(startList(0)._2 + (startList(0)._4 - startList(0)._1),
              			endList(0)._2,
	      			"Catch-up")
		     	     )	 
            		
		      )
         }
    	else{
//Sometimes, for example, many fast forwards, possible the tricker start of 1 FF is a couple mil sec earlier than
//previous FF tricker end. Make them equal in this case.  In other case, if a very bad data, it will
//hide the bad data and not skew the report.  We have other DQ report which will revealthe issue.
	    if ((endList(0)._2 - startList(0)._2) < 0){
		findSection( startList.drop(1),
             		 endList.drop(1),
                         runMode,
            		 returnList :::
                               List((endList(0)._2,
              		             endList(0)._2,
	      		             startList(0)._3
	                              ))      
                        )
	    }
            else{
    	    findSection( startList.drop(1),
             		 endList.drop(1),
                         runMode,
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
       		findSection(startList, endList.drop(1), runMode, returnList)
	}
}


//The following method is to determine the start and end of a continuous normal speed view section
def getEventTime(eventType:String, event:Map[String, Any], pointType:String, runMode:String) : Long = eventType match 
{

//For rewind and forward events, the tricker start is the end of a normal speed section
//tricker end is the start of the next normal speed section -- Lilun

  case "AssetTuneRewindEvent" | "AssetTuneForwardEvent" => 
	(if(pointType == "start") 
		event("eventData").asInstanceOf[Map[String, String]]("trickEndPoint").toLong
         else 
	     	event("eventData").asInstanceOf[Map[String, String]]("trickStartPoint").toLong   
         )

//For startover event, if linear asset, the program start time is the start of the next normal speed section.
//For Vod, it will be 0
//For LinearAsset, the program start time + the startOverPoint is where the end of the previous normal speed section
//For Vod, the startoverpoint is the end of the previous normal speed section - Lilun

  case "AssetStartOverEvent" =>
        (if ((event("eventData").asInstanceOf[Map[String, String]]("assetType") == "LinearAsset") && (pointType == "start"))
	     	event("eventData").asInstanceOf[Map[String, String]]("programStartTime").toLong 
         else if
            ((event("eventData").asInstanceOf[Map[String, String]]("assetType") == "LinearAsset") && (pointType == "end")) 
	     event("eventData").asInstanceOf[Map[String, String]]("startOverPoint").toLong + 
	     event("eventData").asInstanceOf[Map[String, String]]("programStartTime").toLong
	else if
	    ((event("eventData").asInstanceOf[Map[String, String]]("assetType") == "VodAsset") && (pointType == "start")) 
		0.toLong
         else
	    event("eventData").asInstanceOf[Map[String, String]]("startOverPoint").toLong           
	)

//AssetTunedEvent's currentWatchPoint is the start of the normal play speed section. 
//Has nothing to do with the end of a normal speed section -- Lilun

  case "AssetTunedEvent" =>  
	(if(pointType == "start") 
		event("eventData").asInstanceOf[Map[String, String]]("currentWatchPoint").toLong
         else 
	     	-1.toLong  
         )                                                                                   
   
//AssetTunedEnd's currentWatchpoint is the end of a normal play speed section.
//Has nothing to do with the start of a section -- Lilun
 		
  case "AssetTunedEnd" =>  
	(if(pointType == "end") 
		event("eventData").asInstanceOf[Map[String, String]]("currentWatchPoint").toLong
         else 
	     	-1.toLong  
         )    

//AssetTuneResumeEvent's startPoint is the start point of a normal play speed section
//It is not needed for regular Distinct duraration calculation.  Due to possible
//introducing more bad data, exclude from normal distinct/total duration calculation
//(if data is good, it won't hurt to calculate, but no need)
//But it is used to calculate on-now, catchup and time-shift durations -- Lilun

  case "AssetTuneResumeEvent" =>
	(if((pointType == "start") && (runMode == "Time-Shift"))
		event("eventData").asInstanceOf[Map[String, String]]("startPoint").toLong
         else 
	     	-1.toLong  
         )
//AssetTunePauseEvent's stopPoint is the end point of a normal play speed section
//It is not needed for regular Distinct duraration calculation.  Due to possible
//introducing more bad data, exclude from normal distinct/total duration calculation
//(if data is good, it won't hurt to calculate, but, no need)
//But it is used to calculate on-now, catchup and time-shift durations -- Lilun

  case "AssetTunePauseEvent" =>
	(if((pointType == "end") && (runMode == "Time-Shift"))
		event("eventData").asInstanceOf[Map[String, String]]("stopPoint").toLong
         else 
	     	-1.toLong  
         )
					   
  case _ => -1.toLong
}

def getSortedSection(events: List[Map[String, Any]], runMode:String) : List[Tuple3[Long, Long, String]]  = {   
 findSection(
	(

            for (event <- events) yield {
                    (event("eventData").asInstanceOf[Map[String, String]]("deviceTimestamp").toLong,
                     getEventTime(event("name").toString, event, "start", runMode),
		     event("eventData").asInstanceOf[Map[String, String]]("liveTuneType"),
		     event("eventData").asInstanceOf[Map[String, String]]("programEndTime").toLong
                    )
              }

         ).filter(x => x._2 != -1),
	(

            for (event <- events) yield {
                    (event("eventData").asInstanceOf[Map[String, String]]("deviceTimestamp").toLong,
                     getEventTime(event("name").toString, event, "end", runMode),
		     event("eventData").asInstanceOf[Map[String, String]]("liveTuneType"),
		     event("eventData").asInstanceOf[Map[String, String]]("programEndTime").toLong
                    )
              }

         ).filter(x => x._2 != -1),
         runMode,
	List()
 )
}


def sumVideoStreamDuration(sectionList:List[Tuple3[Long, Long, String]], cSum:Long) : Long = {
        if(sectionList.isEmpty) return cSum
        else
          sumVideoStreamDuration(sectionList.drop(1), cSum + (sectionList(0)._2 - sectionList(0)._1))
      }


def sumVideoStreamDistinctDuration(sectionList:List[Tuple3[Long, Long, String]], cTime:Long, cSum:Long) : Long = {
        if(sectionList.isEmpty) return cSum
        if(cTime <= sectionList(0)._1)
          sumVideoStreamDistinctDuration(sectionList.drop(1), sectionList(0)._2, cSum + (sectionList(0)._2 - sectionList(0)._1))
        else if((sectionList(0)._1 < cTime) &&  (cTime < sectionList(0)._2))
          sumVideoStreamDistinctDuration(sectionList.drop(1), sectionList(0)._2, cSum + (sectionList(0)._2 - cTime))
        else
          sumVideoStreamDistinctDuration(sectionList.drop(1), cTime, cSum)
}





