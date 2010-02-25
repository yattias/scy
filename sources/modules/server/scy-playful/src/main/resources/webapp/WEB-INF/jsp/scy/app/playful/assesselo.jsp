<%@ include file="common-taglibs.jsp" %>
<tiles:insertDefinition name="default-page">
	<tiles:putAttribute name="extrahead">
		<script type="text/javascript">
			dojo.require("dijit.layout.SplitContainer");
			dojo.require("dojox.image.Lightbox");
			dojo.require("dijit.layout.ContentPane");
			dojo.require("dijit.TooltipDialog");
			dojo.require("dijit.form.Button");
			dojo.require("dijit.form.TextBox");
			dojo.require("dijit.form.DropDownButton");
			dojo.require("dijit.form.HorizontalSlider");
			dojo.require("dijit.form.HorizontalRuleLabels");
			dojo.require("dijit.form.SimpleTextarea");
			dojo.require("dijit.form.HorizontalRule");
			dojo.require("dijit.form.ToggleButton");
			dojo.require("dijit.layout.StackContainer");
		</script>
		<script type="text/javascript">
			dojo.addOnLoad(function() {
				dojo.addClass(dojo.body, 'tundra');
			})
		</script>
		<style type="text/css">
			@import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dijit/themes/tundra/tundra.css";
			@import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/image/resources/Lightbox.css";
			@import "http://ajax.googleapis.com/ajax/libs/dojo/1.4/dojox/layout/resources/RotatorContainer.css";
		</style>
		<style type="text/css">
			.splitpane .dijitSplitContainer-child {
				border: none;
			}

			.splitpane .dijitContentPane {
				background: none;
			}

			.splitpane .dojoxRotatorContainer {
				border: none;
			}
		</style>
	</tiles:putAttribute>

	<tiles:putAttribute name="bodyclass" value="tundra"/>

	<tiles:putAttribute name="main">
		<div class="splitpane" dojoType="dijit.layout.SplitContainer" orientation="horizontal" sizerWidth="7"
			 activeSizing="false" style="width: 100%; height: 100%;min-height: 40em">
			<div dojoType="dijit.layout.ContentPane" sizeMin="20" sizeShare="20">
				<div class="rounded" style="background:#eee; padding: 1em;margin-top: 0.1em;">
					<h2>Latest contributions</h2>
					<ul>
						<c:forEach var="contr" items="${contributions}">
							<li class="rounded-small contribution">
								<strong>${contr.reviewer.userDetails.username}</strong>
								rated ${contr.ELORef.author.userDetails.username}'s ${contr.ELORef.type}</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div dojoType="dijit.layout.ContentPane" sizeMin="50" sizeShare="50">
				<div class="rounded" style="background:#eee; padding: 1em;">
					<h1>${eloRef.author.userDetails.username}'s ${eloRef.type}</h1>
					<table class="elo-summary" style="float:right">
						<tr>
							<th>Date:</th>
							<td>${eloRef.timeCreated}</td>
						</tr>
						<tr>
							<th>Time:</th>
							<td>${eloRef.timeCreated}</td>
						</tr>
						<tr>
							<th>Rating:</th>
							<td>TODO% (TODO votes)</td>
						</tr>
					</table>
					<table class="elo-summary">
						<tr>
							<th>Tool:</th>
							<td>${eloRef.tool}</td>
						</tr>
						<tr>
							<th>Mission:</th>
							<td>${eloRef.mission.description}</td>
						</tr>
						<tr>
							<th>Topic:</th>
							<td>${eloRef.topic}</td>
						</tr>
					</table>
				</div>
				<div class="rounded" style="background:#fff; padding: 1em;margin-top:2px; text-align:center;">
					<a href="${eloRef.image}" dojoType="dojox.image.Lightbox"><img src="${eloRef.image}"
																				   style="width: 70%;max-height:23.5em;"/></a>
				</div>
				<div class="rounded" style="background-color:#fff; padding: 1em;margin-top:2px;">
					<div class="rounded-small" style="float:left">
						<div dojoType="dijit.form.DropDownButton" id="dropdown">
							<span>
								Rate this ${eloRef.type}
							</span>

							<div dojoType="dijit.TooltipDialog">
								<form method="post" action="">
									<table class="rateform">
										<tr>
											<th><label for="score">Score:</label></th>
											<td>
												<!--todo: print field errors -->
												<div dojoType="dijit.form.HorizontalSlider" name="score"
													 minimum="1"
													 value="5"
													 maximum="10"
													 discreteValues="10"
													 showButtons="false"
													 intermediateChanges="true"
													 slideDuration="0"
													 style="width:200px; height: 40px;margin-left:20px"
													 id="score">
													<div dojoType="dijit.form.HorizontalRule"
														 container="bottomDecoration" count=3 style="height:5px;"></div>
													<ol dojoType="dijit.form.HorizontalRuleLabels"
														container="bottomDecoration"
														style="height:1em;font-size:75%;color:gray;">
														<li>1</li>
														<li>2</li>
														<li>3</li>
														<li>4</li>
														<li>5</li>
														<li>6</li>
														<li>7</li>
														<li>8</li>
														<li>9</li>
														<li>10</li>
													</ol>
												</div>
											</td>
										</tr>
										<tr>
											<th><label for="comment">Comment:</label></th>
											<td>
												<!--todo: print field errors -->
												<textarea id="comment" name="comment"
														  dojoType="dijit.form.SimpleTextarea" rows="4" cols="50"
														  style="width:auto;">put comment here</textarea>
											</td>
										</tr>
										<tr>
											<th></th>
											<td style="text-align:left;">
												<button dojoType="dijit.form.Button" type="submit">Save</button>
											</td>
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
					<div class="rounded-small" style="float:right">
						<button dojoType="dijit.form.Button" id="previous"
								onClick="dijit.byId('assessment_stack').back()">&lt;</button>
						<span dojoType="dijit.layout.StackController" containerId="assessment_stack"></span>
						<button dojoType="dijit.form.Button" id="next"
								onClick="dijit.byId('assessment_stack').forward()">&gt;</button>
					</div>
					<div id="assessment_stack" dojoType="dijit.layout.StackContainer"
						 style="width: 90%; height: 18em; margin: 0.5em 0 0.5em 0; padding: 0.5em;">
						<c:forEach var="assessment" items="${assessments}" varStatus="row">
							<div dojoType="dijit.layout.ContentPane" title="${row.count}">
								<p style="margin-bottom:1em;">
									<strong>${assessment.reviewer.userDetails.username}</strong>
									gave ${eloRef.author.userDetails.username}'s ${eloRef.type} a score of
									<span style="color:#0066cc; font-weight: bold;">${assessment.score}</span> out of 10
									(${assessment.date})</p>

								<p style="font-size:x-large;">${assessment.comment}</p>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
</tiles:insertDefinition>