/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dummy.woodstox.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author davik3000
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public Main() {
    }

    public void execute(String[] args) {
        if (args.length > 0) {

        }
    }

    public static void main(String[] args) {
        LOG.debug("Starting application");
        Main m = new Main();
        m.execute(args);
        LOG.debug("Ending application");
    }
}
