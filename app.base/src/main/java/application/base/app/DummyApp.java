package application.base.app;

import javax.swing.JFrame;

import application.definition.ApplicationDefinition;
import application.storage.LoadData;
import application.storage.StoreDetails;

public class DummyApp extends ApplicationBaseForGUI {
	private static final long serialVersionUID = 1L;

	public DummyApp() {
		System.out.println("DummyApp");
	}

	@Override
	public void start(JFrame frame) {
		System.out.println("start");
	}

	@Override
	public ApplicationDefinition createApplicationDefinition(Parameters parameters) {
		ApplicationDefinition definition = new ApplicationDefinition(parameters.getNamed().get("name"));
		return definition;
	}

	@Override
	public void configureStoreDetails() {
		LoadData loadData = new LoadData() {

			@Override
			public void run() {
			}

			@Override
			public void setFileName(String fileName) {
			}

			@Override
			public String fileName() {
				return null;
			}
		};
		storeDetails = new StoreDetails(loadData, "model", "dummy.dat");

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void terminate() {
		System.out.println("terminate");
	}
}
