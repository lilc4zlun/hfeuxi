/**** Basic time shift duration test case 1  ****

val m1 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "10000", "currentWatchPoint" -> "10000", "liveTuneType" -> "On-now", "programEndTime" -> "15000")
val m2 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "11000", "stopPoint" -> "11000", "liveTuneType" -> "On-now", "programEndTime" -> "15000")
val m3 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "12000", "startPoint" -> "11000", "liveTuneType" -> "Time-Shift", "programEndTime" -> "15000")
val m4 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "16000", "currentWatchPoint" -> "15000", "liveTuneType" -> "Catchup", "programEndTime" -> "15000")

val  events_simple_timesift_case_1 = List(
                    	Map( "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEvent", 
                                             "event.version" -> "0.0.4", 
                                             "eventData" -> m1
                                  ),
               		Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunePauseEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m2
                                 ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneResumeEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m3
                                 ),

              		Map(       "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEnd", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m4
                           )
                 
                       )
*****/

/******************* Expected results
Sections -> List((10000,11000,On-now), (11000,14000,Time-Shift), (14000,15000,Catch-up))

Time-shift duration -> 3000

Catch-up duration -> 1000

On-now duration -> 1000

Total stream duration -> 5000

Total distinct stream duration -> 5000
************************************************************************************************************************************************************/



/**** Basic time shift duration test case 2 ***/
val m1 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "10000", "currentWatchPoint" -> "10000", "liveTuneType" -> "On-now", "programEndTime" -> "25000", "assetType" -> "LinearAsset")
val m2 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "11000", "stopPoint" -> "11000", "liveTuneType" -> "On-now", "programEndTime" -> "25000")
val m3 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "26000", "startPoint" -> "11000", "liveTuneType" -> "Catch-up", "programEndTime" -> "25000")
//val m4 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "12500", "trickStartPoint" -> "11500", "trickEndPoint" -> "16000", "liveTuneType" -> "On-now", "programEndTime" -> "25000")
val m5 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "27000", "currentWatchPoint" -> "25000", "liveTuneType" -> "Catch-up", "programEndTime" -> "25000")

val  events_simple_timesift_case_1 = List(
                    	Map( "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEvent", 
                                             "event.version" -> "0.0.4", 
                                             "eventData" -> m1
                                  ),
               		Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunePauseEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m2
                                 ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneResumeEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m3
                                 ),

              		Map(       "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEnd", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m5
                           )
                 
                       )
/*****/
/*****
val m1 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "10000", "currentWatchPoint" -> "10000", "liveTuneType" -> "On-now", "programEndTime" -> "25000")
val m2 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "11000", "stopPoint" -> "11000", "liveTuneType" -> "On-now", "programEndTime" -> "25000")
val m3 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "12000", "startPoint" -> "11000", "liveTuneType" -> "Time-Shift", "programEndTime" -> "25000")
val m4 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "12500", "trickStartPoint" -> "11500", "trickEndPoint" -> "16000", "liveTuneType" -> "On-now", "programEndTime" -> "25000")
val m5 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "19500", "trickStartPoint" -> "17000", "trickEndPoint" -> "14000", "liveTuneType" -> "Time-Shift", "programEndTime" -> "25000")
val m6 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "28000", "currentWatchPoint" -> "25000", "liveTuneType" -> "Catchup", "programEndTime" -> "25000")

val  events_simple_timesift_case_1 = List(
                    	Map( "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEvent", 
                                             "event.version" -> "0.0.4", 
                                             "eventData" -> m1
                                  ),
               		Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunePauseEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m2
                                 ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneResumeEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m3
                                 ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneForwardEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m4
                                 ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneRewindEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m5
                                 ),

              		Map(       "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEnd", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m6
                           )
                 
                       )
****/

/****
val m1 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "10000", "currentWatchPoint" -> "1384718388000", "liveTuneType" -> "Catch-up", "programEndTime" -> "1384718400000")
val m2 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "12500", "trickStartPoint" -> "1384718381000", "trickEndPoint" -> "1384718374000", "liveTuneType" -> "Catch-up", "programEndTime" -> "1384718400000")
val m3 = Map("profileId" -> "26386", "ip" -> "99.111.73.147", "deviceTimestamp" -> "28000", "currentWatchPoint" -> "1384718401000", "liveTuneType" -> "Catchup", "programEndTime" -> "1384718400000")

val  events_simple_timesift_case_1 = List(
                    	Map( "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEvent", 
                                             "event.version" -> "0.0.4", 
                                             "eventData" -> m1
                                  ),
                        Map(      "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTuneRewindEvent", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m2
                                 ),

              		Map(       "namespace" -> "intelmedia.ws.ceventsent", 
                                             "name" -> "AssetTunedEnd", 
                                             "event.version" -> "0.0.6", 
                                             "eventData" -> m3
                           )
                 
                       )

******/


val sortedSectionList =  getSortedSection(events_simple_timesift_case_1)
val tmp1 = sumVideoStreamDuration(sortedSectionList.filter(x => x._3 == "On-now"), 0).toLong
val tmp2 = sumVideoStreamDuration(sortedSectionList.filter(x => x._3 == "Time-Shift"), 0).toLong
val tmp3 = sumVideoStreamDuration(sortedSectionList.filter(x => x._3 == "Catch-up"), 0).toLong
val tmp4 = sumVideoStreamDuration(sortedSectionList, 0).toLong
val tmp5 = sumVideoStreamDistinctDuration(sortedSectionList, 0, 0).toLong
val tmp6 = getDurationSections(events_simple_timesift_case_1, List());
/****
println("\n*************************************************************************************************************************************")
println("\nSections -> "+tmp0)
println("\nTime-shift duration -> "+tmp1)
println("\nCatch-up duration -> "+tmp2)
println("\nOn-now duration -> "+tmp3)
println("\nTotal stream duration -> "+tmp4)
println("\nTotal distinct stream duration -> "+tmp5)
println("\n*************************************************************************************************************************************")
*****/

