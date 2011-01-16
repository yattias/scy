package eu.scy.client.tools.formauthor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class ViewData extends JFrame {
	private DataFormElementModel _dfem;
	private JButton jbtnPrev;
	private JButton jbtnNext;
	private JPanel jpCenter;
	private Integer currentPos = 1;
	private JPanel jpBottom;
	private JLabel jlPositionLbl;

	public ViewData(DataFormElementModel dfem) {
		_dfem = dfem;
		initView();
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(300, 200));
		pack();
		setVisible(true);
	}

	private void initView() {
		createGUI();
	}

	private void createGUI() {
		setLayout(new BorderLayout());
		jbtnPrev = new JButton();
		jbtnPrev.setToolTipText(Localizer.getString("PREV_DATA"));
		jbtnNext = new JButton();
		jbtnNext.setToolTipText(Localizer.getString("NEXT_DATA"));

		ImageIcon icon;
		try {
			icon = IconCreator.createImageIcon(this.getClass().getResource("images/arrow_left.png"), "vorheriger Datensatz");
			jbtnPrev.setIcon(icon);

			icon = IconCreator.createImageIcon(this.getClass().getResource("images/arrow_right.png"), "vorheriger Datensatz");
			jbtnNext.setIcon(icon);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ControllListener cl = new ControllListener();

		jbtnPrev.addActionListener(cl);
		jbtnNext.addActionListener(cl);

		jpCenter = new JPanel(new BorderLayout());

		add(jbtnPrev, BorderLayout.LINE_START);
		add(jpCenter, BorderLayout.CENTER);
		add(jbtnNext, BorderLayout.LINE_END);

		if (_dfem.getCardinality() == "0" || new Integer(_dfem.getCardinality()) > 1) {
			jbtnPrev.setEnabled(false);
			jbtnNext.setEnabled(false);
		}

		jlPositionLbl = new JLabel();

		jpBottom = new JPanel();
		Border bdrSelected = BorderFactory.createRaisedBevelBorder();
		jpBottom.setBorder(bdrSelected);
		jpBottom.add(jlPositionLbl);
		add(jpBottom, BorderLayout.PAGE_END);

		activateButtons(_dfem.getDataList().size());
		jpCenter.setBorder(bdrSelected);
		createDataView(jpCenter);

	}

	private void createDataView(Container container) {
		container.removeAll();
		switch (_dfem.getType()) {
		case IMAGE:
			createImageView(container);
			break;
		case VOICE:
			createAudioView(container);
			break;
		case GPS:
			createMapView(container);
			break;
		default:
			createTextView(container);
			break;
		}
		if (_dfem.getEvents().size() > 0)
			createEventView(container);

	}

	private void createMapView(Container container) {
		JPanel imgContainer = new JPanel(new BorderLayout());
		imgContainer.setBorder(BorderFactory.createTitledBorder(_dfem.getType().toString() + " (Click on the image to open in Google Maps)"));
		container.add(imgContainer, BorderLayout.PAGE_START);
		try {
			ByteArrayInputStream baos = new ByteArrayInputStream(_dfem.getStoredData(currentPos - 1));
			ObjectInputStream in;
			in = new ObjectInputStream(baos);
			final String[] coords = (String[]) in.readObject();
			in.close();
			ImagePanel imagePanel = new ImagePanel(new URI("http://maps.google.com/maps/api/staticmap?size=512x512&maptype=roadmap&markers=color:blue%7C" + coords[0] + "," + coords[1] + "&sensor=false"));
			imgContainer.add(imagePanel, BorderLayout.CENTER);
			imagePanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI("http://maps.google.de/?ie=UTF8&ll=" + coords[0] + "," + coords[1] + "&z=16"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTextView(Container container) {
		JPanel jpEventData = new JPanel(new BorderLayout());

		JTextArea jtTextArea = new JTextArea();
		jtTextArea.setText(new String(_dfem.getStoredData(currentPos - 1)));
		jpEventData.add(jtTextArea);
		Border bdr = BorderFactory.createTitledBorder(_dfem.getType().toString());
		jpEventData.setBorder(bdr);
		container.add(jpEventData);

	}

	private void createAudioView(Container container) {
		// JPanel jpEventData = new JPanel(new BorderLayout());
		//
		// JTextArea jtTextArea = new JTextArea();
		// jtTextArea.setText(new String(_dfem.getStoredData(currentPos - 1)));
		// jpEventData.add(jtTextArea);
		// Border bdr = BorderFactory.createTitledBorder(_dfem.getType()
		// .toString());
		// jpEventData.setBorder(bdr);
		PlayerPanel pp;
		pp = new PlayerPanel(_dfem.getStoredData(currentPos - 1));
		container.add(pp);

	}

	private void createEventView(Container container) {
		JPanel jpEventData = new JPanel();
		jpEventData.setLayout(new GridLayout(_dfem.getEvents().size() + 1, 3));

		// AddHeader
		jpEventData.add(new JLabel(Localizer.getString("EVENT_TYPE")));
		jpEventData.add(new JLabel(Localizer.getString("EVENT_DATA_TYPE")));
		jpEventData.add(new JLabel(Localizer.getString("DATA")));

		for (DataFormElementEventModel dfeem : _dfem.getEvents()) {
			JLabel jlEventTyp = new JLabel(dfeem.getEventType().toString());
			JLabel jlEventDataTyp = new JLabel(dfeem.getEventDataType().toString());

			jpEventData.add(jlEventTyp);
			jpEventData.add(jlEventDataTyp);
			switch (dfeem.getEventDataType()) {
			case GPS:
				byte[] storedData = dfeem.getStoredData(currentPos - 1);
				if (storedData != null) {
					ByteArrayInputStream baos = new ByteArrayInputStream(storedData);
					ObjectInputStream in;
					JButton jlEventData = new JButton("Google Maps");
					try {
						in = new ObjectInputStream(baos);
						final String[] coords = (String[]) in.readObject();

						in.close();
						jlEventData.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								java.awt.Desktop desk = java.awt.Desktop.getDesktop();

								try {
									desk.browse(new URI("http://maps.google.com/?ie=UTF8&ll=" + coords[0] + "," + coords[1] + "&spn=0.001876,0.00457&t=h&z=18"));
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (URISyntaxException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
						});
						jpEventData.add(jlEventData);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					JLabel jlNoGPS = new JLabel(Localizer.getString("NOGPS"));
					jpEventData.add(jlNoGPS);
				}

				break;

			default:
				JLabel jlEventDataLabel = new JLabel(new String(dfeem.getStoredData(currentPos - 1)));
				jpEventData.add(jlEventDataLabel);
				break;
			}
		}
		Border bdr = BorderFactory.createTitledBorder(Localizer.getString("EVENTS") + ":");
		jpEventData.setBorder(bdr);

		container.add(jpEventData, BorderLayout.PAGE_END);
	}

	private void createImageView(Container container) {
		try {
			JPanel imgContainer = new JPanel(new BorderLayout());

			Border bdr = BorderFactory.createTitledBorder(Localizer.getString("IMAGE") + ":");
			imgContainer.setBorder(bdr);

			imgContainer.add(new ImagePanel(_dfem.getStoredData(currentPos - 1)), BorderLayout.CENTER);
			container.add(imgContainer, BorderLayout.PAGE_START);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void activateButtons(Integer count) {
		if (currentPos > 1 && count > 1)
			jbtnPrev.setEnabled(true);
		else
			jbtnPrev.setEnabled(false);

		if (currentPos < count)
			jbtnNext.setEnabled(true);
		else
			jbtnNext.setEnabled(false);

		jlPositionLbl.setText(Localizer.getString("DATASET") + " " + currentPos.toString() + " " + Localizer.getString("OF") + " " + _dfem.getDataList().size());

		createDataView(jpCenter);

	}

	private class ControllListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			Integer count = _dfem.getDataList().size();
			if (source == jbtnPrev) {
				if (currentPos > 1) {
					currentPos--;
				}
			}
			if (source == jbtnNext) {
				if (currentPos < count) {
					currentPos++;
				}
			}

			activateButtons(count);
		}

	}

	private class ImagePanel extends JPanel {

		// image object
		private Image img;

		public ImagePanel(byte[] imgArray) throws IOException {

			// load image
			InputStream in = new ByteArrayInputStream(_dfem.getStoredData(currentPos - 1));
			img = ImageIO.read(in);
			this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
		}
		
		public ImagePanel(URI uri) throws IOException {
			InputStream openStream = uri.toURL().openStream();
			img = ImageIO.read(openStream);
			this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
		}

		// override paint method of panel
		public void paint(Graphics g) {
			// draw the image
			if (img != null)
				g.drawImage(img, this.getWidth() / 2 - img.getWidth(this) / 2, this.getHeight() / 2 - img.getHeight(this) / 2, this);
		}

	}

}
