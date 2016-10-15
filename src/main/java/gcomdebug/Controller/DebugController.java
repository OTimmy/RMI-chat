package gcomdebug.Controller;

import gcomdebug.view.DebugClient;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {
    private static int num = 0;
    private static DebugClient gui;

    public DebugController(DebugClient gui) {
        this.gui = gui;
    }

    public static void main(String[] args) {

        DebugController controller;

        controller = new DebugController(new DebugClient());



        gui.addListenerToRa(createActionlistenerRa());
        gui.addListenerToDs(createActionlistenerDs());
        gui.addListenerToRefresh(createSctionListenerRefresh());
        gui.addListenerToIncTable(createIncommingTableListener());

        //gui.updateDebugGroups();


    }

    private static ActionListener createActionlistenerRa(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int countRows = gui.getTableRowCount();
                for (int i = 0; i < countRows; i++){
                    int index = gui.moveRow();
                    //do something with index!
                }
            }
        };
    }

    private static ActionListener createActionlistenerDs(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = gui.dropSelected();
                //do something with index!!
            }
        };
    }

    private static ActionListener createSctionListenerRefresh(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.addIncomming("test", "message" + num);
                num++;
            }
        };
    }

    private static MouseListener createIncommingTableListener(){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2 ) {
                    int index = gui.moveRow(e);
                    //do something with index!
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }
}
