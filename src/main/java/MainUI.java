import org.eclipse.swt.widgets.Display;
import ui.MainForm;

public class MainUI {

  public static void main(String[] args) {
    Display.setAppName("DCT Compressor");
    Display.setAppVersion("1.0");

    MainForm mainForm = new MainForm();
    mainForm.show();
  }

}
