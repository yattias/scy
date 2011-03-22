package eu.scy.agents;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import roolo.elo.api.IELO;
import roolo.elo.content.BasicContent;

public class ModelEloCreator extends AbstractTestFixture {

	private IELO elo;
	private String eloTitle;

	public static void main(String[] args) throws Exception {
		ModelEloCreator modelEloCreator = args.length == 1 ? new ModelEloCreator()
				: new ModelEloCreator(args[1]);
		InputStream in = new FileInputStream(args[0]);
		modelEloCreator.createElo(in);
		modelEloCreator.saveElo();
	}

	public ModelEloCreator() throws Exception {
		this("modelElo");
	}

	public ModelEloCreator(String string) throws Exception {
		super.setUp();
		eloTitle = string;
	}

	private void saveElo() {
		repository.addNewELO(elo);
	}

	private void createElo(InputStream in) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			copy(in, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		elo = createNewElo(eloTitle, "scy/agentmodel");
		elo.setContent(new BasicContent(bytes.toByteArray()));
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[2048];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
}
