package gcomdebug.Controller;

import gcomdebug.view.DebugClient;

/**
 * Created by c12ton on 10/12/16.
 */
public class DebugController {

    private DebugClient gui;

    public DebugController(DebugClient gui) {
        this.gui = gui;
    }

    public static void main(String[] args){

        DebugController controller = new DebugController(new DebugClient());

    }
}
