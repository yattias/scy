/* 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * Copyright 2008 Sun Microsystems, Inc. All rights reserved. Use is subject to license terms. 
 * 
 * This file is available and licensed under the following license:
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *   * Neither the name of Sun Microsystems nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.scy.elobrowser.tool.displayshelf;
import eu.scy.scywindows.*;
import eu.scy.elobrowser.main.Roolo;
import java.awt.Dimension;
import javafx.animation.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.*;


public class DisplayShelf extends CustomNode {
    //public var content:ShelfItem[];
    public var content:Node[];
    public-init var spacing = 110;
    public-init var leftOffset = -50;
    public-init var rightOffset = 50;
    var center:Group = Group { };
    public-init var perspective = false;
    public-init var scaleSmall = 0.5;
	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()};

	function setScyWindowTitle(){
		if (scyWindow == null)  return;
		scyWindow.title = "Picture Browser";
		var eloUri = null;//colemoActionWrapper.getEloUri();
		if (eloUri != null)
			scyWindow.id = eloUri.toString()
		else
			scyWindow.id = "";
	}

    var leftContent:Node[];
    var left:Group = Group { };
    
    var rightContent:Node[];
    var right:Group = Group { };

    public var centerIndex = 0;
    
    override public function create():Node {
        var half = content.size()/2-1;

        left.content = content[0..half-2];
        center.content = content[half-1];
        right.content = content[half..content.size()-1];
        right.content = Sequences.<<reverse>>(right.content) as Node[];

        centerIndex = half-1;

        doLayout();
        return Group {
            content: [
                left,
                right,
                center
            ]
        }
    }
    
    public function shift(offset:Integer):Void {
        if(centerIndex <= 0 and offset > 0 ) {
            return;
        }
        if(centerIndex >= content.size()-1 and offset < 0) {
            return;
        }


        centerIndex -= offset;
        left.content = content[0..centerIndex-1];
        center.content = content[centerIndex];
        right.content = Sequences.<<reverse>>(content[centerIndex+1..content.size()-1]) as Node[];
        doLayout();
    }

    function doLayout() {

        var startKeyframes:KeyFrame[];
        var endKeyframes:KeyFrame[];
        var duration = 0.5s;


        for(n in content) {
            var it = n as Item;
            insert KeyFrame { time: 0s values: [
                    n.translateX => n.translateX,
                    n.scaleX => n.scaleX,
                    n.scaleY => n.scaleY,
                    it.angle => it.angle,
                    ] } into startKeyframes;
        }

        for(n in left.content) {
            var it = n as Item;
            var newX = -left.content.size()*spacing +  spacing * indexof n + leftOffset;
            insert KeyFrame { time: duration values: [
                    n.translateX => newX,
                    n.scaleX => scaleSmall,
                    n.scaleY => scaleSmall,
                    it.angle => 45
                ] } into endKeyframes;
        }

        for(n in center.content) {
            var it = n as Item;
            insert KeyFrame { time: duration values: [
                    n.translateX => 0,
                    n.scaleX => 1.0,
                    n.scaleY => 1.0,
                    it.angle => 90
                ] } into endKeyframes;
        }

        for(n in right.content) {
            var it = n as Item;
            var newX = right.content.size()*spacing -spacing * indexof n + rightOffset;
            insert KeyFrame { time: duration values: [
                    n.translateX => newX,
                    n.scaleX => scaleSmall,
                    n.scaleY => scaleSmall,
                    it.angle => 135
                ] } into endKeyframes;
        }

        var anim = Timeline {
            keyFrames: [startKeyframes, endKeyframes]
        };
        anim.play();

    }

    public function shiftToCenter(item:Item):Void {
        for(n in left.content) {
            if(n == item) {
                var index = indexof n;
                var shiftAmount = left.content.size()-index;
                shift(shiftAmount);
                return;
            }
        }
        for(n in center.content) {
            if(n == item) {
                return;
            }
        }
        for(n in right.content) {
            if(n == item) {
                var index = indexof n;
                var shiftAmount = -(right.content.size()-index);
                shift(shiftAmount);
                return;
            }
        }
    }
}
    public function createDisplayShelf(roolo:Roolo):DisplayShelf{
        var images = [
            "IMG_0828.JPG",
            "IMG_0837.JPG",
            "IMG_0838.JPG",
            "IMG_0839.JPG",
            "SCY-Oslo 036.JPG",
            "SCY-Oslo 042.JPG",
            "SCY-Oslo 047.JPG",
            "SCY-Oslo 048.JPG",
            "SCY-Oslo 049.JPG",
            "SCY-Oslo 053.JPG",
            "SCY-Oslo 062.JPG",
            "SCY-Oslo 068.JPG",
            "SCY-Oslo 070.JPG",
            "SCY-Oslo 075.JPG",
            "SCY-Oslo 077.JPG"
        ];

        var half = images.size()/2;

        var shelf:DisplayShelf;
        shelf = DisplayShelf {
            spacing: 50
            scaleSmall: 0.7
            leftOffset: -110
            rightOffset: 110
            perspective: true
            content: bind for(i in images) {
                var item:Item = Item {
                    angle: 45
                    position: indexof i - half
                    image:Image { url: "{__DIR__}photos/{i}" }
                    shelf: bind shelf
                };
                item;
            }

            onKeyPressed:function(e:KeyEvent):Void {
                if(e.code == KeyCode.VK_LEFT) {
                    shelf.shift(1);
                }
                if(e.code == KeyCode.VK_RIGHT) {
                    shelf.shift(-1);
                }
            }
        }

        var width = 600;
        shelf.translateX = width/2 - 200/2;
        shelf.translateY = 50;


//        var stage:Stage = Stage {
//            title: "Display Shelf"
//            visible: true
//            resizable: false
//            style: StageStyle.UNDECORATED
//            scene: Scene {
//                content: [
//                    Rectangle {
//                        width: width
//                        height: 300
//                        fill: LinearGradient {
//                            startX: 0 startY: 0
//                            endX: 0 endY: 1
//                            proportional: true
//                            stops: [
//                                Stop { offset: 0.0 color: Color.rgb(150,150,150) }
//                                Stop { offset: 1.0 color: Color.rgb(30,30,30)},
//                            ]
//                        }
//                    },
//                    shelf,
//                ]
//            }
//        }

		return shelf
	}

	public function createScyDisplayShelf(roolo:Roolo):ScyWindow{
		return  createScyDisplayShelf(DisplayShelf.createDisplayShelf(roolo));
	}

	public function createScyDisplayShelf(dispShelf:DisplayShelf):ScyWindow{
        var dispWindow = ScyWindow{
			color:Color.YELLOWGREEN
			title:"Picture Display"
			scyContent: dispShelf
			minimumWidth:600;
			minimumHeight:300;
			width: 600;
			height: 300;
			cache:true;
      }
		dispShelf.scyWindow = dispWindow;
        dispWindow.width = 320;
		return dispWindow;
	}


