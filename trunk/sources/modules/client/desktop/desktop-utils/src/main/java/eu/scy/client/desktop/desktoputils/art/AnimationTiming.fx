/*
 * AnimationTiming.fx
 *
 * Created on 20-apr-2010, 14:50:41
 */

package eu.scy.client.desktop.desktoputils.art;

/**
 * @author sikken
 */

def startShowTime = 500ms;
def appearTime = 500ms;
def showTime = 5s;
def disappearTime = 1000ms;

public def startAppearingTime = startShowTime;
public def fullAppearingTime = startShowTime + appearTime;
public def startDisappearingTime = startShowTime + appearTime + showTime;
public def fullDisappearingTime = startShowTime + appearTime + showTime + disappearTime;

public def drawerOpenCloseDuration = 250ms;
