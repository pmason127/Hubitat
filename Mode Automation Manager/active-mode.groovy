/*

 */

definition(
    name: "Active Mode",
    parent: "pmason:Mode Automation Manager",
    namespace: "pmason",
    author: "Patrick Mason",
    description: "Automation For Leaving the House",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences
{
    page(name: "mainPage1")
}

def mainPage1()
{
    dynamicPage(name: "mainPage1", title: "Mode Settings", install: true, uninstall: true)
    {
        
        section
        {
            input name:	"currentMode", type: "mode", title: "Mode", multiple: false, required: true
            
            input name:	"locksToLock", type: "capability.lock", title: "Lock These Locks", multiple: true, required: false
            input name:	"locksToUnLock", type: "capability.lock", title: "Unlock Locks", multiple: true, required: false
            input name:	"turnOffSwitches", type: "capability.switch", title: "Turn Off Switches", multiple: true, required: false
            input name:	"turnOnSwitches", type:	"capability.switch", title: "Turn Off Switches", multiple: true, required: false
        }
        
        section
        {
            input name:	"enableLogging", type: "bool", title: "Enable Debug Logging?", defaultValue: true, required: true
        }
        
    }
}

def logDebug(msg)
{
    if(enableLogging)
    {
        log.debug "${msg}"
    }
}

def installed()
{
    logDebug("installed()")    
    
    initialize()
}

def updated()
{
    logDebug("updated()")
      if (currentMode) {
		app.updateLabel(currentMode)
    }
    installed()
}

def initialize()
{
    logDebug("initialize()")
    unsubscribe()
     if (currentMode) {
		            app.updateLabel(currentMode)
            }
    subscribeToEvents()
}

def uninstalled()
{
    logDebug("uninstalled()")
    unsubscribe()
}

def subscribeToEvents()
{
    logDebug("subscribeToEvents()")
    subscribe(location, "mode", modeHandler)
    
}
def modeHandler(evt) {
	log.info "Got mode change: ${evt.value}"
	if (evt.value == currentMode) {
        
        if(locksToLock)
        {
            locksToLock.each
            {
                it.lock()
            }
        }
        if(locksToUnLock)
        {
            locksToUnLock.each
            {
                it.unlock()
            }
        }
        if(turnOnSwitches)
        {
            turnOnSwitches.each
            {
                it.on()
            }
        }
        if(turnOffSwitches)
        {
            turnOffSwitches.each
            {
                it.off()
            }
        }
    }
		
}
