package gcomdebug.Controller;

import gcomdebug.view.DebugClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {

    private static DebugClient gui;

    public DebugController(DebugClient gui) {
        this.gui = gui;
    }

    public static void main(String[] args){

        DebugController controller;

        controller = new DebugController(new DebugClient());

        gui.addListenerToDs(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


    }


}
