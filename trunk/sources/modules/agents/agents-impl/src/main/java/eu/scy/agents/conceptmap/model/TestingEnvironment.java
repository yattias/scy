package eu.scy.agents.conceptmap.model;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.HashMap;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;

public class TestingEnvironment {
	
//	TupleSpace commandSpace;
	TupleSpace actionSpace;
	
	private TestingEnvironment() {
		// System.out.println("Test running");

		try {
//			commandSpace = new TupleSpace("127.0.0.1", 2525, AgentProtocol.COMMAND_SPACE_NAME);
			actionSpace = new TupleSpace("127.0.0.1", 2525, AgentProtocol.ACTION_SPACE_NAME);
			
//			action	15c388fa-6d93-4f57-a9a2-e62ba3d7145e	1284460592362	node_added	chris	scymapper	mission1	N/A	n/a	id=eef7e751-9c83-4c20-b99f-b2b0366cb798	model=	name=New concept
						
			// Add nodes
//			Tuple t1 = new Tuple(
//					"action", "15c388fa-6d93-4f57-a9a2-e62ba3d7145e", 
//					1284460592362L, "node_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb798",  "model="+getExampleXml(),
//					"name=New concept");
//			Tuple t2 = new Tuple(
//					"action", "15c388fa-6d93-4f57-a9a2-e62ba3d7145e", 
//					1284460592363L, "node_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb799",  "model="+getExampleXml(),
//					"name=New concept2");
//			Tuple t3 = new Tuple(
//					"action", "15c388fa-6d93-4f57-a9a2-e62ba3d7145e", 
//					1284460592364L, "node_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb790",  "model="+getExampleXml(),
//					"name=New concept3");
//			Tuple t2 = new Tuple(
//					"action", "fc739328-26f1-4a41-83a0-945581595467", 
//					1284460439337L, "node_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=6c41c093-adde-470a-8727-87d69a9a1da8", "model="+getExampleXml(), 
//					"name=New concept");
//			Tuple t3 = new Tuple(
//					"action", "15c388fa-6d93-4f57-a9a2-e62ba3d7145e", 
//					1284460592362L, "node_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb798", "model="+getExampleXml(),
//			"name=New concept");
//			
//			// Rename nodes - Not working - format has changed
//			Tuple t4 = new Tuple(
//					"action", "63eebb4d-2c36-4f45-8811-c7cfe36b1831", 
//					1284460444167L, "node_renamed", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=5849afc6-52a9-4a86-bab9-fbed51b700b5", 
//					"new=NodeA");
//			Tuple t5 = new Tuple(
//					"action", "41a5ebe5-059d-4ae4-988a-6448afa8a394", 
//					1284460448760L, "node_renamed", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=6c41c093-adde-470a-8727-87d69a9a1da8", 
//					"new=NodeB");
//			Tuple t6 = new Tuple(
//					"action", "675d9656-ada6-4b45-abd0-ec0f460a6909", 
//					1284460599549L, "node_renamed", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb798", 
//					"name=test");
			
//			
//			// Add edge - Not working - format has changed
//			Tuple t7 = new Tuple(
//					"action", "5d92b65c-f023-4077-9cc1-15cfec482cb8", 
//					1285666299614L, "link_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=78fb45bd-0990-4811-94fd-65cb08db598a",
//					"from_node=810fe641-d5ee-4a1f-b090-7e7cd1552d7d",
//					"name=",
//					"to_node=f8efe453-9c6d-4e8a-83e5-b0cf3491c233");
//			
//			// rename edge
//			Tuple t8 = new Tuple(
//					"action", "7e918c0b-78ce-47ef-b517-c7e937e88291", 
//					1284460458664L, "link_renamed", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=25505b00-dc96-4ebf-aac5-30c944ba1f5e", 
//					"new=Edge1");

//			Tuple t9 = new Tuple(
//					"action", "15c388fa-6d93-4f57-a9a2-e62ba3d7145e", 
//					1284460592369L, "synonym_added", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//					"id=eef7e751-9c83-4c20-b99f-b2b0366cb798",  "model="+getExampleXml(),
//					"synonym=syn");
//
//			Tuple printGraph = new Tuple(
//					"action", "675d9656-ada6-4b45-abd0-ec0f460a6910", 
//					12844605995100L, "print_graph", "chris", "scymapper", 
//					"mission1", "N/A",	"n/a", 
//			"id=eef7e751-9c83-4c20-b99f-b2b0366cb790");
			
			// These tuples were used for the example XML file  
//			1	28.09.2010 09:56:07	28.09.2010 09:56:07	-	action	e3898655-004f-424e-8fdd-b50547a9147d	1285667767271	node_added	chris	scymapper	mission1	N/A	n/a	id=a8c2c72e-f397-40ac-9ab5-5c61a244e02f	name=New concept
//			2	28.09.2010 09:56:07	28.09.2010 09:56:07	-	action	edd4f0f3-4f30-4d64-8f8b-2cd7c4f98c28	1285667767954	node_added	chris	scymapper	mission1	N/A	n/a	id=44ee664b-a355-4470-821c-f00efaad3887	name=New concept
//			3	28.09.2010 09:56:08	28.09.2010 09:56:08	-	action	019a9077-ca8b-4f41-92f4-bff1b883719d	1285667768681	node_added	chris	scymapper	mission1	N/A	n/a	id=a3f02708-ee92-4456-867e-1106b84e487b	name=New concept
//			4	28.09.2010 09:56:14	28.09.2010 09:56:14	-	action	0c5f5260-3d64-435d-b899-d81d6bb38aa8	1285667774490	link_added	chris	scymapper	mission1	N/A	n/a	id=eecd9e42-3520-4aab-bbaf-c22c6c1e2b2e	from_node=44ee664b-a355-4470-821c-f00efaad3887	name=	to_node=a8c2c72e-f397-40ac-9ab5-5c61a244e02f
//			5	28.09.2010 09:56:16	28.09.2010 09:56:16	-	action	41b61639-3da7-407a-92c1-e53ed9422b7c	1285667776629	link_added	chris	scymapper	mission1	N/A	n/a	id=4f17d1d2-6fec-4ec1-ba63-d1f7db7dae9b	from_node=a3f02708-ee92-4456-867e-1106b84e487b	name=	to_node=a8c2c72e-f397-40ac-9ab5-5c61a244e02f
//			6	28.09.2010 09:57:12	28.09.2010 09:57:12	-	action	fd5d8a7b-8b3d-456a-bf67-edef0daafca2	1285667832358	node_added	chris	scymapper	mission1	N/A	n/a	id=888c8a5b-0031-48f6-b98b-50e102464b59	name=New concept
//			7	28.09.2010 09:57:13	28.09.2010 09:57:13	-	action	54eec4ed-f4fc-4e13-84b3-52f88467ebbd	1285667833508	node_added	chris	scymapper	mission1	N/A	n/a	id=d994484c-c06d-40c4-9689-de27d6357079	name=New concept
			
//			actionSpace.write(t1);
//			actionSpace.write(t2);
//			actionSpace.write(t3);
//			actionSpace.write(t4);
//			actionSpace.write(t5);
//			actionSpace.write(t6);
//			actionSpace.write(t7);
//			actionSpace.write(t8);
//			actionSpace.write(t9);

//			actionSpace.write(printGraph);

		} catch (TupleSpaceException e) {
			// System.out.println("Error connecting to TupleSpace");
		}

	}

	public static void main(String[] args) {
		
		try {
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put(AgentProtocol.PARAM_AGENT_ID, "a nice little id should be inserted here");
			m.put(AgentProtocol.TS_HOST, "127.0.0.1");
			m.put(AgentProtocol.TS_PORT, 2525);
			UserConceptMapAgent agent = new UserConceptMapAgent(m);
			agent.start();
			// System.out.println("Agent started");
			
			Thread.sleep(1000);
			new TestingEnvironment();
				
		} catch (AgentLifecycleException e) {
			// System.out.println("Error in AgentLifecycle");
		} catch (InterruptedException e) {
			// System.out.println("Interrupted!");
			e.printStackTrace();
		}
	}

	protected static String getExampleXml() {
		return "<eu.scy.scymapper.impl.model.DefaultConceptMap>"+
		  "<diagram class=\"eu.scy.scymapper.impl.DiagramModel\">"+
		    "<nodes>"+
		      "<eu.scy.scymapper.impl.model.NodeModel>"+
		        "<label>New concept</label>"+
		        "<shape class=\"eu.scy.scymapper.impl.shapes.nodes.Diamond\">"+
		          "<mode>1</mode>"+
		          "<xPoints>"+
		            "<int>100</int>"+
		            "<int>200</int>"+
		            "<int>100</int>"+
		            "<int>0</int>"+
		          "</xPoints>"+
		          "<yPoints>"+
		            "<int>0</int>"+
		            "<int>100</int>"+
		            "<int>200</int>"+
		            "<int>100</int>"+
		          "</yPoints>"+
		          "<star serialization=\"custom\">"+
		            "<unserializable-parents/>"+
		            "<java.awt.geom.Path2D_-Float>"+
		              "<default/>"+
		              "<byte>48</byte>"+
		              "<int>5</int>"+
		              "<int>8</int>"+
		              "<byte>1</byte>"+
		              "<byte>64</byte>"+
		              "<float>100.0</float>"+
		              "<float>0.0</float>"+
		              "<byte>65</byte>"+
		              "<float>200.0</float>"+
		              "<float>100.0</float>"+
		              "<byte>65</byte>"+
		              "<float>100.0</float>"+
		              "<float>200.0</float>"+
		              "<byte>65</byte>"+
		              "<float>0.0</float>"+
		              "<float>100.0</float>"+
		              "<byte>96</byte>"+
		              "<byte>97</byte>"+
		            "</java.awt.geom.Path2D_-Float>"+
		          "</star>"+
		        "</shape>"+
		        "<style class=\"eu.scy.scymapper.impl.model.DefaultNodeStyle\">"+
		          "<foregroundColor>"+
		            "<red>0</red>"+
		            "<green>0</green>"+
		            "<blue>0</blue>"+
		            "<alpha>255</alpha>"+
		          "</foregroundColor>"+
		          "<backgroundColor>"+
		            "<red>0</red>"+
		            "<green>78</green>"+
		            "<blue>194</blue>"+
		            "<alpha>255</alpha>"+
		          "</backgroundColor>"+
		          "<stroke class=\"java.awt.BasicStroke\">"+
		            "<width>2.0</width>"+
		            "<join>0</join>"+
		            "<cap>2</cap>"+
		            "<miterlimit>10.0</miterlimit>"+
		            "<dash__phase>0.0</dash__phase>"+
		          "</stroke>"+
		          "<opaque>true</opaque>"+
		          "<minWidth>60</minWidth>"+
		          "<minHeight>46</minHeight>"+
		          "<shadow>true</shadow>"+
		          "<border class=\"javax.swing.border.EmptyBorder\">"+
		            "<left>0</left>"+
		            "<right>0</right>"+
		            "<top>0</top>"+
		            "<bottom>0</bottom>"+
		          "</border>"+
		        "</style>"+
		        "<labelHidden>false</labelHidden>"+
		        "<deleted>false</deleted>"+
		        "<constraints class=\"eu.scy.scymapper.impl.model.NodeModelConstraints\">"+
		          "<canMove>true</canMove>"+
		          "<canDelete>true</canDelete>"+
		          "<canResize>true</canResize>"+
		          "<canEditLabel>true</canEditLabel>"+
		          "<canConnect>true</canConnect>"+
		          "<canSelect>true</canSelect>"+
		          "<canChangeStyle>true</canChangeStyle>"+
		          "<alwaysOnTop>false</alwaysOnTop>"+
		        "</constraints>"+
		        "<x>239</x>"+
		        "<y>102</y>"+
		        "<height>100</height>"+
		        "<width>100</width>"+
		        "<id>a8c2c72e-f397-40ac-9ab5-5c61a244e02f</id>"+
		      "</eu.scy.scymapper.impl.model.NodeModel>"+
		      "<eu.scy.scymapper.impl.model.NodeModel>"+
		        "<label>New concept</label>"+
		        "<shape class=\"eu.scy.scymapper.impl.shapes.nodes.Ellipse\">"+
		          "<mode>1</mode>"+
		        "</shape>"+
		        "<style class=\"eu.scy.scymapper.impl.model.DefaultNodeStyle\">"+
		          "<foregroundColor>"+
		            "<red>0</red>"+
		            "<green>0</green>"+
		            "<blue>0</blue>"+
		            "<alpha>255</alpha>"+
		          "</foregroundColor>"+
		          "<backgroundColor>"+
		            "<red>0</red>"+
		            "<green>78</green>"+
		            "<blue>194</blue>"+
		            "<alpha>255</alpha>"+
		          "</backgroundColor>"+
		          "<stroke class=\"java.awt.BasicStroke\">"+
		            "<width>2.0</width>"+
		            "<join>0</join>"+
		            "<cap>2</cap>"+
		            "<miterlimit>10.0</miterlimit>"+
		            "<dash__phase>0.0</dash__phase>"+
		          "</stroke>"+
		          "<opaque>true</opaque>"+
		          "<minWidth>60</minWidth>"+
		          "<minHeight>46</minHeight>"+
		          "<shadow>true</shadow>"+
		          "<border class=\"javax.swing.border.EmptyBorder\" reference=\"../../../eu.scy.scymapper.impl.model.NodeModel/style/border\"/>"+
		        "</style>"+
		        "<labelHidden>false</labelHidden>"+
		        "<deleted>false</deleted>"+
		        "<constraints class=\"eu.scy.scymapper.impl.model.NodeModelConstraints\">"+
		          "<canMove>true</canMove>"+
		          "<canDelete>true</canDelete>"+
		          "<canResize>true</canResize>"+
		          "<canEditLabel>true</canEditLabel>"+
		          "<canConnect>true</canConnect>"+
		          "<canSelect>true</canSelect>"+
		          "<canChangeStyle>true</canChangeStyle>"+
		          "<alwaysOnTop>false</alwaysOnTop>"+
		        "</constraints>"+
		        "<x>112</x>"+
		        "<y>94</y>"+
		        "<height>100</height>"+
		        "<width>100</width>"+
		        "<id>44ee664b-a355-4470-821c-f00efaad3887</id>"+
		      "</eu.scy.scymapper.impl.model.NodeModel>"+
		      "<eu.scy.scymapper.impl.model.NodeModel>"+
		        "<label>New concept</label>"+
		        "<shape class=\"eu.scy.scymapper.impl.shapes.nodes.Diamond\" reference=\"../../eu.scy.scymapper.impl.model.NodeModel/shape\"/>"+
		        "<style class=\"eu.scy.scymapper.impl.model.DefaultNodeStyle\">"+
		          "<foregroundColor reference=\"../../../eu.scy.scymapper.impl.model.NodeModel/style/foregroundColor\"/>"+
		          "<backgroundColor reference=\"../../../eu.scy.scymapper.impl.model.NodeModel/style/backgroundColor\"/>"+
		          "<stroke class=\"java.awt.BasicStroke\" reference=\"../../../eu.scy.scymapper.impl.model.NodeModel/style/stroke\"/>"+
		          "<opaque>true</opaque>"+
		          "<minWidth>60</minWidth>"+
		          "<minHeight>46</minHeight>"+
		          "<shadow>true</shadow>"+
		          "<border class=\"javax.swing.border.EmptyBorder\" reference=\"../../../eu.scy.scymapper.impl.model.NodeModel/style/border\"/>"+
		        "</style>"+
		        "<labelHidden>false</labelHidden>"+
		        "<deleted>false</deleted>"+
		        "<constraints class=\"eu.scy.scymapper.impl.model.NodeModelConstraints\">"+
		          "<canMove>true</canMove>"+
		          "<canDelete>true</canDelete>"+
		          "<canResize>true</canResize>"+
		          "<canEditLabel>true</canEditLabel>"+
		          "<canConnect>true</canConnect>"+
		          "<canSelect>true</canSelect>"+
		          "<canChangeStyle>true</canChangeStyle>"+
		          "<alwaysOnTop>false</alwaysOnTop>"+
		        "</constraints>"+
		        "<x>210</x>"+
		        "<y>254</y>"+
		        "<height>100</height>"+
		        "<width>100</width>"+
		        "<id>a3f02708-ee92-4456-867e-1106b84e487b</id>"+
		      "</eu.scy.scymapper.impl.model.NodeModel>"+
		    "</nodes>"+
		    "<links>"+
		      "<eu.scy.scymapper.impl.model.NodeLinkModel>"+
		        "<label></label>"+
		        "<shape class=\"eu.scy.scymapper.impl.shapes.links.Arrow\">"+
		          "<arrowhead>"+
		            "<length>10.0</length>"+
		            "<fixedSize>true</fixedSize>"+
		          "</arrowhead>"+
		          "<lineShape class=\"eu.scy.scymapper.impl.shapes.links.CubicCurvedLine\">"+
		            "<curving>0.75</curving>"+
		          "</lineShape>"+
		        "</shape>"+
		        "<style class=\"eu.scy.scymapper.impl.model.DefaultLinkStyle\">"+
		          "<foregroundColor>"+
		            "<red>51</red>"+
		            "<green>51</green>"+
		            "<blue>51</blue>"+
		            "<alpha>255</alpha>"+
		          "</foregroundColor>"+
		          "<stroke class=\"java.awt.BasicStroke\">"+
		            "<width>1.0</width>"+
		            "<join>0</join>"+
		            "<cap>2</cap>"+
		            "<miterlimit>10.0</miterlimit>"+
		            "<dash__phase>0.0</dash__phase>"+
		          "</stroke>"+
		          "<backgroundColor>"+
		            "<red>0</red>"+
		            "<green>0</green>"+
		            "<blue>0</blue>"+
		            "<alpha>255</alpha>"+
		          "</backgroundColor>"+
		        "</style>"+
		        "<labelHidden>false</labelHidden>"+
		        "<id>eecd9e42-3520-4aab-bbaf-c22c6c1e2b2e</id>"+
		        "<fromNode class=\"eu.scy.scymapper.impl.model.NodeModel\" reference=\"../../../nodes/eu.scy.scymapper.impl.model.NodeModel[2]\"/>"+
		        "<toNode class=\"eu.scy.scymapper.impl.model.NodeModel\" reference=\"../../../nodes/eu.scy.scymapper.impl.model.NodeModel\"/>"+
		        "<myLabel></myLabel>"+
		      "</eu.scy.scymapper.impl.model.NodeLinkModel>"+
		      "<eu.scy.scymapper.impl.model.NodeLinkModel>"+
		        "<label></label>"+
		        "<shape class=\"eu.scy.scymapper.impl.shapes.links.Arrow\">"+
		          "<arrowhead>"+
		            "<length>10.0</length>"+
		            "<fixedSize>true</fixedSize>"+
		          "</arrowhead>"+
		          "<tail>"+
		            "<length>10.0</length>"+
		            "<fixedSize>true</fixedSize>"+
		          "</tail>"+
		          "<lineShape class=\"eu.scy.scymapper.impl.shapes.links.CubicCurvedLine\" reference=\"../../../eu.scy.scymapper.impl.model.NodeLinkModel/shape/lineShape\"/>"+
		        "</shape>"+
		        "<style class=\"eu.scy.scymapper.impl.model.DefaultLinkStyle\">"+
		          "<foregroundColor>"+
		            "<red>51</red>"+
		            "<green>51</green>"+
		            "<blue>51</blue>"+
		            "<alpha>255</alpha>"+
		          "</foregroundColor>"+
		          "<stroke class=\"java.awt.BasicStroke\">"+
		            "<width>1.0</width>"+
		            "<join>0</join>"+
		            "<cap>2</cap>"+
		            "<miterlimit>10.0</miterlimit>"+
		            "<dash__phase>0.0</dash__phase>"+
		          "</stroke>"+
		          "<backgroundColor reference=\"../../../eu.scy.scymapper.impl.model.NodeLinkModel/style/backgroundColor\"/>"+
		        "</style>"+
		        "<labelHidden>false</labelHidden>"+
		        "<id>4f17d1d2-6fec-4ec1-ba63-d1f7db7dae9b</id>"+
		        "<fromNode class=\"eu.scy.scymapper.impl.model.NodeModel\" reference=\"../../../nodes/eu.scy.scymapper.impl.model.NodeModel[3]\"/>"+
		        "<toNode class=\"eu.scy.scymapper.impl.model.NodeModel\" reference=\"../../../nodes/eu.scy.scymapper.impl.model.NodeModel\"/>"+
		        "<myLabel></myLabel>"+
		      "</eu.scy.scymapper.impl.model.NodeLinkModel>"+
		    "</links>"+
		  "</diagram>"+
		"</eu.scy.scymapper.impl.model.DefaultConceptMap>";
	}
}
