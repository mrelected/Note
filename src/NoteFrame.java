import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Scanner;
import java.io.*;


public class NoteFrame extends JFrame {
    MenuBar menuBar = new MenuBar();
    private MenuItem menuNew;
    private MenuItem menuOpen;
    //private MenuItem menuSave;
    private MenuItem menuSaveas;
    private MenuItem menuExit;
    private MenuItem menuAbout;
    private JTextArea theText;
    private JScrollPane theScroll;
    private boolean change = false;
    private String theName = "";

    public NoteFrame() {
        super("Note");
        initMenu();
        initMainPanel();
        initListeners();
    }

    private void initMenu() {
        PopupMenu fileMenu = new PopupMenu("File");
        PopupMenu helpMenu = new PopupMenu("Help");

        menuNew = new MenuItem("New");
        menuOpen = new MenuItem("Open");
        //menuSave = new MenuItem("Save");
        menuSaveas = new MenuItem("Save as...");
        menuExit = new MenuItem("Exit");
        menuAbout = new MenuItem("About");

        fileMenu.add(menuNew);
        fileMenu.add(menuOpen);
        //fileMenu.add(menuSave);
        fileMenu.add(menuSaveas);
        fileMenu.add(menuExit);

        helpMenu.add(menuAbout);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        this.setMenuBar(menuBar);

    }

    private void initMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        theText = new JTextArea();
        theText.getDocument().addDocumentListener(new DocumentListenerImpl());
        theText.setLineWrap(true);
        theText.setWrapStyleWord(true);
        theScroll = new JScrollPane(theText);
        theScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(theScroll, BorderLayout.CENTER);
        getContentPane().add(panel);

    }

    private void ClearParametrs() {
        theName = "";
        theText.setText("");
        change = false;
    }

    /*private boolean Saving(String theName) throws IOException {
        if ( theName.equals("") )   {
            try {
                saveAsDocument();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            if (theName == null)  {
                return false;
            } else {
                WriteIntoFile(theName);

                return true;
            }
        } else {
            WriteIntoFile(theName);
            return true;
        }
    }


    private void WriteIntoFile(String theName) throws IOException {
        FileWriter fstream = new FileWriter(theName);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(theText.getText());
        out.close();
    }*/
    private void exit() throws IOException {
        if (change == true) {
            int replay = JOptionPane.showConfirmDialog(null, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (replay == JOptionPane.YES_OPTION)
                try {
                    saveAsDocument();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            if (replay == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            if (replay == JOptionPane.CANCEL_OPTION) {
                //replay.setVisible(false);
                this.dispose();
            }
        } else {
            System.exit(0);
        }
    }

    private void openDocument() throws IOException {
        JFileChooser open = new JFileChooser();
        int option = open.showDialog(null, "Open file");
        if (option == JFileChooser.APPROVE_OPTION) {
            theText.setText("");
            try {
                Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getName()));
                while (scan.hasNext())
                    theText.append(scan.nextLine() + "\n");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(null, "Open file error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsDocument() throws IOException {
        JFileChooser save = new JFileChooser();
        int option = save.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File f = new File(save.getSelectedFile().getAbsolutePath());
            String theName = f.getName();
            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(save, "File already exist overwrite?") == JOptionPane.YES_OPTION) {
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
                        out.write(theText.getText());
                        out.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } else {
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
                    out.write(theText.getText());
                    out.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    private void initListeners() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    exit();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        menuNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (change == true) {
                    int replay = JOptionPane.showConfirmDialog(null, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (replay == JOptionPane.YES_OPTION)
                        try {
                            saveAsDocument();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    if (replay == JOptionPane.NO_OPTION) {
                        ClearParametrs();
                    }
                } else {
                    ClearParametrs();
                }
                //TODO: Create file
            }
        });
        menuAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Nothing interesting here(", getTitle(), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    exit();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        menuSaveas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == menuSaveas) {
                    try {
                        saveAsDocument();
                        change = false;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
       /* menuSave.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         if (e.getSource() == menuSave) {
         try {
         Saving(theName);
         } catch (Exception ex) {
         System.out.println(ex.getMessage());
         }
         }
         //TODO: save file
         }
         });*/
        menuOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == menuOpen) {

                    /*open.addChoosableFileFilter(new FileFilter(){
                        public String getDescription(){
                            return "Text documents (*.txt)";
                        }
                        public boolean accept(File f){
                            if (f.isDirectory()){
                                return true;
                            } else {
                                return f.getName().toLowerCase().endsWith(".txt");
                            }

                    } } );}*/
                    if (change == true) {
                        int replay = JOptionPane.showConfirmDialog(null, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (replay == JOptionPane.YES_OPTION)
                            try {
                                saveAsDocument();
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        if (replay == JOptionPane.NO_OPTION) {
                            try {
                                openDocument();
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                        //TODO: open file
                    } else {
                        try {
                            openDocument();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    private class DocumentListenerImpl implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            change = true;
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            change = true;
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            change = true;
        }
    }
}
