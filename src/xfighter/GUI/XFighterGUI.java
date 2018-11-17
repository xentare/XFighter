/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.PathingEntity;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.types.TargetEvent;
import xfighter.Helpers;
import xfighter.Location;
import xfighter.SaveSettings;
import xfighter.Settings;
import xfighter.XFighter;

public class XFighterGUI implements Serializable {
    
    // Open/Close button on paint
    public static Rectangle openGUIButton = new Rectangle(555, 440, 50, 12);
    
    private Listeners listeners;
    private static JFrame frame;
    
    public static JTextField fightAreaCentralTileXField;
    private JLabel fightAreaCentralTileXLabel;
    public static JTextField fightAreaCentralTileYField;
    private JLabel fightAreaCentralTileYLabel;
    private JLabel fightAreaCentralTileLabel;
    
    private JButton getCentralTileButton;
    
    public static JTextField fightAreaSizeField;
    private JLabel fightAreaSizeLabel;
    
    private JButton launchButton;
    private JButton refreshParametersButton;
    
    private JCheckBox enablePaint;
    
    //NPC panel
    public static JList attackableNpcsField;
    public static JList selectedNpcsField;
    public static DefaultListModel attackableNpcsListModel;
    public static DefaultListModel selectedNpcsListModel;
    
    // Item panel
    public static JTable itemTable;
    public static DefaultTableModel itemTableModel;
    
    // Food panel
    public static JTextField foodName;
    public static JTextField foodAmount;
    
    // Save & Load button
    public static JButton saveButton;
    public static JButton loadButton;
    public static JTextField fileName;
    
    private String path;
    
//    public static XFighterGUI getInstance() {
//        return instance;
//    }
    
    public XFighterGUI(){
        
        try {
        path = new File(XFighter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException ex) {
            System.out.println("Couldn't fetch rspeer path. Saving/Loading won't work.");
        }
        
        frame = new JFrame();
        frame.setTitle("XFighter configuration");
        
        frame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        frame.add(getFightAreaPanel());
        
        frame.add(getNPCPanel());
        
        frame.add(getLootPanel());
        
        frame.add(getFoodPanel());
        
        frame.add(getSaveLoadPanel());
        
        // launch button
        launchButton = new JButton("Launch");
        frame.add(launchButton);
        launchButton.addActionListener((ActionEvent e) -> {
            frame.setVisible(false);
            Settings.started = true;
        });
        
        // refresh params button
        refreshParametersButton = new JButton("Refresh parameters");
        refreshParametersButton.setToolTipText("You can refresh parameters when the bot is running.");
        frame.add(refreshParametersButton);
        refreshParametersButton.addActionListener((ActionEvent e) -> {
            refreshParameters();
        });
        
        // enable paint
        enablePaint = new JCheckBox("Enable paint");
        enablePaint.addActionListener((ActionEvent e) -> {
            if (enablePaint.isSelected()) {
                Settings.paintEnabled = true;
            }else {
                Settings.paintEnabled = false;
            }
        });
        
        frame.add(enablePaint);
        
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setPreferredSize(new Dimension(500, 1000));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        listeners = new Listeners(this);
        
    }
//    public static void main(String... args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new XFighterGUI();
//            }
//            
//        });
//    }

    private JPanel getFightAreaPanel() {
        
        // central tile
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new FlowLayout());
        fightAreaCentralTileLabel = new JLabel();
        fightAreaCentralTileLabel.setText("Central tile:");
        panel.add(fightAreaCentralTileLabel);
        
        // x
        fightAreaCentralTileXLabel = new JLabel("x");
        panel.add(fightAreaCentralTileXLabel);
        fightAreaCentralTileXField = new JTextField("3030");
        panel.add(fightAreaCentralTileXField);
        
        // y
        fightAreaCentralTileYLabel = new JLabel("y");
        panel.add(fightAreaCentralTileYLabel);
        fightAreaCentralTileYField = new JTextField("3236");
        panel.add(fightAreaCentralTileYField);
        
        // get fight area
        getCentralTileButton = new JButton("Get central tile");
        panel.add(getCentralTileButton);
        getCentralTileButton.addActionListener((ActionEvent e) -> {
            loadCentralTile();
        });
        
        // fight area size
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));     
        fightAreaSizeLabel = new JLabel("Size:");
        panel2.add(fightAreaSizeLabel);        
        fightAreaSizeField = new JTextField("5");
        panel2.add(fightAreaSizeField);
        
        // fight area panel
        JPanel fightAreaPanel = new JPanel();
        fightAreaPanel.setLayout(new GridLayout(2,1));
        fightAreaPanel.add(panel);
        fightAreaPanel.add(panel2);
        
        // title
        TitledBorder border = new TitledBorder("Fight area");
        fightAreaPanel.setBorder(border);
        
        return fightAreaPanel;
        
    }

    private JPanel getNPCPanel() {
        
        //NPC panel
        JPanel npcPanel = new JPanel();
        //npcPanel.setPreferredSize(new Dimension(1000, 1000));
        npcPanel.setBorder(new TitledBorder("NPCs"));
        
        //Attackable
        attackableNpcsListModel = new DefaultListModel();
        attackableNpcsField = new JList(attackableNpcsListModel);
        JScrollPane scrlPane = new JScrollPane(attackableNpcsField);
        scrlPane.setPreferredSize(new Dimension(200, 200));
        scrlPane.setBorder(new TitledBorder("Attackable"));        
        //attackableNpcsPanel.setPreferredSize(new Dimension(150, 200));
        //attackableNpcsPanel.add(attackableNpcsField);
        
        //Selected
        JPanel selectedNpcsPanel = new JPanel();
        selectedNpcsPanel.setBorder(new TitledBorder("Selected"));
        selectedNpcsListModel = new DefaultListModel();
        selectedNpcsField = new JList(selectedNpcsListModel);
        selectedNpcsPanel.setPreferredSize(new Dimension(200, 200));
        selectedNpcsPanel.add(selectedNpcsField);
        
        //Add panels to main one
        npcPanel.add(scrlPane);
        npcPanel.add(selectedNpcsPanel);

        initializeAttackableNpcsList();
        
        return npcPanel;
    }
 
    public JPanel getLootPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Loot"));
        
        itemTableModel = new DefaultTableModel(50, 1);
        itemTable = new JTable(itemTableModel);
        JScrollPane scrlPane = new JScrollPane(itemTable);
        scrlPane.setPreferredSize(new Dimension(200, 200));
        scrlPane.setBorder(new TitledBorder("Loot items"));
        
        initItemTable(Arrays.asList("Item name"));
        
        panel.add(scrlPane);
        
        return panel;
    }
    
    public JPanel getFoodPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.setBorder(new TitledBorder("Food"));
        
        foodName = new JTextField();
        foodName.setPreferredSize(new Dimension(100, 20));
        JLabel foodLabel = new JLabel("Name");
        
        foodAmount = new JTextField();
        foodAmount.setPreferredSize(new Dimension(20, 20));
        JLabel foodAmountLabel = new JLabel("Amount");
        
        
        panel.add(foodLabel);
        panel.add(foodName);
        panel.add(foodAmountLabel);
        panel.add(foodAmount);
        return panel;
    }
    
    public JPanel getSaveLoadPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBorder(new TitledBorder("Save and load"));
        
        // Save
        saveButton = new JButton("Save");
        saveButton.addActionListener((ActionEvent e) -> {
            save();
        });
        
        // Load
        loadButton = new JButton("Load");
        loadButton.addActionListener((ActionEvent e) -> {
            load();
        });
        JLabel fileNameLabel = new JLabel("File name");
        fileName = new JTextField();
        fileName.setPreferredSize(new Dimension(100, 20));
        
        panel.add(fileNameLabel);
        panel.add(fileName);
        panel.add(saveButton);
        panel.add(loadButton);
        return panel;
    }
 
    public static void update(TargetEvent te) {
        if (isVisible() && Helpers.isInGame()) {
            initializeAttackableNpcsList();
        }
    }
    
    public static boolean isVisible() {
        return (frame != null && frame.isVisible());
    }
    
    public static void setFightArea() {
        try {
            int x = Integer.parseInt(fightAreaCentralTileXField.getText());
            int y = Integer.parseInt(fightAreaCentralTileYField.getText());
            int size = Integer.parseInt(fightAreaSizeField.getText());
            System.out.println("X: " + x + " Y: " + y);
            Settings.fightArea = new Location(new Position(x, y), size);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void loadCentralTile() {
        int x = Players.getLocal().getX();
          int y = Players.getLocal().getY();
        fightAreaCentralTileXField.setText(Integer.toString(x));
        fightAreaCentralTileYField.setText(Integer.toString(y));
    }
    
    public static JFrame getFrame() {
        return frame;
    }

    private void refreshParameters() {
        setFightArea();
        saveFoodSettings();
        saveNpcsSettings();
    }

    private void saveFoodSettings() {
        Settings.foodName = foodName.getText();
        try {
            Settings.foodWithdrawAmount = Integer.parseInt(foodAmount.getText());
        } catch (NumberFormatException ex) {
           System.out.println("Input a number for food amount.");
        }
        System.out.println("Eating: " + foodName.getText());
    }

    private void save(){
       
        if(fileName.getText().isEmpty()) {
            System.out.println("Input a file name.");
        } else {
            Settings.save(path, fileName.getText());
        }
        
    }

    private void load() {
        try
            {
                if(fileName.getText().isEmpty()) {
                    System.out.println("No file name specified.");
                    return;
                }
                load(path, fileName.getText());
            }
        catch (Exception e)
        {
            System.out.println("Error when loading from file.");
            System.out.println(e);
        }
    }

    private void saveNpcsSettings() {
        Settings.NPCsToFight.clear();
        if (selectedNpcsListModel.isEmpty()) return;
        
        for(int i = 0; i < selectedNpcsListModel.getSize(); i++) {
            Settings.NPCsToFight.add(((ListNpc)selectedNpcsListModel.getElementAt(i)));
        }
    }
    
    static void addNpcToList(int index) {
        ListNpc listNpc = (ListNpc)attackableNpcsListModel.getElementAt(index);
        if (!Settings.NPCsToFight.contains(listNpc)) {                          
          XFighterGUI.selectedNpcsListModel.addElement(listNpc);
          Settings.NPCsToFight.add(listNpc);
        }
        Settings.NPCsToFight.forEach(npc -> System.out.println(npc));
    }

    static void removeNpcFromList(int index) {
        ListNpc listNpc = (ListNpc)attackableNpcsListModel.getElementAt(index);
        System.out.println(index);
        XFighterGUI.selectedNpcsListModel.removeElement(listNpc);
        Settings.NPCsToFight.remove(listNpc);
        Settings.NPCsToFight.forEach(npc -> System.out.println(npc));
    }
  
    public static void initializeAttackableNpcsList() {
        // Get attackable NPCs to listPanel
        if (Helpers.isInGame()) {
            for(Npc npc : Npcs.getLoaded()) {
                setAttackableNpcToList(npc);
            }
        }
    }
    
    public static void setAttackableNpcToList(PathingEntity npc) {
        if (npc != null && npc.getActions() != null && Arrays.asList(npc.getActions()).contains("Attack")) {
            ListNpc listNpc = new ListNpc(npc.getName(), npc.getId());
            if(!attackableNpcsListModel.contains(listNpc)){
                attackableNpcsListModel.addElement(listNpc);
            }
        }
    }
    
    public void setValues(SaveSettings s) {
        
        // NPCS
        List<ListNpc> npcs = s.getNPCsToFight();
        selectedNpcsListModel.clear();
        System.out.println(selectedNpcsListModel.size());
        for(int i = 0; i < npcs.size(); i++) {
            ListNpc npc = new ListNpc(npcs.get(i).getName(), npcs.get(i).getId());
            selectedNpcsListModel.addElement(npc);
        }
              
        // Food
        foodName.setText(s.getFoodName());
        foodAmount.setText(Integer.toString(s.getFoodWithdrawAmount()));
        
        // Fight area
        fightAreaSizeField.setText(Integer.toString(s.getFightAreaSize()));
        fightAreaCentralTileXField.setText(Integer.toString(s.getFightAreaX()));
        fightAreaCentralTileYField.setText(Integer.toString(s.getFightAreaY()));
        
        // Items
        initItemTable(Settings.ItemsToLoot);
        
    }
    
    private void initItemTable(List<String> items) {
        List<String> itemList = new ArrayList<>();
        
        for(String str: items) {
            itemList.add(str);
            //System.out.println(str);
        }
        
        itemTable.removeAll();
        
        Vector<String> header = new Vector<String>(1);
        header.add("Item name");
        
        Vector<String> data = new Vector(itemList);
        Vector<Vector<String>> dataVector = new Vector<Vector<String>>();
        
        dataVector.add(data);
        itemTableModel.setDataVector(dataVector, header);
    }
    
    private void load(String path, String fileName) {
        try {
            GsonBuilder gsonBuilder  = new GsonBuilder();
            // Allowing the serialization of static fields    

            gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
            // Creates a Gson instance based on the current configuration
            Gson gson = gsonBuilder.create();
            List<String> json = Files.readAllLines(Paths.get(fileName + ".json"));
            SaveSettings s = gson.fromJson(String.join("", json), SaveSettings.class);
            
            Settings.ItemsToLoot = Arrays.asList(s.getItemsToLoot());
            Settings.NPCsToFight = s.getNPCsToFight();
            Settings.fightArea = new Location(new Position(s.getFightAreaX(), s.getFightAreaY()),s.getFightAreaSize());
            Settings.foodName = s.getFoodName();
            Settings.foodWithdrawAmount = s.getFoodWithdrawAmount();
            System.out.println("Items loaded: " + Settings.ItemsToLoot);
            System.out.println(json);
            setValues(s);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void dispose(){
        frame.dispose();
    }
    
}