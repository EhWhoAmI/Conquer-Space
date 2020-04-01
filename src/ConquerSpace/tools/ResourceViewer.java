/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.tools;

import ConquerSpace.game.universe.resources.Element;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.TempNonElement;
import ConquerSpace.util.ResourceLoader;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Temporary construction...
 *
 * @author EhWhoAmI
 */
public class ResourceViewer extends JFrame {

    public static void main(String[] args) {
        new ResourceViewer();
    }

    private JPanel resourceListPanel;
    private JList<Good> resourceList;
    private DefaultListModel<Good> resourceListModel;
    private ArrayList<Good> resources;

    private JTextField searchField;

    private JPanel resourceInfoPanel;

    private JTextField resourceNameField;
    private JSpinner id;
    private JTextField mass;
    private JTextField volume;

    private DefaultTableModel forumlaTableModel;

    private JScrollPane formulaTableWrapper;
    private JTable formulaTable;

    private DefaultListModel<String> tagsListModel;
    private JList<String> tagsList;

    private JButton addTagButton;
    private JTextField toAddTagFieldTextField;
    private JButton deleteTagButton;

    boolean everythingLoaded = true;

    File loadedFile = null;

    private JSONArray currentlySelectedArray;

    public ResourceViewer() {
        setTitle("Resource Config");

        //Open resource file
        setLayout(new HorizontalFlowLayout());

        resetLoadedGoods();

        resourceListPanel = new JPanel();
        resourceListPanel.setLayout(new VerticalFlowLayout());

        searchField = new JTextField(16);

        searchField.addActionListener(l -> {
            resourceListModel.removeAllElements();
            for (Good g : resources) {
                if (g.getName().toLowerCase().contains(searchField.getText().toLowerCase())) {
                    resourceListModel.addElement(g);
                }
            }
        });
        resourceListPanel.add(searchField);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(l -> {
            searchField.setText("");
            loadGoodsFromList();
        });
        resourceListPanel.add(clearButton);

        resourceListModel = new DefaultListModel<>();
        loadGoodsFromList();

        resourceList = new JList<>(resourceListModel);
        resourceList.setVisibleRowCount(40);
        resourceList.addListSelectionListener(l -> {
            //Add components...
            Good selected = resourceList.getSelectedValue();
            if (selected != null) {
                resourceNameField.setText(selected.getName());
                id.setValue(selected.getId());
                mass.setText("" + selected.getMass());
                volume.setText("" + selected.getVolume());

                //do formula
                forumlaTableModel.setRowCount(0);
                if (selected instanceof TempNonElement) {
                    TempNonElement nonElement = (TempNonElement) selected;
                    for (Map.Entry<String, Integer> map : nonElement.recipie.entrySet()) {
                        String key = map.getKey();
                        Integer val = map.getValue();

                        forumlaTableModel.addRow(new Object[]{key, val});
                    }
                    forumlaTableModel.fireTableDataChanged();
                    formulaTableWrapper.setVisible(true);
                    formulaTable.setVisible(true);
                } else {
                    formulaTableWrapper.setVisible(false);
                }
                //Add tags
                tagsListModel.removeAllElements();
                for (String s : selected.tags) {
                    tagsListModel.addElement(s);
                }
            }
        });

        resourceListPanel.add(new JScrollPane(resourceList));
        add(resourceListPanel);

        resourceInfoPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        resourceInfoPanel.add(new JLabel("Name: "), constraints);

        resourceNameField = new JTextField(16);
        constraints.gridx = 1;
        constraints.gridy = 0;
        resourceInfoPanel.add(resourceNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        resourceInfoPanel.add(new JLabel("Id: "), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        id = new JSpinner(new SpinnerNumberModel());
        resourceInfoPanel.add(id, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        resourceInfoPanel.add(new JLabel("Mass: "), constraints);

        mass = new JTextField();
        mass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                keyEvent(ke, mass);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                keyEvent(ke, mass);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                keyEvent(ke, mass);
            }
        });

        constraints.gridx = 1;
        constraints.gridy = 2;
        resourceInfoPanel.add(mass, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        resourceInfoPanel.add(new JLabel("Volume: "), constraints);

        volume = new JTextField();
        volume.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                keyEvent(ke, volume);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                keyEvent(ke, volume);
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                keyEvent(ke, volume);
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 3;
        resourceInfoPanel.add(volume, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        resourceInfoPanel.add(new JLabel("Formula: "), constraints);

        forumlaTableModel = new DefaultTableModel(new String[]{"Resource", "Amount"}, 0);
        formulaTable = new JTable(forumlaTableModel);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        formulaTableWrapper = new JScrollPane(formulaTable);
        resourceInfoPanel.add(formulaTableWrapper, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        resourceInfoPanel.add(new JLabel("Tags: "), constraints);

        tagsListModel = new DefaultListModel<>();
        tagsList = new JList<>(tagsListModel);
        constraints.gridx = 0;
        constraints.gridy = 7;
        resourceInfoPanel.add(new JScrollPane(tagsList), constraints);

        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        toAddTagFieldTextField = new JTextField(30);
        resourceInfoPanel.add(toAddTagFieldTextField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 8;
        addTagButton = new JButton("Add tag");
        addTagButton.addActionListener(l -> {
            if (!toAddTagFieldTextField.getText().isEmpty()) {
                tagsListModel.addElement(toAddTagFieldTextField.getText());
                toAddTagFieldTextField.setText("");
                Good selected = resourceList.getSelectedValue();
                selected.tags = Arrays.copyOf(tagsListModel.toArray(), tagsListModel.toArray().length, String[].class);
            }
        });
        resourceInfoPanel.add(addTagButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        deleteTagButton = new JButton("Delete tag");
        deleteTagButton.addActionListener(l -> {
            tagsListModel.remove(tagsList.getSelectedIndex());
            Good selected = resourceList.getSelectedValue();
            selected.tags = Arrays.copyOf(tagsListModel.toArray(), tagsListModel.toArray().length, String[].class);
        });
        resourceInfoPanel.add(deleteTagButton, constraints);

        add(resourceInfoPanel);

        //Set menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveCurrentElement = new JMenuItem("Save current good");
        saveCurrentElement.addActionListener(l -> {
            if (!everythingLoaded) {
                //Find the name...
                for (int i = 0; i < currentlySelectedArray.length(); i++) {
                    JSONObject obj = currentlySelectedArray.getJSONObject(i);
                    if (obj.getString("name").equals(resourceNameField.getText())) {
                        //Set values
                        obj.put("volume", Double.parseDouble(volume.getText()));
                        obj.put("mass", Double.parseDouble(mass.getText()));
                        obj.put("tags", tagsListModel.toArray());
                        break;
                    }
                }

                String hjsonString = JsonValue.readHjson(currentlySelectedArray.toString()).toString(Stringify.HJSON);
                //Save
                loadedFile.delete();
                try {
                    loadedFile.createNewFile();

                    FileWriter write = new FileWriter(loadedFile);
                    write.write(hjsonString);
                    write.close();
                } catch (IOException ex) {
                }
            }
        });

        JMenuItem loadGoodFile = new JMenuItem("Load Goods in a File");
        loadGoodFile.addActionListener(l -> {
            JFileChooser chooser = new JFileChooser(ResourceLoader.getResourceByFile("dirs.goods"));
            int returnVal = chooser.showOpenDialog(ResourceViewer.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                //Read file
                ArrayList<Good> nonElements = new ArrayList<>();
                readFile(file, nonElements);
                resources = nonElements;
                loadGoodsFromList();
                everythingLoaded = false;
                loadedFile = file;
            } else {
                System.out.println("no load file");
            }
        });

        JMenuItem resetEverything = new JMenuItem("Reset Elements");
        resetEverything.addActionListener(l -> {
            everythingLoaded = true;
            resetLoadedGoods();
            loadGoodsFromList();
        });

        fileMenu.add(saveCurrentElement);
        fileMenu.add(loadGoodFile);
        fileMenu.add(resetEverything);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void keyEvent(KeyEvent ke, JTextField textField) {
        String value = textField.getText();

        if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') || ke.getKeyChar() == '.' || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            //Check for decimal points
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == '.' && ke.getKeyChar() == '.') {
                    ke.consume();
                }
            }
        } else {
            ke.consume();
        }
    }

    private void readFile(File file, ArrayList<Good> goods) {
        FileInputStream fis = null;
        try {
            //If it is readme, continue
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String text = new String(data);
            text = JsonValue.readHjson(text).toString();
            JSONArray root = new JSONArray(text);
            for (int i = 0; i < root.length(); i++) {
                try {
                    JSONObject obj = root.getJSONObject(i);
                    String name = obj.getString("name");
                    double volume = obj.getDouble("volume");
                    double mass = obj.getDouble("mass");

                    TempNonElement tempNonElement = new TempNonElement(name, i, volume, mass);
                    //NonElement nonElement = new NonElement(name, i, volume, mass);

                    //Process formula
                    JSONArray arr = obj.getJSONArray("formula");
                    //Sort through things
                    for (int k = 0; k < arr.length(); k++) {
                        String s = arr.getString(k);
                        String[] content = s.split(":");
                        //Find the resources
                        String resourceName = content[0];
                        int amount = Integer.parseInt(content[1]);
                        tempNonElement.recipie.put(resourceName, amount);
                    }

                    //Sort through elements
                    JSONArray tags = obj.getJSONArray("tags");
                    String[] tagArray = Arrays.copyOf(tags.toList().toArray(), tags.toList().toArray().length, String[].class);
                    tempNonElement.tags = tagArray;
                    goods.add(tempNonElement);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            currentlySelectedArray = root;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                //Because continue stat
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    private ArrayList<Element> getElements() {
        ArrayList<Element> elements = new ArrayList<>();
        //Get the launch systems folder
        File resourceFolder = ResourceLoader.getResourceByFile("dirs.elements");
        File[] files = resourceFolder.listFiles();
        for (File f : files) {
            FileInputStream fis = null;
            try {
                //If it is readme, continue
                if (f.getName().endsWith("readme.txt")) {
                    continue;
                }
                fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String text = new String(data);
                text = JsonValue.readHjson(text).toString();
                JSONArray root = new JSONArray(text);
                for (int i = 0; i < root.length(); i++) {
                    try {
                        JSONObject obj = root.getJSONObject(i);
                        //Because periodic table number is the id
                        int id = obj.getInt("number");
                        String name = obj.getString("name");
                        Object densityT = obj.get("density");
                        //if null, put 0
                        double density = 0;
                        if (densityT instanceof Double) {
                            density = (Double) densityT;
                        }
                        Element e = new Element(name, id, 1d, density);

                        //Set tags
                        e.tags = new String[0];
                        elements.add(e);
                    } catch (ClassCastException e) {
                    } catch (JSONException exception) {
                    } catch (IllegalArgumentException ile) {
                    }
                }
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } catch (JSONException ex) {
            } finally {
                try {
                    //Because continue stat
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        return elements;
    }

    private void resetLoadedGoods() {
        ArrayList<Good> allGoods = new ArrayList<>();
        File goodDir = ResourceLoader.getResourceByFile("dirs.goods");
        for (File goodFile : goodDir.listFiles()) {
            readFile(goodFile, allGoods);
        }

        allGoods.addAll(getElements());

        resources = allGoods;
    }

    private void loadGoodsFromList() {
        resourceListModel.removeAllElements();
        for (Good r : resources) {
            resourceListModel.addElement(r);
        }
    }
}
