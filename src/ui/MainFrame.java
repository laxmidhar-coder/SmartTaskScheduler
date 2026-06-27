package ui;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.io.*;

import java.awt.Color;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;

import java.util.Date;
import java.text.SimpleDateFormat;

public class MainFrame extends JFrame {

    private JTextField txtTitle;
    private JSpinner deadlineSpinner;
    private JComboBox<String> cmbPriority;
    private JButton btnAdd;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JComboBox<String> cmbStatus;

    public MainFrame() {

        setTitle("Smart Task Scheduler");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("Task Title:");
        lblTitle.setBounds(30, 30, 100, 25);
        add(lblTitle);

        txtTitle = new JTextField();
        txtTitle.setBounds(120, 30, 200, 25);
        add(txtTitle);

        JLabel lblPriority = new JLabel("Priority:");
        lblPriority.setBounds(30, 70, 100, 25);
        add(lblPriority);

        String[] priorities = {"High", "Medium", "Low"};
        cmbPriority = new JComboBox<>(priorities);
        cmbPriority.setBounds(120, 70, 200, 25);
        add(cmbPriority);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(350, 70, 60, 25);
        add(lblStatus);

        String[] status = {"Pending", "In Progress", "Completed"};
        cmbStatus = new JComboBox<>(status);
        cmbStatus.setBounds(420, 70, 180, 25);
        add(cmbStatus);

        JLabel lblDeadline = new JLabel("Deadline:");
        lblDeadline.setBounds(30, 110, 100, 25);
        add(lblDeadline);

        deadlineSpinner = new JSpinner(new SpinnerDateModel());

deadlineSpinner.setEditor(
    new JSpinner.DateEditor(deadlineSpinner, "dd-MM-yyyy"));

deadlineSpinner.setBounds(120,110,200,25);

add(deadlineSpinner);

        btnAdd = new JButton("Add Task");
        btnAdd.setBounds(120, 160, 120, 30);
        add(btnAdd);

        btnDelete = new JButton("Delete Task");
        btnDelete.setBounds(260,160,120,30);
        add(btnDelete);

        btnUpdate = new JButton("Update Task");
        btnUpdate.setBounds(400,160,120,30);
        add(btnUpdate);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(400, 30, 60, 25);
        add(lblSearch);

        txtSearch= new JTextField();
        txtSearch.setBounds(460, 30, 150, 25);
        add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(620,30,70,25);
        add(btnSearch);



        btnAdd.addActionListener(e -> {

    String title = txtTitle.getText().trim();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

String deadline = sdf.format((Date) deadlineSpinner.getValue());

    if(title.isEmpty()){

       JOptionPane.showMessageDialog(this,"Please Enter Task.");
       return;

    }

    
    String priority = cmbPriority.getSelectedItem().toString();
    String taskstatus = cmbStatus.getSelectedItem().toString();


    model.addRow(new Object[]{title, priority, deadline,taskstatus});
    saveTasksToFile();

    txtTitle.setText("");
    deadlineSpinner.setValue(new Date());
    cmbPriority.setSelectedIndex(0);
    cmbStatus.setSelectedIndex(0);
});

        btnDelete.addActionListener(e -> {
            int row=table.getSelectedRow();
            if(row!= -1){
                model.removeRow(row);
                saveTasksToFile();
            }else{
                JOptionPane.showMessageDialog(this,"Please select a task to delete.");
            }
        });

        btnUpdate.addActionListener(e -> {
            int row=table.getSelectedRow();
            if(row!= -1){
                model.setValueAt(txtTitle.getText(),row,0);

                model.setValueAt(cmbPriority.getSelectedItem(),row,1);

               SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

model.setValueAt(
    sdf.format((Date)deadlineSpinner.getValue()),
    row,
    2
);

                model.setValueAt(cmbStatus.getSelectedItem(),row,3);


                JOptionPane.showMessageDialog(this,"Task Updated Successfully ");

                saveTasksToFile();

            }else{
                JOptionPane.showMessageDialog(this,"Please select a task.");
            }
        });

        btnSearch.addActionListener(e -> {

            String search = txtSearch.getText().toLowerCase();

            for (int i = 0; i < table.getRowCount(); i++) {

                String title = model.getValueAt(i, 0).toString().toLowerCase();

                if (title.contains(search)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
                }
            }

            JOptionPane.showMessageDialog(this, "Task not found!");

        });


        String[] columns = {"Task Title", "Priority", "Deadline","Status"};

model = new DefaultTableModel(columns, 0);

loadTasksFromFile();

table = new JTable(model);

table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        String status = table.getValueAt(row, 3).toString();

        if (!isSelected) {
            if (status.equals("Completed")) {
                c.setBackground(Color.GREEN);
            } else if (status.equals("In Progress")) {
                c.setBackground(Color.CYAN);
            } else {
                c.setBackground(Color.YELLOW);
            }
        }

        return c;
    }
});

JScrollPane scrollPane = new JScrollPane(table);
scrollPane.setBounds(30, 220, 620, 200);

add(scrollPane);

table.getSelectionModel().addListSelectionListener(e->{
    int row = table.getSelectedRow();
    if(row != -1){
        txtTitle.setText(model.getValueAt(row, 0).toString());

        cmbPriority.setSelectedItem(model.getValueAt(row, 1).toString());

        try{
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    Date d = sdf.parse(model.getValueAt(row,2).toString());

    deadlineSpinner.setValue(d);

}catch(Exception ex){
    ex.printStackTrace();
}

        cmbStatus.setSelectedItem(model.getValueAt(row, 3).toString());
    }
});

        setVisible(true);
    }

    private void saveTasksToFile(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/tasks.txt"));
            for(int i=0; i< model.getRowCount();i++){
                String title = model.getValueAt(i, 0).toString();
                String priority = model.getValueAt(i, 1).toString();
                String deadline = model.getValueAt(i, 2).toString();
                String status = model.getValueAt(i, 3).toString();

                writer.write(title + "," + priority + "," +deadline + "," + status);
                writer.newLine();
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void loadTasksFromFile(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("data/tasks.txt"));
        
            String line;

            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length == 4){
                    model.addRow(new Object[]{
                data[0],
                data[1],
                data[2],
                data[3]
                });
                }
                
            }
            
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
