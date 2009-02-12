/**
 * This is an example of how to connect to and retrieve data from
 * flickr using HTTP requests 
 *
 */

/*
Copyright (c) 2007, Sun Microsystems, Inc.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
 * Neither the name of Sun Microsystems, Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.scy.example;

import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.*;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class FlickrExample extends MIDlet implements CommandListener, RequestListener {

    /* TODO: Get own API Key */
    private static final String API_KEY = "150a2b053df75a37d7173e56394c8bba";

//#if MD5 && CameraPhone && MMAPI
//#     /* TODO: Get own Shared Secret */
//#     private static final String SHARED_SECRET = "";
//#
//#     private static final String UPLOAD_BASE = "http://api.flickr.com/services/upload/";
//#
//#     /* TODO: Will be assigned when registering own API Key */
//#     private static final String AUTH_URL = "http://www.flickr.com/auth-";
//#
//#     private static final String STORE_NAME = "authToken";
//#
//#     private static final String GEOCODE_BASE = "http://api.local.yahoo.com/MapsService/V1/geocode";
//#
//#     /* TODO: Get own APP ID for Yahoo Maps Geocoder */
//#     private static final String GEOCODE_APPID = "";
//#endif

    private static final String WEB_API_BASE = "http://api.flickr.com/services/rest/";

    private static final boolean DEBUG = true;

    // the default is 100, too much for mobile
    private static final int INTERESTING_PHOTOS_PER_PAGE = DEBUG ? 8 : 10;

    private Random randomPage = new Random();

    private StringItem status = new StringItem(null, null);

	private static final class Photo {
        private String id;
        private String owner;
        private String secret;
        private String server;
        private String farm;
        private String title;
        private boolean ispublic;
    }
    private Photo[] photos;

    private int page = 1;
    private int maxPage = 1;

    private String token = null;
    private String perms = null;
    private String userid = null;
    private String username = null;

    private String errorCode = null;
    private String errorMessage = null;

//#if CameraPhone && MMAPI
//#     private Form captureForm;
//#     private Command photoCommand;
//#     private Player player;
//#     private VideoControl control;
//#     private byte[] photoBits;
//#     private Image photo;
//#     private String uploadedPhotoId;
//#endif

    public FlickrExample() {
        initialize();
    }

    private Form authenticate;
    private Command exitCommand;
    private Alert alert;
    private Command interestingCommand;
    private Form thumbnails;
    private Form image;
    private Command viewCommand;
    private Command backCommand;
    private Command nextCommand;
    private Command previousCommand;
    private Command loginCommand;
    private Command randomCommand;
    private TextField authCode1;
    private TextField authCode2;
    private TextField authCode3;
    private StringItem authCodeLabel;
    private Form start;
    private Command okCommand;
    private Form takePhoto;
    private Command takePhotoCommand;
    private Command postPhotoCommand;
    private TextField title;
    private TextField tags;
    private ChoiceGroup isPublicGroup;
    private Command authenticateCommand;
    private ChoiceGroup locationGroup;
    private Command itemCommand1;
    private Command clearAuthTokenCommand;



    /** Called by the system to indicate that a command has been invoked on a particular displayable.
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        // Insert global pre-action code here
        if (displayable == authenticate) {
            if (command == exitCommand) {
                // Insert pre-action code here
                exitMIDlet();
                // Insert post-action code here
            } else if (command == backCommand) {
                // Insert pre-action code here
                getDisplay().setCurrent(get_start());
                // Insert post-action code here
            } else if (command == authenticateCommand) {
                // Insert pre-action code here
//#if MD5 && CameraPhone && MMAPI
//#                 doAuthenticate();
//#endif
                // Do nothing
                // Insert post-action code here
            }
        } else if (displayable == image) {
            if (command == backCommand) {
                // Insert pre-action code here
                getDisplay().setCurrent(get_thumbnails());
                // Insert post-action code here
            } else if (command == exitCommand) {
                // Insert pre-action code here
                exitMIDlet();
                // Insert post-action code here
            }
        } else if (displayable == thumbnails) {
            if (command == backCommand) {
                // Insert pre-action code here
                getDisplay().setCurrent(get_start());
                // Insert post-action code here
            } else if (command == previousCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
                page = Math.max(1, --page);
                doGetInteresting();
            } else if (command == exitCommand) {
                // Insert pre-action code here
                exitMIDlet();
                // Insert post-action code here
            } else if (command == nextCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
                page = Math.min(maxPage, ++page);
                doGetInteresting();
            } else if (command == randomCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
                page = randomPage.nextInt(maxPage);
                doGetInteresting();
            }
        } else if (displayable == start) {
            if (command == loginCommand) {
                // Insert pre-action code here
//#if MD5 && CameraPhone && MMAPI
//#                 if (readAuthToken()) {
//#                     if (doValidateAuthToken()) {
//#                         getDisplay().setCurrent(get_takePhoto());
//#                         return;
//#                     }
//#                 }
//#                 alert(AlertType.INFO, Alert.FOREVER,
//#                     "Please visit " + AUTH_URL + " to obtain a mini-token", null, get_authenticate());
//#                 // TODO: in-phone authentication - does not work yet
//#                 // launch(AUTH_URL);
//#endif
                // Do nothing
                // Insert post-action code here
            } else if (command == exitCommand) {
                // Insert pre-action code here
                exitMIDlet();
                // Insert post-action code here
            } else if (command == interestingCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
                doGetInteresting();
            } else if (command == clearAuthTokenCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
//#if MD5 && CameraPhone && MMAPI
//#                 deleteAuthToken();
//#endif
            }
        } else if (displayable == takePhoto) {
            if (command == backCommand) {
                // Insert pre-action code here
//#if CameraPhone && MMAPI
//#                 photo = null;
//#                 photoBits = null;
//#endif
                getDisplay().setCurrent(get_start());
                // Insert post-action code here
            } else if (command == exitCommand) {
                // Insert pre-action code here
                exitMIDlet();
                // Insert post-action code here
            } else if (command == takePhotoCommand) {
                // Insert pre-action code here
                // Do nothing
                // Insert post-action code here
//#if CameraPhone && MMAPI
//#                 try {
//#                     initCamera();
//#                 } catch (Exception ex) {
//#                     alert(AlertType.ERROR, Alert.FOREVER, "Error accessing camera", ex, null);
//#                 }
//#endif
            } else if (command == postPhotoCommand) {
                // Insert pre-action code here
//#if CameraPhone && MMAPI
//#                 getDisplay().setCurrent(get_alert(), get_takePhoto());
//#                 // Insert post-action code here
//#                 doPostPhoto();
//#endif
            }
        }
        // Insert global post-action code here
//#if CameraPhone && MMAPI
//#         if (displayable == captureForm) {
//#             if (command == okCommand) {
//#                 new Thread(new Runnable() {
//#                     public void run() {
//#                         doTakePhoto();
//#                     }
//#                 }).start();
//#             }
//#         }
//#endif
}

    /** This method initializes UI of the application.
     */
    private void initialize() {
        // Insert pre-init code here
        configInit();
        status.setLayout(ImageItem.LAYOUT_NEWLINE_BEFORE | ImageItem.LAYOUT_NEWLINE_AFTER | ImageItem.LAYOUT_EXPAND);
        getDisplay().setCurrent(get_start());
        // Insert post-init code here
//#if MD5 && CameraPhone && MMAPI
//#         readAuthToken();
//#endif
    }

    /**
     * This method should return an instance of the display.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * This method should exit the midlet.
     */
    public void exitMIDlet() {
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }

    /** This method returns instance for authenticate component and should be called instead of accessing authenticate field directly.
     * @return Instance for authenticate component
     */
    public Form get_authenticate() {
        if (authenticate == null) {
            // Insert pre-init code here
            authenticate = new Form("Authenticate", new Item[] {
                get_authCodeLabel(),
                get_authCode1(),
                get_authCode2(),
                get_authCode3()
            });
            authenticate.addCommand(get_exitCommand());
            authenticate.addCommand(get_backCommand());
            authenticate.addCommand(get_authenticateCommand());
            authenticate.setCommandListener(this);
            // Insert post-init code here
        }
        return authenticate;
    }


    /** This method returns instance for exitCommand component and should be called instead of accessing exitCommand field directly.
     * @return Instance for exitCommand component
     */
    public Command get_exitCommand() {
        if (exitCommand == null) {
            // Insert pre-init code here
            exitCommand = new Command("Exit", Command.EXIT, 1);
            // Insert post-init code here
        }
        return exitCommand;
    }

    /** This method returns instance for alert component and should be called instead of accessing alert field directly.
     * @return Instance for alert component
     */
    public Alert get_alert() {
        if (alert == null) {
            // Insert pre-init code here
            alert = new Alert("Alert", "<Enter Text>", null, AlertType.INFO);
            alert.setTimeout(-2);
            // Insert post-init code here
        }
        return alert;
    }



    /** This method returns instance for interestingCommand component and should be called instead of accessing interestingCommand field directly.
     * @return Instance for interestingCommand component
     */
    public Command get_interestingCommand() {
        if (interestingCommand == null) {
            // Insert pre-init code here
            interestingCommand = new Command("Get Interesting", Command.ITEM, 1);
            // Insert post-init code here
        }
        return interestingCommand;
    }



    /** This method returns instance for thumbnails component and should be called instead of accessing thumbnails field directly.
     * @return Instance for thumbnails component
     */
    public Form get_thumbnails() {
        if (thumbnails == null) {
            // Insert pre-init code here
            thumbnails = new Form(null, new Item[0]);
            thumbnails.addCommand(get_backCommand());
            thumbnails.addCommand(get_exitCommand());
            thumbnails.addCommand(get_nextCommand());
            thumbnails.addCommand(get_previousCommand());
            thumbnails.addCommand(get_randomCommand());
            thumbnails.setCommandListener(this);
            // Insert post-init code here
        }
        return thumbnails;
    }

    /** This method returns instance for image component and should be called instead of accessing image field directly.
     * @return Instance for image component
     */
    public Form get_image() {
        if (image == null) {
            // Insert pre-init code here
            image = new Form(null, new Item[0]);
            image.addCommand(get_backCommand());
            image.addCommand(get_exitCommand());
            image.setCommandListener(this);
            // Insert post-init code here
        }
        return image;
    }

    /** This method returns instance for viewCommand component and should be called instead of accessing viewCommand field directly.
     * @return Instance for viewCommand component
     */
    public Command get_viewCommand() {
        if (viewCommand == null) {
            // Insert pre-init code here
            viewCommand = new Command("View", Command.ITEM, 1);
            // Insert post-init code here
        }
        return viewCommand;
    }

    /** This method returns instance for backCommand component and should be called instead of accessing backCommand field directly.
     * @return Instance for backCommand component
     */
    public Command get_backCommand() {
        if (backCommand == null) {
            // Insert pre-init code here
            backCommand = new Command("Back", Command.BACK, 1);
            // Insert post-init code here
        }
        return backCommand;
    }

    /** This method returns instance for nextCommand component and should be called instead of accessing nextCommand field directly.
     * @return Instance for nextCommand component
     */
    public Command get_nextCommand() {
        if (nextCommand == null) {
            // Insert pre-init code here
            nextCommand = new Command("Next Page", Command.ITEM, 1);
            // Insert post-init code here
        }
        return nextCommand;
    }

    /** This method returns instance for previousCommand component and should be called instead of accessing previousCommand field directly.
     * @return Instance for previousCommand component
     */
    public Command get_previousCommand() {
        if (previousCommand == null) {
            // Insert pre-init code here
            previousCommand = new Command("Previous Page", Command.ITEM, 1);
            // Insert post-init code here
        }
        return previousCommand;
    }

    /** This method returns instance for loginCommand component and should be called instead of accessing loginCommand field directly.
     * @return Instance for loginCommand component
     */
    public Command get_loginCommand() {
        if (loginCommand == null) {
            // Insert pre-init code here
            loginCommand = new Command("Login", Command.OK, 1);
            // Insert post-init code here
        }
        return loginCommand;
    }

    /** This method returns instance for randomCommand component and should be called instead of accessing randomCommand field directly.
     * @return Instance for randomCommand component
     */
    public Command get_randomCommand() {
        if (randomCommand == null) {
            // Insert pre-init code here
            randomCommand = new Command("Random Page", Command.ITEM, 1);
            // Insert post-init code here
        }
        return randomCommand;
    }

    /** This method returns instance for authCode1 component and should be called instead of accessing authCode1 field directly.
     * @return Instance for authCode1 component
     */
    public TextField get_authCode1() {
        if (authCode1 == null) {
            // Insert pre-init code here
            authCode1 = new TextField(null, null, 3, TextField.NUMERIC);
            authCode1.setLayout(ImageItem.LAYOUT_LEFT | ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_SHRINK);
            // Insert post-init code here
        }
        return authCode1;
    }

    /** This method returns instance for authCode2 component and should be called instead of accessing authCode2 field directly.
     * @return Instance for authCode2 component
     */
    public TextField get_authCode2() {
        if (authCode2 == null) {
            // Insert pre-init code here
            authCode2 = new TextField(null, null, 3, TextField.NUMERIC);
            authCode2.setLayout(ImageItem.LAYOUT_LEFT | ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_SHRINK);
            // Insert post-init code here
        }
        return authCode2;
    }

    /** This method returns instance for authCode3 component and should be called instead of accessing authCode3 field directly.
     * @return Instance for authCode3 component
     */
    public TextField get_authCode3() {
        if (authCode3 == null) {
            // Insert pre-init code here
            authCode3 = new TextField(null, null, 3, TextField.NUMERIC);
            authCode3.setLayout(ImageItem.LAYOUT_LEFT | ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_SHRINK);
            // Insert post-init code here
        }
        return authCode3;
    }

    /** This method returns instance for authCodeLabel component and should be called instead of accessing authCodeLabel field directly.
     * @return Instance for authCodeLabel component
     */
    public StringItem get_authCodeLabel() {
        if (authCodeLabel == null) {
            // Insert pre-init code here
            authCodeLabel = new StringItem("Auth Code:", null);
            authCodeLabel.setLayout(ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_NEWLINE_BEFORE | ImageItem.LAYOUT_NEWLINE_AFTER | ImageItem.LAYOUT_EXPAND);
            // Insert post-init code here
        }
        return authCodeLabel;
    }

    /** This method returns instance for start component and should be called instead of accessing start field directly.
     * @return Instance for start component
     */
    public Form get_start() {
        if (start == null) {
            // Insert pre-init code here
            start = new Form("Foto", new Item[0]);
            start.addCommand(get_exitCommand());
            start.addCommand(get_loginCommand());
            start.addCommand(get_interestingCommand());
            start.addCommand(get_clearAuthTokenCommand());
            start.setCommandListener(this);
            // Insert post-init code here
        }
        return start;
    }

    /** This method returns instance for okCommand component and should be called instead of accessing okCommand field directly.
     * @return Instance for okCommand component
     */
    public Command get_okCommand() {
        if (okCommand == null) {
            // Insert pre-init code here
            okCommand = new Command("Ok", Command.OK, 1);
            // Insert post-init code here
        }
        return okCommand;
    }

    /** This method returns instance for takePhoto component and should be called instead of accessing takePhoto field directly.
     * @return Instance for takePhoto component
     */
    public Form get_takePhoto() {
        if (takePhoto == null) {
            // Insert pre-init code here
            takePhoto = new Form("Take Photo", new Item[] {
                get_title(),
                get_tags(),
                get_locationGroup(),
                get_isPublicGroup()
            });
            takePhoto.addCommand(get_exitCommand());
            takePhoto.addCommand(get_backCommand());
            takePhoto.addCommand(get_takePhotoCommand());
            takePhoto.addCommand(get_postPhotoCommand());
            takePhoto.setCommandListener(this);
            // Insert post-init code here
        }
        return takePhoto;
    }

    /** This method returns instance for takePhotoCommand component and should be called instead of accessing takePhotoCommand field directly.
     * @return Instance for takePhotoCommand component
     */
    public Command get_takePhotoCommand() {
        if (takePhotoCommand == null) {
            // Insert pre-init code here
            takePhotoCommand = new Command("Take Photo", Command.OK, 1);
            // Insert post-init code here
        }
        return takePhotoCommand;
    }

    /** This method returns instance for postPhotoCommand component and should be called instead of accessing postPhotoCommand field directly.
     * @return Instance for postPhotoCommand component
     */
    public Command get_postPhotoCommand() {
        if (postPhotoCommand == null) {
            // Insert pre-init code here
            postPhotoCommand = new Command("Post Photo", Command.ITEM, 1);
            // Insert post-init code here
        }
        return postPhotoCommand;
    }



    /** This method returns instance for title component and should be called instead of accessing title field directly.
     * @return Instance for title component
     */
    public TextField get_title() {
        if (title == null) {
            // Insert pre-init code here
            title = new TextField("Title:", null, 120, TextField.ANY);
            // Insert post-init code here
        }
        return title;
    }

    /** This method returns instance for tags component and should be called instead of accessing tags field directly.
     * @return Instance for tags component
     */
    public TextField get_tags() {
        if (tags == null) {
            // Insert pre-init code here
            tags = new TextField("Tags:", null, 120, TextField.ANY);
            // Insert post-init code here
        }
        return tags;
    }

    /** This method returns instance for isPublicGroup component and should be called instead of accessing isPublicGroup field directly.
     * @return Instance for isPublicGroup component
     */
    public ChoiceGroup get_isPublicGroup() {
        if (isPublicGroup == null) {
            // Insert pre-init code here
            isPublicGroup = new ChoiceGroup("Is Public:", Choice.EXCLUSIVE, new String[] {
                "Yes",
                "No"
            }, new Image[] {
                null,
                null
            });
            isPublicGroup.setSelectedFlags(new boolean[] {
                true,
                false
            });
            // Insert post-init code here
        }
        return isPublicGroup;
    }

    /** This method returns instance for authenticateCommand component and should be called instead of accessing authenticateCommand field directly.
     * @return Instance for authenticateCommand component
     */
    public Command get_authenticateCommand() {
        if (authenticateCommand == null) {
            // Insert pre-init code here
            authenticateCommand = new Command("Ok", Command.OK, 1);
            // Insert post-init code here
        }
        return authenticateCommand;
    }


    /** This method returns instance for locationGroup component and should be called instead of accessing locationGroup field directly.
     * @return Instance for locationGroup component
     */
    public ChoiceGroup get_locationGroup() {
        if (locationGroup == null) {
            // Insert pre-init code here
            locationGroup = new ChoiceGroup("Location:", Choice.POPUP, new String[] {
                "Moscone Center, San Francisco, CA",
                "4210 Network Circle, 95054",
                "16 Network Circle, 94025",
                "500 Howard St, 94105"
            }, new Image[] {
                null,
                null,
                null,
                null
            });
            locationGroup.setSelectedFlags(new boolean[] {
                false,
                true,
                false,
                false
            });
            // Insert post-init code here
        }
        return locationGroup;
    }

    /** This method returns instance for itemCommand1 component and should be called instead of accessing itemCommand1 field directly.
     * @return Instance for itemCommand1 component
     */
    public Command get_itemCommand1() {
        if (itemCommand1 == null) {
            // Insert pre-init code here
            itemCommand1 = new Command("Item", Command.ITEM, 1);
            // Insert post-init code here
        }
        return itemCommand1;
    }

    /** This method returns instance for clearAuthTokenCommand component and should be called instead of accessing clearAuthTokenCommand field directly.
     * @return Instance for clearAuthTokenCommand component
     */
    public Command get_clearAuthTokenCommand() {
        if (clearAuthTokenCommand == null) {
            // Insert pre-init code here
            clearAuthTokenCommand = new Command("Delete Auth Token", Command.ITEM, 1);
            // Insert post-init code here
        }
        return clearAuthTokenCommand;
    }


    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    private void configInit() {
//#if MD5 && CameraPhone && MMAPI
//#else
        get_start().removeCommand(get_loginCommand());
        get_start().removeCommand(get_clearAuthTokenCommand());
//#endif
    }

    private void alert(final AlertType type, final int timeout,
        final String message, final Exception ex, final Displayable next) {
        final Alert alert = get_alert();
        alert.setType(type);
        final String exMsg = ex == null ? null : ex.getMessage();
        final String text = exMsg == null ? message : message + ": " + exMsg;
        alert.setString(text);
        alert.setTimeout(timeout);
        if (DEBUG) {
            if (ex != null) {
                ex.printStackTrace();
            }
        }
        if (next == null) {
            getDisplay().setCurrent(alert);
        } else {
            getDisplay().setCurrent(alert, next);
        }
    }

    private void status(final String message) {
        get_alert().setString(message);
        if (DEBUG) {
            System.out.println(message);
        }
    }

//#if MD5 && CameraPhone && MMAPI
//#     // important: it is assumed that the args are sorted by key
//#     private static Arg sign(final Arg[] args) throws Exception {
//#         if (args == null) {
//#             throw new IllegalArgumentException("args cannot be null");
//#         }
//#         if (args.length == 0) {
//#             throw new IllegalArgumentException("args must not be empty");
//#         }
//#
//#         final StringBuffer concat = new StringBuffer();
//#
//#         // first append the shared secret
//#         concat.append(SHARED_SECRET);
//#
//#         // now append the args, which are assumed to be sorted by key
//#         for (int i=0; i < args.length; i++) {
//#             if (args[i] != null) {
//#                 // do not include the signature or the photo arg in the signature computation
//#                 final String key = args[i].getKey();
//#                 if ("api_sig".equals(key) || "photo".equals(key)) {
//#                     continue;
//#                 }
//#                 if (key != null) {
//#                     concat.append(key);
//#                 }
//#                 final String value = args[i].getValue();
//#                 if (value != null) {
//#                     concat.append(value);
//#                 }
//#             }
//#         }
//#
//#         final byte[] input = concat.toString().getBytes();
//#         // an md5 hash is 128 bits = 16 bytes
//#         final byte[] hash = new byte[16];
//#
//#         final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
//#         md.update(input, 0, input.length);
//#         md.digest(hash, 0, hash.length);
//#
//#         final StringBuffer sig = new StringBuffer(hash.length);
//#         for (int i=0; i < hash.length; i++) {
//#             sig.append(Integer.toHexString((hash[i] & 0xf0) >>> 4));
//#             sig.append(Integer.toHexString((hash[i] & 0x0f)));
//#         }
//#
//#         // the sig arg, whose value should be null (a placeholder), is replaced with the sig
//#         int sigIndex = -1;
//#         for (int i=0; i < args.length; i++) {
//#             if ("api_sig".equals(args[i].getKey())) {
//#                 if (args[i].getValue() != null) {
//#                     throw new IllegalArgumentException("the api_sig arg must be null to replace with the actual signature");
//#                 }
//#                 sigIndex = i;
//#             }
//#         }
//#         if (sigIndex < 0) {
//#             throw new IllegalArgumentException("the api_sig arg must be present");
//#         }
//#
//#         return args[sigIndex] = new Arg("api_sig", sig.toString());
//#     }
//#endif

    private void doGetInteresting() {

        // args must be sorted by key for signing to work
        final Arg[] args = {
            new Arg("api_key", API_KEY),
            new Arg("format", "json"),
            new Arg("method", "flickr.interestingness.getList"),
            new Arg("nojsoncallback", "1"),
            new Arg("page", Integer.toString(page)),
            new Arg("per_page", Integer.toString(INTERESTING_PHOTOS_PER_PAGE))
        };

        status.setText("Getting...");
        Request.get(WEB_API_BASE, args, null, this, get_thumbnails());
    }

    private void handleGetInterestingResponse(final Response response) {

        if (isError(response)) {
            alert(AlertType.ERROR, Alert.FOREVER,
                errorCode + ": " + errorMessage, null, get_start());
            return;
        }

        final Result result = response.getResult();
        try {
            maxPage = Integer.valueOf(result.getAsString("photos.pages")).intValue();
        } catch (Exception nex) {
            // default to 1 in case of errors
            maxPage = 1;
        }

        get_thumbnails().setTitle("Page " + page + "/" + maxPage);

        try {
            photos = new Photo[result.getSizeOfArray("photos.photo")];
        } catch (ResultException jex) {
            alert(AlertType.ERROR, Alert.FOREVER,
                "Error parsing size of getInteresting results", jex, get_start());
            return;
        }

        for (int i=0; i < photos.length; i++) {
            final Photo foto = new Photo();
            final String root = "photos.photo[" + i + "].";
            try {
                foto.id = result.getAsString(root + "id");
                foto.owner = result.getAsString(root + "owner");
                foto.secret = result.getAsString(root + "secret");
                foto.server = result.getAsString(root + "server");
                foto.farm = result.getAsString(root + "farm");
                foto.title = result.getAsString(root + "title");
                foto.ispublic = !"0".equals(result.getAsString(root + "ispublic"));
                photos[i] = foto;
            } catch (ResultException jex) {
                alert(AlertType.ERROR, Alert.FOREVER,
                    "Error parsing result " + i + " of getInteresting", jex, get_start());
                return;
            }
        }

        for (int i=0; i < photos.length; i++) {
            final Photo foto = photos[i];
            if (foto.ispublic) {
                status.setText("Loading " + (i+1) + "/" + photos.length + "...");
                final Image thumb = getImage(foto, "t");
                if (thumb != null) {
                    final ImageItem imgItem = new ImageItem(null,
                        thumb, ImageItem.LAYOUT_DEFAULT, "Thumbnail", ImageItem.BUTTON);
                    imgItem.setDefaultCommand(get_viewCommand());
                    imgItem.setItemCommandListener(new ItemCommandListener() {
                        public void commandAction(Command command, Item item) {
                            new Thread(new Runnable() {
                                public void run() {
                                    get_image().deleteAll();
                                    status.setText("Loading " + foto.id + "...");
                                    get_image().insert(0, status);
                                    get_image().setTitle(foto.title);
                                    getDisplay().setCurrent(get_image());
                                    final Image image = getImage(foto, "m");
                                    get_image().delete(0);
                                    if (image != null) {
                                        get_image().append(image);
                                    }
                                }
                            }).start();
                        }
                    });
                    get_thumbnails().append(imgItem);
                }
            }
        }
        getDisplay().setCurrent(get_thumbnails());
    }

    private Image getImage(final Photo foto, final String size) {
        final String imageLoc =
            "http://farm" +
            foto.farm + ".static.flickr.com/" +
            foto.server + "/" +
            foto.id + "_" +
            foto.secret + "_" +
            size + ".jpg";
        try {
            return loadImage(imageLoc);
        } catch (Exception iex) {
            if (DEBUG) {
                iex.printStackTrace();
            }
            return null;
        }
    }

    private Image loadImage(final String location) throws IOException {
        HttpConnection conn = null;
        InputStream is = null;
        try {
            conn = (HttpConnection) Connector.open(location);
                        conn.setRequestProperty("accept", "image/*");

            final int responseCode = conn.getResponseCode();
            if (responseCode != HttpConnection.HTTP_OK) {
                // TODO: handle redirects
                return null;
            }

            final int totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
            is = new ProgressInputStream(conn.openInputStream(), totalToReceive, this, null, 1024);

            final ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(totalToReceive, 8192));
            final byte[] buffer = new byte[4096];
            for (int nread = is.read(buffer); nread >= 0; nread = is.read(buffer)) {
                bos.write(buffer, 0, nread);
            }
            return Image.createImage(new ByteArrayInputStream(bos.toByteArray()));
        }  finally {
            if (is != null) { is.close(); }
            if (conn != null) { conn.close(); }
        }
    }

    private boolean isError(final Response response) {
        boolean isError = false;
        final Exception ex = response.getException();
        if (ex != null) {
            isError = true;
            errorMessage = ex.getMessage();
        } else {
            try {
                final Result result = response.getResult();
                final String stat = result.getAsString("stat");
                if ("fail".equals(stat)) {
                    isError = true;
                    errorCode = result.getAsString("code");
                    errorMessage = result.getAsString("message");
                }
            } catch (ResultException rex) {
                isError = true;
            }
        }
        return isError;
    }

//#if MD5 && CameraPhone && MMAPI
//#     private boolean isPostError(final Response response) {
//#         boolean isError = false;
//#         final Exception ex = response.getException();
//#         if (ex != null) {
//#             isError = true;
//#             errorMessage = ex.getMessage();
//#         } else {
//#             try {
//#                 final Result result = response.getResult();
//#                 final String stat = result.getAsString("rsp.stat");
//#                 if ("fail".equals(stat)) {
//#                     isError = true;
//#                     errorCode = result.getAsString("rsp.err.code");
//#                     errorMessage = result.getAsString("rsp.err.msg");
//#                 }
//#             } catch (ResultException rex) {
//#                 isError = true;
//#             }
//#         }
//#         return isError;
//#     }
//#endif

//#if MD5 && CameraPhone && MMAPI
//#     private void doAuthenticate() {
//#
//#         final String authCode1 = get_authCode1().getString();
//#         final String authCode2 = get_authCode2().getString();
//#         final String authCode3 = get_authCode3().getString();
//#         if (authCode1 == null || authCode2 == null || authCode3 == null) {
//#             alert(AlertType.ERROR, Alert.FOREVER,
//#                 "Please enter the 9-digit auth code obtained after successful login", null, null);
//#             return;
//#         }
//#
//#         final String miniToken = authCode1 + "-" + authCode2 + "-" + authCode3;
//#         if (miniToken.length() != 11) {
//#             alert(AlertType.ERROR, Alert.FOREVER, "The auth code must be 9 digits", null, null);
//#             return;
//#         }
//#
//#         // args must be sorted by key for signing to work
//#         final Arg[] args = {
//#             new Arg("api_key", API_KEY),
//#             new Arg("api_sig", null), // placeholder for the signature
//#             new Arg("format", "json"),
//#             new Arg("method", "flickr.auth.getFullToken"),
//#             new Arg("mini_token", miniToken),
//#             new Arg("nojsoncallback", "1")
//#         };
//#
//#         try {
//#             sign(args);
//#         } catch (Exception ex) {
//#             alert(AlertType.ERROR, Alert.FOREVER, "Error signing full token request", ex, get_start());
//#             return;
//#         }
//#
//#         status("Getting full token...");
//#         Request.get(WEB_API_BASE, args, null, this, get_authenticate());
//#     }
//#
//#     private void handleAuthenticateResponse(final Response response) {
//#
//#         if (isError(response)) {
//#             alert(AlertType.ERROR, Alert.FOREVER, errorCode + ": " + errorMessage, null, get_start());
//#             return;
//#         }
//#
//#         final Result result = response.getResult();
//#         System.out.println(result);
//#         try {
//#             token = result.getAsString("auth.token._content");
//#             perms = result.getAsString("auth.perms._content");
//#             userid = result.getAsString("auth.user.nsid");
//#             username = result.getAsString("auth.user.username");
//#         } catch (Exception ex) {
//#             alert(AlertType.ERROR, Alert.FOREVER, "Error extracting full token from result", ex, get_start());
//#             return;
//#         }
//#
//#         saveAuthToken();
//#
//#         status("Login successful");
//#         getDisplay().setCurrent(get_takePhoto());
//#     }
//#
//#     private void deleteAuthToken() {
//#         token = null;
//#         perms = null;
//#         userid = null;
//#         username = null;
//#         try { RecordStore.deleteRecordStore(STORE_NAME); } catch (Exception ignore) {}
//#     }
//#
//#     private boolean saveAuthToken() {
//#         RecordStore store = null;
//#         try {
//#             try { RecordStore.deleteRecordStore(STORE_NAME); } catch (Exception ignore) {}
//#             store = RecordStore.openRecordStore(STORE_NAME, true);
//#             final ByteArrayOutputStream bos = new ByteArrayOutputStream();
//#             final DataOutputStream dos = new DataOutputStream(bos);
//#             dos.writeUTF(token);
//#             dos.writeUTF(perms);
//#             dos.writeUTF(userid);
//#             dos.writeUTF(username);
//#             final byte[] bits = bos.toByteArray();
//#             store.addRecord(bits, 0, bits.length);
//#             return true;
//#         } catch (Exception ex) {
//#             return false;
//#         } finally {
//#             if (store != null) { try { store.closeRecordStore(); } catch (Exception ignore) {}}
//#         }
//#     }
//#
//#     private boolean readAuthToken() {
//#         // try to read the persisted auth token
//#         RecordStore store = null;
//#         try {
//#             store = RecordStore.openRecordStore(STORE_NAME, false);
//#             final RecordEnumeration en = store.enumerateRecords(null, null, false);
//#             if (!en.hasNextElement()) {
//#                 return false;
//#             }
//#             final ByteArrayInputStream bis = new ByteArrayInputStream(en.nextRecord());
//#             final DataInputStream dis = new DataInputStream(bis);
//#             token = dis.readUTF();
//#             perms = dis.readUTF();
//#             userid = dis.readUTF();
//#             username = dis.readUTF();
//#             return true;
//#         } catch (Exception ex) {
//#             return false;
//#         } finally {
//#             if (store != null) { try { store.closeRecordStore(); } catch (Exception ignore) {}}
//#         }
//#     }
//#endif

//#if MD5 && CameraPhone && MMAPI
//#     private void doPostPhoto() {
//#
//#         final Arg apiKeyArg = new Arg("api_key", API_KEY);
//#         final Arg apiSigArg = new Arg("api_sig", null); // placeholder for the signature
//#         final Arg authTokenArg = new Arg("auth_token", token);
//#         final Arg formatArg = new Arg("format", "json");
//#         final Arg isPublicArg = new Arg("is_public",
//#             "Yes".equals(get_isPublicGroup().getString(0)) ? "1" : "0");
//#         final Arg noJsonCallbackArg = new Arg("nojsoncallback", "1");
//#         final Arg photoArg = new Arg("photo", "img" + Long.toString(System.currentTimeMillis(), 16) + ".jpg");
//#         final Arg tagsArg = new Arg("tags", get_tags().getString());
//#         final Arg titleArg = new Arg("title", get_title().getString());
//#
//#         // args must be sorted by key for signing to work
//#         final Arg[] postArgs = {
//#             apiKeyArg,
//#             apiSigArg,
//#             authTokenArg,
//#             formatArg,
//#             isPublicArg,
//#             noJsonCallbackArg,
//#             photoArg,
//#             tagsArg,
//#             titleArg
//#         };
//#
//#         final Arg[] apiKeyArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + apiKeyArg.getKey())
//#         };
//#         final Arg[] authTokenArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + authTokenArg.getKey())
//#         };
//#         final Arg[] formatArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + formatArg.getKey())
//#         };
//#         final Arg[] isPublicArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + isPublicArg.getKey())
//#         };
//#         final Arg[] noJsonCallbackArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + noJsonCallbackArg.getKey())
//#         };
//#         final Arg[] photoArgs = {
//#             new Arg(Arg.CONTENT_TYPE, "image/jpeg"),
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + photoArg.getKey() +
//#                 "; filename=" + photoArg.getValue())
//#         };
//#         final Arg[] tagsArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + tagsArg.getKey())
//#         };
//#         final Arg[] titleArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + titleArg.getKey())
//#         };
//#
//#         final String multipartBoundary =
//#             Long.toString(System.currentTimeMillis(), 16) +
//#             "--" + getClass().getName();
//#         final Arg[] httpArgs = {
//#             new Arg("MIME-Version", "1.0"),
//#             new Arg("Content-Type", "multipart/form-data; boundary=" + multipartBoundary)
//#         };
//#
//#         final Arg signedArg;
//#         try {
//#             signedArg = sign(postArgs);
//#         } catch (Exception ex) {
//#             alert(AlertType.ERROR, Alert.FOREVER, "Error signing upload request", ex, get_takePhoto());
//#             return;
//#         }
//#
//#         final Arg[] apiSigArgs = {
//#             new Arg(Arg.CONTENT_DISPOSITION, "form-data; name=" + signedArg.getKey())
//#         };
//#         final Part[] postData = {
//#             new Part(apiKeyArg.getValue().getBytes(), apiKeyArgs),
//#             new Part(authTokenArg.getValue().getBytes(), authTokenArgs),
//#             new Part(signedArg.getValue().getBytes(), apiSigArgs),
//#             new Part(formatArg.getValue().getBytes(), formatArgs),
//#             new Part(isPublicArg.getValue().getBytes(), isPublicArgs),
//#             new Part(noJsonCallbackArg.getValue().getBytes(), noJsonCallbackArgs),
//#             new Part(tagsArg.getValue().getBytes(), tagsArgs),
//#             new Part(titleArg.getValue().getBytes(), titleArgs),
//#             new Part(photoBits, photoArgs)
//#         };
//#         final PostData mp = new PostData(postData, multipartBoundary);
//#
//#         status("Starting upload...");
//#         Request.post(UPLOAD_BASE, null, httpArgs, this, mp, get_takePhoto());
//#     }
//#
//#     private void handlePostPhotoResponse(final Response response) {
//#
//#         if (isPostError(response)) {
//#             status(errorCode + ": " + errorMessage);
//#             return;
//#         }
//#         status("Upload successful");
//#
//#         photo = null;
//#         photoBits = null;
//#         get_title().setString(null);
//#         get_tags().setString(null);
//#         get_takePhoto().delete(get_takePhoto().size() - 1);
//#
//#         final Result result = response.getResult();
//#         uploadedPhotoId = null;
//#         try {
//#             uploadedPhotoId = result.getAsString("rsp.photoid");
//#         } catch (ResultException rex) {
//#             status("Geocoding error: " + rex.getMessage());
//#             return;
//#         }
//#
//#         doGeocoding();
//#     }
//#endif

//#if MD5 && CameraPhone && MMAPI
//#     private void doGeocoding() {
//#
//#         // args must be sorted by key for signing to work
//#         final Arg[] args = {
//#             new Arg("output", "xml"),
//#             new Arg("appid", GEOCODE_APPID),
//#             new Arg("location", locationGroup.getString(locationGroup.getSelectedIndex()))
//#         };
//#
//#         status("Getting coordinates...");
//#         Request.get(GEOCODE_BASE, args, null, this, get_locationGroup());
//#     }
//#
//#     private void handleGeocodeResponse(final Response response) {
//#
//#         if (isPostError(response)) {
//#             status(errorCode + ": " + errorMessage);
//#             return;
//#         }
//#         status("Got coordinates");
//#
//#         final Result result = response.getResult();
//#
//#         String latitude = null;
//#         String longitude = null;
//#         String precision = null;
//#         try {
//#             latitude = result.getAsString("ResultSet.Result.Latitude");
//#             longitude = result.getAsString("ResultSet.Result.Longitude");
//#             precision = result.getAsString("ResultSet.Result.precision");
//#         } catch (ResultException rex) {
//#             status("Error extracting coordinates: " + rex.getMessage());
//#             return;
//#         }
//#
//#         // args must be sorted by key for signing to work
//#         final Arg[] args = {
//#             new Arg("api_key", API_KEY),
//#             new Arg("api_sig", null), // placeholder for the signature
//#             new Arg("auth_token", token),
//#             new Arg("format", "json"),
//#             new Arg("lat", latitude),
//#             new Arg("lon", longitude),
//#             new Arg("method", "flickr.photos.geo.setLocation"),
//#             new Arg("nojsoncallback", "1"),
//#             new Arg("photo_id", uploadedPhotoId)
//#         };
//#
//#         try {
//#             sign(args);
//#         } catch (Exception ex) {
//#             status("Error signing setLocation request" + ex.getMessage());
//#             return;
//#         }
//#
//#         status("Adding location...");
//#         Request.get(WEB_API_BASE, args, null, this, GEOCODE_BASE);
//#     }
//#
//#     private void handleApplyGeocodeResponse(final Response response) {
//#
//#         if (isPostError(response)) {
//#             status(errorCode + ": " + errorMessage);
//#             return;
//#         }
//#         status("Placed photo");
//#
//#         try {
//#             Thread.currentThread().sleep(2000);
//#         } catch (InterruptedException ignore) {}
//#
//#         getDisplay().setCurrent(get_takePhoto());
//#     }
//#endif

//#if CameraPhone && MMAPI
//#     private void initCamera() throws Exception {
//#         // use capture://image on nokia series 40 and capture://video on WTK and elsewhere
//#         player = Manager.createPlayer("capture://image");
//#         player.realize();
//#         control = (VideoControl) player.getControl("VideoControl");
//#         if (control != null) {
//#             captureForm = new Form("Take Photo");
//#             captureForm.append((Item) control.initDisplayMode(control.USE_GUI_PRIMITIVE, null));
//#             captureForm.addCommand(get_okCommand());
//#             captureForm.setCommandListener(this);
//#             getDisplay().setCurrent(captureForm);
//#         }
//#         player.start();
//#     }
//#endif

//#if CameraPhone && MMAPI
//#     private void doTakePhoto() {
//#         if (player.getState() == Player.STARTED) {
//#             try {
//#                 photoBits = control.getSnapshot(null);
//#                 player.stop();
//#                 photo = Image.createImage(photoBits, 0, photoBits.length);
//#                 get_takePhoto().append(photo);
//#                 getDisplay().setCurrent(get_takePhoto());
//#             } catch (Exception ex) {
//#                 alert(AlertType.ERROR, Alert.FOREVER, "Error taking photo", ex, null);
//#             } finally {
//#                 player.close();
//#             }
//#         }
//#     }
//#endif

    public void readProgress(final Object context, final int bytes, final int total) {
        status.setText(total > 0 ? (((bytes * 100) / total) + "%") : Integer.toString(bytes));
    }

    public void writeProgress(final Object context, final int bytes, final int total) {
        status("Uploading... " +
            (total > 0 ? (((bytes * 100) / total) + "%") : Integer.toString(bytes)));
    }

    public void done(final Object context, final Response response) throws Exception {
        if (context == thumbnails) {
            try {
                get_thumbnails().deleteAll();
                get_thumbnails().insert(0, status);
                getDisplay().setCurrent(get_thumbnails());
                handleGetInterestingResponse(response);
            } finally {
                get_thumbnails().delete(0);
            }
//#if MD5 && CameraPhone && MMAPI
//#         } else if (context == authenticate) {
//#             handleAuthenticateResponse(response);
//#endif
//#if CameraPhone && MMAPI
//#         } else if (context == takePhoto) {
//#             handlePostPhotoResponse(response);
//#         } else if (context == locationGroup) {
//#             handleGeocodeResponse(response);
//#         } else if (context == GEOCODE_BASE) {
//#             handleApplyGeocodeResponse(response);
//#endif
        } else {
            throw new IllegalArgumentException("unknown context returned: " + context.toString());
        }
    }

//#if MD5 && CameraPhone && MMAPI
//#     private boolean doValidateAuthToken() {
//#
//#         // args must be sorted by key for signing to work
//#         final Arg[] args = {
//#             new Arg("api_key", API_KEY),
//#             new Arg("api_sig", null), // placeholder for the signature
//#             new Arg("auth_token", token),
//#             new Arg("format", "json"),
//#             new Arg("method", "flickr.auth.checkToken"),
//#             new Arg("nojsoncallback", "1")
//#         };
//#
//#         try {
//#             sign(args);
//#         } catch (Exception ex) {
//#             alert(AlertType.ERROR, Alert.FOREVER,
//#                 "Error signing auth.checkToken request", ex, get_start());
//#             return false;
//#         }
//#
//#         Response response = null;
//#         try {
//#             response = Request.get(WEB_API_BASE, args, null, this);
//#         } catch (IOException rex) {
//#             status("failed to check auth.checkToken");
//#             return false;
//#         }
//#
//#         if (isError(response)) {
//#             deleteAuthToken();
//#             return false;
//#         }
//#
//#         return true;
//#     }
//#endif
}
