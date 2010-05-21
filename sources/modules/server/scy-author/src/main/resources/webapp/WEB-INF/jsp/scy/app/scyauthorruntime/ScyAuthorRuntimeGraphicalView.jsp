<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
    <tiles:putAttribute name="main">
        <style type="text/css">
            #world {
              background-color: white;
              margin: 0 auto 0 auto;
              width: 640px;
              height: 500px;
             /* border: 3px solid gray;
              border-radius: 30px;*/
            }

        </style>




        <h1>WELCOME TO SCYAuthor Runtime!</h1>  ${baseUrl}

        <c:choose>
        <c:when test="${fn:length(pedagogicalPlans) > 0}">
            <table id="pedagogicalPlansTable">
                <tr>
                    <th>Pedagogical Plan</th>
                    <th>Active</th>
                    <th>Number of online students</th>
                </tr>
                <c:forEach var="pedagogicalPlan" items="${pedagogicalPlans}">
                    <tr class="${oddEven.oddEven}">
                        <td><a href="viewActivePedagogicalPlan.html?id=${pedagogicalPlan.id}">${pedagogicalPlan.name}</a></td>
                        <td><s:ajaxCheckBox model="${pedagogicalPlan}" property="published"/></td>
                        <td>?</td>
                    </tr>
                </c:forEach>
            </table>
            <br>
            <div id="gfx_holder"></div>


        </c:when>
    </c:choose>
     <div id="world"></div>
        <script type="text/javascript">
            var uml = Joint.dia.uml;
            var devs = Joint.dia.devs;
            
            Joint.paper("world", 640, 500);

                     

            var s1 = createLas(10, 60, 80, 50, 'Orientation');
            var s2 = createLas(200, 30, 120, 50, 'Conceptualization');
            var s3 = createLas(450, 30,  80, 50,'Design');
            var s4 = createLas(450, 120, 80, 50, 'Build');
            var s5 = createLas(120, 150, 80, 50, 'Evaluation');
            var s6 = createLas(400, 200,  80, 50,'Experiment');
            var s7 = createLas(100, 250, 80, 50, 'Reflection');
            var s8 = createLas(50, 390, 80, 50, 'Reporting');


           
            var se = uml.EndState.create({
              position: {x: 100, y: 480}
            }).toggleGhosting();

            var all = [se, s1, s2, s3, s4, s5, s6, s7, s8];
            
            var elo1 = createElo(150, 40);
            var elo2 = createElo(360, 40);
            var elo3 = createElo(600, 70);
            var elo4 = createElo(600, 150);
            var elo5 = createElo(300, 190);
            var elo6 = createElo(320, 100);
            var elo7 = createElo(220, 100);
            var elo8 = createElo(160, 210);
            var elo9 = createElo(75, 330);



            function createElo(eloX, eloY){
               return  uml.Class.create({
                  rect: {x: eloX, y: eloY, width: 20, height: 20, rotate: 45},
                  //label: "Client",
                  attrs: {
                    fill: "135-#000-#0a0:1-#fff"
                  }
                });
            }

            function createLas(lasX, lasY,lasW, lasH,  lasTitle){
                return uml.State.create({
                  rect: {x: lasX, y: lasY, width: lasW, height: lasH},
                  label: lasTitle,
                  attrs: {
                    fill: "90-#000-#0af:1-#fff"
                  },
                  actions: {
                   // entry: "create()"
                  }
                }).toggleGhosting();
            }
            //s2.scale(2);
            //s2.addInner(s4);

            //s0.joint(s1, uml.arrow).register(all);
            s1.joint(elo1, uml.arrow).register(all);
            elo1.joint(s2, uml.arrow).register(all);
            //s2.joint(s3, uml.arrow).register(all);
            elo2.joint(s2, uml.arrow).register(all);
            elo2.joint(s3, uml.arrow).register(all);
            //s2.joint(s6, uml.arrow).register(all);
            elo7.joint(s2, uml.arrow).register(all);
            elo7.joint(s5, uml.arrow).register(all);
            s5.joint(elo8, uml.arrow).register(all);
            elo8.joint(s7, uml.arrow).register(all);
            //s2.joint(s5, uml.arrow).register(all);
            s3.joint(elo3, uml.arrow).register(all);
            elo3.joint(s4, uml.arrow).register(all);
            //s3.joint(s5, uml.arrow).register(all);
            s4.joint(elo4, uml.arrow).register(all);
            elo5.joint(s5, uml.arrow).register(all);
            elo4.joint(s6, uml.arrow).register(all);
            elo5.joint(s5, uml.arrow).register(all);
            elo5.joint(s6, uml.arrow).register(all);
            elo6.joint(s6, uml.arrow).register(all);
            elo6.joint(s2, uml.arrow).register(all);
            //s5.joint(s6, uml.arrow).register(all);
            s5.joint(s3, uml.arrow).register(all);
            //s5.joint(s7, uml.arrow).register(all);
            s7.joint(elo9, uml.arrow).register(all);
            elo9.joint(s8, uml.arrow).register(all);
            s8.joint(se, uml.arrow).register(all);

            //setTimeout("updates2(s2)", 3000);
            for(var i = 0;i<all.length;i++){
                if(i > 0){
                    createLasContentBox(all[i]);
                }
            }

            function createLasContentBox(lasObj){

                var worldLeftOffset = document.getElementById("world").offsetLeft;
                var worldTopOffset = document.getElementById("world").offsetTop;

                var s2w = lasObj.rect.width;
                var s2h = lasObj.rect.height;
                var s2x = lasObj.rect.x;
                var s2y = lasObj.rect.y;
                var s2Div = document.createElement("div");
                s2Div.setAttribute("id", lasObj.label)
                s2Div.style.width = s2w + "px";
                s2Div.style.height = "40px";
                s2Div.style.backgroundColor = "transparent";
                //s2Div.style.border = "1px solid #000000";
                s2Div.style.position = "absolute"
                s2Div.style.left = (worldLeftOffset + s2x) + "px";
                //s2Div.style.left = s2x + "px";
                s2Div.style.top = (worldTopOffset + s2y + 20) + "px";
                document.getElementById("world").appendChild(s2Div);
                //s2Div.innerHTML = "<img src=\"http://localhost:8080/webapp/themes/scy/default/images/brown_man_icon.png\" />";
            }
            setInterval("updateLasContentBox()", 3500);
            function updateLasContentBox(){
                var maxNumberOfParticipants = 6;
                var rndNumber = 1;
                var rndIconNumber = 0;
                var userIconArray = new Array();
                userIconArray[0] = "<img src=\"${baseUrl}/themes/scy/default/images/green_man_icon.png\" />";
                userIconArray[1] = "<img src=\"${baseUrl}/themes/scy/default/images/brown_man_icon.png\" />";

                for(var i = 0; i<all.length;i++){
                    if(i > 0){

                        rndNumber =Math.floor(Math.random()*maxNumberOfParticipants);
                        var imgStr = "";
                        for(j = 0;j<rndNumber;j++){
                             rndIconNumber = Math.floor(Math.random()*2);
                             imgStr += userIconArray[rndIconNumber];
                        }

                        var elementId = all[i].label;
                        document.getElementById(elementId).innerHTML = imgStr;

                    }
                }

            }
            
        </script>
    </tiles:putAttribute>
</tiles:insertDefinition>