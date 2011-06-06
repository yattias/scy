/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor.viewer.element;
import javafx.scene.text.Text;
import eu.scy.client.tools.fxformauthor.datamodel.FormDataElement;
import java.lang.System;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.Exception;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

/**
 * @author pg
 */

public class ElementViewAudio extends IFormViewElement, AbstractElementView {
    postinit {
        //loadFormElement();
        //this.translateX = -2;
    }
    override public function loadFormElement (fde : FormDataElement) : Void {
        if(fde != null) {
            this.fde = fde;
            loadFormElement();
        }
    }


   override  function loadFormElement():Void {

        title = "Voice: {fde.getTitle()}";
        //display data..
        if(fde.getUsedCardinality() > 0) {
            for(i in [0..fde.getUsedCardinality()-1]) {
                println("load form element.. {i}");
                var data:Byte[] = fde.getStoredData(i);
                if(data != null) {
                    println("data != null.. loading AUDIOOOH");
                    //var path:String = "{System.getProperty("java.io.tmpdir")}SCYFormSound{System.currentTimeMillis()}.scy";
                    var path:String = "{System.getProperty("java.io.tmpdir")}SCYFormSound{System.currentTimeMillis()}.scysound";

                    //path = path.replaceAll( "\\\\", "/" );
                    //println(path);
                    var error:Boolean = false;
                    try {
                        var file:File = new File(path);
                        var fos:FileOutputStream = new FileOutputStream(file);
                        fos.write(data);
                        fos.close();
                    }
                    catch(e:Exception) {
                        error = true;
                    }
                    if(not error) {
                        var pathFile = new File(path);
                        println(path);
                        println(pathFile.toURI());
                        var soundfile = null;
                        var player = MediaPlayer{};

                        var playBT:Button = Button {
                            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/control_play_blue.png" } }
                            tooltip: Tooltip { text: "play sound" }
                            action: function():Void {
                                if(not (soundfile == null))
                                    player.play();
                            }
                        }
                        var pauseBT:Button = Button {
                            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/control_pause_blue.png" } }
                            tooltip: Tooltip { text: "pause sound" }
                            action: function():Void {
                                if(not (soundfile == null))
                                    player.pause();
                            }
                        }
                        var stopBT:Button = Button {
                            graphic: ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/control_stop_blue.png" } }
                            tooltip: Tooltip { text: "stop sound" }
                            action: function():Void {
                                if(not (soundfile == null))
                                    player.stop();
                            }
                        }
                        var durationText = Text {
                            content: "Duration: 0s"
                            font: Font { size: 14 }
                        }

                        try {
                            soundfile = Media {
                                source: "{pathFile.toURI()}"
                            }
                            player = MediaPlayer {
                                media: soundfile
                            }
                            durationText.content = "Duration: {soundfile.duration.toSeconds()}s";
                        }
                        catch(e:Exception) {
                            //coud not load data -> cannot has codec
                            playBT.graphic = ImageView{ image: Image { url: "{__DIR__.substring(0, __DIR__.length()-15)}resources/control_cancel_blue.png" } };
                            playBT.tooltip.text = "Error: missing codec. Please install a codec package";
                            durationText.content = "Error: Mising Codec. Please Install A codec Package And Try Again";
                        }


                        itemList.add(HBox {
                                content: [playBT, pauseBT, stopBT, durationText]
                                spacing: 2.0;
                            });
                        //into dataDisplay;
                    }
                }
                else {
                    itemList.add(Text { content: "No Data Found. Entry #{i}"; });

                }
            }
        }
        if(itemList.size() == 0) {
            itemList.add(Text { font: defaultErrorFont; fill: Color.RED; content: "No Data Found." });
        }

    }
}