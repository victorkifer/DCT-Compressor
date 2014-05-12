package ui;

import compressor.Compressor;
import compressor.image.CImage;
import compressor.image.JPEG;
import compressor.internal.Config;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import java.io.IOException;

public class MainForm {

  private Display mDisplay;
  private Shell mShell;
  private Label imageView;

  Compressor compressor;

  public MainForm() {
    mDisplay = Display.getDefault();
    mShell = new Shell(mDisplay);

    mShell.setSize(550, 600);
    mShell.setLayout(new FillLayout());
    mShell.setText(Display.getAppName());

    compressor = new Compressor(Config.DEFAULT_QUALITY);

    createUI();
    createMenuBar();
  }

  private void createUI() {
    imageView = new Label(mShell, SWT.CENTER | SWT.V_SCROLL | SWT.H_SCROLL);
  }

  private void createMenuBar() {
    Menu menuBar = new Menu(mShell, SWT.BAR);
    Menu fileMenu, helpMenu;

    MenuItem fileMenuHeader, helpMenuHeader;

    MenuItem fileOpenItem, fileSaveAsItem, fileExitItem;
    MenuItem helpAboutItem;

    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    fileMenuHeader.setText("&File");

    fileMenu = new Menu(mShell, SWT.DROP_DOWN);
    fileMenuHeader.setMenu(fileMenu);

    fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
    fileOpenItem.setText("&Open image");
    fileOpenItem.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        openFile();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        openFile();
      }
    });

    fileSaveAsItem = new MenuItem(fileMenu, SWT.PUSH);
    fileSaveAsItem.setText("&Save");
    fileSaveAsItem.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        saveAsFile();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        saveAsFile();
      }
    });

    fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
    fileExitItem.setText("E&xit");
    fileExitItem.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        close();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        close();
      }
    });

    helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
    helpMenuHeader.setText("&Help");

    helpMenu = new Menu(mShell, SWT.DROP_DOWN);
    helpMenuHeader.setMenu(helpMenu);

    helpAboutItem = new MenuItem(helpMenu, SWT.PUSH);
    helpAboutItem.setText("&About");
    helpAboutItem.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        about();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        about();
      }
    });

    mShell.setMenuBar(menuBar);
  }

  public void openFile() {
    FileDialog dialog = new FileDialog (mShell, SWT.OPEN);
    dialog.setFilterExtensions(new String [] {"*.jpg", "*.png", "*.cimg"});
    String fileName = dialog.open();

    if(fileName == null || fileName.isEmpty())
      return;

    Image image = null;

    if(fileName.endsWith(".cimg")) {
      try {
        CImage cimg = CImage.fromFile(fileName);

        JPEG img = compressor.decompressImage(cimg);

        ImageData imageData = img.getImageData();

        image = new Image(mDisplay, imageData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      image = new Image(mDisplay, fileName);
    }

    if(image != null) {
      imageView.setImage(image);
    }
  }

  public void saveAsFile() {
    FileDialog dialog = new FileDialog (mShell, SWT.SAVE);
    dialog.setFilterExtensions(new String [] {"*.jpg", "*.cimg"});
    String fileName = dialog.open();

    if(fileName == null || fileName.isEmpty())
      return;


    if(imageView.getImage() == null)
      return;

    ImageData imageData = imageView.getImage().getImageData();
    JPEG jpeg = JPEG.fromImageData(imageData);

    try {

      if(fileName.endsWith(".cimg")) {
        CImage img = compressor.compressImage(jpeg);
        img.toFile(fileName);
      } else {
        jpeg.toFile(fileName);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void about() {
    MessageBox aboutDialog = new MessageBox(mShell,
        SWT.ICON_INFORMATION |
            SWT.OK);
    aboutDialog.setText("About");
    aboutDialog.setMessage(Display.getAppName() + "\nVersion " + Display.getAppVersion() + "\nAuthor: Dariya Yagolnyk\n2014");
    aboutDialog.open();
  }

  public void show() {
    mShell.open();
    while(!mShell.isDisposed())
      if(!mDisplay.readAndDispatch())
        mDisplay.sleep();
    mDisplay.dispose();
  }

  public void close() {
    mShell.close();
    mDisplay.dispose();
  }

}
