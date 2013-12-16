val  durationCalcMatrix = 
		 Map(
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTunedEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTunePauseEvent) -> ("use", AssetEventField.currentWatchPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTuneForwardEvent) -> ("use", AssetEventField.currentWatchPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTuneRewindEvent) -> ("use", AssetEventField.currentWatchPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetStartOverEvent) -> ("use", AssetEventField.currentWatchPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTunedEvent, AssetEvent.AssetTunedEnd) -> ("use", AssetEventField.currentWatchPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTunedEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTunedEvent) -> ("invalidate", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTunePauseEvent) -> ("discard", AssetEventField.stopPoint, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTuneForwardEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTuneRewindEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetStartOverEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, AssetEvent.AssetTunedEnd) -> ("discard", null, null),
				 (AssetEvent.AssetTunePauseEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTunedEvent) -> ("invalidate", null, null),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTunePauseEvent) -> ("use", AssetEventField.startPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", AssetEventField.startPoint, null),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTuneForwardEvent) -> ("use", AssetEventField.startPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTuneRewindEvent) -> ("use", AssetEventField.startPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetStartOverEvent) -> ("use", AssetEventField.startPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneResumeEvent, AssetEvent.AssetTunedEnd) -> ("use", AssetEventField.startPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneResumeEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTunedEvent) -> ("discard", AssetEventField.trickStartPoint, null),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTunePauseEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", AssetEventField.trickEndPoint, null),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTuneForwardEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTuneRewindEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetStartOverEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneForwardEvent, AssetEvent.AssetTunedEnd) -> ("use", AssetEventField.trickEndPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneForwardEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTunedEvent) -> ("discard", AssetEventField.trickStartPoint, null),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTunePauseEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.stopPoint),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", AssetEventField.trickEndPoint,  null),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTuneForwardEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTuneRewindEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetStartOverEvent) -> ("use", AssetEventField.trickEndPoint, AssetEventField.startOverPoint),
				 (AssetEvent.AssetTuneRewindEvent, AssetEvent.AssetTunedEnd) -> ("use", AssetEventField.trickEndPoint, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetTuneRewindEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTunedEvent) -> ("discard", AssetEventField.programStartTime, null),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTunePauseEvent) -> ("use", AssetEventField.programStartTime, AssetEventField.stopPoint),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTuneResumeEvent) -> ("discard", AssetEventField.programStartTime, null),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTuneForwardEvent) -> ("use", AssetEventField.programStartTime, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTuneRewindEvent) -> ("use", AssetEventField.programStartTime, AssetEventField.trickStartPoint),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetStartOverEvent) -> ("use", AssetEventField.programStartTime, AssetEventField.startOverPoint),
				 (AssetEvent.AssetStartOverEvent, AssetEvent.AssetTunedEnd) -> ("use", AssetEventField.programStartTime, AssetEventField.currentWatchPoint),
				 (AssetEvent.AssetStartOverEvent, null) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTunedEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTunePauseEvent) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTuneResumeEvent) -> ("discard",null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTuneForwardEvent) -> ("discard",null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTuneRewindEvent) -> ("discard",null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetStartOverEvent) -> ("discard",null, null),
				 (AssetEvent.AssetTunedEnd, AssetEvent.AssetTunedEnd) -> ("discard", null, null),
				 (AssetEvent.AssetTunedEnd, null) -> ("discard", null, null)
   )