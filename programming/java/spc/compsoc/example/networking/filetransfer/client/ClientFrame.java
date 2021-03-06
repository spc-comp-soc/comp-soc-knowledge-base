/*
 *     Comp-Soc-DB
 *     Copyright (C) 2020  Colin Chow
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package spc.compsoc.example.networking.filetransfer.client;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClientFrame extends JFrame {
    private File[] read;
    public ClientFrame() {
        super("Client Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        JPanel jp = new JPanel();
        jp.add(new JLabel("IP:"));
        JTextField ip = new JTextField(20);
        jp.add(ip);
        jp.add(new JLabel("Port:"));
        JTextField port = new JTextField(6);
        jp.add(port);
        add(jp);
        JPanel selectAndOut = new JPanel();
        JButton select = new JButton("Select a File");
        selectAndOut.add(select);
        JTextField selectedFile = new JTextField(30);
        selectAndOut.add(selectedFile);
        selectedFile.setEditable(false);
        select.addActionListener(ae -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setMultiSelectionEnabled(true);
            int stat = jfc.showDialog(ClientFrame.this,"Select a file to send.");
            if(stat == JFileChooser.APPROVE_OPTION){
                read = jfc.getSelectedFiles();
                selectedFile.setText(Arrays.asList(read).stream().map(f -> f.getName()).collect(Collectors.joining(" ")));
                repaint();
            }
        });
        JButton jb = new JButton("Send");
        JPanel progressP = new JPanel();
        progressP.setLayout(new BoxLayout(progressP,BoxLayout.Y_AXIS));
        JLabel transfer = new JLabel("Ready!");
        progressP.add(transfer);
        JProgressBar progress = new JProgressBar();
        progressP.add(progress);
        jb.addActionListener(ae ->{
            String fromIp = ip.getText();
            String fromPort = port.getText();
            Thread async = new Thread(() -> {
                for (File f : read) {
                    try {
                        Socket s = new Socket(InetAddress.getByName(fromIp), Integer.parseInt(fromPort));
                        SwingUtilities.invokeLater(() ->{
                            transfer.setText("Transferring file:" + f.getName());
                        });
                        OutputStream write = s.getOutputStream();
                        write.write(f.getName().getBytes().length);
                        write.write(f.getName().getBytes());
                        FileInputStream fis = new FileInputStream(f);
                        long size = f.length();
                        System.out.println(size);
                        byte[] buffer = new byte[8 * 1024];
                        int bytesRead;
                        int adder = 0;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            write.write(buffer, 0, bytesRead);
                            adder += bytesRead;
                            final int cache = adder;
                            SwingUtilities.invokeLater(() ->{
                                progress.setValue((int) (cache * 100.0 / size));
                            });
                        }
                        write.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                SwingUtilities.invokeLater(() ->{
                    transfer.setText("Done!");

                });
            });
            async.start();
        });
        selectAndOut.add(jb);
        add(selectAndOut);
        add(progressP);
        pack();
        setVisible(true);
    }
}
